/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-11 8:09 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import android.content.Context;

import com.google.gson.JsonObject;

import leaf.prod.walletsdk.service.AppService;

public class ConfigDataManager {

    private Context context;

    private JsonObject config;

    private AppService appService;

    private static ConfigDataManager configDataManager = null;

    private ConfigDataManager(Context context) {
        this.context = context;
        this.appService = new AppService();
        this.loadConfigFromService();
    }

    public static ConfigDataManager getInstance(Context context) {
        if (configDataManager == null) {
            configDataManager = new ConfigDataManager(context);
        }
        return configDataManager;
    }

    private void loadConfigFromService() {
    }
}
