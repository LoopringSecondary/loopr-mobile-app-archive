package leaf.prod.walletsdk.service;

import android.util.Log;

import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.ThirdLogin;
import leaf.prod.walletsdk.model.response.AppResponseWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 下午5:54
 * Cooperation: Loopring
 */
public class ThirdLoginService {

    private RpcDelegate rpcDelegate;

    public ThirdLoginService() {
        String url = SDK.appServiceBase();
        rpcDelegate = RpcDelegate.getService(url);
    }

    public void getUser(String accountToken, Callback<AppResponseWrapper<ThirdLogin>> callback) {
        rpcDelegate.getUser(accountToken).enqueue(callback);
    }

    public void addUser(ThirdLogin thirdLogin) {
        rpcDelegate.addUser(thirdLogin).enqueue(new Callback<AppResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<AppResponseWrapper<String>> call, Response<AppResponseWrapper<String>> response) {
                Log.d("third login add user: ", response.body().toString());
            }

            @Override
            public void onFailure(Call<AppResponseWrapper<String>> call, Throwable t) {
                Log.e("third login add user: ", t.getMessage());
            }
        });
    }

    public void deleteUser(String accountToken) {
        rpcDelegate.deleteUser(accountToken).enqueue(new Callback<AppResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<AppResponseWrapper<String>> call, Response<AppResponseWrapper<String>> response) {
                Log.d("third login del user: ", response.body().toString());
            }

            @Override
            public void onFailure(Call<AppResponseWrapper<String>> call, Throwable t) {
                Log.e("third login del user: ", t.getMessage());
            }
        });
    }
}
