package com.lyqb.walletsdk;

import com.lyqb.walletsdk.exception.SdkInitializeException;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

public class SDK {

    private static OkHttpClient okHttpClient = null;
    private static Socket socketClient = null;

    private static String relayBase = null;
    private static String ethBase = null;

    public static String relayBase() {
        return relayBase;
    }

    public static String ethBase() {
        return ethBase;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            throw new SdkInitializeException();
        }
        return okHttpClient;
    }

    public static Socket getSocketClient() {
        if (okHttpClient == null) {
            throw new SdkInitializeException();
        }
        return socketClient;
    }

    public static void initSDK() {
        relayBase = "https://relay1.loopring.io";
        ethBase = "https://relay1.loopring.io/eth";
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        IO.Options opt = new IO.Options();
        opt.reconnection = true;
        opt.reconnectionAttempts = 5;
        opt.transports = new String[]{"websocket"};
        opt.callFactory = okHttpClient;
        opt.webSocketFactory = okHttpClient;
        relayBase = relayBase.endsWith("/") ? relayBase : relayBase + "/";
        try {
            socketClient = IO.socket(relayBase, opt);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new SdkInitializeException();
        }
        socketClient.on(Socket.EVENT_CONNECT, args -> System.out.println("connected!"));
        socketClient.on(Socket.EVENT_CONNECT_ERROR, args -> System.out.println("network error"));
        socketClient.on(Socket.EVENT_CONNECTING, args -> System.out.println("connecting"));
        socketClient.connect();
    }
}
