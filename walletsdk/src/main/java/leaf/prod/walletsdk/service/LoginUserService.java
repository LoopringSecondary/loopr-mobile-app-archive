package leaf.prod.walletsdk.service;

import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.LoginUser;
import leaf.prod.walletsdk.model.response.AppResponseWrapper;
import retrofit2.Callback;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 下午5:54
 * Cooperation: Loopring
 */
public class LoginUserService {

    private RpcDelegate rpcDelegate;

    public LoginUserService() {
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
}
