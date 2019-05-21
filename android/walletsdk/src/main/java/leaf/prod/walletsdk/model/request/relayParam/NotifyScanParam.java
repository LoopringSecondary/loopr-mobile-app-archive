/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-12 12:16 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.request.relayParam;

import java.math.BigInteger;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotifyScanParam {

    private String owner;

    private String uuid;

    private SignParam sign;

    @Data
    @Builder
    public static class SignParam {

        private String timestamp;

        private BigInteger v;

        private String r;

        private String s;

        private String owner;
    }
}
