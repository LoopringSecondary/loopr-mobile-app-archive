package leaf.prod.walletsdk.model.response.relay;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-04-08 5:17 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class MetadataResult {

	private MetadataChanged metadataChanged;

	@Data
	public static class MetadataChanged {

		private boolean tokenMetadataChanged;

		private boolean tokenInfoChanged;

		private boolean marketMetadataChanged;

		private boolean tickerChanged;
	}
}
