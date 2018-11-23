package leaf.prod.walletsdk.model.response.app;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan wangchen@loopring.org
 * Time: 2018-11-08 6:02 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class ThirdLoginResp {

    private Boolean success;

    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ThirdLoginResp{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
