package leaf.prod.walletsdk.service;

import java.io.IOException;

import android.util.Log;

import leaf.prod.walletsdk.SDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 下午5:54
 * Cooperation: Loopring
 */
public class ThirdLoginService {

    private OkHttpClient okHttpClient;

    private static String webUrl = "https://www.loopring.mobi/api/v1/users";

    public ThirdLoginService() {
        this.okHttpClient = SDK.getOkHttpClient();
    }

    public Call getUser(String accountToken) {
        return okHttpClient.newCall(new Request.Builder().addHeader("Content-Type", "application/json")
                .url(webUrl + "?account_token=" + accountToken)
                .get()
                .build());
    }

    public void addUser(String json) {
        okHttpClient.newCall(new Request.Builder().addHeader("Content-Type", "application/json")
                .url(webUrl)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("addUser: ", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("addUser: ", response.body().string());
            }
        });
    }

    public void deleteUser(String uid) {
        okHttpClient.newCall(new Request.Builder().addHeader("Content-Type", "application/json")
                .url(webUrl)
                .delete(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{\"account_token\": \"" + uid + "\"}"))
                .build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("deleteUser: ", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("deleteUser: ", response.body().string());
            }
        });
    }
}
