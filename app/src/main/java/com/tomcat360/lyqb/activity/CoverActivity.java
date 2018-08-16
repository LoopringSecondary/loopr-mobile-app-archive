package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomcat360.lyqb.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoverActivity extends BaseActivity {


    @BindView(R.id.tv_import)
    TextView tvImport;
    @BindView(R.id.rl_import)
    RelativeLayout rlImport;
    @BindView(R.id.tv_generate)
    TextView tvGenerate;
    @BindView(R.id.rl_generate)
    RelativeLayout rlGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cover);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
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

    @OnClick({R.id.rl_import, R.id.rl_generate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_import:
                getOperation().forward(ImportWalletActivity.class);
                break;
            case R.id.rl_generate:
                getOperation().forward(GenerateWalletActivity.class);
                break;
        }
    }
}
