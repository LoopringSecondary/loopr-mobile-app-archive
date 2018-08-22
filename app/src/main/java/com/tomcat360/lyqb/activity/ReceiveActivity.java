package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.TitleView;
import com.vondear.rxfeature.tool.RxQRCode;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vondear.rxtool.RxConstants.SP_MADE_CODE;

public class ReceiveActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.coin_name)
    TextView coinName;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.coin_address)
    TextView coinAddress;
    @BindView(R.id.btn_copy)
    Button btnCopy;
    @BindView(R.id.btn_save)
    Button btnSave;

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
        String str = "收款地址";
        coinAddress.setText(str);

        //二维码生成方式一  推荐此方法
        RxQRCode.builder(str).
                backColor(0xFFFFFFFF).
                codeColor(0xFF000000).
                codeSide(600).
                into(ivCode);
    }

    @OnClick({R.id.btn_copy, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_copy:
                break;
            case R.id.btn_save:
                break;
        }
    }
}
