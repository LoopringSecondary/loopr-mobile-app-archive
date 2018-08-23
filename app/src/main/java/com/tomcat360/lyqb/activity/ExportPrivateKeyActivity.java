package com.tomcat360.lyqb.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExportPrivateKeyActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.tv_private_key)
    TextView tvPrivateKey;
    @BindView(R.id.btn_copy_private_key)
    Button btnCopyPrivateKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_export_private_key);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.export_private_key));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        tvPrivateKey.setText("hdsjkdhskhdskhfdhsdbajbdjabdjw");
    }


    @OnClick(R.id.btn_copy_private_key)
    public void onViewClicked() {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(tvPrivateKey.getText());
        ToastUtils.toast("复制成功，可以发给朋友们了。");
    }


}
