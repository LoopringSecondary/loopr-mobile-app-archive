package leaf.prod.walletsdk.service;

import java.util.List;

import org.web3j.crypto.RawTransaction;
import org.web3j.utils.Numeric;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.CancelOrder;
import leaf.prod.walletsdk.model.Depth;
import leaf.prod.walletsdk.model.order.RawOrder;
import leaf.prod.walletsdk.model.OrderFill;
import leaf.prod.walletsdk.model.order.OrderStatus;
import leaf.prod.walletsdk.model.OrderType;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.Partner;
import leaf.prod.walletsdk.model.Ticker;
import leaf.prod.walletsdk.model.TickerSource;
import leaf.prod.walletsdk.model.Trend;
import leaf.prod.walletsdk.model.TrendInterval;
import leaf.prod.walletsdk.model.request.RequestWrapper;
import leaf.prod.walletsdk.model.request.relayParam.AddTokenParam;
import leaf.prod.walletsdk.model.request.relayParam.BalanceParam;
import leaf.prod.walletsdk.model.request.relayParam.CancelOrderParam;
import leaf.prod.walletsdk.model.request.relayParam.GetAllowanceParam;
import leaf.prod.walletsdk.model.request.relayParam.GetDepthsParam;
import leaf.prod.walletsdk.model.request.relayParam.GetFrozenParam;
import leaf.prod.walletsdk.model.request.relayParam.GetOrderFillsParam;
import leaf.prod.walletsdk.model.request.relayParam.GetOrdersParam;
import leaf.prod.walletsdk.model.request.relayParam.GetSignParam;
import leaf.prod.walletsdk.model.request.relayParam.GetTickersParam;
import leaf.prod.walletsdk.model.request.relayParam.GetTokenParam;
import leaf.prod.walletsdk.model.request.relayParam.GetTrendsParam;
import leaf.prod.walletsdk.model.request.relayParam.MarketcapParam;
import leaf.prod.walletsdk.model.request.relayParam.NonceParam;
import leaf.prod.walletsdk.model.request.relayParam.NotifyScanParam;
import leaf.prod.walletsdk.model.request.relayParam.NotifyStatusParam;
import leaf.prod.walletsdk.model.request.relayParam.NotifyTransactionSubmitParam;
import leaf.prod.walletsdk.model.request.relayParam.PartnerParam;
import leaf.prod.walletsdk.model.request.relayParam.SubmitOrderP2PParam;
import leaf.prod.walletsdk.model.request.relayParam.SubmitOrderParam;
import leaf.prod.walletsdk.model.request.relayParam.SubmitRingParam;
import leaf.prod.walletsdk.model.request.relayParam.TransactionParam;
import leaf.prod.walletsdk.model.request.relayParam.UnlockWallet;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.model.response.relay.MarketcapResult;
import leaf.prod.walletsdk.model.response.relay.PageWrapper;
import leaf.prod.walletsdk.model.response.relay.Token;
import leaf.prod.walletsdk.model.response.relay.TransactionPageWrapper;
import rx.Observable;

public class LoopringService {

    private RpcDelegate rpcDelegate;

    public LoopringService() {
        String url = SDK.relayBase();
        rpcDelegate = RpcDelegate.getService(url);
    }

    public Observable<String> getNonce(String owner) {
        NonceParam nonceParamParam = NonceParam.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getNonce", nonceParamParam);
        return rpcDelegate.getNonce(request).map(RelayResponseWrapper::getResult);
    }

    public Observable<String> getEstimateGasPrice() {
        RequestWrapper request = new RequestWrapper("loopring_getEstimateGasPrice", Maps.newHashMap());
        return rpcDelegate.estimateGasPrice(request).map(RelayResponseWrapper::getResult);
    }

    public Observable<String> notifyCreateWallet(String owner) {
        UnlockWallet unlockWallet = UnlockWallet.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_unlockWallet", unlockWallet);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.unlockWallet(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<BalanceResult> getBalance(String owner) {
        BalanceParam param = BalanceParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getBalance", param);
        return rpcDelegate.getBalance(request).map(RelayResponseWrapper::getResult);
    }

