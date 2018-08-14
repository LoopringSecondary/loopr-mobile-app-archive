package com.tomcat360.lyqb.activity;

import android.os.Bundle;

import com.tomcat360.lyqb.R;

import butterknife.ButterKnife;

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

    }

    @Override
    public void initData() {

    }
}
