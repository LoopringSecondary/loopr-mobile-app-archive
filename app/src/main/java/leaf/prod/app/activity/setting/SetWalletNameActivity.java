/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-10-09 1:57 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.activity.wallet.MainActivity;
import leaf.prod.app.utils.AppManager;
import leaf.prod.app.utils.DialogUtil;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.model.wallet.WalletEntity;
import leaf.prod.walletsdk.util.WalletUtil;

public class SetWalletNameActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.wallet_name)
    MaterialEditText walletName;

    @BindView(R.id.btn_unlock)
    Button btnUnlock;

    @BindView(R.id.password_hint)
    TextView passwordHint;

    private Animation shakeAnimation;

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
        title.setBTitle(getResources().getString(R.string.set_wallet_name));
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
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_x);
    }

    @OnClick(R.id.btn_unlock)
    public void onViewClicked() {
        if (TextUtils.isEmpty(walletName.getText().toString().trim())) {
            passwordHint.setText(getResources().getString(R.string.wallet_name_hint));
            passwordHint.setVisibility(View.VISIBLE);
            passwordHint.setAnimation(shakeAnimation);
        } else if (WalletUtil.isWalletExisted(this, walletName.getText().toString().trim())) {
            passwordHint.setText(getResources().getString(R.string.wallet_name_existed));
            passwordHint.setVisibility(View.VISIBLE);
            passwordHint.setAnimation(shakeAnimation);
        } else {
            WalletEntity newWallet = (WalletEntity) getIntent().getSerializableExtra("newWallet");
            newWallet.setWalletname(walletName.getText().toString());
            WalletUtil.addWallet(this, newWallet);
            DialogUtil.showWalletCreateResultDialog(this, v -> {
                DialogUtil.dialog.dismiss();
                AppManager.getAppManager().finishAllActivity();
                getOperation().forward(MainActivity.class);
            });
        }
    }
}
