package com.tomcat360.lyqb.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
    }

    @Override
    public void initView() {
        mHandler.sendEmptyMessageDelayed(1,1000);
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.imageview)
    public void onViewClicked() {

    }
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if ((boolean) SPUtils.get(SplashActivity.this,"hasWallet",false)) {
                getOperation().forward(MainActivity.class);
            }else {
                getOperation().forward(CoverActivity.class);
            }
            finish();
        }
    };
}
