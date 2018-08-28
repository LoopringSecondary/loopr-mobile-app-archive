package com.lyqb.walletsdk.service;

import com.lyqb.walletsdk.util.Assert;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import rx.Observable;

public class EthHttpService {
    private Web3j web3j;

    public EthHttpService(Web3j web3j) {
        this.web3j = web3j;
    }

    public Observable<EthSendTransaction> sendTransaction(String signedTransaction) {
        Assert.hasText(signedTransaction);
        return web3j.ethSendRawTransaction(signedTransaction).observable();
    }
}
