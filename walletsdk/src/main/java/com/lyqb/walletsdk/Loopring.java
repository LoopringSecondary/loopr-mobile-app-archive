//package com.lyqb.walletsdk;
//
//import com.lyqb.walletsdk.exception.SdkInitializeException;
//import com.lyqb.walletsdk.exception.TransactionException;
//import com.lyqb.walletsdk.listener.BalanceListener;
//import com.lyqb.walletsdk.listener.TransactionListener;
//import com.lyqb.walletsdk.service.EthereumService;
//import com.lyqb.walletsdk.service.LoopringService;
//
//import org.web3j.crypto.Credentials;
//import org.web3j.crypto.RawTransaction;
//import org.web3j.crypto.TransactionEncoder;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.Web3jFactory;
//import org.web3j.protocol.core.methods.response.EthSendTransaction;
//import org.web3j.protocol.http.HttpService;
//import org.web3j.utils.Numeric;
//
//import java.math.BigInteger;
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
//public final class Loopring {
//    private OkHttpClient okHttpClient;
//    public Socket socketClient;
//    private Retrofit retrofitClient;
//    private Web3j web3jClient;
//
//    private LoopringService httpService;
////    private LooprSocketService socketService;
//    private EthereumService ethService;
//
//
//    /************constructor**************/
//    public Loopring() {
//        this(new LoopringConfig());
//    }
//
//    public Loopring(LoopringConfig config) {
//        initOkHttp(config);
//        initRetrofit(config);
//        initSocketIO(config);
//        initWeb3j(config);
//        initServices();
//    }
//    /************************************/
//
//
//    /**************initializer*******************/
//
//    private void initOkHttp(LoopringConfig config) {
//        okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .build();
//    }
//
//    private void initRetrofit(LoopringConfig config) {
//        retrofitClient = new Retrofit.Builder()
//                .baseUrl(config.relayBase)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(okHttpClient)
//                .build();
//    }
//
//    private void initSocketIO(LoopringConfig config) {
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
//            throw new SdkInitializeException();
//        }
//        socketClient.on(Socket.EVENT_CONNECT, args -> System.out.println("connected!"));
//        socketClient.on(Socket.EVENT_CONNECT_ERROR, args -> System.out.println("network error"));
//        socketClient.on(Socket.EVENT_CONNECTING, args -> System.out.println("connecting"));
//        socketClient.connect();
//    }
//
//    private void initWeb3j(LoopringConfig config) {
//        HttpService httpService = new HttpService(config.ethRpcUrl);
//        web3jClient = Web3jFactory.build(httpService);
//    }
//
//    private void initServices() {
////        httpService = new LoopringService(retrofitClient);
////        socketService = new LooprSocketService(socketClient);
////        ethService = new EthereumService();
//    }
//
//
//    /****************************************/
//
//
//    /*******************methods*************************/
//
//
//
//    public String sendTransaction(String to, BigInteger value, Credentials credentials) throws TransactionException {
//        String nonceStr = httpService.getNonce(credentials.getAddress()).toBlocking().first();
//        BigInteger nonce = Numeric.toBigInt(Numeric.cleanHexPrefix(nonceStr));
//
//        String gasPriceStr = httpService.getEstimateGasPrice().toBlocking().first();
//        BigInteger gasPrice = Numeric.toBigInt(Numeric.cleanHexPrefix(gasPriceStr));
//
//        BigInteger gasLimited = new BigInteger("21000");
//        RawTransaction rawTransaction = RawTransaction.createTransaction(
//                nonce,
//                gasPrice,
//                gasLimited,
//                to,
//                value,
//                ""
//        );
//
//        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//        String signedTransaction = Numeric.toHexString(signedMessage);
//
//
//        //send to eth node.
//        EthSendTransaction ethSendTransaction = ethService.sendTransaction(signedTransaction).toBlocking().first();
//
//        if (ethSendTransaction.hasError()) {
//            String message = ethSendTransaction.getError().getMessage();
//            throw new TransactionException(message);
//        }else {
//            //notify relay.
//            String tx = ethSendTransaction.getTransactionHash();
//            String txReply = httpService.notifyTransactionSubmitted(
//                    tx,
//                    nonceStr,
//                    to,
//                    Numeric.toHexStringWithPrefix(value),
//                    gasPriceStr,
//                    Numeric.toHexStringWithPrefix(gasLimited),
//                    "",
//                    credentials.getAddress()
//            ).toBlocking().first();
//            return tx;
//        }
//    }
//
//    /********************************************/
//
//    private void registerToRelay(String owner){
//        // notify relay.
//        String s = httpService.unlockWallet(owner).toBlocking().first();
//        System.out.println(s);
//    }
//
////    public void destroy() {
////        socketService.close();
////        socketClient.close();
////    }
//
//    public LoopringService getHttpService() {
//        return httpService;
//    }
//
////    public LooprSocketService getSocketService() {
////        // wait till socket connection established.
////        while (!socketClient.connected()) {
////            try {
////                Thread.sleep(200);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        }
////        return socketService;
////    }
//
//    public EthereumService getEthService() {
//        return ethService;
//    }
//
//    public BalanceListener newBalanceListener() {
//        return new BalanceListener();
//    }
//
//    public TransactionListener newTransactionListener() {
//        return new TransactionListener();
//    }
//
//}