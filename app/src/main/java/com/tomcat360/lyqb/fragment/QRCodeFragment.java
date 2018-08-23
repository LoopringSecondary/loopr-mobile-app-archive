package com.tomcat360.lyqb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tomcat360.lyqb.R;
import com.vondear.rxfeature.tool.RxQRCode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 *
 */
public class QRCodeFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.iv_code)
    ImageView ivCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_qr_code, container, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String str = "收款地址";

        //二维码生成方式一  推荐此方法
        RxQRCode.builder(str).
                backColor(0xFFFFFFFF).
                codeColor(0xFF000000).
                codeSide(800).
                into(ivCode);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
