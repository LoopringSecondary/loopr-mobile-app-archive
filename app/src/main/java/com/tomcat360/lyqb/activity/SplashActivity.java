package com.tomcat360.lyqb.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.SPUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if ((boolean) SPUtils.get(SplashActivity.this, "hasWallet", false)) {
                getOperation().forward(MainActivity.class);
            } else {
                getOperation().forward(CoverActivity.class);
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
    }

    @Override
    public void initView() {
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void initData() {
    }

    @OnClick(R.id.imageview)
    public void onViewClicked() {
    }
}
