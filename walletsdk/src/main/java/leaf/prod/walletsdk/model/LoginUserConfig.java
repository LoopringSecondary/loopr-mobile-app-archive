/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 上午10:06
 */
package leaf.prod.walletsdk.model;

import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserConfig {

    private String userId;

    private String language;

    private String currency;

    private List<WalletInfo> walletList;

    private Set<Contact> contacts;

    @Data
    @Builder
    private static class WalletInfo {

        private String walletname;

        private String address;

        private List<String> chooseTokenList;
    }
}
