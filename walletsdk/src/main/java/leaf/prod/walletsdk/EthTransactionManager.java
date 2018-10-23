package leaf.prod.walletsdk;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;

import leaf.prod.walletsdk.exception.TransactionException;

public class EthTransactionManager {

    private static final BigInteger gasLimit = BigInteger.valueOf(22000);

    private RawTransactionManager transactionManager;

    public EthTransactionManager(RawTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public String send(BigInteger gasPrice, String to, BigInteger weiValue) throws TransactionException, IOException {
        return send(gasPrice, to, "0x", weiValue);
    }

    public String send(BigInteger gasPrice, String to, String data, BigInteger weiValue) throws IOException, TransactionException {
        EthSendTransaction ethSendTransaction = transactionManager.sendTransaction(gasPrice, gasLimit, to, data, weiValue);
        if (ethSendTransaction.hasError()) {
            throw new TransactionException(ethSendTransaction.getError().getMessage());
        }
        String transactionHash = ethSendTransaction.getTransactionHash();
        EventAdvisor.notifyTransaction(transactionHash);
        return transactionHash;
    }
}
