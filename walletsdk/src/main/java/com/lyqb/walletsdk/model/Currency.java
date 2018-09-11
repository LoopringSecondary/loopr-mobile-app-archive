package com.lyqb.walletsdk.model;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-11 下午6:32
 * Cooperation: loopring.org 路印协议基金会
 */
public enum Currency {
    CNY("CNY"),
    USD("USD");

    private final String text;

    Currency(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
