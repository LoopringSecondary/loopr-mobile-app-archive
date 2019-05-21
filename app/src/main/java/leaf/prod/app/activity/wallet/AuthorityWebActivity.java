package leaf.prod.app.activity.wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.presenter.wallet.AuthorityWebPresenter;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.walletsdk.model.common.QRCodeType;
import leaf.prod.walletsdk.util.WalletUtil;

public class AuthorityWebActivity extends BaseActivity {

    @BindView(R.id.return_btn)
    Button returnBtn;

    @BindView(R.id.authority_btn)
    Button authorityBtn;

    private AuthorityWebPresenter authorityLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_authority_web);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    public void initTitle() {
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initPresenter() {
        String info = getIntent().getStringExtra("qrcode_info");
        QRCodeType type = QRCodeType.valueOf(getIntent().getStringExtra("qrcode_type"));
        authorityLoginPresenter = new AuthorityWebPresenter(this, this, info, type);
    }

    @OnClick({R.id.return_btn, R.id.authority_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.return_btn:
                finish();
                getOperation().forward(MainActivity.class);
                break;
            case R.id.authority_btn:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    try {
                        if (WalletUtil.needPassword(this)) {
                            authorityLoginPresenter.showPasswordDialog();
                        } else {
                            authorityLoginPresenter.handle(WalletUtil.getCredential(this, ""));
                        }
                    } catch (Exception e) {
                        RxToast.error(getResources().getString(R.string.authority_login_error));
                    }
                }
                break;
        }
    }
}
