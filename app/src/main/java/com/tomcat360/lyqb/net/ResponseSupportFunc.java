package com.tomcat360.lyqb.net;

import com.lyqb.walletsdk.model.response.ResponseWrapper;

import rx.functions.Func1;

/**
 * Title:ServerResponseFunc
 */

public class ResponseSupportFunc<T> implements Func1<ResponseWrapper<T>, T> {

    @Override
    public T call(ResponseWrapper<T> baseResponse) {
        //对返回码进行判断，如果不是000000，则证明服务器端返回错误信息了，便根据跟服务器约定好的错误码去解析异常
        //		ZgqbLogger.log(baseResponse.getSuccess());
        if (!G.TRUE.equals(baseResponse.getError())) {
            //如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
            throw new ServerException(baseResponse.getError(), baseResponse.getError());
        }
        //服务器请求数据成功，返回里面的数据实体
        if (baseResponse.getResult() != null) {
            return baseResponse.getResult();
        }
        return null;
    }
}
