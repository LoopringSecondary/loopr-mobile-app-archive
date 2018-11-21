package leaf.prod.walletsdk.service;

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

    private static String webUrl = "https://www.loopring.mobi/api/v1/app_versions";

    public VersionService() {
        this.okHttpClient = SDK.getOkHttpClient();
    }

    public Call getNewVersion() {
        return okHttpClient.newCall(new Request.Builder().addHeader("application/json", "Content-Type")
                .url(webUrl)
                .get()
                .build());
    }
}
