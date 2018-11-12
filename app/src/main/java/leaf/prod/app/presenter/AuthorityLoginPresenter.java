package leaf.prod.app.presenter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.R;
import leaf.prod.app.activity.AuthorityLoginActivity;
import leaf.prod.app.utils.FileUtils;
import leaf.prod.walletsdk.model.TransactionSignature;
import leaf.prod.walletsdk.util.SignUtils;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan wangchen@loopring.org
 * Time: 2018-11-12 11:45 AM
 * Cooperation: loopring.org 路印协议基金会
 */
public class AuthorityLoginPresenter extends BasePresenter<AuthorityLoginActivity> {

    private AlertDialog passwordDialog;

    public AuthorityLoginPresenter(AuthorityLoginActivity view, Context context) {
        super(view, context);
    }

    public void showPasswordDialog(String loginInfo) {
        if (passwordDialog == null) {
            final AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_put_password, null);
            builder.setView(view);
            final EditText passwordInput = view.findViewById(R.id.password_input);
            view.findViewById(R.id.cancel).setOnClickListener(v -> passwordDialog.dismiss());
            view.findViewById(R.id.confirm).setOnClickListener(v -> {
                try {
                    TransactionSignature signature = SignUtils.genSignMessage(FileUtils.getKeystoreFromSD(context), loginInfo, passwordInput
                            .getText()
                            .toString()
                            .trim());

                } catch (Exception e) {
                    passwordInput.setText("");
                    e.printStackTrace();
                    RxToast.error("签名出错, 请重试");
                }
            });
            builder.setCancelable(true);
            passwordDialog = builder.create();
            passwordDialog.setCancelable(true);
            passwordDialog.setCanceledOnTouchOutside(true);
        }
        passwordDialog.show();
    }
}
