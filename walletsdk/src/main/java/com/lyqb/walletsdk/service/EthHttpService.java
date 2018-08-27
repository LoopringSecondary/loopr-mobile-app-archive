package com.lyqb.walletsdk.service;

import com.lyqb.walletsdk.util.Assert;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.io.IOException;

public class EthHttpService {
    private Web3j web3j;

    public EthHttpService(Web3j web3j) {
        this.web3j = web3j;
    }

    public String sendSignedTransaction(String signedTransaction) throws IOException {
        Assert.hasText(signedTransaction);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedTransaction).send();
        if (ethSendTransaction.hasError()) {
            String message = ethSendTransaction.getError().getMessage();
            throw new RuntimeException(message);
        }else {
            return ethSendTransaction.getTransactionHash();
        }


    }
}
