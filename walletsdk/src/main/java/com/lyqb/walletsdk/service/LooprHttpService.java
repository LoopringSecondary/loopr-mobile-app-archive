package com.lyqb.walletsdk.service;

import com.google.common.collect.Maps;
import com.lyqb.walletsdk.model.request.RequestWrapper;
import com.lyqb.walletsdk.model.request.param.GetBalance;
import com.lyqb.walletsdk.model.request.param.GetNonce;
import com.lyqb.walletsdk.model.request.param.NotifyTransactionSubmitParam;
import com.lyqb.walletsdk.model.request.param.UnlockWallet;
import com.lyqb.walletsdk.model.response.BalanceResult;
import com.lyqb.walletsdk.model.response.ResponseWrapper;
import com.lyqb.walletsdk.model.response.SupportedToken;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;

public class LooprHttpService {

    private RpcDelegate rpcDelegate;

//    public LooprHttpService(String url) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(OkHttpInstance.getClient())
//                .build();
//        rpcDelegate = retrofit.create(RpcDelegate.class);
//    }

    public LooprHttpService(Retrofit retrofit) {
        rpcDelegate = retrofit.create(RpcDelegate.class);
    }

    public Observable<String> getNonce(String owner) {
        GetNonce getNonceParam = GetNonce.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getNonce", getNonceParam);
        return rpcDelegate.getNonce(request).map(ResponseWrapper::getResult);
    }

    public Observable<String> getEstimateGasPrice() {
        RequestWrapper request = new RequestWrapper("loopring_getEstimateGasPrice", Maps.newHashMap());
        return rpcDelegate.estimateGasPrice(request).map(ResponseWrapper::getResult);
    }

    public Observable<String> unlockWallet(String owner) {
        UnlockWallet unlockWallet = UnlockWallet.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_unlockWallet", unlockWallet);
        Observable<ResponseWrapper<String>> observable = rpcDelegate.unlockWallet(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<BalanceResult> getBalance(String owner) {
        GetBalance param = GetBalance.builder()
                .delegateAddress("0x17233e07c67d086464fD408148c3ABB56245FA64")
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getBalance", param);
        return rpcDelegate.getBalance(request).map(ResponseWrapper::getResult);
    }

    public Observable<List<SupportedToken>> getSupportedToken() {
        RequestWrapper request = new RequestWrapper("loopring_getSupportedTokens", Maps.newHashMap());
        Observable<ResponseWrapper<List<SupportedToken>>> observable = rpcDelegate.getSupportedTokens(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<String> notifyTransactionSubmitted(String hash, String nonce, String to,String valueInHex, String gasPriceInHex, String gasLimitInHex, String dataInHex, String from) {
        NotifyTransactionSubmitParam notifyTransactionSubmitParam = NotifyTransactionSubmitParam.builder()
                .hash(hash)
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
}
