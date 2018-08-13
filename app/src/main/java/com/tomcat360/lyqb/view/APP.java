package com.tomcat360.lyqb.view;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.tomcat360.lyqb.utils.AndroidUtils;
import com.tomcat360.lyqb.utils.SPUtils;

public class APP extends Application {
	private static APP mInstance;
	String appVersion;


	@Override
	public void onCreate() {
		super.onCreate();
		appVersion = AndroidUtils.getVersionName(this);
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
		mInstance = this;

	}

	public static APP getInstance() {
		return mInstance;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	public boolean hasLogin() {
		boolean flag = "1".equals(SPUtils.get(getApplicationContext(), "isLogin", "0"));
		return flag;
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		/**
		 * 设置 当手机字体大小变化时，APP重新设置为默认字体大小，防止不适配
		 */
		if (newConfig.fontScale != 1) {
			getResources();
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		if (res.getConfiguration().fontScale != 1) {//非默认值
			Configuration newConfig = new Configuration();
			newConfig.setToDefaults();//设置默认
			res.updateConfiguration(newConfig, res.getDisplayMetrics());
		}
		return res;
	}
}
