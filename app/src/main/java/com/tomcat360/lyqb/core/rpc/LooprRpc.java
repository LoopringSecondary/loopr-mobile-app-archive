package com.tomcat360.lyqb.core.rpc;

import com.tomcat360.lyqb.core.model.loopr.request.RequestWrapper;
import com.tomcat360.lyqb.core.model.loopr.response.BalanceResult;
import com.tomcat360.lyqb.core.model.loopr.response.Response;
import com.tomcat360.lyqb.core.model.loopr.response.SupportedToken;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface LooprRpc {

    @POST("rpc/v2/")
    Observable<Map> send(@Body RequestWrapper request);


    @POST("rpc/v2/")
//    Observable<BalanceResult> getBalance(@Body RequestWrapper request);
    Observable<Response<BalanceResult>> getBalance(@Body RequestWrapper request);


    @POST("rpc/v2/")
    Observable<Response<String>> getNonce(@Body RequestWrapper request);

    @POST("rpc/v2/")
    Observable<Response<String>> estimateGasPrice(@Body RequestWrapper request);

    @POST("rpc/v2/")
    Observable<Response<String>> unlockWallet(@Body RequestWrapper request);

    @POST("rpc/v2/")
    Observable<Response<List<SupportedToken>>> getSupportedTokens(@Body RequestWrapper request);

}
