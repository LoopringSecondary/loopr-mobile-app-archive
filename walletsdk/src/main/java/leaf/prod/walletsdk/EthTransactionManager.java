package leaf.prod.walletsdk;

import java.io.IOException;
import java.math.BigInteger;

import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;

import leaf.prod.walletsdk.exception.TransactionException;
import leaf.prod.walletsdk.model.TransactionSignature;
import leaf.prod.walletsdk.service.LoopringService;
import leaf.prod.walletsdk.util.SignUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EthTransactionManager {

    private static final BigInteger gasLimit = BigInteger.valueOf(22000);

    private Web3j web3j = SDK.getWeb3j();

    private RawTransactionManager transactionManager;

    private LoopringService loopringService = new LoopringService();

    public EthTransactionManager(RawTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public String send(Credentials credentials, String address, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger weiValue) throws TransactionException, IOException {
        return send(credentials, address, gasPrice, gasLimit, to, "0x", weiValue);
    }

    public String send(Credentials credentials, String address, BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger weiValue) throws IOException, TransactionException {
        EthSendTransaction ethSendTransaction = transactionManager.sendTransaction(gasPrice, gasLimit, to, data, weiValue);
        if (ethSendTransaction.hasError()) {
            throw new TransactionException(ethSendTransaction.getError().getMessage());
        }
        // notify relay
        String transactionHash = ethSendTransaction.getTransactionHash();
        RawTransaction rawTransaction = getRawTransaction(credentials, gasPrice, gasLimit, to, data, weiValue);
        TransactionSignature transactionSignature = SignUtils.getSignature(credentials, rawTransaction);
        loopringService.notifyTransactionSubmitted(rawTransaction, address, transactionHash, transactionSignature)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("notifyTx_error", e.getMessage());
                        unsubscribe();
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("notifyTx_success", s);
                        unsubscribe();
                    }
                });
        return transactionHash;
    }

    public RawTransaction getRawTransaction(Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
        return RawTransaction.createTransaction(
                ethGetTransactionCount.getTransactionCount(),
                gasPrice,
                gasLimit,
                to,
                value,
                data);
    }
}
