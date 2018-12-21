package leaf.prod.walletsdk.model.request.crawlerParam;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NewsParam {

    private String currency;

    private String language;

    private String category;

    private Integer pageIndex;

    private Integer pageSize;
}
