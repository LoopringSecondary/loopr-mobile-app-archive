package leaf.prod.walletsdk.service;

import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Numeric;
import com.google.common.collect.Maps;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.Partner;
import leaf.prod.walletsdk.model.TransactionSignature;
import leaf.prod.walletsdk.model.request.RequestWrapper;
import leaf.prod.walletsdk.model.request.param.AddTokenParam;
import leaf.prod.walletsdk.model.request.param.BalanceParam;
import leaf.prod.walletsdk.model.request.param.MarketcapParam;
import leaf.prod.walletsdk.model.request.param.NonceParam;
import leaf.prod.walletsdk.model.request.param.NotifyTransactionSubmitParam;
import leaf.prod.walletsdk.model.request.param.PartnerParam;
import leaf.prod.walletsdk.model.request.param.TransactionParam;
import leaf.prod.walletsdk.model.request.param.UnlockWallet;
import leaf.prod.walletsdk.model.response.ResponseWrapper;
import leaf.prod.walletsdk.model.response.data.BalanceResult;
import leaf.prod.walletsdk.model.response.data.MarketcapResult;
import leaf.prod.walletsdk.model.response.data.Token;
import leaf.prod.walletsdk.model.response.data.TransactionPageWrapper;
import leaf.prod.walletsdk.pojo.loopring.response.data.ScanLoginInfo;
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
        return rpcDelegate.getNonce(request).map(ResponseWrapper::getResult);
    }

    public Observable<String> getEstimateGasPrice() {
        RequestWrapper request = new RequestWrapper("loopring_getEstimateGasPrice", Maps.newHashMap());
        return rpcDelegate.estimateGasPrice(request).map(ResponseWrapper::getResult);
    }

    public Observable<String> notifyCreateWallet(String owner) {
        UnlockWallet unlockWallet = UnlockWallet.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_unlockWallet", unlockWallet);
        Observable<ResponseWrapper<String>> observable = rpcDelegate.unlockWallet(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<BalanceResult> getBalance(String owner) {
        BalanceParam param = BalanceParam.builder()
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getBalance", param);
        return rpcDelegate.getBalance(request).map(ResponseWrapper::getResult);
    }

    public Observable<TransactionPageWrapper> getTransactions(String owner, String symbol, int pageIndex, int pageSize) {
        TransactionParam param = TransactionParam.builder()
                .owner(owner)
                .symbol(symbol)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getTransactions", param);
        Observable<ResponseWrapper<TransactionPageWrapper>> observable = rpcDelegate.getTransactions(request);
        return observable.map(ResponseWrapper::getResult);
    }

    /**
     * use getCustomToken instead: getCustomToken = getSupportedToken + customToken
     */
    @Deprecated
    public Observable<List<Token>> getSupportedToken() {
        RequestWrapper request = new RequestWrapper("loopring_getSupportedTokens", Maps.newHashMap());
        Observable<ResponseWrapper<List<Token>>> observable = rpcDelegate.getSupportedTokens(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<MarketcapResult> getMarketcap(String currency) {
        RequestWrapper request = new RequestWrapper("loopring_getPriceQuote", MarketcapParam.builder()
                .currency(currency)
                .build());
        return rpcDelegate.getMarketcap(request).map(ResponseWrapper::getResult);
    }

    public Observable<MarketcapResult> getPriceQuoteByToken(String currency, String token) {
        RequestWrapper request = new RequestWrapper("loopring_getPriceQuoteByToken", MarketcapParam.builder()
                .currency(currency)
                .token(token)
                .build());
        return rpcDelegate.getMarketcap(request).map(ResponseWrapper::getResult);
    }

    public Observable<String> notifyTransactionSubmitted(String txHash, String nonce, String to, String valueInHex, String gasPriceInHex, String gasLimitInHex, String dataInHex, String from) {
        NotifyTransactionSubmitParam notifyTransactionSubmitParam = NotifyTransactionSubmitParam.builder()
                .hash(txHash)
                .nonce(nonce)
                .to(to)
                .value(valueInHex)
                .gasPrice(gasPriceInHex)
                .gas(gasLimitInHex)
                .input(dataInHex)
                .from(from)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_notifyTransactionSubmitted", notifyTransactionSubmitParam);
        Observable<ResponseWrapper<String>> observable = rpcDelegate.notifyTransactionSubmitted(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<String> notifyTransactionSubmitted(RawTransaction rawTransaction, String from, String txHash, TransactionSignature signature) {
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
        Observable<ResponseWrapper<String>> observable = rpcDelegate.notifyTransactionSubmitted(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<String> notifyTransactionSubmitted(String txHash, Transaction transaction) {
        NotifyTransactionSubmitParam param = NotifyTransactionSubmitParam.builder()
                .hash(txHash)
                .nonce(transaction.getNonceRaw())
                .to(transaction.getTo())
                .value(transaction.getValueRaw())
                .gasPrice(transaction.getGasPriceRaw())
                .gas(transaction.getGasRaw())
                .input(transaction.getInput())
                .from(transaction.getFrom())
                .v(Numeric.toHexStringWithPrefixSafe(BigInteger.valueOf(transaction.getV())))
                .r(transaction.getR())
                .s(transaction.getS())
                .build();
        RequestWrapper request = new RequestWrapper("loopring_notifyTransactionSubmitted", param);
        Observable<ResponseWrapper<String>> observable = rpcDelegate.notifyTransactionSubmitted(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<String> notifyScanLogin(ScanLoginInfo scanLoginInfo) {
        RequestWrapper request = new RequestWrapper("loopring_notifyScanLogin", scanLoginInfo);
        Observable<ResponseWrapper<String>> observable = rpcDelegate.notifyScanLogin(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<Partner> createPartner(String owner) {
        RequestWrapper request = new RequestWrapper("loopring_createCityPartner", PartnerParam.builder()
                .walletAddress(owner));
        return rpcDelegate.createPartner(request).map(ResponseWrapper::getResult);
    }

    public Observable<Partner> activateInvitation() {
        Observable<ResponseWrapper<Partner>> observable = rpcDelegate.activateInvitation();
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<String> addCustomToken(String owner, String address, String symbol, String decimals) {
        AddTokenParam param = AddTokenParam.builder()
                .owner(owner)
                .address(address)
                .symbol(symbol)
                .decimals(decimals)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_addCustomToken", param);
        Observable<ResponseWrapper<String>> observable = rpcDelegate.addCustomToken(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<List<Token>> getCustomToken(String owner) {
        com.lyqb.walletsdk.model.request.param.GetTokenParam param = com.lyqb.walletsdk.model.request.param.GetTokenParam
                .builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getCustomTokens", param);
        Observable<ResponseWrapper<List<Token>>> observable = rpcDelegate.getCustomToken(request);
        return observable.map(ResponseWrapper::getResult);
    }
}
