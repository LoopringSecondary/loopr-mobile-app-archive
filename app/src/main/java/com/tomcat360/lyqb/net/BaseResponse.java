package com.tomcat360.lyqb.net;

/**
 * Title:HttpResponse
 */

public class BaseResponse<T> {

    public Body<T> body;

    private String success;

    private String errCode;

    private String errMsg;

    private int size;

    public Body<T> getBody() {
        return body;
    }

    public void setBody(Body<T> body) {
        this.body = body;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "BaseResponse{" + "success='" + success + '\'' + ", errCode='" + errCode + '\'' + ", errMsg='" + errMsg + '\'' + ", size=" + size + ", body=" + body + '}';
    }

    public static class Body<T> {

        public T data;

        public int pageIndex;

        public int pageSize;

        public int total;

        public int totalPage;

        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Body{" + "data=" + data + ", pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", total=" + total + ", totalPage=" + totalPage + '}';
        }
    }
}
