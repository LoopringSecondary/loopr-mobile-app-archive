package leaf.prod.app.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.model.ImportWalletType;
import leaf.prod.app.model.WalletEntity;
import leaf.prod.app.model.eventbusData.NameChangeData;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.views.TitleView;

public class WalletSafeActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.ll_wallet_name)
    LinearLayout llWalletName;

    @BindView(R.id.ll_backup_mnemonic)
    LinearLayout llBackupMnemonic;

    @BindView(R.id.ll_export_private_key)
    LinearLayout llExportPrivateKey;

    @BindView(R.id.ll_export_keystore)
    LinearLayout llExportKeystore;

    @BindView(R.id.ll_clear_records)
    LinearLayout llClearRecords;

    @BindView(R.id.btn_switch)
    Button btnSwitch;

    private int position;

    private String filename;

    private String address;

    private String walletname;

    private String mnemonic;

    private String pas;

    private AlertDialog.Builder confirmClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_safe);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        position = getIntent().getIntExtra("position", 0);
        filename = getIntent().getStringExtra("filename");
        address = getIntent().getStringExtra("address");
        walletname = getIntent().getStringExtra("walletname");
        mnemonic = getIntent().getStringExtra("mnemonic");
        pas = getIntent().getStringExtra("pas");
        title.setBTitle(walletname);
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        ImportWalletType importWalletType = ImportWalletType.valueOf((String) SPUtils.get(this, "create_method_" + address, "ERROR"));
        switch (importWalletType) {
            case MNEMONIC:
                llBackupMnemonic.setVisibility(View.VISIBLE);
                break;
            case KEY_STORE:
                llExportKeystore.setVisibility(View.VISIBLE);
                break;
            case PRIVATE_KEY:
                llExportKeystore.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void initData() {
    }

    @OnClick({R.id.ll_wallet_name, R.id.ll_backup_mnemonic, R.id.ll_export_private_key, R.id.ll_export_keystore, R.id.ll_clear_records, R.id.btn_switch})
    public void onViewClicked(View view) {
        getOperation().addParameter("position", position);
        getOperation().addParameter("filename", filename);
        getOperation().addParameter("address", address);
        getOperation().addParameter("walletname", walletname);
        getOperation().addParameter("mnemonic", mnemonic);
        getOperation().addParameter("pas", pas);
        switch (view.getId()) {
            case R.id.ll_wallet_name:
                getOperation().forward(ReviseWalletNameActivity.class);
                break;
            case R.id.ll_backup_mnemonic:
                getOperation().addParameter("type", 1);
                getOperation().forward(ExportMatchPasSafeActivity.class);
                break;
            case R.id.ll_export_private_key:
                getOperation().addParameter("type", 2);
                getOperation().forward(ExportMatchPasSafeActivity.class);
                break;
            case R.id.ll_export_keystore:
                getOperation().addParameter("type", 3);
                getOperation().forward(ExportMatchPasSafeActivity.class);
                break;
            case R.id.ll_clear_records:
                if (confirmClear == null) {
                    confirmClear = new AlertDialog.Builder(this);
                    confirmClear.setPositiveButton(getResources().getString(R.string.confirm), (dialogInterface, i0) -> {
                        String addressUsed = (String) SPUtils.get(this, "address", "");//当前使用钱包的地址
                        List<WalletEntity> list = SPUtils.getWalletDataList(this, "walletlist", WalletEntity.class);
                        for (WalletEntity walletEntity : list) {
                            if (walletEntity.getAddress().equals(address)) {
                                list.remove(walletEntity);
                                SPUtils.remove(this, "choose_token_" + address);
                                SPUtils.setDataList(this, "walletlist", list);
                                if (list.size() == 0) {
                                    SPUtils.remove(this, "walletlist");
                                    SPUtils.remove(this, "create_method_" + address);
                                    getOperation().forwardClearTop(CoverActivity.class);
                                } else {
                                    if (address.equals(addressUsed)) {
                                        SPUtils.put(this, "address", list.get(0).getAddress());
                                        SPUtils.put(this, "filename", list.get(0).getFilename());
                                    }
                                    getOperation().forwardClearTop(MainActivity.class);
                                }
                                return;
                            }
                        }
                    });
                    confirmClear.setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
                    confirmClear.setMessage(getResources().getString(R.string.confirm_clear_record));
                    confirmClear.setTitle(getResources().getString(R.string.hint));
                }
                confirmClear.show();
                break;
            case R.id.btn_switch:
                SPUtils.put(this, "address", address);
                SPUtils.put(this, "filename", filename);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NameChangeData event) {
        walletname = event.getName();
        title.setBTitle(walletname);
    }
}
