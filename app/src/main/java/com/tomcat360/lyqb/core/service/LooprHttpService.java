package com.tomcat360.lyqb.core.service;

import com.google.common.collect.Maps;
import com.tomcat360.lyqb.core.model.loopr.request.RequestWrapper;
import com.tomcat360.lyqb.core.model.loopr.request.param.GetBalance;
import com.tomcat360.lyqb.core.model.loopr.request.param.GetNonce;
import com.tomcat360.lyqb.core.model.loopr.request.param.UnlockWallet;
import com.tomcat360.lyqb.core.model.loopr.response.BalanceResult;
import com.tomcat360.lyqb.core.model.loopr.response.Response;
import com.tomcat360.lyqb.core.model.loopr.response.SupportedToken;
import com.tomcat360.lyqb.core.rpc.LooprRpc;
import com.tomcat360.lyqb.core.singleton.ObjectMapperInstance;
import com.tomcat360.lyqb.core.singleton.OkHttpInstance;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;

public class LooprHttpService {

    private LooprRpc looprRpc;

    public LooprHttpService(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create(ObjectMapperInstance.getMapper()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(OkHttpInstance.getClient())
                .build();
        looprRpc = retrofit.create(LooprRpc.class);
    }

    public Observable<String> getNonce(String owner) {
        GetNonce getNonceParam = GetNonce.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getNonce", getNonceParam);
        return looprRpc.getNonce(request).map(Response::getResult);
    }

    public Observable<String> getEstimateGasPrice() {
        RequestWrapper request = new RequestWrapper("loopring_getEstimateGasPrice", Maps.newHashMap());
        return looprRpc.estimateGasPrice(request).map(Response::getResult);
    }

    public Observable<String> unlockWallet(String owner) {
        UnlockWallet unlockWallet = UnlockWallet.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_unlockWallet", unlockWallet);
        Observable<Response<String>> observable = looprRpc.unlockWallet(request);
        return observable.map(Response::getResult);
    }

    public Observable<BalanceResult> getBalance(String owner) {
        GetBalance param = GetBalance.builder()
                .delegateAddress("0x17233e07c67d086464fD408148c3ABB56245FA64")
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getBalance", param);
        return looprRpc.getBalance(request).map(Response::getResult);
    }

    public Observable<List<SupportedToken>> getSupportedToken() {
        RequestWrapper request = new RequestWrapper("loopring_getSupportedTokens", Maps.newHashMap());
        Observable<Response<List<SupportedToken>>> observable = looprRpc.getSupportedTokens(request);
        return observable.map(Response::getResult);
    }
}
