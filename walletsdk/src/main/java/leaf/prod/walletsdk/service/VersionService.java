package leaf.prod.walletsdk.service;

import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.response.AppResponseWrapper;
import leaf.prod.walletsdk.model.response.app.VersionResp;
import retrofit2.Callback;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-15 下午5:54
 * Cooperation: Loopring
 */
public class VersionService {

    private RpcDelegate rpcDelegate;

    public VersionService() {
        String url = SDK.appServiceBase();
        rpcDelegate = RpcDelegate.getService(url);
    }

    public void getNewVersion(Callback<AppResponseWrapper<VersionResp>> cb) {
        rpcDelegate.getLatestVersion().enqueue(cb);
    }
}
