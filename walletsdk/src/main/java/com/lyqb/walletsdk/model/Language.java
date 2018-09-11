package com.lyqb.walletsdk.model;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-11 下午6:35
 * Cooperation: loopring.org 路印协议基金会
 */
public enum Language {
    zh_CN("CN"),
    en_US("US");

    private final String text;

    Language(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
