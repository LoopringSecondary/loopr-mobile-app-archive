package leaf.prod.walletsdk.service;

import android.util.Log;

import leaf.prod.walletsdk.SDK;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 下午5:54
 * Cooperation: Loopring
 */
public class ThirdLoginService {

    private OkHttpClient okHttpClient;

    public ThirdLoginService() {
        this.okHttpClient = SDK.getOkHttpClient();
    }

    public Call getUser(String accountToken) {
        Log.d("111111", "https://www.loopring.mobi/api/v1/users?account_token=" + accountToken);
        return okHttpClient.newCall(new Request.Builder().addHeader("application/json", "Content-Type")
                .url("https://www.loopring.mobi/api/v1/users?account_token=" + accountToken)
                .get()
                .build());
    }

    public Call addUser(String json) {
        return okHttpClient.newCall(new Request.Builder().addHeader("application/json", "Content-Type")
                .url("https://www.loopring.mobi/api/v1/users")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build());
    }
}
