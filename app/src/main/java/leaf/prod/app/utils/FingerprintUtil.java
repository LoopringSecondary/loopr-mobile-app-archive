/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-10-16 4:53 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.utils;

import android.content.Context;

import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;

public class FingerprintUtil {

    public static boolean isEnable(Context context) {
        FingerprintIdentify fingerprintIdentify = new FingerprintIdentify(context, exception -> {
        });
        return fingerprintIdentify.isFingerprintEnable();
    }
}
