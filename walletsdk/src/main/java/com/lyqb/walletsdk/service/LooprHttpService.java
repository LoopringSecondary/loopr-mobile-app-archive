package com.lyqb.walletsdk.service;

import com.google.common.collect.Maps;
import com.lyqb.walletsdk.model.loopr.request.RequestWrapper;
import com.lyqb.walletsdk.model.loopr.request.param.GetBalance;
import com.lyqb.walletsdk.model.loopr.request.param.GetNonce;
import com.lyqb.walletsdk.model.loopr.request.param.UnlockWallet;
import com.lyqb.walletsdk.model.loopr.response.BalanceResult;
import com.lyqb.walletsdk.model.loopr.response.ResponseWrapper;
import com.lyqb.walletsdk.model.loopr.response.SupportedToken;
import com.lyqb.walletsdk.singleton.ObjectMapperInstance;
import com.lyqb.walletsdk.singleton.OkHttpInstance;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;

public class LooprHttpService {

    private LooprRpcDelegate looprRpcDelegate;

    public LooprHttpService(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create(ObjectMapperInstance.getMapper()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(OkHttpInstance.getClient())
                .build();
        looprRpcDelegate = retrofit.create(LooprRpcDelegate.class);
    }

    public Observable<String> getNonce(String owner) {
        GetNonce getNonceParam = GetNonce.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getNonce", getNonceParam);
        return looprRpcDelegate.getNonce(request).map(ResponseWrapper::getResult);
    }

    public Observable<String> getEstimateGasPrice() {
        RequestWrapper request = new RequestWrapper("loopring_getEstimateGasPrice", Maps.newHashMap());
        return looprRpcDelegate.estimateGasPrice(request).map(ResponseWrapper::getResult);
    }

    public Observable<String> unlockWallet(String owner) {
        UnlockWallet unlockWallet = UnlockWallet.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_unlockWallet", unlockWallet);
        Observable<ResponseWrapper<String>> observable = looprRpcDelegate.unlockWallet(request);
        return observable.map(ResponseWrapper::getResult);
    }

    public Observable<BalanceResult> getBalance(String owner) {
        GetBalance param = GetBalance.builder()
                .delegateAddress("0x17233e07c67d086464fD408148c3ABB56245FA64")
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getBalance", param);
        return looprRpcDelegate.getBalance(request).map(ResponseWrapper::getResult);
    }

    public Observable<List<SupportedToken>> getSupportedToken() {
        RequestWrapper request = new RequestWrapper("loopring_getSupportedTokens", Maps.newHashMap());
        Observable<ResponseWrapper<List<SupportedToken>>> observable = looprRpcDelegate.getSupportedTokens(request);
        return observable.map(ResponseWrapper::getResult);
    }
}
