package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.view.View;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceiveActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_receive);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle("Receive Code");
        title.clickLeftGoBack(getWContext());
        title.setRightImageButton(R.mipmap.icon_share, new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                ToastUtils.toast("分享");
            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
