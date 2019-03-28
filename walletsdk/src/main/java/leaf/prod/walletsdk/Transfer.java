package leaf.prod.walletsdk;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;

import leaf.prod.walletsdk.api.EthereumApi;
import leaf.prod.walletsdk.service.RelayService;

public class Transfer {

    private RawTransactionManager transactionManager;

    private RelayService relayApi;
    private EthereumApi ethereumApi;

    public Transfer(Credentials credentials) {
        this.transactionManager = new RawTransactionManager(SDK.getWeb3j(), credentials, SDK.CHAIN_ID);
        relayApi = new RelayService();
        ethereumApi = new EthereumApi();
    }

    public Erc20TransactionManager erc20(String contractAddress, BigInteger gasPrice, BigInteger gasLimit) {
        return new Erc20TransactionManager(contractAddress, gasPrice, gasLimit, transactionManager);
    }

    public EthTransactionManager eth(BigInteger gasPrice, BigInteger gasLimit) {
        return new EthTransactionManager(gasPrice, gasLimit, transactionManager);
    }
}
