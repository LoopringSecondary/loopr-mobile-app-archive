package com.tomcat360.lyqb.net;

/**
 * Title:ServerException
 * Package:com.tomcat360.m.NetEntity
 * Description:TODO
 * Author: wwh@tomcat360.com
 * Date: 16/8/24
 * Version: V1.0.0
 * 版本号修改日期修改人修改内容
 */

public class ServerException extends RuntimeException {
	public String code;
	public String message;

	public ServerException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

}
