/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 下午5:54
 * Cooperation: Loopring
 */
package leaf.prod.walletsdk.service;

import com.google.gson.JsonObject;

import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.LoginUser;
import leaf.prod.walletsdk.model.response.AppResponseWrapper;
import leaf.prod.walletsdk.model.response.app.VersionResp;
import retrofit2.Callback;

public class AppService {

    private RpcDelegate rpcDelegate;

    public AppService() {
        String url = SDK.appServiceBase();
        rpcDelegate = RpcDelegate.getService(url);
    }

    public void getUser(String accountToken, Callback<AppResponseWrapper<LoginUser>> callback) {
        rpcDelegate.getUser(accountToken).enqueue(callback);
    }

    public void addUser(LoginUser loginUser, Callback<AppResponseWrapper<String>> callback) {
        rpcDelegate.addUser(loginUser).enqueue(callback);
    }

    public void deleteUser(String accountToken, Callback<AppResponseWrapper<String>> callback) {
        rpcDelegate.deleteUser(accountToken).enqueue(callback);
    }

    public void updateConfig(String accountToken, String config, Callback<AppResponseWrapper<String>> callback) {


    }

    public void updateConfig(String accountToken, JsonObject config, Callback<AppResponseWrapper<String>> callback) {

    }

    public void getLatestVersion(Callback<AppResponseWrapper<VersionResp>> cb) {
        rpcDelegate.getLatestVersion().enqueue(cb);
    }
}
