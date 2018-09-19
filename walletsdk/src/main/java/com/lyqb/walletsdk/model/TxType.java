/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-19 下午2:32
 * Cooperation: loopring.org 路印协议基金会
 */
package com.lyqb.walletsdk.model;

public enum TxType {
    APPROVED("approve"),
    SEND("send"),
    RECEIVED("receive"),
    SOLD("sell"),
    BOUGHT("buy"),
    CONVERT_INCOME("convert_income"),
    CONVERT_OUTCOME("convert_outcome"),
    CANCEL("cancel_order"),
    CUTOFF("cutoff"),
    UNSUPPORTED("unsupported_contract"),
    OTHER("unknown");

    private String description;

    TxType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static TxType fromValue(String value) {
        for (TxType t : TxType.values()) {
            if (t.description.equalsIgnoreCase(value)) {
                return t;
            }
        }
        return null;
    }
}
