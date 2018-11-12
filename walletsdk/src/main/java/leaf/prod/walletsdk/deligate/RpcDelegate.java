package leaf.prod.walletsdk.deligate;

import java.util.List;
import java.util.Map;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.model.Partner;
import leaf.prod.walletsdk.model.request.RequestWrapper;
import leaf.prod.walletsdk.model.response.ResponseWrapper;
import leaf.prod.walletsdk.model.response.data.BalanceResult;
import leaf.prod.walletsdk.model.response.data.MarketcapResult;
import leaf.prod.walletsdk.model.response.data.Token;
import leaf.prod.walletsdk.model.response.data.TransactionPageWrapper;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface RpcDelegate {

    static RpcDelegate getService(String url) {
        OkHttpClient okHttpClient = SDK.getOkHttpClient();
        Retrofit retrofitClient = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofitClient.create(RpcDelegate.class);
    }

    @POST(Default.RELAY_RPC_URL)
    Observable<Map> send(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<BalanceResult>> getBalance(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<TransactionPageWrapper>> getTransactions(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> getNonce(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> estimateGasPrice(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> unlockWallet(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<List<Token>>> getSupportedTokens(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<MarketcapResult>> getMarketcap(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> notifyTransactionSubmitted(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> notifyScanLogin(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> notifyStatus(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> getSignMessage(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<Partner>> createPartner(@Body RequestWrapper request);

    @POST(Default.PARTNER_ACTIVATE)
    Observable<ResponseWrapper<Partner>> activateInvitation();

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> addCustomToken(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<ResponseWrapper<List<Token>>> getCustomToken(@Body RequestWrapper request);
}
