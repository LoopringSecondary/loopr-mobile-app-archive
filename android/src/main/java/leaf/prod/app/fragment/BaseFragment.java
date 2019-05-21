package leaf.prod.app.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.app.view.Operation;

public abstract class BaseFragment extends LazyFragment {

    // Fragment的View
    protected View layout;

    protected BasePresenter presenter;

    private Operation mBaseOperation = null;

    private ProgressDialog progressDialog;

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
    protected abstract void initView();

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

    @Override
    protected void lazyLoad() {
    }

    public void showProgress(String message) {
        showProgress(message, false);
    }

    public void showProgress(String message, boolean cancelable) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(message);
                progressDialog.setCancelable(cancelable);
                progressDialog.show();
            } else {
                progressDialog.setCancelable(cancelable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgress() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
