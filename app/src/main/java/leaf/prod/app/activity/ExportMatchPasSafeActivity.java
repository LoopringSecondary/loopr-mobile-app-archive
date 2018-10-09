package leaf.prod.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.utils.ToastUtils;
import leaf.prod.app.views.TitleView;

public class ExportMatchPasSafeActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.et_password)
    MaterialEditText etPassword;

    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private int type;  //密码验证类型，1为备份助记词验证密码  2为导出私钥验证密码  3为导出keystore验证密码

    private int position;

    private String filename;

    private String address;

    private String walletname;

    private String mnemonic;

    private String pas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_export_keystore);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        type = getIntent().getIntExtra("type", 1);
        title.setBTitle(getResources().getString(R.string.match_password));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        position = getIntent().getIntExtra("position", 0);
        filename = getIntent().getStringExtra("filename");
        address = getIntent().getStringExtra("address");
        walletname = getIntent().getStringExtra("walletname");
        mnemonic = getIntent().getStringExtra("mnemonic");
        pas = getIntent().getStringExtra("pas");
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            ToastUtils.toast("请输入密码");
            return;
        }
        if (!etPassword.getText().toString().equals(pas)) {
            ToastUtils.toast("您输入的密码不对");
            return;
        }
        if (type == 1) {
            getOperation().addParameter("position", position);
            getOperation().addParameter("mnemonic", mnemonic);
            getOperation().forward(BackupMnemonicActivity.class);
        } else if (type == 2) {
            getOperation().addParameter("filename", filename);
            getOperation().addParameter("address", address);
            getOperation().addParameter("password", etPassword.getText().toString());
            getOperation().forward(ExportPrivateKeyActivity.class);
        } else if (type == 3) {
            getOperation().addParameter("filename", filename);
            getOperation().addParameter("address", address);
            getOperation().forward(ExportKeystoreDetailActivity.class);
        }
    }
}
