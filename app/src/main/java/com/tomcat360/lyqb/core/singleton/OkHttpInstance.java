package com.tomcat360.lyqb.core.singleton;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class OkHttpInstance {
    private static final OkHttpClient okHttpClient;

    static {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new LogInterceptor())
                .build();
    }

    public static OkHttpClient getClient() {
        return okHttpClient;
    }

    private OkHttpInstance() {
    }

    @Slf4j
    private static class LogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(request);
            long endTime = System.currentTimeMillis();

            final long duration = endTime - startTime;

            final String url = request.url().toString();
            final String method = request.method();

            Buffer buffer = new Buffer();
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                requestBody.writeTo(buffer);
            }
            final String requestBodyStr = buffer.readUtf8();

            final int responseCode = response.code();

            log.info(String.format("-------------------Http Request begin--------------------"));
            log.info(String.format("| Request url: %s", url));
            log.info(String.format("| Request method: %s", method));
            log.info(String.format("| Request body: %s", requestBodyStr));
            log.info(String.format("| Response status code: %d", responseCode));
            log.info(String.format("| Http request complete in %d ms.", duration));
            log.info(String.format("-------------------Http Request end-----------------------"));
            return response;
        }
    }
}


