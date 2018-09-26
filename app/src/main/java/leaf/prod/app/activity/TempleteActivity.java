package leaf.prod.app.activity;

import android.os.Bundle;

import leaf.prod.app.R;
import leaf.prod.app.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TempleteActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_templete);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle("");
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
    }
}
