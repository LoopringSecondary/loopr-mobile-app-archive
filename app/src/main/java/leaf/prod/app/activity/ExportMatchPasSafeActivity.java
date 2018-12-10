package leaf.prod.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.model.WalletEntity;
import leaf.prod.walletsdk.util.WalletUtil;

public class ExportMatchPasSafeActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.et_password)
    MaterialEditText etPassword;

    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private int type;  //密码验证类型，1为备份助记词验证密码  2为导出私钥验证密码  3为导出keystore验证密码

    private WalletEntity selectedWallet;

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
        selectedWallet = (WalletEntity) getIntent().getSerializableExtra("selectedWallet");
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            RxToast.error(getResources().getString(R.string.put_password));
            return;
        }
        if (!WalletUtil.passwordValid(etPassword.getText().toString(), selectedWallet.getPas())) {
            RxToast.error(getResources().getString(R.string.keystore_psw_error));
            return;
        }
        if (type == 1) {
            getOperation().addParameter("selectedWallet", selectedWallet);
            getOperation().addParameter("mnemonic", selectedWallet.getMnemonic());
            getOperation().addParameter("password", etPassword.getText().toString());
            getOperation().forward(BackupMnemonicActivity.class);
        } else if (type == 2) {
            getOperation().addParameter("filename", selectedWallet.getFilename());
            getOperation().addParameter("address", selectedWallet.getAddress());
            getOperation().addParameter("password", etPassword.getText().toString());
            getOperation().forward(ExportPrivateKeyActivity.class);
        } else if (type == 3) {
            getOperation().addParameter("filename", selectedWallet.getFilename());
            getOperation().addParameter("address", selectedWallet.getAddress());
            getOperation().forward(ExportKeystoreDetailActivity.class);
        }
    }
}
