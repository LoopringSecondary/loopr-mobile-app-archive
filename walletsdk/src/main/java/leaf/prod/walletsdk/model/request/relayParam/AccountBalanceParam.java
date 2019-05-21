package leaf.prod.walletsdk.model.request.relayParam;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 4:11 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class AccountBalanceParam {

    private List<String> addresses;

    private List<String> tokens;

    private boolean allTokens;
}
