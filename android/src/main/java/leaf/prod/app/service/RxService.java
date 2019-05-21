package leaf.prod.app.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.util.Log;

import leaf.prod.app.net.G;
import leaf.prod.app.utils.AndroidUtils;
import leaf.prod.walletsdk.util.SPUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Title:RxService
 */

public class RxService {

    private Context mcontext;

    public RxService with(Context context) {
        mcontext = context;
        return this;
    }

    private OkHttpClient getClient() {
        return new OkHttpClient.Builder().addInterceptor(new TokenInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request() // originalRequest
                                .newBuilder()
                                //						.addHeader("Content-Type", "application/json;charset=UTF-8")
                                .addHeader("Accept", "application/json,application/javascript")
                                .addHeader("Cookie", (String) SPUtils.get(mcontext, "SP_COOKIE", ""))
                                .addHeader("AppSource", AndroidUtils.getChannel(mcontext))
                                .addHeader("vr", AndroidUtils.getVersionName(mcontext))
                                .addHeader("pf", "Android")
                                .addHeader("dt", System.currentTimeMillis() + "")
                                .build());
                    }
                })
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
    }

    public <T> T createApi(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(G.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();
        return retrofit.create(clazz);
    }

    private class TokenInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            //这里获取请求返回的cookie
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                final StringBuffer cookieBuffer = new StringBuffer();
                //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可.大家可以用别的方法保存cookie数据
                Observable.from(originalResponse.headers("Set-Cookie")).map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        String[] cookieArray = s.split(";");
                        return cookieArray[0];
                    }
                }).subscribe(new Action1<String>() {
                    @Override
                    public void call(String cookie) {
                        cookieBuffer.append(cookie).append(";");
                    }
                });
                //				SPUtils.put(APP.getInstance(),G.SP_COOKIE,cookieBuffer.toString());
                Log.d("turn_session", cookieBuffer.toString());
                if (cookieBuffer.toString().contains("JSESSIONID="))
                    SPUtils.put(mcontext, "SP_COOKIE", cookieBuffer.toString());
                //					G.SP_COOKIE = cookieBuffer.toString();
            }
            return originalResponse;
        }
    }
}
