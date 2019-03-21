package leaf.prod.walletsdk.model.response.relay;

import java.util.List;

import lombok.Data;

@Data
public class MarketcapResult {

    private String currency;

    private List<Token> tokens;

    @Data
    static class Token {

        private String symbol;

        private double price;
    }
}
