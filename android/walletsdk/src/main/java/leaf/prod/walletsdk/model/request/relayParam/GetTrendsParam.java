package leaf.prod.walletsdk.model.request.relayParam;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetTrendsParam {

    private String market;

    private String interval;
}
