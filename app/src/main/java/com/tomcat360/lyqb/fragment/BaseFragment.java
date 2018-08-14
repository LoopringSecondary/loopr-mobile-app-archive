package com.tomcat360.lyqb.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tomcat360.lyqb.view.Operation;

public abstract class BaseFragment extends LazyFragment {
    private Operation mBaseOperation = null;
    // Fragment的View
    protected View layout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBaseOperation = new Operation(getActivity());
        initPresenter();
        initView();
        initData();
    }

    /**
     * 初始化P层
     */
    protected abstract void initPresenter();
    /**
     * 初始化视图
     */
    protected abstract  void initView();
    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 获取共通操作机能
     */
    public Operation getOperation() {
        return this.mBaseOperation;
    }

    /**
     * 吐司
     * @param msg 消息
     */
    public void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void lazyLoad() {

    }


}
