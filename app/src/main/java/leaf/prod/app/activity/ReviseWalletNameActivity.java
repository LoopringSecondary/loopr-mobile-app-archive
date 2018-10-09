package leaf.prod.app.activity;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
        title.setRightButton(getResources().getString(R.string.save), new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                if (TextUtils.isEmpty(walletName.getText().toString())) {
                    ToastUtils.toast("请输入钱包名称");
                    return;
                } else {
                    list.get(position).setWalletname(walletName.getText().toString());
                    SPUtils.setDataList(ReviseWalletNameActivity.this, "walletlist", list);
                    EventBus.getDefault().post(new NameChangeData(walletName.getText().toString()));
                }
                //                SPUtils.put(ReviseWalletNameActivity.this, "walletname", walletName.getText().toString());
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
