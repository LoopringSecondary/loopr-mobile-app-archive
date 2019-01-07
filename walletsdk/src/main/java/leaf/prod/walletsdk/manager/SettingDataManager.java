/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-13 下午3:47
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.text.NumberFormat;

import android.content.Context;

import leaf.prod.walletsdk.util.SPUtils;

public class SettingDataManager {

    private static SettingDataManager settingDataManager = null;

    private Context context;

    private SettingDataManager(Context context) {
        this.context = context;
    }

    public static SettingDataManager getInstance(Context context) {
        if (settingDataManager == null) {
            settingDataManager = new SettingDataManager(context);
        }
        return settingDataManager;
    }

    public float getLrcFeeFloat() {
        int value = (int) SPUtils.get(context, "ratio", 2);
        return (float) value / 1000;
    }

    public String getLrcFeeString() {
        float ratio = getLrcFeeFloat();
        NumberFormat formatter = NumberFormat.getPercentInstance();
        formatter.setMaximumFractionDigits(1);
        formatter.setMinimumFractionDigits(1);
        return formatter.format(ratio);
    }

    public void setLrcFee(int lrcFee) {
        SPUtils.put(context, "ratio", lrcFee);
    }

}
