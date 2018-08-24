package com.lyqb.walletsdk.service;

import com.lyqb.walletsdk.Constant;
import com.lyqb.walletsdk.model.loopr.request.RequestWrapper;
import com.lyqb.walletsdk.model.loopr.response.BalanceResult;
import com.lyqb.walletsdk.model.loopr.response.ResponseWrapper;
import com.lyqb.walletsdk.model.loopr.response.SupportedToken;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface LooprRpcDelegate {

    @POST(Constant.RELAY_RPC_URL)
    Observable<Map> send(@Body RequestWrapper request);


    @POST(Constant.RELAY_RPC_URL)
    Observable<ResponseWrapper<BalanceResult>> getBalance(@Body RequestWrapper request);

    @POST(Constant.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> getNonce(@Body RequestWrapper request);

    @POST(Constant.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> estimateGasPrice(@Body RequestWrapper request);

    @POST(Constant.RELAY_RPC_URL)
    Observable<ResponseWrapper<String>> unlockWallet(@Body RequestWrapper request);

    @POST(Constant.RELAY_RPC_URL)
    Observable<ResponseWrapper<List<SupportedToken>>> getSupportedTokens(@Body RequestWrapper request);

}
