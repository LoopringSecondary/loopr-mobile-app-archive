package leaf.prod.app.activity.setting;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.walletsdk.model.WalletEntity;
import leaf.prod.walletsdk.model.eventbusData.NameChangeData;
import leaf.prod.walletsdk.util.WalletUtil;
import leaf.prod.app.views.TitleView;

public class ReviseWalletNameActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.wallet_name)
    MaterialEditText walletName;

    @BindView(R.id.ll_clear_records)
    LinearLayout llClearRecords;

    private List<WalletEntity> list;

    private WalletEntity selectedWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_revise_wallet_name);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.wallet_name));
        title.clickLeftGoBack(getWContext());
        title.setRightText(getResources().getString(R.string.save), button -> {
            if (TextUtils.isEmpty(walletName.getText().toString())) {
                RxToast.error(getResources().getString(R.string.wallet_name_hint));
            } else if (selectedWallet.getWalletname().equalsIgnoreCase(walletName.getText().toString())) {
                finish();
            } else if (WalletUtil.isWalletExisted(this, walletName.getText().toString())) {
                RxToast.error(getResources().getString(R.string.wallet_name_existed));
            } else {
                /**
                 * 更新钱包配置
                 */
                selectedWallet.setWalletname(walletName.getText().toString());
                WalletUtil.updateWallet(this, selectedWallet);
                EventBus.getDefault().post(new NameChangeData(walletName.getText().toString()));
                finish();
            }
        });
    }

    @Override
    public void initView() {
        selectedWallet = (WalletEntity) getIntent().getSerializableExtra("selectedWallet");
        list = WalletUtil.getWalletList(this);
        walletName.setText(selectedWallet.getWalletname());
    }

    @Override
    public void initData() {
    }
}
