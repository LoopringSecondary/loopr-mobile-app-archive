package leaf.prod.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.presenter.AuthorityWebPresenter;
import leaf.prod.walletsdk.model.QRCodeType;

public class AuthorityWebActivity extends BaseActivity {

    @BindView(R.id.return_btn)
    Button returnBtn;

    @BindView(R.id.authority_btn)
    Button authorityBtn;

    private AuthorityWebPresenter authorityLoginPresenter;

    private QRCodeType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_authority_web);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
        this.type = QRCodeType.valueOf(getIntent().getStringExtra("qrcode_type"));
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
        authorityLoginPresenter = new AuthorityWebPresenter(this, this);
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
                    if (WalletUtil.needPassword(this)) {
                        authorityLoginPresenter.showPasswordDialog(getIntent().getStringExtra("login_info"));
                    } else {
                        authorityLoginPresenter.sign(getIntent().getStringExtra("login_info"), "");
                    }
                }
                break;
        }
    }
}
