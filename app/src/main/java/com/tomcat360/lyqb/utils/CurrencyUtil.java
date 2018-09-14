/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-11 下午6:01
 * Cooperation: loopring.org 路印协议基金会
 */
package com.tomcat360.lyqb.utils;

import java.text.NumberFormat;
import java.util.Locale;

import android.content.Context;

import com.lyqb.walletsdk.model.Currency;

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

    public static String format(Context context, double value) {
        Locale locale = getCurrency(context).getLocale();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(value);
    }
}
