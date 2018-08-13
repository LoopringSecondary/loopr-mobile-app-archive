package com.tomcat360.lyqb.net;


import rx.functions.Func1;

/**
 * Title:ServerResponseFunc
 * Package:com.tomcat360.m.RespEntity
 * Description:TODO
 * Author: wwh@tomcat360.com
 * Date: 16/8/24
 * Version: V1.0.0
 * 版本号修改日期修改人修改内容
 */

public class ResponseFunc<T> implements Func1<BaseResponse<T>,T> {
	@Override
	public T call(BaseResponse<T> baseResponse) {
		//对返回码进行判断，如果不是000000，则证明服务器端返回错误信息了，便根据跟服务器约定好的错误码去解析异常
//		ZgqbLogger.log(baseResponse.getSuccess());
		if (!G.TRUE.equals(baseResponse.getSuccess())) {
			//如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
			throw new ServerException(baseResponse.getErrCode(),baseResponse.getErrMsg());
		}
		//服务器请求数据成功，返回里面的数据实体
		if(baseResponse.getBody() != null){
			return baseResponse.getBody().data;
		}
		return null;
	}
}
