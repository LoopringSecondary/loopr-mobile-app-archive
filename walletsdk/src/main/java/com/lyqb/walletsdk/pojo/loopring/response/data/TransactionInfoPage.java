package com.lyqb.walletsdk.pojo.loopring.response.data;

import java.util.List;

import lombok.Data;

@Data
public class TransactionInfoPage {
    private List<TransactionInfo> data;
    private int pageIndex;
    private int pageSize;
    private int total;
}
