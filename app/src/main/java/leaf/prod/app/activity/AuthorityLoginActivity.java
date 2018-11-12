package leaf.prod.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.presenter.AuthorityLoginPresenter;

public class AuthorityLoginActivity extends BaseActivity {

    @BindView(R.id.return_btn)
    Button returnBtn;

    @BindView(R.id.authority_btn)
    Button authorityBtn;

    private AuthorityLoginPresenter authorityLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_authority_login);
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
        authorityLoginPresenter = new AuthorityLoginPresenter(this, this);
    }

    @OnClick({R.id.return_btn, R.id.authority_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.return_btn:
                finish();
                getOperation().forward(MainActivity.class);
                break;
            case R.id.authority_btn:
                authorityLoginPresenter.showPasswordDialog(getIntent().getStringExtra("login_info"));
                break;
        }
    }
}
