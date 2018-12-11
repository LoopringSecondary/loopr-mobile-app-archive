package leaf.prod.walletsdk.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 上午10:06
 */
@Data
@Builder
public class LoginUserConfig {

    private String userId;

    private String language;

    private String currency;

    private List<WalletInfo> walletList;

    @Data
    @Builder
    private static class WalletInfo {

        private String walletname;

        private String address;

        private List<String> chooseTokenList;
    }
}
