package leaf.prod.walletsdk.pojo.loopring.response.data;

import java.math.BigInteger;

import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan wangchen@loopring.org
 * Time: 2018-11-12 12:16 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class ScanLoginInfo {

    private String owner;

    private String uuid;

    private LoginSign sign;

    @Data
    @Builder
    public static class LoginSign {

        private String timestamp;

        private BigInteger v;

        private String r;

        private String s;

        private String owner;
    }
}
