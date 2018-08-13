package com.tomcat360.lyqb.net;

import android.content.Context;



public final class G {

	private G() {
	}





	public static final String URL_PREFIX = "http://183.129.157.218:2000/Api-App";//新测试环境
	public static final String BASE_URL = URL_PREFIX + "/";//新测试环境

	public static final String TRUE = "true";

	/**
	 * 生成tag
	 */
	public static String tag() {
		return new Throwable().getStackTrace()[1].getClassName();
	}

	/**
	 * 根据前缀生成tag
	 */
	public static String tag(String prefix) {
		return prefix + "-" + new Throwable().getStackTrace()[1].getClassName();
	}




}
