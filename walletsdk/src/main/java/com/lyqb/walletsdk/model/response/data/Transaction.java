package com.lyqb.walletsdk.model.response.data;

import com.lyqb.walletsdk.model.TxStatus;
import com.lyqb.walletsdk.model.TxType;

import lombok.Data;

@Data
public class Transaction {

    private String from;

    private String to;

    private String owner;

    private long createTime;

    private long updateTime;

    private String txHash;

    private String symbol;

    private long blockNumber;

    private String value;

    private TxType type;

    private TxStatus status;

    private String gas_price;

    private String gas_limit;

    private String gas_used;
}
