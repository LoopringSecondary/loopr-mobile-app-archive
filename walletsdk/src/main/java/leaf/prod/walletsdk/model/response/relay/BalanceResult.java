package leaf.prod.walletsdk.model.response.relay;

import lombok.Data;

@Data
public class BalanceResult {

	private Account account;

	@Data
	public static class Account {

		private String address;

		private TokenBalance tokenBalance;

		@Data
		public static class TokenBalance {

			private String token;

			private String balance;

			private String allowance;

			private long block;
		}
	}
}
