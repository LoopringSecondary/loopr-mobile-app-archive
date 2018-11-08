/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-11 下午6:01
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.utils;

import java.text.NumberFormat;
import java.util.Locale;

import android.content.Context;

import leaf.prod.walletsdk.model.Currency;

public class CurrencyUtil {

    public static Currency getCurrency(Context context) {
        Currency result = null;
        String currency = (String) SPUtils.get(context, "coin", "CNY");
        if (currency != null) {
            if (currency.equals("CNY")) {
                result = Currency.CNY;
            } else {
                result = Currency.USD;
            }
        } else {
            Locale locale = context.getResources().getConfiguration().locale;
            if (locale == Locale.SIMPLIFIED_CHINESE) {
                result = Currency.CNY;
            } else if (locale == Locale.US) {
                result = Currency.USD;
            }
        }
        return result;
    }

    public static String format(Context context, double value) {
        Locale locale = getCurrency(context).getLocale();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(value);
    }

    public static void setCurrency(Context context, Currency currency) {
        SPUtils.put(context, "coin", currency.getText());
        ThirdLoginUtil.updateLocal(context, null, currency);
    }
}
