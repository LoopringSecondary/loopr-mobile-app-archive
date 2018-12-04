package leaf.prod.app.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.util.StringUtils;

public class P2PErrorActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.tv_error_info)
    TextView tvErrorInfo;

    @BindView(R.id.cl_need_token1)
    ConstraintLayout clNeedToken1;

    @BindView(R.id.cl_need_token2)
    ConstraintLayout clNeedToken2;

    @BindView(R.id.tv_token_amount1)
    TextView tvTokenAmount1;

    @BindView(R.id.tv_token_amount2)
    TextView tvTokenAmount2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_p2p_error);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitleNoBar(getResources().getString(R.string.send_result));
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        clNeedToken1.setVisibility(View.INVISIBLE);
        clNeedToken2.setVisibility(View.INVISIBLE);
        if (!StringUtils.isEmpty(getIntent().getStringExtra("error"))) {
            tvErrorInfo.setText(getIntent().getStringExtra("error"));
        }
        if (!StringUtils.isEmpty(getIntent().getStringExtra("tokenAmount1"))) {
            tvTokenAmount1.setText(getIntent().getStringExtra("tokenAmount1"));
            clNeedToken1.setVisibility(View.VISIBLE);
            if (!StringUtils.isEmpty(getIntent().getStringExtra("tokenAmount2"))) {
                tvTokenAmount2.setText(getIntent().getStringExtra("tokenAmount2"));
                clNeedToken2.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.btn_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_return:
                getOperation().forward(MainActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        getOperation().forward(MainActivity.class);
    }
}
