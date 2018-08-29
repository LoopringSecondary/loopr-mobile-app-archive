package com.lyqb.walletsdk.service;

import com.lyqb.walletsdk.SDK;
import com.lyqb.walletsdk.model.TransactionDetail;
import com.lyqb.walletsdk.util.Assert;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

import rx.Observable;

public class EthereumService {
    private Web3j web3j;

    public EthereumService() {
        String ethBase = SDK.getEthBase();
        HttpService httpService = new HttpService(ethBase);
        this.web3j = Web3jFactory.build(httpService);
    }

    public Observable<EthSendTransaction> sendTransaction(String signedTransaction) {
        Assert.hasText(signedTransaction);
        return web3j.ethSendRawTransaction(signedTransaction).observable();
    }

    public EthEstimateGas estimateGasLimit(TransactionDetail transactionDetail) throws IOException {
        Transaction transaction = transactionDetail.toTransaction();
        return web3j.ethEstimateGas(transaction).send();
    }
}
