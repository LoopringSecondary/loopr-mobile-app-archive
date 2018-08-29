//package com.lyqb.walletsdk.util;
//
//import com.lyqb.walletsdk.service.LoopringService;
//
//import rx.Observable;
//
//public class NonceUtils {
//
//    private static LoopringService httpService = new LoopringService();
//
//    public static String getNonce(String owner) {
//        Observable<String> nonce = httpService.getNonce(owner);
//        return nonce.toBlocking().first();
//    }
//
//}
