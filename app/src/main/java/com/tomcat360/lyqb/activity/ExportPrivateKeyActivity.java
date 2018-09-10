package com.tomcat360.lyqb.activity;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import com.lyqb.walletsdk.WalletHelper;
import com.lyqb.walletsdk.exception.IllegalCredentialException;
import com.lyqb.walletsdk.exception.InvalidKeystoreException;
import com.lyqb.walletsdk.model.Account;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.FileUtils;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                    ToastUtils.toast("私钥获取失败");
                    break;
                case ERROR_TWO:
                    ToastUtils.toast("json转换失败");
                    break;
                case ERROR_THREE:
                    ToastUtils.toast("密码输入错误");
                    break;
                case ERROR_FOUR:
                    ToastUtils.toast("密码输入错误");
                    break;
            }
        }
    };

    private String filename;

    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_export_private_key);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.export_private_key));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        filename = getIntent().getStringExtra("filename");
        address = getIntent().getStringExtra("address");
    }

    @Override
    public void initData() {
        String password = getIntent().getStringExtra("password");
        Account account = null;
        String keystore = null;
        try {
            keystore = FileUtils.getKeystoreFromSD(this, filename);
            account = WalletHelper.unlockWallet(password, keystore); //获取account信息，里面有privatekey
            tvPrivateKey.setText(account.getPrivateKey());
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
        ToastUtils.toast("复制成功");
    }
}
