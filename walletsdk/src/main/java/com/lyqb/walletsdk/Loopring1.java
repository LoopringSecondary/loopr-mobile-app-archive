//package com.lyqb.walletsdk;
//
//import com.lyqb.walletsdk.exception.InitializeFailureException;
//import com.lyqb.walletsdk.service.EthHttpService;
//import com.lyqb.walletsdk.service.LooprHttpService;
//import com.lyqb.walletsdk.service.LooprSocketService;
//
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.Web3jFactory;
//import org.web3j.protocol.http.HttpService;
//
//import java.net.URISyntaxException;
//import java.util.concurrent.TimeUnit;
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import okhttp3.OkHttpClient;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public final class Loopring1 {
//
//    private static OkHttpClient okHttpClient;
//    private static Socket socketClient;
//    private static Retrofit retrofitClient;
//    private static Web3j web3jClient;
//
//    private static LooprHttpService httpService;
//    private static LooprSocketService socketService;
//    private static EthHttpService ethService;
//
//    private Loopring1() {
//    }
//
//    public static void create() {
//        create(new LoopringConfig());
//    }
//
//    public static void create(LoopringConfig config) {
//        initOkHttp(config);
//        initRetrofit(config);
//        initSocketIO(config);
//        initWeb3j(config);
//        initServices();
//    }
//
//    public static void destroy() {
//        socketService.close();
//        socketClient.close();
//    }
//
//    public static LooprHttpService getHttpService() {
//        if (httpService == null) {
//            throw new InitializeFailureException();
//        }
//        return httpService;
//    }
//
//    public static LooprSocketService getSocketService() {
//        if (socketService == null) {
//            throw new InitializeFailureException();
//        }
//        return socketService;
//    }
//
//    public static EthHttpService getEthService() {
//        if (ethService == null) {
//            throw new InitializeFailureException();
//        }
//        return ethService;
//    }
//
//    private static void initOkHttp(LoopringConfig config) {
//        okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .build();
//    }
//
//    private static void initRetrofit(LoopringConfig config) {
//        retrofitClient = new Retrofit.Builder()
//                .baseUrl(config.relayBase)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(okHttpClient)
//                .build();
//    }
//
//    private static void initSocketIO(LoopringConfig config) {
//        IO.Options opt = new IO.Options();
//        opt.reconnection = true;
//        opt.reconnectionAttempts = 5;
//        opt.transports = new String[]{"websocket"};
//        opt.callFactory = okHttpClient;
//        opt.webSocketFactory = okHttpClient;
//        String relayBase = config.relayBase.endsWith("/") ? config.relayBase : config.relayBase + "/";
//        try {
//            socketClient = IO.socket(relayBase, opt);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            throw new InitializeFailureException();
//        }
//        socketClient.on(Socket.EVENT_CONNECT, args -> System.out.println("connected!"));
//        socketClient.on(Socket.EVENT_CONNECT_ERROR, args -> System.out.println("network error"));
//        socketClient.on(Socket.EVENT_CONNECTING, args -> System.out.println("connecting"));
//        socketClient.connect();
//    }
//
//    private static void initWeb3j(LoopringConfig config) {
//        HttpService httpService = new HttpService(config.ethRpcUrl);
//        web3jClient = Web3jFactory.build(httpService);
//    }
//
//    private static void initServices() {
//        httpService = new LooprHttpService(retrofitClient);
//        socketService = new LooprSocketService(socketClient);
//        ethService = new EthHttpService(web3jClient);
//    }
//
//}
