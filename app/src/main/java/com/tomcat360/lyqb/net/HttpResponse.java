package com.tomcat360.lyqb.net;

/**
 * Title:HttpResponse
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
