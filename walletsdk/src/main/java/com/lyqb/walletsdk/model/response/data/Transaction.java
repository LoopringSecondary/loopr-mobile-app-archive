package com.lyqb.walletsdk.model.response.data;

import com.lyqb.walletsdk.model.TxStatus;
import com.lyqb.walletsdk.model.TxType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

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

    @Setter(AccessLevel.NONE)
    private TxType type;

    @Setter(AccessLevel.NONE)
    private TxStatus status;

    public void setType(String type) {
        this.type = TxType.fromValue(type);
    }

    public void setStatus(String status) {
        this.status = TxStatus.fromValue(status);
    }
}
