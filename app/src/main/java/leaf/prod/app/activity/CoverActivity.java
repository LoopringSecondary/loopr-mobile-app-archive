package leaf.prod.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.utils.AppManager;

public class CoverActivity extends BaseActivity {

    @BindView(R.id.ll_import)
    LinearLayout rlImport;

    @BindView(R.id.ll_generate)
    LinearLayout rlGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cover);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
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
//        if (LanguageUtil.getLanguage(this) != LanguageUtil.getSettingLanguage(this)) {
//            LanguageUtil.changeLanguage(this, LanguageUtil.getSettingLanguage(this));
//            recreate();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initPresenter() {
    }

    @OnClick({R.id.ll_import, R.id.ll_generate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_import:
                getOperation().forward(ImportWalletActivity.class);
                break;
            case R.id.ll_generate:
                getOperation().forward(GenerateWalletActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        if (ThirdLoginUtil.getThirdLoginUserBean(CoverActivity.this) == null) {
//            getOperation().forwardClearTop(ThirdLoginActivity.class);
//        }
    }
}
