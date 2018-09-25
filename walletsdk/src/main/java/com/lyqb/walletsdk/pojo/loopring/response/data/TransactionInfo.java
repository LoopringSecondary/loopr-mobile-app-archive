package com.lyqb.walletsdk.pojo.loopring.response.data;

import lombok.Data;

@Data
public class TransactionInfo {
    private String from;
    private String to;
    private String owner;
    private long createTime;
    private long updateTime;
    private String txHash;
    private long blockNumber;
    private String value;
    private String type;
    private String status;
}
