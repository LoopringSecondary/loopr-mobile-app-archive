package leaf.prod.walletsdk.model.response.relay;

import java.util.List;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 4:50 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class OrderBookResult {

    private OrderBook orderBook;

    @Data
    public static class OrderBook {

        private Double lastPrice;

        private List<Order> sells;

        private List<Order> buys;

        @Data
        static class Order {

            private Long amount;

            private Double price;

            private Double total;
        }
    }
}
