package com.tomcat360.lyqb.activity;

import android.os.Bundle;

import com.tomcat360.lyqb.R;

import butterknife.ButterKnife;

/**
 * Created by niedengqiang on 2018/8/13.
 */

public class MainActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initTitle() {

    }
}
