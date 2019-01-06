package leaf.prod.app.activity.setting;

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
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.activity.setupWallet.CoverActivity;
import leaf.prod.app.activity.wallet.DefaultWebViewActivity;
import leaf.prod.app.activity.wallet.MainActivity;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.PartnerDataManager;
import leaf.prod.walletsdk.model.WalletEntity;
import leaf.prod.walletsdk.model.eventbusData.NameChangeData;
import leaf.prod.walletsdk.util.WalletUtil;

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

    @BindView(R.id.ll_etherscan_url)
    LinearLayout llEtherscanUrl;

    @BindView(R.id.ll_clear_records)
    LinearLayout llClearRecords;

    @BindView(R.id.btn_switch)
    Button btnSwitch;

    private AlertDialog.Builder confirmClear;

    private WalletEntity selectedWallet;

    private PartnerDataManager partnerDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_safe);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        selectedWallet = (WalletEntity) getIntent().getSerializableExtra("selectedWallet");
        title.setBTitle(selectedWallet.getWalletname());
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        if (selectedWallet != null && selectedWallet.getWalletType() != null) {
            switch (selectedWallet.getWalletType()) {
                case MNEMONIC:
                    llBackupMnemonic.setVisibility(View.VISIBLE);
                    break;
                case KEY_STORE:
                    llExportKeystore.setVisibility(View.VISIBLE);
                    llExportPrivateKey.setVisibility(View.VISIBLE);
                    break;
                case PRIVATE_KEY:
                    llExportKeystore.setVisibility(View.VISIBLE);
                    llExportPrivateKey.setVisibility(View.VISIBLE);
                    break;
                case ALL:
                    llBackupMnemonic.setVisibility(View.VISIBLE);
                    llExportKeystore.setVisibility(View.VISIBLE);
                    llExportPrivateKey.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void initData() {
        partnerDataManager = PartnerDataManager.getInstance(this);
    }

    @OnClick({R.id.ll_wallet_name, R.id.ll_backup_mnemonic, R.id.ll_export_private_key, R.id.ll_export_keystore, R.id.ll_etherscan_url, R.id.ll_clear_records, R.id.btn_switch})
    public void onViewClicked(View view) {
        getOperation().addParameter("selectedWallet", selectedWallet);
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
                        String addressUsed = WalletUtil.getCurrentAddress(this); //当前使用钱包的地址
                        WalletUtil.removeWallet(this, selectedWallet.getAddress());
                        List<WalletEntity> wallets = WalletUtil.getWalletList(this);
                        if (wallets == null || wallets.size() == 0) {
                            getOperation().forwardClearTop(CoverActivity.class);
                        } else {
                            if (selectedWallet.getAddress().equals(addressUsed)) {
                                WalletUtil.setCurrentWallet(this, wallets.get(0));
                            }
                            getOperation().forwardClearTop(MainActivity.class);
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
            case R.id.ll_etherscan_url:
                String etherUrl = "https://etherscan.io/address/";
                final String url = etherUrl + selectedWallet.getAddress();
                getOperation().addParameter("url", url);
                getOperation().addParameter("title", getString(R.string.etherscan));
                getOperation().forward(DefaultWebViewActivity.class);
                break;
            case R.id.btn_switch:
                WalletUtil.setCurrentWallet(this, selectedWallet);
                partnerDataManager.createPartner();
                partnerDataManager.activatePartner();
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
        selectedWallet.setWalletname(event.getName());
        WalletUtil.updateWallet(this, selectedWallet);
        title.setBTitle(event.getName());
    }
}
