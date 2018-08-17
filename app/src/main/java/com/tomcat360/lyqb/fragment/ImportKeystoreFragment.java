package com.tomcat360.lyqb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 *
 */
public class ImportKeystoreFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_mnemonic)
    MaterialEditText etMnemonic;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;
    @BindView(R.id.btn_unlock)
    Button btnUnlock;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_import_keystore, container, false);
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

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_unlock)
    public void onViewClicked() {

        if (TextUtils.isEmpty(etMnemonic.getText().toString())){
            ToastUtils.toast("请输入keystore文件");
            return;
        }
        if (TextUtils.isEmpty(etPassword.getText().toString())){
            ToastUtils.toast("请输入keystore密码");
            return;
        }


    }
}
