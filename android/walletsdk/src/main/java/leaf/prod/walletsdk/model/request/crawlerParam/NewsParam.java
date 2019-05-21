package leaf.prod.walletsdk.model.request.crawlerParam;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NewsParam {

    @SerializedName("currency")
    private String token;

    private String language;

    private String category;

    private Integer pageIndex;

    private Integer pageSize;
}
