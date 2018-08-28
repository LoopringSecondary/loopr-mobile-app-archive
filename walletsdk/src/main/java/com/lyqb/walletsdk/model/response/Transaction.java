package com.lyqb.walletsdk.model.response;

import lombok.Data;

@Data
public class Transaction {
    private String from;
    private String to;
    private String owner;
    private long createTime;
    private long updateTime;
    private String hash;
    private long blockNumber;
    private String value;
    private String type;
    private String status;
}
