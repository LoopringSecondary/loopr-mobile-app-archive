package com.tomcat360.lyqb.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.SystemStatusManager;
import com.tomcat360.lyqb.view.APP;
import com.tomcat360.lyqb.view.Operation;
import com.tomcat360.lyqb.views.swipeback.SwipeBackActivity;
import com.tomcat360.lyqb.views.swipeback.SwipeBackLayout;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;


public abstract class BaseActivity extends SwipeBackActivity {

	private SwipeBackLayout mSwipeBackLayout;


	/**当前Activity的弱引用，防止内存泄露**/
	private WeakReference<Activity> context = null;
	
	/**共通操作**/
	private Operation mBaseOperation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTranslucentStatus();
		mSwipeBackLayout = getSwipeBackLayout();
		// 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		//将当前Activity压入栈
		context = new WeakReference<Activity>(this);
		mBaseOperation = new Operation(this);
		MobclickAgent.setCatchUncaughtExceptions(false);
		initTitle();
		initView();
	}

	protected WeakReference<Activity> getWContext(){
		return context;
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 友盟流量统计
		MobclickAgent.onResume(this);
		initData();
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen");
		MobclickAgent.onPause(this);
	}

	@Override
	public void finish() {
		super.finish();
		if (android.os.Build.VERSION.SDK_INT >= 5) {
			overridePendingTransition(
					R.anim.translate_between_interface_left_in,
					R.anim.translate_between_interface_right_out);
		}
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		if (android.os.Build.VERSION.SDK_INT >= 5) {
			overridePendingTransition(
					R.anim.zoomin,
					R.anim.zoomout);
		}
	}

	/**0
	 * 初始化数据
	 */
	public abstract void initData();

	/**
	 * 初始化视图
	 */
	public abstract  void initView();

	/**
	 * 初始化标题
	 */
	public abstract void initTitle();

	/**
	 * 设置状态栏背景状态
	 */
	@SuppressLint("InlinedApi")
	private void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(0);// 状态栏无背景
	}

	@Override
	public APP getApplicationContext() {
		return (APP) super.getApplicationContext();
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == REQ_CODE_LOGIN) {
//			if (resultCode == RET_CODE_LOGIN_SUCCESS) {
//				this.startActivity(this.afterLoginIntent);
//			}
//		} else {
//			super.onActivityResult(requestCode, resultCode, data);
//		}
//	}

//	public void startActivityAfterLogin(Intent intent) {
//		this.afterLoginIntent = intent;
//		if (this.getApplicationContext().hasLogin()) {
//			this.startActivity(this.afterLoginIntent);
//			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//		} else {
//			Intent loginIntent = new Intent();
//			loginIntent.setClass(this, LoginActivity.class);
//			loginIntent.putExtra("setFnum", REQ_TOLOGIN);
//			this.startActivityForResult(loginIntent, REQ_CODE_LOGIN);
//			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//		}
//	}

	/**
	 * 获取共通操作机能
	 */
	public Operation getOperation(){
		return this.mBaseOperation;
	}

	/**
	 * 当ui元素都不可见时，如切换到另一个app，进行资源释放操作
	 */
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		switch (level) {
		case TRIM_MEMORY_UI_HIDDEN:
			// 进行资源释放操作
			break;
		}
	}

	private AlertDialog progressDialog;
	public void showProgress(int messageResId) {
		showProgress(getString(messageResId));
	}
	public void showProgress(String message) {
		showProgress(message, false);
	}
	public void showProgress(String message, boolean cancelable) {
		try {
			if (progressDialog == null) {
//				progressDialog = new ProgressDialog(this);
//				progressDialog.setMessage(message);
//				progressDialog.setCancelable(cancelable);
//				progressDialog.show();
				final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this,R.style.MyDialog);
				View view= LayoutInflater.from(this).inflate(R.layout.progress_lottle,null);
				builder.setView(view);
				builder.setCancelable(false);
				progressDialog = builder.create();
				progressDialog.show();
			}
			else {
//				progressDialog.setMessage(message);
				progressDialog.setCancelable(cancelable);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hideProgress() {
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
				progressDialog = null;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
