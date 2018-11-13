/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-12 11:45 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter;

import java.io.IOException;
import java.math.BigInteger;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.R;
import leaf.prod.app.activity.AuthorityWebActivity;
import leaf.prod.app.activity.MainActivity;
import leaf.prod.app.model.ImportWalletType;
import leaf.prod.app.model.WalletEntity;
import leaf.prod.app.utils.FileUtils;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.WalletUtil;
import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.model.QRCodeType;
import leaf.prod.walletsdk.model.ScanWebQRCode;
import leaf.prod.walletsdk.model.SignStatus;
import leaf.prod.walletsdk.model.request.param.NotifyScanParam;
import leaf.prod.walletsdk.model.request.param.NotifyStatusParam;
import leaf.prod.walletsdk.service.LoopringService;
import leaf.prod.walletsdk.util.KeystoreUtils;
import leaf.prod.walletsdk.util.MnemonicUtils;
import leaf.prod.walletsdk.util.SignUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AuthorityWebPresenter extends BasePresenter<AuthorityWebActivity> {

    private AlertDialog passwordDialog;

    private static LoopringService loopringService = new LoopringService();

    private static Gson gson = new Gson();

    private String value;

    private QRCodeType type;

    private Credentials credentials;

    public AuthorityWebPresenter(AuthorityWebActivity view, Context context, String info, QRCodeType type) {
        super(view, context);
        this.type = type;
        this.value = gson.fromJson(info, ScanWebQRCode.class).getValue();
    }

    public void showPasswordDialog() {
        if (passwordDialog == null) {
            final AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);
            View passwordView = LayoutInflater.from(context).inflate(R.layout.dialog_put_password, null);
            builder.setView(passwordView);
            final EditText passwordInput = passwordView.findViewById(R.id.password_input);
            passwordView.findViewById(R.id.cancel).setOnClickListener(v -> passwordDialog.dismiss());
            passwordView.findViewById(R.id.confirm).setOnClickListener(v -> {
                try {
                    String password = passwordInput.getText().toString().trim();
                    generateCredentials(password);
                    handle();
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

    private void generateCredentials(String password) {
        try {
            WalletEntity walletEntity = WalletUtil.getCurrentWallet(context);
            if (walletEntity != null && walletEntity.getWalletType() != null && walletEntity.getWalletType() == ImportWalletType.MNEMONIC) {
                LyqbLogger.log(walletEntity.toString());
                credentials = MnemonicUtils.calculateCredentialsFromMnemonic(walletEntity.getMnemonic(), walletEntity.getdPath(), password);
            } else {
                String keystore = FileUtils.getKeystoreFromSD(context);
                credentials = KeystoreUtils.unlock(password, keystore);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle() {
        switch (type) {
            case LOGIN:
                handleLogin();
                break;
            case APPROVE:
                handleApprove();
                break;
            case CONVERT:
                handleConvert();
                break;
            case ORDER:
                handleOrder();
                break;
            case CANCEL_ORDER:
                handleCancel();
                break;
        }
    }

    private void handleApprove() {
    }

    private void handleConvert() {
        if (value != null) {
            NotifyStatusParam.NotifyBody body = NotifyStatusParam.NotifyBody.builder()
                    .hash(value)
                    .status(SignStatus.received.name())
                    .build();
            String owner = WalletUtil.getCurrentAddress(context);
            loopringService.notifyStatus(body, owner)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap((Func1<String, Observable<String>>) result -> loopringService.getSignMessage(value))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::signAndSendRawTx);
        }
    }

    private void signAndSendRawTx(String rawTx) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(rawTx).getAsJsonObject().getAsJsonObject("tx").getAsJsonObject();
            BigInteger gasPrice = json.get("gasPrice").getAsBigInteger();
            BigInteger gasLimit = json.get("gasLimit").getAsBigInteger();
            String to = json.get("to").getAsString();
            String data = json.get("data").getAsString();
            BigInteger value = json.get("value").getAsBigInteger();
            RawTransactionManager transactionManager = new RawTransactionManager(SDK.getWeb3j(), credentials, SDK.CHAIN_ID);
            transactionManager.sendTransaction(gasPrice, gasLimit, to, data, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleOrder() {
    }

    private void handleCancel() {
    }

    public void handleLogin() {
        try {
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
            NotifyScanParam.LoginSign loginSign = SignUtils.genSignMessage(credentials, timeStamp);
            loopringService.notifyScanLogin(loginSign, WalletUtil.getCurrentAddress(context), value)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
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
        } catch (Exception e) {
            if (passwordDialog != null) {
                ((TextView) passwordDialog.findViewById(R.id.password_input)).setText("");
            }
            RxToast.error(context.getResources().getString(R.string.authority_login_error));
        }
    }
}
