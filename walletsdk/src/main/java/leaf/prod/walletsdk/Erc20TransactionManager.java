package leaf.prod.walletsdk;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.tx.TransactionManager;

import leaf.prod.walletsdk.api.Erc20Contract;
import leaf.prod.walletsdk.service.RelayService;
import rx.Observable;

public class Erc20TransactionManager {

    public Web3j web3j = SDK.getWeb3j();

    private Erc20Contract erc20Contract;

    private BigInteger gasPrice;

    private BigInteger gasLimit;

    private RelayService relayService = new RelayService();

    Erc20TransactionManager(String contractAddress, BigInteger gasPrice, BigInteger gasLimit, TransactionManager transactionManager) {
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.erc20Contract = Erc20Contract.load(contractAddress, SDK.getWeb3j(), transactionManager, gasPrice, gasLimit);
    }

    public String transfer(Credentials credentials, String contractAddress, String to, BigInteger value) throws Exception {
        String hash = erc20Contract.transfer(to, value).send().getTransactionHash();
        //todo order
//        notifyRelay(hash, credentials, contractAddress, to, value);
        return hash;
    }

    public Observable<String> approve(Credentials credentials, String contractAddress, String to, BigInteger value) {
        //todo order
//        return erc20Contract.approve(to, value).observable()
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .flatMap((Func1<TransactionReceipt, Observable<String>>) transactionReceipt -> {
//                    try {
//                        RawTransaction rawTransaction = getRawTransaction(credentials, to, contractAddress, value);
//                        String hash = transactionReceipt.getTransactionHash();
//                        return relayService.notifyTransactionSubmitted(rawTransaction, to, hash);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return Observable.just("failed");
//                });
        return null;
    }

    private RawTransaction getRawTransaction(Credentials credentials, String to, String contractAddress, BigInteger value) throws IOException {
        Function function = new Function("transfer",
                Arrays.asList(new Address(to), new Uint256(value)),
                Collections.emptyList());
        String data = FunctionEncoder.encode(function);
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        return RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, data);
    }
}
