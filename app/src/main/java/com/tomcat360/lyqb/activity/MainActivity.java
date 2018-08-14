package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by niedengqiang on 2018/8/13.
 */

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.title)
    TitleView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initTitle() {
        title.setBTitle("首页");
        title.clickLeftGoBack(getWContext());
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        getOperation().forward(LoginActivity.class);
    }
}
