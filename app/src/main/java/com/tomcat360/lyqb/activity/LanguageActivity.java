package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.iv_english_check)
    ImageView ivEnglishCheck;
    @BindView(R.id.ll_english)
    LinearLayout llEnglish;
    @BindView(R.id.iv_chinese_check)
    ImageView ivChineseCheck;
    @BindView(R.id.ll_chinese)
    LinearLayout llChinese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle("语言");
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.ll_english, R.id.ll_chinese})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_english:
                ivEnglishCheck.setVisibility(View.VISIBLE);
                ivChineseCheck.setVisibility(View.GONE);
                break;
            case R.id.ll_chinese:
                ivEnglishCheck.setVisibility(View.GONE);
                ivChineseCheck.setVisibility(View.VISIBLE);
                break;
        }
    }
}
