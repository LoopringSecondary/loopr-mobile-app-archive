/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 上午10:06
 */
package leaf.prod.walletsdk.model.setting;

import java.util.List;

import leaf.prod.walletsdk.model.market.MarketPair;
import leaf.prod.walletsdk.model.wallet.Contact;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserConfig {

    private String userId;

    private String language;

    private String currency;

    private List<WalletInfo> walletList;

    private List<Contact> contacts;

    private List<MarketPair> favMarkets;

    @Data
    @Builder
    private static class WalletInfo {

        private String walletname;

        private String address;

        private List<String> chooseTokenList;
    }
}
