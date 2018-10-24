package leaf.prod.walletsdk.api;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthTransaction;

import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.exception.RpcException;
import rx.Observable;

public class EthereumApi {
    private Web3j web3j = SDK.getWeb3j();

    public BigInteger estimateGasLimit(Transaction transaction) throws RpcException, IOException {
        EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
        if (ethEstimateGas.hasError()) {
            throw new RpcException(ethEstimateGas.getError().getMessage());
        } else {
            return ethEstimateGas.getAmountUsed();
        }
    }

    public Observable<BigInteger> estimateGasLimitObservable(Transaction transaction) {
        return web3j.ethEstimateGas(transaction).observable().map(EthEstimateGas::getAmountUsed);
    }

    public org.web3j.protocol.core.methods.response.Transaction getTransactionByHash(String hash) throws RpcException, IOException {
        EthTransaction ethTransaction = web3j.ethGetTransactionByHash(hash).send();
        if (ethTransaction.hasError()) {
            throw new RpcException(ethTransaction.getError().getMessage());
        } else {
            return ethTransaction.getTransaction();
        }
    }

    public Observable<org.web3j.protocol.core.methods.response.Transaction> getTransactionByHashObservable(String hash) {
        return web3j.ethGetTransactionByHash(hash).observable().map(EthTransaction::getTransaction);
    }

    public BigInteger gasPrice() throws IOException, RpcException {
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        if (ethGasPrice.hasError()) {
            throw new RpcException(ethGasPrice.getError().getMessage());
        } else {
            return ethGasPrice.getGasPrice();
        }
    }

}
