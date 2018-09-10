package com.lyqb.walletsdk.service;

import java.util.List;

import org.web3j.utils.Numeric;
import com.google.common.collect.Maps;
import com.lyqb.walletsdk.SDK;
import com.lyqb.walletsdk.deligate.RpcDelegate;
import com.lyqb.walletsdk.model.TransactionObject;
import com.lyqb.walletsdk.model.request.RequestWrapper;
import com.lyqb.walletsdk.model.request.param.BalanceParam;
import com.lyqb.walletsdk.model.request.param.NonceParam;
import com.lyqb.walletsdk.model.request.param.NotifyTransactionSubmitParam;
import com.lyqb.walletsdk.model.request.param.UnlockWallet;
import com.lyqb.walletsdk.model.response.ResponseWrapper;
import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.lyqb.walletsdk.model.response.data.Token;
import com.lyqb.walletsdk.util.Assert;

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
                .delegateAddress("0x17233e07c67d086464fD408148c3ABB56245FA64")
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getBalance", param);
        return rpcDelegate.getBalance(request).map(ResponseWrapper::getResult);
    }

    public Observable<List<Token>> getSupportedToken() {
        RequestWrapper request = new RequestWrapper("loopring_getSupportedTokens", Maps.newHashMap());
        Observable<ResponseWrapper<List<Token>>> observable = rpcDelegate.getSupportedTokens(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<String> notifyTransactionSubmitted(String txHash, String nonce, String to,String valueInHex, String gasPriceInHex, String gasLimitInHex, String dataInHex, String from) {
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

    public Observable<String> notifyTransactionSubmitted(String txHash, TransactionObject transactionObject) {
        // data validate.
        Assert.notNull(transactionObject, "");
        NotifyTransactionSubmitParam notifyTransactionSubmitParam = NotifyTransactionSubmitParam.builder()
                .hash(txHash)
                .nonce(Numeric.toHexStringWithPrefixSafe(transactionObject.getNonce()))
                .to(transactionObject.getTo())
                .value(Numeric.toHexStringWithPrefixSafe(transactionObject.getValue()))
                .gasPrice(Numeric.toHexStringWithPrefixSafe(transactionObject.getGasPrice()))
                .gas(Numeric.toHexStringWithPrefixSafe(transactionObject.getGasLimit()))
                .input(transactionObject.getData())
                .from(transactionObject.getFrom())
                .build();
        RequestWrapper request = new RequestWrapper("loopring_notifyTransactionSubmitted", notifyTransactionSubmitParam);
        Observable<ResponseWrapper<String>> observable = rpcDelegate.notifyTransactionSubmitted(request);
        return observable.map(ResponseWrapper::getResult);
    }
}
