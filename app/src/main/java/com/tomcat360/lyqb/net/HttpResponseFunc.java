package com.tomcat360.lyqb.net;

import rx.Observable;
import rx.functions.Func1;

/**
 * Title:HttpResponseFunc
 * Package:com.tomcat360.m.RespEntity
 * Description:TODO
 * Author: wwh@tomcat360.com
 * Date: 16/8/24
 * Version: V1.0.0
 * 版本号修改日期修改人修改内容
 */

public class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
	@Override
	public Observable<T> call(Throwable throwable) {
		//ExceptionEngine为处理异常的驱动器
		return Observable.error(ExceptionEngine.handleException(throwable));
	}
}
