package leaf.prod.app.presenter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.R;
import leaf.prod.app.activity.AuthorityLoginActivity;
import leaf.prod.app.activity.MainActivity;
import leaf.prod.app.utils.FileUtils;
import leaf.prod.app.utils.QRCodeUitl;
import leaf.prod.app.utils.WalletUtil;
import leaf.prod.walletsdk.pojo.loopring.request.param.ScanLoginReq;
import leaf.prod.walletsdk.pojo.loopring.response.data.ScanLoginInfo;
import leaf.prod.walletsdk.service.LoopringService;
import leaf.prod.walletsdk.util.KeystoreUtils;
import leaf.prod.walletsdk.util.SignUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-12 11:45 AM
 * Cooperation: loopring.org 路印协议基金会
 */
public class AuthorityLoginPresenter extends BasePresenter<AuthorityLoginActivity> {

    private AlertDialog passwordDialog;

    private static LoopringService loopringService = new LoopringService();

    private static Gson gson = new Gson();

    public AuthorityLoginPresenter(AuthorityLoginActivity view, Context context) {
        super(view, context);
    }

    public void showPasswordDialog(String loginInfo) {
        if (passwordDialog == null) {
            final AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View passwordView = LayoutInflater.from(context).inflate(R.layout.dialog_put_password, null);
            builder.setView(passwordView);
            final EditText passwordInput = passwordView.findViewById(R.id.password_input);
            passwordView.findViewById(R.id.cancel).setOnClickListener(v -> passwordDialog.dismiss());
            passwordView.findViewById(R.id.confirm).setOnClickListener(v -> {
                try {
                    sign(loginInfo, passwordInput.getText().toString().trim());
                } catch (Exception e) {
                    passwordInput.setText("");
                    RxToast.error(context.getResources().getString(R.string.authority_login_error));
                }
            });
            builder.setCancelable(true);
            passwordDialog = builder.create();
            passwordDialog.setCancelable(true);
            passwordDialog.setCanceledOnTouchOutside(true);
        }
        passwordDialog.show();
    }

    public void sign(String loginInfo, String password) {
        try {
            ScanLoginReq scanLoginReq = getScanLoginReq(loginInfo);
            if (scanLoginReq != null) {
                ScanLoginInfo.LoginSign loginSign = SignUtils.genSignMessage(KeystoreUtils.unlock(password, FileUtils.getKeystoreFromSD(context)), String
                        .valueOf(System.currentTimeMillis() / 1000));
                loopringService.notifyScanLogin(loginSign, WalletUtil.getCurrentAddress(context), scanLoginReq.getValue())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (passwordDialog != null) {
                            ((TextView) passwordDialog.findViewById(R.id.password_input)).setText("");
                        }
                        RxToast.error(context.getResources().getString(R.string.authority_login_error));
                    }

                    @Override
                    public void onNext(String s) {
                        RxToast.success(context.getResources().getString(R.string.authority_login_success));
                        view.finish();
                        view.getOperation().forward(MainActivity.class);
                    }
                });
            }
        } catch (Exception e) {
            if (passwordDialog != null) {
                ((TextView) passwordDialog.findViewById(R.id.password_input)).setText("");
            }
            RxToast.error(context.getResources().getString(R.string.authority_login_error));
        }
    }

    private ScanLoginReq getScanLoginReq(String loginInfo) {
        if (QRCodeUitl.isLogin(loginInfo)) {
            return gson.fromJson(loginInfo, ScanLoginReq.class);
        }
        return null;
    }
}
