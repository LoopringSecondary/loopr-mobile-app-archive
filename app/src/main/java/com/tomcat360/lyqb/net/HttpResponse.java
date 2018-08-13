package com.tomcat360.lyqb.net;

/**
 * Title:HttpResponse
 * Package:com.tomcat360.m.RespEntity
 * Description:TODO
 * Author: wwh@tomcat360.com
 * Date: 16/8/24
 * Version: V1.0.0
 * 版本号修改日期修改人修改内容
 */

public class HttpResponse<T> {

	private RespHeadEntity respHead;
	public T body;

	public RespHeadEntity getRespHead() {
		return respHead;
	}

	public void setRespHead(RespHeadEntity respHead) {
		this.respHead = respHead;
	}

	public static class RespHeadEntity {
		private String respCode;
		private String respMsg;

		public String getRespCode() {
			return respCode;
		}

		public void setRespCode(String respCode) {
			this.respCode = respCode;
		}

		public String getRespMsg() {
			return respMsg;
		}

		public void setRespMsg(String respMsg) {
			this.respMsg = respMsg;
		}
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
}
