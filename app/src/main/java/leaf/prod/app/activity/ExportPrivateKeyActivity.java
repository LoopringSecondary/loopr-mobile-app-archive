package leaf.prod.app.activity;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.web3j.crypto.Credentials;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.exception.IllegalCredentialException;
import leaf.prod.walletsdk.exception.InvalidKeystoreException;
import leaf.prod.walletsdk.util.CredentialsUtils;
import leaf.prod.walletsdk.util.FileUtils;
import leaf.prod.walletsdk.util.KeystoreUtils;

public class ExportPrivateKeyActivity extends BaseActivity {

    public final static int ERROR_ONE = 1;

    public final static int ERROR_TWO = 2;

    public final static int ERROR_THREE = 3;

    public final static int ERROR_FOUR = 4;

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.tv_private_key)
    TextView tvPrivateKey;

    @BindView(R.id.btn_copy_private_key)
    Button btnCopyPrivateKey;

    @SuppressLint("HandlerLeak")
    Handler handlerCreate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ERROR_ONE:
                case ERROR_TWO:
                    RxToast.error(getResources().getString(R.string.private_key_error));
                    break;
                case ERROR_THREE:
                case ERROR_FOUR:
                    RxToast.error(getResources().getString(R.string.password_match_toast));
                    break;
            }
        }
    };

    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_export_private_key);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.export_private_key));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        filename = getIntent().getStringExtra("filename");
    }

    @Override
    public void initData() {
        String password = getIntent().getStringExtra("password");
        Credentials credentials = null;
        String keystore = null;
        try {
            keystore = FileUtils.getKeystoreFromSD(this, filename);
            credentials = KeystoreUtils.unlock(password, keystore); //获取account信息，里面有privatekey
            tvPrivateKey.setText(CredentialsUtils.toPrivateKeyHexString(credentials.getEcKeyPair().getPrivateKey()));
        } catch (IOException e) {
            handlerCreate.sendEmptyMessage(ERROR_ONE);
            e.printStackTrace();
        } catch (JSONException e) {
            handlerCreate.sendEmptyMessage(ERROR_TWO);
            e.printStackTrace();
        } catch (InvalidKeystoreException e) {
            handlerCreate.sendEmptyMessage(ERROR_THREE);
            e.printStackTrace();
        } catch (IllegalCredentialException e) {
            handlerCreate.sendEmptyMessage(ERROR_FOUR);
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_copy_private_key)
    public void onViewClicked() {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(tvPrivateKey.getText());
        RxToast.success(getResources().getString(R.string.copy_to_clipborad_success));
    }
}
