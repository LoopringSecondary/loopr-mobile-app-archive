package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCharacterActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.iv_personal)
    ImageView ivPersonal;
    @BindView(R.id.personal_check)
    ImageView personalCheck;
    @BindView(R.id.iv_city)
    ImageView ivCity;
    @BindView(R.id.city_check)
    ImageView cityCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_character);
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

    @OnClick({R.id.iv_personal, R.id.iv_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_personal:
                break;
            case R.id.iv_city:
                break;
        }
    }
}
