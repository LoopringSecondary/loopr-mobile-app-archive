package com.tomcat360.lyqb.core.model.loopr;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Token {
    private String symbol;
    private BigDecimal balance;
    private BigDecimal allowance;
}
