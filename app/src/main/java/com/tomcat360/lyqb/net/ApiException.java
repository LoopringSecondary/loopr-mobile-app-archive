package com.tomcat360.lyqb.net;

/**
 * Title:ApiException
 * Package:com.tomcat360.m.NetEntity
 * Description:
 * Author: wwh@tomcat360.com
 * Date: 16/8/24
 * Version: V1.0.0
 * 版本号修改日期修改人修改内容
 */

public class ApiException extends Exception {
	public String code;
	public String message;

	public ApiException(Throwable throwable, String code) {
		super(throwable);
		this.code = code;

	}
}
