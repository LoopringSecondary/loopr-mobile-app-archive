/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-10-09 1:57 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.model.WalletEntity;
import leaf.prod.app.utils.AppManager;
import leaf.prod.app.utils.DialogUtil;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.utils.ToastUtils;
import leaf.prod.app.utils.WalletUtil;
import leaf.prod.app.views.TitleView;

public class SetWalletNameActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.wallet_name)
    MaterialEditText walletName;

    @BindView(R.id.btn_unlock)
    Button btnUnlock;

    /**
     * 初始化P层
     */
    @Override
    protected void initPresenter() {
    }

    /**
     * 初始化标题
     */
    @Override
    public void initTitle() {
        title.setBTitle("设置钱包名称");
        title.clickLeftGoBack(getWContext());
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
    }

    /**
     * 0
     * 初始化数据
     */
    @Override
    public void initData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_wallet_name);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.btn_unlock)
    public void onViewClicked() {
        if (TextUtils.isEmpty(walletName.getText().toString())) {
            ToastUtils.toast("请输入钱包名称");
            return;
        }
        WalletEntity newWallet = (WalletEntity) getIntent().getSerializableExtra("newWallet");
        newWallet.setWalletname(walletName.getText().toString());
        if (WalletUtil.isWalletExisted(this, newWallet)) {
            ToastUtils.toast("请输入钱包名称");
            return;
        }
        List<WalletEntity> list = SPUtils.getWalletDataList(this, "walletlist", WalletEntity.class);
        list.add(newWallet);
        SPUtils.setDataList(this, "walletlist", list);
        DialogUtil.showWalletCreateResultDialog(this, v -> {
            DialogUtil.dialog.dismiss();
            AppManager.finishAll();
            getOperation().forward(MainActivity.class);
        });
    }
}
