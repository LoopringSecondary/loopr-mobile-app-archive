package leaf.prod.walletsdk.model.request.crawlerParam;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IndexParam {

    public String uuid;

    private String indexName;

    private Integer direction;
}
