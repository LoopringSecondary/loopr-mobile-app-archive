package leaf.prod.walletsdk.service;

import android.util.Log;

import leaf.prod.walletsdk.SDK;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-15 下午5:54
 * Cooperation: Loopring
 */
public class VersionService {

    private OkHttpClient okHttpClient;

    //    private static String webUrl = "https://www.loopring.mobi/api/v1/app_versions";
    private static String webUrl = "http://10.137.107.120:8081/api/v1";

    public VersionService() {
        this.okHttpClient = SDK.getOkHttpClient();
    }

    public Call getNewVersion() {
        try {
            return okHttpClient.newCall(new Request.Builder().addHeader("Content-Type", "application/json")
                    .url(webUrl + "/version/android/getLatest")
                    .get()
                    .build());
        } catch (Exception e) {
            Log.e("", e.getMessage());
            return null;
        }
    }
}
