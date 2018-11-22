package leaf.prod.walletsdk.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-22 10:37 AM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class RandomWallet {

    private String address;

    private String privateKey;
}
