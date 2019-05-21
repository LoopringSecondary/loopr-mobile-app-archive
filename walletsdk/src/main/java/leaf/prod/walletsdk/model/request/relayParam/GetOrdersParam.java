package leaf.prod.walletsdk.model.request.relayParam;

import leaf.prod.walletsdk.model.common.Paging;
import leaf.prod.walletsdk.model.market.MarketPair;
import leaf.prod.walletsdk.model.order.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetOrdersParam {

    private String owner;

    private OrderStatus[] statuses;

    private MarketPair marketPair;

    private String side;

    private String sort;

    private Paging paging;
}
