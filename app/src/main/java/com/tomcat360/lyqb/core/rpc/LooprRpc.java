package com.tomcat360.lyqb.core.rpc;

import com.tomcat360.lyqb.core.model.loopr.request.RequestWrapper;
import com.tomcat360.lyqb.core.model.loopr.response.BalanceResult;
import com.tomcat360.lyqb.core.model.loopr.response.EstimateGasPriceResult;
import com.tomcat360.lyqb.core.model.loopr.response.NonceResult;
import com.tomcat360.lyqb.core.model.loopr.response.UnlockWalletResult;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface LooprRpc {

//    @POST("rpc/v2/")
//    Observable<LooprResponse> sendTransaction(@Body RequestWrapper request);

    @POST("rpc/v2/")
    Observable<BalanceResult> getBalance(@Body RequestWrapper request);

    @POST("rpc/v2/")
    Observable<NonceResult> getNonce(@Body RequestWrapper request);

    @POST("rpc/v2/")
    Observable<EstimateGasPriceResult> estimateGasPrice(@Body RequestWrapper request);

    @POST("rpc/v2/")
    Observable<UnlockWalletResult> unlockWallet(@Body RequestWrapper request);
}
