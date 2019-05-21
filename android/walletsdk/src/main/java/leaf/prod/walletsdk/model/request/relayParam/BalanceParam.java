package leaf.prod.walletsdk.model.request.relayParam;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceParam {

	private ParamsForAccounts paramsForAccounts;

	@Data
	@Builder
	public static class ParamsForAccounts {

		private List<String> addresses;

		private List<String> tokens;
	}
}
