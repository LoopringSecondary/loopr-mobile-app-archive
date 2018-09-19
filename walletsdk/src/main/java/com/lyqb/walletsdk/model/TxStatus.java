/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-19 下午2:33
 * Cooperation: loopring.org 路印协议基金会
 */
package com.lyqb.walletsdk.model;

public enum TxStatus {
    PENDING("pending"),
    SUCCESS("success"),
    FAILED("failed"),
    OTHER("other");

    private String description;

    TxStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static TxStatus fromValue(String value) {
        for (TxStatus t : TxStatus.values()) {
            if (t.description.equalsIgnoreCase(value)) {
                return t;
            }
        }
        return null;
    }
}
