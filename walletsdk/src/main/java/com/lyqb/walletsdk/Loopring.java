package com.lyqb.walletsdk;

import com.lyqb.walletsdk.exception.SdkUninitialized;
import com.lyqb.walletsdk.service.LooprHttpService;
import com.lyqb.walletsdk.service.LooprSocketService;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Loopring {

    private static OkHttpClient okHttpClient;

    private static Socket socketClient;
    private static Retrofit retrofitClient;

    private static Web3j web3jClient;


    private static LooprHttpService httpService;
    private static LooprSocketService socketService;


    public static void init() {
        init(new LoopringConfig());
    }


    public static void init(LoopringConfig config) {
        initOkHttp(config);
        initRetrofit(config);
        initSocketIO(config);
        initWeb3j(config);
        initServices();
    }


    public static void destroy() {

    }

    public static Web3j getWeb3jClient() {
        if (web3jClient == null) {
            throw new SdkUninitialized();
        }
        return web3jClient;
    }

    public static LooprHttpService getHttpService() {
        if (httpService == null) {
            throw new SdkUninitialized();
        }
        return httpService;
    }

    public static LooprSocketService getSocketService() {
        if (socketService == null) {
            throw new SdkUninitialized();
        }
        return socketService;
    }

    private Loopring() {
    }


    private static void initOkHttp(LoopringConfig config) {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    private static void initRetrofit(LoopringConfig config) {
        retrofitClient = new Retrofit.Builder()
                .baseUrl(config.relayBase)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private static void initSocketIO(LoopringConfig config) {
        IO.Options opt = new IO.Options();
        opt.reconnection = true;
        opt.reconnectionAttempts = 10;
        opt.transports = new String[]{"websocket"};
        opt.callFactory = okHttpClient;
        opt.webSocketFactory = okHttpClient;
        String relayBase = config.relayBase.endsWith("/") ? config.relayBase : config.relayBase + "/";
        try {
            socketClient = IO.socket(relayBase, opt);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socketClient.once(Socket.EVENT_CONNECT, args -> System.out.println("connected!"));
        socketClient.connect();
        while (!socketClient.connected()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initWeb3j(LoopringConfig config) {
        HttpService httpService = new HttpService(config.ethRpcUrl);
        web3jClient = Web3jFactory.build(httpService);
    }

    private static void initServices() {
        httpService = new LooprHttpService(retrofitClient);
        socketService = new LooprSocketService(socketClient);
    }

}
