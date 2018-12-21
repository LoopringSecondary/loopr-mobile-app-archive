package leaf.prod.walletsdk.service;

import java.util.Date;

import android.content.Context;

import leaf.prod.walletsdk.R;
import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.request.RequestWrapper;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.util.DateUtil;
import leaf.prod.walletsdk.util.FileUtils;
import neo.model.bytes.UInt160;
import neo.model.util.ModelUtil;
import rx.Observable;

public class NeoService {

    private RpcDelegate rpcDelegate;

    private static String METHOD = "";

    public NeoService(Context context) {
        String url = SDK.neoBase();
        rpcDelegate = RpcDelegate.getService(url);
        METHOD = FileUtils.getFile(context, R.raw.neo);
    }

    public Observable<String> getAirdropAmount(String bindAddress) {
        UInt160 hash = ModelUtil.addressToScriptHash(bindAddress);
        String key = "14" + hash + "51c1157175657279417661696c61626c6542616c616e636567f7c5643ab1896195b8abe8cfd2e3b450441ca45c";
        RequestWrapper request = new RequestWrapper("invokescript", key);
        return rpcDelegate.getAirdropAmount(request).map(result -> result.getResult().getStack().get(0).getValue());
    }

    public Observable<RelayResponseWrapper> claimAirdrop(String bindAddress) {
        UInt160 hash = ModelUtil.addressToScriptHash(bindAddress);
        String id = DateUtil.formatDateTime(new Date(), "yyyyMMddHHmm");
        String key = "d1013d1c" + id + "0000" +  hash + METHOD;
        RequestWrapper request = new RequestWrapper("sendrawtransaction", key);
        return rpcDelegate.claimAirdrop(request).map(result -> result);
    }
}
