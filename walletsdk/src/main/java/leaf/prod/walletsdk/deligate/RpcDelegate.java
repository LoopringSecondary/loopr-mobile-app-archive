package leaf.prod.walletsdk.deligate;

import java.util.List;
import java.util.Map;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.model.Depth;
import leaf.prod.walletsdk.model.LoginUser;
import leaf.prod.walletsdk.model.Order;
import leaf.prod.walletsdk.model.Partner;
import leaf.prod.walletsdk.model.Ticker;
import leaf.prod.walletsdk.model.request.RequestWrapper;
import leaf.prod.walletsdk.model.response.AppResponseWrapper;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.app.VersionResp;
import leaf.prod.walletsdk.model.response.crawler.BlogWrapper;
import leaf.prod.walletsdk.model.response.crawler.IndexResult;
import leaf.prod.walletsdk.model.response.crawler.NewsPageWrapper;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.model.response.relay.ClaimBindAmount;
import leaf.prod.walletsdk.model.response.relay.GetBindAmount;
import leaf.prod.walletsdk.model.response.relay.MarketcapResult;
import leaf.prod.walletsdk.model.response.relay.PageWrapper;
import leaf.prod.walletsdk.model.response.relay.Token;
import leaf.prod.walletsdk.model.response.relay.TransactionPageWrapper;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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

    @GET(Default.APP_RPC_URL + "/version/android/getLatest")
    Call<AppResponseWrapper<VersionResp>> getLatestVersion();

    @GET(Default.APP_RPC_URL + "/user/getUser")
    Call<AppResponseWrapper<LoginUser>> getUser(@Query("account_token") String accountToken);

    @POST(Default.APP_RPC_URL + "/user/addUser")
    Call<AppResponseWrapper<String>> addUser(@Body LoginUser loginUser);

    @DELETE(Default.APP_RPC_URL + "/user/deleteUser")
    Call<AppResponseWrapper<String>> deleteUser(@Query("account_token") String accountToken);

    @POST(Default.NEO_RPC_URL)
    Observable<RelayResponseWrapper<GetBindAmount>> getAirdropAmount(@Body RequestWrapper request);

    @POST(Default.NEO_RPC_URL)
    Observable<RelayResponseWrapper<ClaimBindAmount>> claimAirdrop(@Body RequestWrapper request);

    @POST(Default.CRAWLER_RPC_URL)
    Observable<RelayResponseWrapper<NewsPageWrapper>> getNews(@Body RequestWrapper request);

    @POST(Default.CRAWLER_RPC_URL)
    Observable<RelayResponseWrapper<IndexResult>> updateIndex(@Body RequestWrapper request);

    @POST(Default.CRAWLER_RPC_URL)
    Observable<RelayResponseWrapper<BlogWrapper>> getBlogs(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<Map> send(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<BalanceResult>> getBalance(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<TransactionPageWrapper>> getTransactions(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> getNonce(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> estimateGasPrice(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> unlockWallet(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<List<Token>>> getSupportedTokens(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<MarketcapResult>> getMarketcap(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> notifyTransactionSubmitted(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> notifyScanLogin(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> notifyStatus(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> getSignMessage(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<Partner>> createPartner(@Body RequestWrapper request);

    @POST(Default.PARTNER_ACTIVATE)
    Observable<RelayResponseWrapper<Partner>> activateInvitation();

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> addCustomToken(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<List<Token>>> getCustomToken(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<List<Ticker>>> getTickers(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<List<Depth>>> getDepths(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<PageWrapper<Order>>> getOrders(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<Order>> getOrderByHash(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> getFrozenLRCFee(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> getEstimatedAllocatedAllowance(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> sumitOrder(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> sumitOrderForP2P(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> cancelOrderFlex(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<String>> sumitRing(@Body RequestWrapper request);
}
