package leaf.prod.walletsdk.service;

import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.request.param.VersionParam;
import okhttp3.Callback;

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

    public void getNewVersion(VersionParam param, Callback cb) {
        rpcDelegate.getLatestVersion(param, cb);
    }
}
