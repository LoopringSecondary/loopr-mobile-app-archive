package com.tomcat360.lyqb.core.service;

import com.google.common.collect.Maps;
import com.tomcat360.lyqb.core.model.loopr.request.RequestWrapper;
import com.tomcat360.lyqb.core.model.loopr.request.param.GetBalance;
import com.tomcat360.lyqb.core.model.loopr.request.param.GetNonce;
import com.tomcat360.lyqb.core.model.loopr.request.param.UnlockWallet;
import com.tomcat360.lyqb.core.model.loopr.response.BalanceResult;
import com.tomcat360.lyqb.core.model.loopr.response.EstimateGasPriceResult;
import com.tomcat360.lyqb.core.model.loopr.response.NonceResult;
import com.tomcat360.lyqb.core.model.loopr.response.UnlockWalletResult;
import com.tomcat360.lyqb.core.rpc.LooprRpc;
import com.tomcat360.lyqb.core.singleton.ObjectMapperInstance;
import com.tomcat360.lyqb.core.singleton.OkHttpInstance;

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

    public Observable<NonceResult> getNonce(String owner) {
        GetNonce getNonceParam = GetNonce.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getNonce", getNonceParam);
        return looprRpc.getNonce(request);
    }

    public Observable<EstimateGasPriceResult> getEstimateGasPrice() {
        RequestWrapper request = new RequestWrapper("loopring_getEstimateGasPrice", Maps.newHashMap());
        return looprRpc.estimateGasPrice(request);
    }

    public Observable<UnlockWalletResult> unlockWallet(String owner) {
        UnlockWallet unlockWallet = UnlockWallet.builder()
                .owner(owner)
                .build();
        RequestWrapper request = new RequestWrapper("loopring_unlockWallet", unlockWallet);
        return looprRpc.unlockWallet(request);
    }

    public Observable<BalanceResult> getBalance() {
        GetBalance param =  GetBalance.builder()
                .delegateAddress("")
                .owner("")
                .build();
        RequestWrapper request = new RequestWrapper("loopring_getBalance", param);
        return looprRpc.getBalance(request);
    }
}
