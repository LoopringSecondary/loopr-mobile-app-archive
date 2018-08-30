package com.lyqb.walletsdk.service;

import com.lyqb.walletsdk.SDK;
import com.lyqb.walletsdk.exception.TransactionException;
import com.lyqb.walletsdk.model.TransactionObject;
import com.lyqb.walletsdk.util.Assert;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;

public class EthereumService {
    private Web3j web3j;

    public EthereumService() {
        String ethBase = SDK.ethBase();
        HttpService httpService = new HttpService(ethBase);
        this.web3j = Web3jFactory.build(httpService);
    }

    public String sendRawTransaction(String signedTransaction) throws TransactionException {
        Assert.hasText(signedTransaction,"transaction can not be null!");
        EthSendTransaction ethSendTransaction;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(signedTransaction).send();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        if (ethSendTransaction.hasError()) {
            throw new TransactionException(ethSendTransaction.getError().getMessage());
        }else {
            return ethSendTransaction.getTransactionHash();
        }
    }

    public BigInteger estimateGasLimit(TransactionObject transactionObject) {
        Transaction transaction = transactionObject.toTransaction();
        BigInteger defaultGasLimit = new BigInteger("21000");
        EthEstimateGas ethEstimateGas;
        try {
            ethEstimateGas = web3j.ethEstimateGas(transaction).send();
        } catch (IOException e) {
            e.printStackTrace();
            return defaultGasLimit;
        }
        if (ethEstimateGas.hasError()) {
            System.out.println(ethEstimateGas.getError().getMessage());
            return defaultGasLimit;
        }else {
            return ethEstimateGas.getAmountUsed();
        }
    }
}