    public Observable<TransactionPageWrapper> getTransactions(String owner, String symbol, int pageIndex, int pageSize) {
        TransactionParam param = TransactionParam.builder()
                .owner(owner)
                .symbol(symbol)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getTransactions", param);
        Observable<RelayResponseWrapper<TransactionPageWrapper>> observable = rpcDelegate.getTransactions(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    /**
     * use getCustomToken instead: getCustomToken = getSupportedToken + customToken
     */
    @Deprecated
    public Observable<List<Token>> getSupportedToken() {
        RequestWrapper request = new RequestWrapper("loopring_getSupportedTokens", Maps.newHashMap());
        Observable<RelayResponseWrapper<List<Token>>> observable = rpcDelegate.getSupportedTokens(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<MarketcapResult> getMarketcap(String currency) {
        RequestWrapper request = new RequestWrapper("loopring_getPriceQuote", MarketcapParam.builder()
                .currency(currency)
                .build());
        return rpcDelegate.getMarketcap(request).map(RelayResponseWrapper::getResult);
    }

    public Observable<MarketcapResult> getPriceQuoteByToken(String currency, String token) {
        RequestWrapper request = new RequestWrapper("loopring_getPriceQuoteByToken", MarketcapParam.builder()
                .currency(currency)
                .token(token)
                .build());
        return rpcDelegate.getMarketcap(request).map(RelayResponseWrapper::getResult);
    }

    public Observable<String> notifyTransactionSubmitted(RawTransaction rawTransaction, String from, String txHash) {
        NotifyTransactionSubmitParam notifyTransactionSubmitParam = NotifyTransactionSubmitParam.builder()
                .hash(txHash)
                .nonce(Numeric.toHexStringWithPrefix(rawTransaction.getNonce()))
                .to(rawTransaction.getTo())
                .value(Numeric.toHexStringWithPrefix(rawTransaction.getValue()))
                .gasPrice(Numeric.toHexStringWithPrefix(rawTransaction.getGasPrice()))
                .gas(Numeric.toHexStringWithPrefix(rawTransaction.getGasLimit()))
                .input(rawTransaction.getData())
                .from(from)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_notifyTransactionSubmitted", notifyTransactionSubmitParam);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.notifyTransactionSubmitted(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<String> notifyScanLogin(NotifyScanParam.SignParam signParam, String owner, String uuid) {
        NotifyScanParam notifyScanParam = NotifyScanParam.builder().owner(owner).uuid(uuid).sign(signParam).build();
        RequestWrapper request = new RequestWrapper("loopring_notifyScanLogin", notifyScanParam);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.notifyScanLogin(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<String> notifyStatus(NotifyStatusParam.NotifyBody body, String owner) {
        NotifyStatusParam notifyStatusParam = NotifyStatusParam.builder().owner(owner).body(body).build();
        RequestWrapper request = new RequestWrapper("loopring_notifyCirculr", notifyStatusParam);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.notifyStatus(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<String> getSignMessage(String hash) {
        GetSignParam getSignParam = GetSignParam.builder().key(hash).build();
        RequestWrapper request = new RequestWrapper("loopring_getTempStore", getSignParam);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.getSignMessage(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<Partner> createPartner(String owner) {
        RequestWrapper request = new RequestWrapper("loopring_createCityPartner", PartnerParam.builder()
                .walletAddress(owner));
        return rpcDelegate.createPartner(request).map(RelayResponseWrapper::getResult);
    }

    public Observable<Partner> activateInvitation() {
        Observable<RelayResponseWrapper<Partner>> observable = rpcDelegate.activateInvitation();
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<String> addCustomToken(String owner, String address, String symbol, String decimals) {
        AddTokenParam param = AddTokenParam.builder()
                .owner(owner)
                .address(address)
                .symbol(symbol)
                .decimals(decimals)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_addCustomToken", param);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.addCustomToken(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<List<Token>> getCustomToken(String owner) {
        GetTokenParam param = GetTokenParam.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getCustomTokens", param);
        Observable<RelayResponseWrapper<List<Token>>> observable = rpcDelegate.getCustomToken(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    // 市场相关接口
    public Observable<List<Ticker>> getTickers(TickerSource source) {
        GetTickersParam param = GetTickersParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .tickerSource(source.name())
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getTickerBySource", param);
        Observable<RelayResponseWrapper<List<Ticker>>> observable = rpcDelegate.getTickers(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<List<Trend>> getTrend(String market, TrendInterval interval) {
        GetTrendsParam param = GetTrendsParam.builder()
                .market(market)
                .interval(interval.getDescription())
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getTrend", param);
        Observable<RelayResponseWrapper<List<Trend>>> observable = rpcDelegate.getTrend(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<Depth> getDepths(String market, Integer length) {
        GetDepthsParam param = GetDepthsParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .market(market)
                .length(length)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getDepth", param);
        Observable<RelayResponseWrapper<Depth>> observable = rpcDelegate.getDepths(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<List<OrderFill>> getOrderFills(String market, String side) {
        GetOrderFillsParam param = GetOrderFillsParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .market(market)
                .side(side)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getLatestFills", param);
        Observable<RelayResponseWrapper<List<OrderFill>>> observable = rpcDelegate.getOrderFills(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    // 订单相关接口
    public Observable<PageWrapper<RawOrder>> getOrders(String owner, String orderHash, OrderStatus status, String market,
                                                       String side, OrderType type, int pageIndex, int pageSize) {
        GetOrdersParam param = GetOrdersParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .owner(owner)
                .side(side)
                .orderHash(orderHash)
                .status(status.name())
                .orderType(type.name())
                .market(market)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getOrders", param);
        Observable<RelayResponseWrapper<PageWrapper<RawOrder>>> observable = rpcDelegate.getOrders(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<PageWrapper<RawOrder>> getOrders(String owner, String orderType, int pageIndex, int pageSize) {
        GetOrdersParam param = GetOrdersParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .owner(owner)
                .orderType(orderType)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getOrders", param);
        Observable<RelayResponseWrapper<PageWrapper<RawOrder>>> observable = rpcDelegate.getOrders(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    // 订单相关接口
    public Observable<RawOrder> getOrderByHash(String orderHash) {
        GetOrdersParam param = GetOrdersParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .orderHash(orderHash)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getOrderByHash", param);
        Observable<RelayResponseWrapper<RawOrder>> observable = rpcDelegate.getOrderByHash(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<String> getEstimatedAllocatedAllowance(String owner, String symbol) {
        GetAllowanceParam param = GetAllowanceParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .owner(owner)
                .token(symbol)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getEstimatedAllocatedAllowance", param);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.getEstimatedAllocatedAllowance(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<String> getFrozenLRCFee(String owner) {
        GetFrozenParam param = GetFrozenParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getFrozenLRCFee", param);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.getFrozenLRCFee(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    // support for MARKET order submit
    public Observable<RelayResponseWrapper> submitOrder(OriginOrder order) {
        String p2p = order.getP2pSide() == null ? "" : order.getP2pSide().getDescription();
        SubmitOrderParam param = SubmitOrderParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .protocol(Default.PROTOCOL_ADDRESS)
                .sourceId(Default.SOURCE_ID)
                .owner(order.getOwner())
                .walletAddress(order.getWalletAddress())
                .tokenB(order.getTokenBuy())
                .tokenS(order.getTokenSell())
                .amountB(order.getAmountB())
                .amountS(order.getAmountS())
                .authAddr(order.getAuthAddr())
                .authPrivateKey(order.getAuthPrivateKey())
                .validSince(order.getValidSince())
                .validUntil(order.getValidUntil())
                .lrcFee(order.getLrcFee())
                .buyNoMoreThanAmountB(order.getBuyNoMoreThanAmountB())
                .marginSplitPercentage(order.getMargin())
                .powNonce(order.getPowNonce())
                .orderType(order.getOrderType().getDescription())
                .p2pSide(p2p)
                .v(Numeric.toBigInt(order.getV()).intValue())
                .r(order.getR())
                .s(order.getS())
                .build();
        RequestWrapper request = new RequestWrapper("loopring_submitOrder", param);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.sumitOrder(request);
        return observable.map(stringRelayResponseWrapper -> stringRelayResponseWrapper);
    }

    // support for P2P TAKER order submit
    public Observable<RelayResponseWrapper> submitOrderForP2P(OriginOrder order, String makerOrderHash) {
        SubmitOrderP2PParam param = SubmitOrderP2PParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .protocol(Default.PROTOCOL_ADDRESS)
                .sourceId(Default.SOURCE_ID)
                .owner(order.getOwner())
                .walletAddress(order.getWalletAddress())
                .tokenB(order.getTokenBuy())
                .tokenS(order.getTokenSell())
                .amountB(order.getAmountB())
                .amountS(order.getAmountS())
                .authAddr(order.getAuthAddr())
                .authPrivateKey(order.getAuthPrivateKey())
                .validSince(order.getValidSince())
                .validUntil(order.getValidUntil())
                .lrcFee(order.getLrcFee())
                .buyNoMoreThanAmountB(order.getBuyNoMoreThanAmountB())
                .marginSplitPercentage(order.getMargin())
                .powNonce(order.getPowNonce())
                .orderType(order.getOrderType().getDescription())
                .p2pSide(order.getP2pSide().getDescription())
                .side(order.getSide())
                .makerOrderHash(makerOrderHash)
                .v(Numeric.toBigInt(order.getV()).intValue())
                .r(order.getR())
                .s(order.getS())
                .build();
        Gson gson = new Gson();
        RequestWrapper request = new RequestWrapper("loopring_submitOrderForP2P", param);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.sumitOrderForP2P(request);
        return observable.map(response -> response);
    }

    public Observable<RelayResponseWrapper> submitRing(String makerOrderHash, String takerOrderHash, String rawTx) {
        SubmitRingParam param = SubmitRingParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .protocol(Default.PROTOCOL_ADDRESS)
                .makerOrderHash(makerOrderHash)
                .takerOrderHash(takerOrderHash)
                .rawTx(rawTx)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_submitRingForP2P", param);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.sumitRing(request);
        return observable.map(response -> response);
    }

    public Observable<String> cancelOrderFlex(CancelOrder cancelOrder, NotifyScanParam.SignParam signParam) {
        CancelOrderParam cancelParam = CancelOrderParam.builder()
                .type(cancelOrder.getType().getType())
                .cutoff(cancelOrder.getCutoff())
                .tokenB(cancelOrder.getTokenB())
                .tokenS(cancelOrder.getTokenS())
                .orderHash(cancelOrder.getOrderHash())
                .sign(signParam)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_flexCancelOrder", cancelParam);
        Observable<RelayResponseWrapper<String>> observable = rpcDelegate.cancelOrderFlex(request);
        return observable.map(RelayResponseWrapper::getResult);
    }
}
