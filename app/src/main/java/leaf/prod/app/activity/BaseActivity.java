package leaf.prod.app.activity;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import leaf.prod.app.R;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.app.utils.SystemStatusManager;
import leaf.prod.app.view.APP;
import leaf.prod.app.view.Operation;
import leaf.prod.app.views.swipeback.SwipeBackActivity;
import leaf.prod.app.views.swipeback.SwipeBackLayout;
//import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends SwipeBackActivity {

    public SwipeBackLayout mSwipeBackLayout;

    public BasePresenter presenter;

    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;

    /**
     * 共通操作
     **/
    private Operation mBaseOperation = null;

    //	private AlertDialog progressDialog;
    private ProgressDialog progressDialog;

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
        //		MobclickAgent.setCatchUncaughtExceptions(false);
        initPresenter();
        initTitle();
        initView();
        initData();
    }

    protected WeakReference<Activity> getWContext() {
        return context;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        if (android.os.Build.VERSION.SDK_INT >= 5) {
            overridePendingTransition(R.anim.translate_between_interface_left_in, R.anim.translate_between_interface_right_out);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (android.os.Build.VERSION.SDK_INT >= 5) {
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }

    /**
     * 初始化P层
     */
    protected abstract void initPresenter();

    /**
     * 初始化标题
     */
    public abstract void initTitle();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 0
     * 初始化数据
     */
    public abstract void initData();

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

    /**
     * 获取共通操作机能
     */
    public Operation getOperation() {
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

    public void showProgress(int messageResId) {
        showProgress(getString(messageResId));
    }

    public void showProgress(String message) {
        showProgress(message, false);
    }

    public void showProgress(String message, boolean cancelable) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(message);
                progressDialog.setCancelable(cancelable);
                progressDialog.show();
                //				final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this,R.style.MyDialog);
                //				View view= LayoutInflater.from(this).inflate(R.layout.progress_lottle,null);
                //				builder.setView(view);
                //				builder.setCancelable(false);
                //				progressDialog = builder.create();
                //				progressDialog.show();
            } else {
                //				progressDialog.setMessage(message);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
