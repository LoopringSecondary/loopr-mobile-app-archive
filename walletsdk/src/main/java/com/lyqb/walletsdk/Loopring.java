package com.lyqb.walletsdk;

import com.lyqb.walletsdk.exception.InitializeFailureException;
import com.lyqb.walletsdk.exception.InvalidPrivateKeyException;
import com.lyqb.walletsdk.exception.KeystoreSaveException;
import com.lyqb.walletsdk.exception.TransactionFailureException;
import com.lyqb.walletsdk.model.WalletDetail;
import com.lyqb.walletsdk.service.EthHttpService;
import com.lyqb.walletsdk.service.LooprHttpService;
import com.lyqb.walletsdk.service.LooprSocketService;
import com.lyqb.walletsdk.service.listener.BalanceListener;
import com.lyqb.walletsdk.service.listener.TransactionListener;
import com.lyqb.walletsdk.util.Assert;
import com.lyqb.walletsdk.util.KeystoreUtils;
import com.lyqb.walletsdk.util.MnemonicUtils;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Loopring {
    private OkHttpClient okHttpClient;
    public Socket socketClient;
    private Retrofit retrofitClient;
    private Web3j web3jClient;

    private LooprHttpService httpService;
    private LooprSocketService socketService;
    private EthHttpService ethService;


    /************constructor**************/
    public Loopring() {
        this(new LoopringConfig());
    }

    public Loopring(LoopringConfig config) {
        initOkHttp(config);
        initRetrofit(config);
        initSocketIO(config);
        initWeb3j(config);
        initServices();
    }
    /************************************/


    /**************initializer*******************/

    private void initOkHttp(LoopringConfig config) {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    private void initRetrofit(LoopringConfig config) {
        retrofitClient = new Retrofit.Builder()
                .baseUrl(config.relayBase)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private void initSocketIO(LoopringConfig config) {
        IO.Options opt = new IO.Options();
        opt.reconnection = true;
        opt.reconnectionAttempts = 5;
        opt.transports = new String[]{"websocket"};
        opt.callFactory = okHttpClient;
        opt.webSocketFactory = okHttpClient;
        String relayBase = config.relayBase.endsWith("/") ? config.relayBase : config.relayBase + "/";
        try {
            socketClient = IO.socket(relayBase, opt);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new InitializeFailureException();
        }
        socketClient.on(Socket.EVENT_CONNECT, args -> System.out.println("connected!"));
        socketClient.on(Socket.EVENT_CONNECT_ERROR, args -> System.out.println("network error"));
        socketClient.on(Socket.EVENT_CONNECTING, args -> System.out.println("connecting"));
        socketClient.connect();
    }

    private void initWeb3j(LoopringConfig config) {
        HttpService httpService = new HttpService(config.ethRpcUrl);
        web3jClient = Web3jFactory.build(httpService);
    }

    private void initServices() {
        httpService = new LooprHttpService(retrofitClient);
        socketService = new LooprSocketService(socketClient);
        ethService = new EthHttpService(web3jClient);
    }


    /****************************************/


    /*******************methods*************************/
    public WalletDetail createFromMnemonic(String mnemonic, String dpath, String password, File keystoreDest) throws KeystoreSaveException {
        // validate inputs.
        Assert.validateMnemonic(mnemonic);
//        Assert.hasText(password, "password can not be null");
        Assert.checkDirectory(keystoreDest);

        if (dpath == null) {
            dpath = Default.DEFAULT_DPATH;
        }

        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        List<ChildNumber> childNumberList = HDUtils.parsePath(dpath.replaceAll("\'", "H").toUpperCase());
        DeterministicKey rootKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy hdKey = new DeterministicHierarchy(rootKey);
        DeterministicKey destKey = hdKey.deriveChild(childNumberList, true, true, new ChildNumber(0));
        ECKeyPair ecKeyPair = ECKeyPair.create(destKey.getPrivKey());
        Credentials credentials = Credentials.create(ecKeyPair);

        String walletFileName;
        try {
            walletFileName = WalletUtils.generateWalletFile(password, ecKeyPair, keystoreDest, false);
        } catch (Exception e) {
            throw new KeystoreSaveException(e);
        }
        createWalletNotification(credentials.getAddress());
        return new WalletDetail(walletFileName, mnemonic);
    }

    public WalletDetail importFromMnemonic(String mnemonic, String dpath, String password, File dest, int childNumber) throws KeystoreSaveException {
        // validate inputs.
        Assert.hasText(mnemonic, "illegal mnemonic");
        Assert.hasText(password, "password can not be null");
        Assert.checkDirectory(dest);

        if (dpath == null) {
            dpath = Default.DEFAULT_DPATH;
        }

        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        List<ChildNumber> childNumberList = HDUtils.parsePath(dpath.replaceAll("\'", "H").toUpperCase());
        DeterministicKey rootKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy hdKey = new DeterministicHierarchy(rootKey);
        DeterministicKey destKey = hdKey.deriveChild(childNumberList, true, true, new ChildNumber(childNumber));
        ECKeyPair ecKeyPair = ECKeyPair.create(destKey.getPrivKey());
        Credentials credentials = Credentials.create(ecKeyPair);
        String walletFileName;
        try {
            walletFileName = WalletUtils.generateWalletFile(password, ecKeyPair, dest, false);
        } catch (Exception e) {
            throw new KeystoreSaveException(e);
        }
        createWalletNotification(credentials.getAddress());
        return new WalletDetail(walletFileName, mnemonic);
    }

    public WalletDetail importFromKeystore(String keystoreJson, String password, File dest) throws CipherException, KeystoreSaveException {
        Assert.hasText(keystoreJson, "empty keystore!");
        Assert.checkDirectory(dest);

        Credentials credentials = unlockWallet(password, keystoreJson);

        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'", Locale.CHINA);
        String fileName = dateFormat.format(new Date()) + credentials.getAddress() + ".json";

        File destination = new File(dest, fileName);
        KeystoreUtils.writeToFile(keystoreJson, destination);
        createWalletNotification(credentials.getAddress());
        return new WalletDetail(fileName);
    }

    public WalletDetail importFromPrivateKey(String privateKey, String newPassword, File dest) throws InvalidPrivateKeyException, KeystoreSaveException {
        Assert.hasText(privateKey, "private key can not be null");
        Assert.hasText(newPassword, "new password can not be null");
        Assert.checkDirectory(dest);

        if (!WalletUtils.isValidPrivateKey(privateKey)) {
            throw new InvalidPrivateKeyException();
        }

        Credentials credentials = Credentials.create(privateKey);
        String walletFileName;
        try {
            walletFileName = WalletUtils.generateWalletFile(newPassword, credentials.getEcKeyPair(), dest, false);
        } catch (Exception e) {
            throw new KeystoreSaveException(e);
        }
        createWalletNotification(credentials.getAddress());
        return new WalletDetail(walletFileName);
    }


    public Credentials unlockWallet(String password, File keystore) throws CipherException {
        WalletFile walletFile = KeystoreUtils.loadFromFile(keystore);
        ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
        return Credentials.create(ecKeyPair);
    }

    public Credentials unlockWallet(String password, String keystore) throws CipherException {
        WalletFile walletFile = KeystoreUtils.loadFromJsonString(keystore);
        ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
        return Credentials.create(ecKeyPair);
    }

    public String sendTransaction(String to, BigInteger value, Credentials credentials) throws TransactionFailureException {
        String nonceStr = httpService.getNonce(credentials.getAddress()).toBlocking().first();
        BigInteger nonce = Numeric.toBigInt(Numeric.cleanHexPrefix(nonceStr));

        String gasPriceStr = httpService.getEstimateGasPrice().toBlocking().first();
        BigInteger gasPrice = Numeric.toBigInt(Numeric.cleanHexPrefix(gasPriceStr));

        BigInteger gasLimited = new BigInteger("21000");
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimited,
                to,
                value,
                ""
        );
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String signedTransaction = Numeric.toHexString(signedMessage);
        //send to eth node.
        EthSendTransaction ethSendTransaction = ethService.sendTransaction(signedTransaction).toBlocking().first();

        if (ethSendTransaction.hasError()) {
            String message = ethSendTransaction.getError().getMessage();
            throw new TransactionFailureException(message);
        }else {
            //notify relay.
            String tx = ethSendTransaction.getTransactionHash();
            String txReply = httpService.notifyTransactionSubmitted(
                    tx,
                    nonceStr,
                    to,
                    Numeric.toHexStringWithPrefix(value),
                    gasPriceStr,
                    Numeric.toHexStringWithPrefix(gasLimited),
                    "",
                    credentials.getAddress()
            ).toBlocking().first();
            return tx;
        }
    }

    /********************************************/

    private void createWalletNotification(String owner){
        // notify relay.
        String s = httpService.unlockWallet(owner).toBlocking().first();
        System.out.println(s);
    }

    public void destroy() {
        socketService.close();
        socketClient.close();
    }

    public LooprHttpService getHttpService() {
        return httpService;
    }

    public LooprSocketService getSocketService() {
        // wait till socket connection established.
        while (!socketClient.connected()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return socketService;
    }

    public EthHttpService getEthService() {
        return ethService;
    }

    public BalanceListener newBalanceListener() {
        return new BalanceListener(socketClient);
    }

    public TransactionListener newTransactionListener() {
        return new TransactionListener(socketClient);
    }

}