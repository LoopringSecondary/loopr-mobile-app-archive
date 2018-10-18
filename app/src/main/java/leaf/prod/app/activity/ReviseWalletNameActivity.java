package leaf.prod.app.activity;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.model.WalletEntity;
import leaf.prod.app.model.eventbusData.NameChangeData;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.utils.ToastUtils;
import leaf.prod.app.views.TitleView;

public class ReviseWalletNameActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.wallet_name)
    MaterialEditText walletName;

    @BindView(R.id.ll_clear_records)
    LinearLayout llClearRecords;

    private int position;

    private List<WalletEntity> list;

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
                ToastUtils.toastError(getResources().getString(R.string.wallet_name_hint));
            } else if (getIntent().getStringExtra("walletname").equalsIgnoreCase(walletName.getText().toString())) {
                finish();
            } else {
                for (WalletEntity walletEntity : list) {
                    if (walletEntity.getWalletname().equalsIgnoreCase(walletName.getText().toString())) {
                        ToastUtils.toastError(getResources().getString(R.string.wallet_name_existed));
                        return;
                    }
                }
                list.get(position).setWalletname(walletName.getText().toString());
                SPUtils.setDataList(ReviseWalletNameActivity.this, "walletlist", list);
                EventBus.getDefault().post(new NameChangeData(walletName.getText().toString()));
                finish();
            }
        });
    }

    @Override
    public void initView() {
        list = SPUtils.getWalletDataList(this, "walletlist", WalletEntity.class);//多钱包，将钱包信息存在本地
        position = getIntent().getIntExtra("position", 0);
        walletName.setText(getIntent().getStringExtra("walletname"));
    }

    @Override
    public void initData() {
    }
}
