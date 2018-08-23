package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExportKeystoreActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_export_keystore);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.export_keystore));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etPassword.getText().toString())){
            ToastUtils.toast("请输入密码");
            return;
        }
        getOperation().forward(ExportKeystoreDetailActivity.class);
    }
}
