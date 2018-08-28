package com.lyqb.walletsdk.model.response;

import java.util.List;

import lombok.Data;

@Data
public class TransactionPageWrapper {
    private List<Transaction> data;
    private int pageIndex;
    private int pageSize;
    private int total;
}
