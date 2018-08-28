package com.tomcat360.lyqb.activity;

import android.content.Intent;
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

public class ExportMatchPasSafeActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private int type;  //密码验证类型，1为备份助记词验证密码  2为导出私钥验证密码  3为导出keystore验证密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_export_keystore);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        type = getIntent().getIntExtra("type",1);
        title.setBTitle(getResources().getString(R.string.match_password));
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
        if (type == 1){
            getOperation().forward(BackupMnemonicActivity.class);
        }else if (type == 2){
            getOperation().forward(ExportPrivateKeyActivity.class);
        }else if (type == 3) {
            getOperation().forward(ExportKeystoreDetailActivity.class);
        }
    }
}
