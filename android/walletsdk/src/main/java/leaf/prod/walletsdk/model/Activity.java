package leaf.prod.walletsdk.model;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 5:22 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class Activity {

    private String owner;

    private Long block;

    private String txHash;

    private String activityType;

    private Long timestamp;

    private Long fiatValue;

    private String token;

    private String from;

    private Long nonce;

    private String txStatus;

    private Detail detail;

    @Data
    public static class Detail {

        private TokenTransfer tokenTransfer;

        @Data
        public static class TokenTransfer {

            private String address;

            private String token;

            private String amount;
        }
    }
}
