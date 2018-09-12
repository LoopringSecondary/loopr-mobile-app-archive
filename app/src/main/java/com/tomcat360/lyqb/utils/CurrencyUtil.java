package com.tomcat360.lyqb.utils;

import android.content.Context;

import com.lyqb.walletsdk.model.Currency;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-11 下午6:01
 * Cooperation: loopring.org 路印协议基金会
 */
public class CurrencyUtil {

    public static Currency getCurrency(Context context) {
        String currency = (String) SPUtils.get(context, "coin", "$");
        if (currency != null) {
            if (currency.equals("$")) {
                return Currency.USD;
            } else {
                return Currency.CNY;
            }
        } else {
            return Currency.USD;
        }
    }
}
