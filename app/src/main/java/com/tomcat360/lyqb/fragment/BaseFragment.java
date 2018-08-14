package com.tomcat360.lyqb.fragment;

import android.os.Bundle;

import com.tomcat360.lyqb.view.Operation;
import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends LazyFragment {
    private Operation mBaseOperation = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//		setTranslucentStatus();
        mBaseOperation = new Operation(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

//	/**
//	 * 设置状态栏背景状态
//	 */
//	@SuppressLint("InlinedApi")
//	private void setTranslucentStatus() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			Window win = getActivity().getWindow();
//			WindowManager.LayoutParams winParams = win.getAttributes();
//			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//			winParams.flags |= bits;
//			win.setAttributes(winParams);
//		}
//		SystemStatusManager tintManager = new SystemStatusManager(
//				this.getActivity());
//		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setStatusBarTintResource(0);// 状态栏无背景
//	}

    /**
     * 获取共通操作机能
     */
    public Operation getOperation() {
        return this.mBaseOperation;
    }

    @Override
    protected void lazyLoad() {

    }


}
