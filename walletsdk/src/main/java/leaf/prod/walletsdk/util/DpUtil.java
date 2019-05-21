package leaf.prod.walletsdk.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-19 10:26 AM
 * Cooperation: loopring.org 路印协议基金会
 */
public class DpUtil {

    public static float dp2Float(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int dp2Int(Context context, int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                .getDisplayMetrics()) + 1);
    }
}
