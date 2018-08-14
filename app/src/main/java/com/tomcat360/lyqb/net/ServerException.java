package com.tomcat360.lyqb.net;

/**
 * Title:ServerException
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
