package leaf.prod.walletsdk.service;

import java.util.List;

import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.IntervalType;
import leaf.prod.walletsdk.model.common.Currency;
import leaf.prod.walletsdk.model.common.Paging;
import leaf.prod.walletsdk.model.common.Sort;
import leaf.prod.walletsdk.model.common.TradeType;
import leaf.prod.walletsdk.model.market.MarketPair;
import leaf.prod.walletsdk.model.order.OrderStatus;
import leaf.prod.walletsdk.model.order.RawOrder;
import leaf.prod.walletsdk.model.request.RequestWrapper;
import leaf.prod.walletsdk.model.request.relayParam.AccountBalanceParam;
import leaf.prod.walletsdk.model.request.relayParam.ActivityParam;
import leaf.prod.walletsdk.model.request.relayParam.CancelOrderParam;
import leaf.prod.walletsdk.model.request.relayParam.GetMarketsParam;
import leaf.prod.walletsdk.model.request.relayParam.GetOrdersParam;
import leaf.prod.walletsdk.model.request.relayParam.GetTokenParam;
import leaf.prod.walletsdk.model.request.relayParam.MarketHistoryParam;
import leaf.prod.walletsdk.model.request.relayParam.OrderBookParam;
import leaf.prod.walletsdk.model.request.relayParam.RingParam;
import leaf.prod.walletsdk.model.request.relayParam.SubmitOrderParam;
import leaf.prod.walletsdk.model.request.relayParam.UserFillsParam;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.relay.AccountBalance;
import leaf.prod.walletsdk.model.response.relay.ActivityResult;
import leaf.prod.walletsdk.model.response.relay.FillsResult;
import leaf.prod.walletsdk.model.response.relay.MarketHistoryResult;
import leaf.prod.walletsdk.model.response.relay.MarketsResult;
import leaf.prod.walletsdk.model.response.relay.OrderBookResult;
import leaf.prod.walletsdk.model.response.relay.OrdersResult;
import leaf.prod.walletsdk.model.response.relay.RingsResult;
import leaf.prod.walletsdk.model.response.relay.TokensResult;
import leaf.prod.walletsdk.model.token.Token;
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


    public Observable<MarketsResult> getMarkets(Boolean requireMetadata, Boolean requireTicker, Currency currency, MarketPair[] pairs) {
        GetMarketsParam param = GetMarketsParam.builder()
                .requireMetadata(requireMetadata)
                .requireTicker(requireTicker)
                .quoteCurrencyForTicker(currency.getText())
                .marketPairs(pairs)
                .build();
        RequestWrapper request = new RequestWrapper("get_markets", param);
        Observable<RelayResponseWrapper<MarketsResult>> observable = rpcDelegate.getMarkets(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<TokensResult> getTokens(Boolean requireMetadata, Boolean requireInfo, Boolean requirePrice, Token[] tokens) {
        GetTokenParam param = GetTokenParam.builder()
                .requireMetadata(requireMetadata)
                .requireInfo(requireInfo)
                .requirePrice(requirePrice)
                .tokens(tokens)
                .build();
        RequestWrapper request = new RequestWrapper("get_tokens", param);
        Observable<RelayResponseWrapper<TokensResult>> observable = rpcDelegate.getTokens(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<OrdersResult> getOrders(String owner, OrderStatus[] statuses, MarketPair marketPair, TradeType side, Sort sort, Paging paging) {
        GetOrdersParam param = GetOrdersParam.builder()
                .owner(owner)
                .statuses(statuses) // TODO
                .marketPair(marketPair)
                .side(side.name())
                .sort(sort.getDescription())
                .paging(paging)
                .build();
        RequestWrapper request = new RequestWrapper("get_orders", param);
        Observable<RelayResponseWrapper<OrdersResult>> observable = rpcDelegate.getOrders(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<String> submitOrder(RawOrder rawOrder) {
        SubmitOrderParam param = SubmitOrderParam.builder()
                .rawOrder(rawOrder)
                .build();
        RequestWrapper request = new RequestWrapper("submit_order", param);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.submitOrder(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<String> cancelOrders(String id, MarketPair marketPair, String owner, Integer time, String sig) {
        CancelOrderParam param = CancelOrderParam.builder()
                .id(id)
                .marketPair(marketPair)
                .owner(owner)
                .time(time)
                .sig(sig)
                .build();
        RequestWrapper request = new RequestWrapper("cancel_orders", param);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.cancelOrders(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<AccountBalance> getAccounts(List<String> addresses, List<String> tokens, boolean allTokens) {
        AccountBalanceParam param = AccountBalanceParam.builder()
                .addresses(addresses)
                .tokens(tokens)
                .allTokens(allTokens)
                .build();
        RequestWrapper request = new RequestWrapper("get_accounts", param);
        Observable<RelayResponseWrapper<AccountBalance>> observable = rpcDelegate.getAccount(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<Integer> getAccountNonce(String address) {
        RequestWrapper request = new RequestWrapper("get_user_fills", address);
        Observable<RelayResponseWrapper<Integer>> observable = rpcDelegate.getAccountNonce(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<FillsResult> getUserFills(String owner, String baseToken, String quoteToken, String sort, Paging paging) {
        UserFillsParam param = UserFillsParam.builder()
                .owner(owner)
                .marketPair(MarketPair.builder().baseToken(baseToken).quoteToken(quoteToken).build())
                .sort(sort)
                .paging(paging)
                .build();
        RequestWrapper request = new RequestWrapper("get_user_fills", param);
        Observable<RelayResponseWrapper<FillsResult>> observable = rpcDelegate.getUserFills(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<FillsResult> getMarketFills(String baseToken, String quoteToken) {
        MarketPair pair = MarketPair.builder().baseToken(baseToken).quoteToken(quoteToken).build();
        RequestWrapper request = new RequestWrapper("get_market_fills", pair);
        Observable<RelayResponseWrapper<FillsResult>> observable = rpcDelegate.getMarketFills(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<OrderBookResult> getOrderBook(int level, int size, String baseToken, String quoteToken) {
        OrderBookParam param = OrderBookParam.builder()
                .level(level)
                .size(size)
                .marketPair(MarketPair.builder().baseToken(baseToken).quoteToken(quoteToken).build())
                .build();
        RequestWrapper request = new RequestWrapper("get_order_book", param);
        Observable<RelayResponseWrapper<OrderBookResult>> observable = rpcDelegate.getOrderBook(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<RingsResult> getRings(String sort, Paging paging, int ringIndex) {
        RingParam param = RingParam.builder()
                .sort(sort)
                .paging(paging)
                .filter(RingParam.Filter.builder().ringIndex(ringIndex).build())
                .build();
        RequestWrapper request = new RequestWrapper("get_rings", param);
        Observable<RelayResponseWrapper<RingsResult>> observable = rpcDelegate.getRings(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<ActivityResult> getActivities(String owner, String token, Paging paging) {
        ActivityParam param = ActivityParam.builder().owner(owner).token(token).paging(paging).build();
        RequestWrapper request = new RequestWrapper("get_activities", param);
        Observable<RelayResponseWrapper<ActivityResult>> observable = rpcDelegate.getActivities(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<MarketHistoryResult> getMarketHistory(MarketPair marketPair, IntervalType interval, Long beginTime, Long endTime) {
        MarketHistoryParam param = MarketHistoryParam.builder()
                .marketPair(marketPair)
                .interval(interval)
                .beginTime(beginTime)
                .endTime(endTime)
                .build();
        RequestWrapper request = new RequestWrapper("get_market_history", param);
        Observable<RelayResponseWrapper<MarketHistoryResult>> observable = rpcDelegate.getMarketHistory(request);
        return observable.map(RelayResponseWrapper::getResult);
    }
}
