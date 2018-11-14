package leaf.prod.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.presenter.ConvertPresenter;
import leaf.prod.app.views.TitleView;

public class ConvertActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.switch_img)
    ImageView switchImg;

    private ConvertPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_convert);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
        presenter = new ConvertPresenter(this, this);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.convert));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        switchImg.setOnClickListener(view -> {
            presenter.switchToken();
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @OnClick({R.id.ll_show_fee, R.id.max, R.id.btn_convert})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_show_fee:
                presenter.showFeeDialog();
                break;
            case R.id.max:
                presenter.setMax();
                break;
            case R.id.btn_convert:
                presenter.convert();
                break;
        }
    }
}
