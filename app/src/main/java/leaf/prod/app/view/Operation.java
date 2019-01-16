package leaf.prod.app.view;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import leaf.prod.app.R;
import leaf.prod.app.utils.AppManager;

/**
 * 基本的操作共通抽取
 *
 * @version 1.0
 */
public class Operation {

    /**
     * 激活Activity组件意图
     **/
    public Intent mIntent = new Intent();

    /***上下文**/
    private Activity mContext = null;

    public Operation(Activity mContext) {
        this.mContext = mContext;
    }

    /**
     * 跳转Activity
     *
     * @param activity 需要跳转至的Activity
     */
    public void forward(Class<? extends Activity> activity) {
        mIntent.setClass(mContext, activity);
        mContext.startActivity(mIntent);
        mContext.overridePendingTransition(R.anim.translate_between_interface_right_in, R.anim.translate_between_interface_left_out);
        AppManager.getAppManager().show();
    }

    public void forwardUp(Class<? extends Activity> activity) {
        mIntent.setClass(mContext, activity);
        mContext.startActivity(mIntent);
        mContext.overridePendingTransition(R.anim.translate_between_interface_top_in, R.anim.translate_between_interface_bottom_out);
        AppManager.getAppManager().show();
    }

    public void forwardDown(Class<? extends Activity> activity) {
        mIntent.setClass(mContext, activity);
        mContext.startActivity(mIntent);
        mContext.overridePendingTransition(R.anim.translate_between_interface_bottom_in, R.anim.translate_between_interface_top_out);
        AppManager.getAppManager().show();
    }

    /**
     * 跳转Activity
     *
     * @param activity 需要跳转至的Activity
     */
    public void forwardClearTop(Class<? extends Activity> activity) {
        mIntent.setClass(mContext, activity);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(mIntent);
        mContext.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void forwardForResult(Class<? extends Activity> activity, int code) {
        mIntent.setClass(mContext, activity);
        mContext.startActivityForResult(mIntent, code);
        mContext.overridePendingTransition(R.anim.translate_between_interface_right_in, R.anim.translate_between_interface_left_out);
    }

    public void forwardUPForResult(Class<? extends Activity> activity, int code) {
        mIntent.setClass(mContext, activity);
        mContext.startActivityForResult(mIntent, code);
        mContext.overridePendingTransition(R.anim.translate_between_interface_top_in, R.anim.translate_between_interface_bottom_out);
    }

    /**
     * 设置传递参数
     *
     * @param value 数据传输对象
     */
    public void addParameter(Bundle value) {
        mIntent.putExtras(value);
    }

    /**
     * 设置传递参数 zhiwei.yin
     *
     * @param key   参数key
     * @param value 数据传输对象
     */
    public void addParameterSerializable(String key, Serializable value) {
        mIntent.putExtra(key, value);
    }

    /**
     * 获取传递参数：Bundle
     *
     * @param key 参数key
     */
    public Serializable getParameterSerializable(String key) {
        return mIntent.getExtras().getSerializable(key);
    }

    /**
     * 设置传递参数
     *
     * @param key   参数key
     * @param value 数据传输对象
     */
    public void addParameter(String key, Serializable value) {
        mIntent.putExtra(key, value);
    }

    public void addParameter(String key, boolean value) {
        mIntent.putExtra(key, value);
    }

    /**
     * 设置传递参数
     *
     * @param key   参数key
     * @param value 数据传输对象
     */
    public void addParameter(String key, String value) {
        mIntent.putExtra(key, value);
    }

    public void addParameter(String key, int value) {
        mIntent.putExtra(key, value);
    }

    public void addParameter(String key, long value) {
        mIntent.putExtra(key, value);
    }

    /**
     * 获取传递参数
     *
     * @param key 参数key
     */
    public String getParameter(String key) {
        return mIntent.getStringExtra(key);
    }

    /**
     * 获取传递参数
     *
     * @param key 参数key
     */
    public Integer getParameterInteger(String key) {
        return mIntent.getIntExtra(key, 0);
    }
}

