package com.lyqb.walletsdk.pojo.loopring.response.data;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class MarketcapResult {
    private String currency;
    private List<Price> tokens;

    @Data
    public static class Price {
        private String symbol;
        private BigDecimal price;
    }
}
