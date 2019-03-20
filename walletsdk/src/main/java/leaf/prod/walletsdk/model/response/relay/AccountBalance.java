package leaf.prod.walletsdk.model.response.relay;

import java.io.Serializable;
import java.util.Map;

import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.util.StringUtils;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 2:13 PM
 * Cooperation: loopring.org 路印协议基金会
 * RELAY2.0
 */
@Data
public class AccountBalance implements Serializable {

    private Map<String, TokenBalanceMap> accountBalances;

    @Data
    public static class TokenBalanceMap {

        private String address;

        private Map<String, TokenBalance> tokenBalanceMap;

        public void convert() {
            if (this.tokenBalanceMap != null) {
                for (String tokenBalance : tokenBalanceMap.keySet()) {
                    tokenBalanceMap.get(tokenBalance).convert();
                }
            }
        }

        @Data
        static class TokenBalance {

            private String token;

            private String tokenSymbol;

            private String balance;

            private Double balanceDouble;

            private String allowance;

            private Double allowanceDouble;

            private String availableBalance;

            private Double availableBalanceDouble;

            private String availableAllowance;

            private Double availableAllowanceDouble;

            public void convert() {
                this.balanceDouble = !StringUtils.isEmpty(balance) ? Numeric.toBigInt(balance).doubleValue() : 0;
                this.allowanceDouble = !StringUtils.isEmpty(allowance) ? Numeric.toBigInt(allowance).doubleValue() : 0;
                this.availableBalanceDouble = !StringUtils.isEmpty(availableBalance) ? Numeric.toBigInt(availableBalance)
                        .doubleValue() : 0;
                this.availableAllowanceDouble = !StringUtils.isEmpty(availableAllowance) ? Numeric.toBigInt(availableAllowance)
                        .doubleValue() : 0;
            }
        }
    }

    public void convert() {
        if (this.accountBalances != null) {
            for (String key : this.accountBalances.keySet()) {
                this.accountBalances.get(key).convert();
            }
        }
    }
}
