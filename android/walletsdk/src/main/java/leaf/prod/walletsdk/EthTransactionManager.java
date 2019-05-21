package leaf.prod.walletsdk;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;

import leaf.prod.walletsdk.exception.TransactionException;
import leaf.prod.walletsdk.service.RelayService;

public class EthTransactionManager {

    private BigInteger gasPrice;

    private BigInteger gasLimit;

    private Web3j web3j = SDK.getWeb3j();

    private RawTransactionManager transactionManager;

    private RelayService relayService = new RelayService();

    public EthTransactionManager(BigInteger gasPrice, BigInteger gasLimit, RawTransactionManager transactionManager) {
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.transactionManager = transactionManager;
    }

    public String send(Credentials credentials, String address, String to, BigInteger weiValue) throws TransactionException, IOException {
        return send(credentials, address, to, weiValue, "0x");
    }

    public String send(Credentials credentials, String address, String to, BigInteger weiValue, String data) throws IOException, TransactionException {
        EthSendTransaction ethSendTransaction = transactionManager.sendTransaction(gasPrice, gasLimit, to, data, weiValue);
        if (ethSendTransaction.hasError()) {
            throw new TransactionException(ethSendTransaction.getError().getMessage());
        }
        // notify relay
        String transactionHash = ethSendTransaction.getTransactionHash();
        RawTransaction rawTransaction = getRawTransaction(credentials, to, data, weiValue);
        //todo order
//        relayService.notifyTransactionSubmitted(rawTransaction, address, transactionHash)
//                .subscribeOn(Schedulers.io())
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Subscriber<String>() {
        //                    @Override
        //                    public void onCompleted() {
        //                    }
        //
        //                    @Override
        //                    public void onError(Throwable e) {
        //                        Log.e("notifyTx_error", e.getMessage());
        //                        unsubscribe();
        //                    }
        //
        //                    @Override
        //                    public void onNext(String s) {
        //                        Log.d("notifyTx_success", s);
        //                        unsubscribe();
        //                    }
        //                });
        return transactionHash;
    }

    // convert eth -> weth
    public void deposit(Credentials credentials, String walletAddress, BigInteger valueInWei) throws IOException, TransactionException {
        Function function = new Function("deposit", Collections.emptyList(), Collections.emptyList());
        String data = FunctionEncoder.encode(function);
        send(credentials, walletAddress, Default.WETH_CONTRACT, valueInWei, data);
    }

    // convert weth -> eth
    public void withDraw(Credentials credentials, String walletAddress, BigInteger valueInWei) throws IOException, TransactionException {
        Function function = new Function("withdraw", Arrays.asList(new Uint256(valueInWei)), Collections.emptyList());
        String data = FunctionEncoder.encode(function);
        send(credentials, walletAddress, Default.WETH_CONTRACT, BigInteger.ZERO, data);
    }

    public RawTransaction getRawTransaction(Credentials credentials, String to, String data, BigInteger value) throws IOException {
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
