package leaf.prod.walletsdk.deligate;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.model.request.RequestWrapper;
import leaf.prod.walletsdk.model.response.AppResponseWrapper;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.app.VersionResp;
import leaf.prod.walletsdk.model.response.crawler.BlogWrapper;
import leaf.prod.walletsdk.model.response.crawler.IndexResult;
import leaf.prod.walletsdk.model.response.crawler.NewsPageWrapper;
import leaf.prod.walletsdk.model.response.relay.AccountBalanceWrapper;
import leaf.prod.walletsdk.model.response.relay.ActivityResult;
import leaf.prod.walletsdk.model.response.relay.CancelOrdersResult;
import leaf.prod.walletsdk.model.response.relay.ClaimBindAmount;
import leaf.prod.walletsdk.model.response.relay.FillsResult;
import leaf.prod.walletsdk.model.response.relay.GetBindAmount;
import leaf.prod.walletsdk.model.response.relay.GetGasResult;
import leaf.prod.walletsdk.model.response.relay.MarketHistoryResult;
import leaf.prod.walletsdk.model.response.relay.MarketsResult;
import leaf.prod.walletsdk.model.response.relay.OrderBookResult;
import leaf.prod.walletsdk.model.response.relay.OrdersResult;
import leaf.prod.walletsdk.model.response.relay.RingsResult;
import leaf.prod.walletsdk.model.response.relay.SubmitOrderResult;
import leaf.prod.walletsdk.model.response.relay.TokensResult;
import leaf.prod.walletsdk.model.setting.LoginUser;
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

    // 2.0
    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<MarketsResult>> getMarkets(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<TokensResult>> getTokens(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<OrdersResult>> getOrders(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<SubmitOrderResult>> submitOrder(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<CancelOrdersResult>> cancelOrders(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<AccountBalanceWrapper>> getAccount(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<Integer>> getAccountNonce(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<FillsResult>> getUserFills(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<FillsResult>> getMarketFills(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<OrderBookResult>> getOrderBook(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<RingsResult>> getRings(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<ActivityResult>> getActivities(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<MarketHistoryResult>> getMarketHistory(@Body RequestWrapper request);

    @POST(Default.RELAY_RPC_URL)
    Observable<RelayResponseWrapper<GetGasResult>> getGasPrice(@Body RequestWrapper request);
}
