package com.lyqb.walletsdk.model.response.data;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Token {
    private String protocol;
    private String symbol;
    private String name;
    private String source;
    private int time;
    private boolean deny;
    private BigDecimal decimals;
    private String isMarket;
    private Object icoPrice;
}
