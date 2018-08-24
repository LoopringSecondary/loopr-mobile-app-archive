package com.tomcat360.lyqb.core.model.loopr.response;

import com.tomcat360.lyqb.core.model.loopr.Token;

import java.util.List;

import lombok.Data;

@Data
public class BalanceResult {
    private String delegateAddress;
    private String owner;
    private List<Token> tokens;
}
