package com.tomcat360.lyqb.activity;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.views.TitleView;

import android.os.Bundle;

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
