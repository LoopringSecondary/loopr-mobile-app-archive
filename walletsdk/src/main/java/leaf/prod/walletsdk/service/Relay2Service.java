package leaf.prod.walletsdk.service;

import java.util.List;

import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.IntervalType;
import leaf.prod.walletsdk.model.MarketPair;
import leaf.prod.walletsdk.model.request.relayParam.AccountBalanceParam;
import leaf.prod.walletsdk.model.request.relayParam.ActivityParam;
import leaf.prod.walletsdk.model.request.relayParam.MarketHistoryParam;
import leaf.prod.walletsdk.model.request.relayParam.OrderBookParam;
import leaf.prod.walletsdk.model.request.relayParam.RingParam;
import leaf.prod.walletsdk.model.request.relayParam.UserFillsParam;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.relay.AccountBalance;
import leaf.prod.walletsdk.model.response.relay.ActivityResult;
import leaf.prod.walletsdk.model.response.relay.MarketHistoryResult;
import leaf.prod.walletsdk.model.response.relay.OrderBookResult;
import leaf.prod.walletsdk.model.response.relay.PageWrapper2;
import leaf.prod.walletsdk.model.response.relay.FillsResult;
import leaf.prod.walletsdk.model.response.relay.RingsResult;
import rx.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 3:52 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class Relay2Service {

    private RpcDelegate rpcDelegate;

    public Relay2Service() {
        String url = SDK.relay2Base();
        rpcDelegate = RpcDelegate.getService(url);
    }

    public Observable<AccountBalance> getAccounts(List<String> addresses, List<String> tokens, boolean allTokens) {
        AccountBalanceParam param = AccountBalanceParam.builder()
                .addresses(addresses)
                .tokens(tokens)
                .allTokens(allTokens)
                .build();
        Observable<RelayResponseWrapper<AccountBalance>> observable = rpcDelegate.getAccount();
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<Integer> getAccountNonce(String address) {
        Observable<RelayResponseWrapper<Integer>> observable = rpcDelegate.getAccountNonce();
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<FillsResult> getUserFills(String owner, String baseToken, String quoteToken, String sort, PageWrapper2 paging) {
        UserFillsParam param = UserFillsParam.builder()
                .owner(owner)
                .marketPair(MarketPair.builder().baseToken(baseToken).quoteToken(quoteToken).build())
                .sort(sort)
                .paging(paging)
                .build();
        Observable<RelayResponseWrapper<FillsResult>> observable = rpcDelegate.getUserFills();
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<FillsResult> getMarketFills(String baseToken, String quoteToken) {
        MarketPair pair = MarketPair.builder().baseToken(baseToken).quoteToken(quoteToken).build();
        Observable<RelayResponseWrapper<FillsResult>> observable = rpcDelegate.getMarketFills();
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<OrderBookResult> getOrderBook(int level, int size, String baseToken, String quoteToken) {
        OrderBookParam param = OrderBookParam.builder()
                .level(level)
                .size(size)
                .marketPair(MarketPair.builder().baseToken(baseToken).quoteToken(quoteToken).build())
                .build();
        Observable<RelayResponseWrapper<OrderBookResult>> observable = rpcDelegate.getOrderBook();
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<RingsResult> getRings(String sort, PageWrapper2 paging, int ringIndex) {
        RingParam param = RingParam.builder()
                .sort(sort)
                .paging(paging)
                .filter(RingParam.Filter.builder().ringIndex(ringIndex).build())
                .build();
        Observable<RelayResponseWrapper<RingsResult>> observable = rpcDelegate.getRings();
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<ActivityResult> getActivities(String owner, String token, PageWrapper2 paging) {
        ActivityParam param = ActivityParam.builder().owner(owner).token(token).paging(paging).build();
        Observable<RelayResponseWrapper<ActivityResult>> observable = rpcDelegate.getActivities();
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<MarketHistoryResult> getMarketHistory(String baseToken, String quoteToken, IntervalType interval, long beginTime, long endTime) {
        MarketHistoryParam param = MarketHistoryParam.builder()
                .marketPair(MarketPair.builder().baseToken(baseToken).quoteToken(quoteToken).build())
                .interval(interval)
                .beginTime(beginTime)
                .endTime(endTime)
                .build();
        Observable<RelayResponseWrapper<MarketHistoryResult>> observable = rpcDelegate.getMarketHistory();
        return observable.map(RelayResponseWrapper::getResult);
    }
}
