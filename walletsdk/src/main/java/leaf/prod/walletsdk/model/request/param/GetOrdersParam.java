package leaf.prod.walletsdk.model.request.param;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetOrdersParam {

    private String delegateAddress;

    private String owner;

    private String orderHash;

    private String market;

    private String side;

    private String status;

    private String orderType;

    private int pageIndex;

    private int pageSize;
}
