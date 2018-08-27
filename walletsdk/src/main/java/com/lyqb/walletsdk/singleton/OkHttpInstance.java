//package com.lyqb.walletsdk.singleton;
//
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.OkHttpClient;
//
//public class OkHttpInstance {
//    private static final OkHttpClient okHttpClient;
//
//    static {
//        okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .build();
//    }
//
//    public static OkHttpClient getClient() {
//        return okHttpClient;
//    }
//
//    private OkHttpInstance() {
//    }
//
//}
//
//
