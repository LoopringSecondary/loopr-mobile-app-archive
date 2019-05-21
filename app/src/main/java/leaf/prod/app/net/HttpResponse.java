package leaf.prod.app.net;

/**
 * Title:HttpResponse
 */

public class HttpResponse<T> {

    public T body;

    private RespHeadEntity respHead;

    public RespHeadEntity getRespHead() {
        return respHead;
    }

    public void setRespHead(RespHeadEntity respHead) {
        this.respHead = respHead;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
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
}
