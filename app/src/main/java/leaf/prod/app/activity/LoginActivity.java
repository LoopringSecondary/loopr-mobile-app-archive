package leaf.prod.app.activity;

import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.views.TitleView;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void initView() {
    }

    @Override
    public void initTitle() {
        title.setBTitle("首页");
        title.clickLeftGoBack(getWContext());
    }
}
