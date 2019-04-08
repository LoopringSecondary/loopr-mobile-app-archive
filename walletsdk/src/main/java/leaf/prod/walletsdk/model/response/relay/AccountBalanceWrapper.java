package leaf.prod.walletsdk.model.response.relay;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 2:13 PM
 * Cooperation: loopring.org 路印协议基金会
 * RELAY2.0
 */
@Data
public class AccountBalanceWrapper implements Serializable {

	private Map<String, TokenBalanceMap> accountBalances;

	@Data
	public static class TokenBalanceMap {

		private String address;

		private Map<String, TokenBalance> tokenBalanceMap;

		@Data
		public static class TokenBalance {

			private String token;

			private String balance;

			private String allowance;

			private String availableBalance;

			private String availableAllowance;
		}
	}
}
