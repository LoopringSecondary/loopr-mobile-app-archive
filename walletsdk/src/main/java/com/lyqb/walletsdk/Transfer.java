package com.lyqb.walletsdk;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Numeric;
import com.lyqb.walletsdk.api.EthereumApi;
import com.lyqb.walletsdk.exception.RpcException;
import com.lyqb.walletsdk.service.LoopringService;

public class Transfer {

    private RawTransactionManager transactionManager;

    private LoopringService loopringApi;
    private EthereumApi ethereumApi;

    public Transfer(Credentials credentials) {
        this.transactionManager = new RawTransactionManager(SDK.getWeb3j(), credentials, SDK.CHAIN_ID);
        loopringApi = new LoopringService();
        ethereumApi = new EthereumApi();
    }

    public Erc20TransactionManager erc20(String contractAddress) {
        return new Erc20TransactionManager(contractAddress, transactionManager);
    }

    public EthTransactionManager eth() {
        return new EthTransactionManager(transactionManager);
    }

    public BigInteger getSuggestGasPriceFromLoopring() {
        String estimateGasPrice = loopringApi.getEstimateGasPrice().toBlocking().single();
        return Numeric.toBigInt(Numeric.cleanHexPrefix(estimateGasPrice));
    }

    public BigInteger getSuggestGasPriceFromNode() throws IOException, RpcException {
        return ethereumApi.gasPrice();
    }
}
