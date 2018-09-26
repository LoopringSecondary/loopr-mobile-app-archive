package leaf.prod.walletsdk.model.request.param;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotifyTransactionSubmitParam {

    private String hash;

    private String nonce;

    private String to;

    private String value;

    private String gasPrice;

    private String gas;

    private String input;

    private String from;

    private String v;

    private String r;

    private String s;
}
