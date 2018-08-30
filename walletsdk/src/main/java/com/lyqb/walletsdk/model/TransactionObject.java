package com.lyqb.walletsdk.model;


import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString
public class TransactionObject {
    private byte chainId;

    private String from;
    @NonNull
    private String to;

    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;

    @NonNull
    private BigInteger value;
    private String data;

    private String signedTransaction;

    public TransactionObject(byte chainId, String from, String to, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigInteger value, String data) {
        this.chainId = chainId;
        this.from = from;
        this.to = to;
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.value = value;
        this.data = Numeric.toHexString(data.getBytes());
    }

    public org.web3j.protocol.core.methods.request.Transaction toTransaction() {
        return new org.web3j.protocol.core.methods.request.Transaction(from, nonce, gasPrice, gasLimit, to, value, data);
    }

    public RawTransaction toRawTransaction() {
        return RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
    }

    public void sign(Credentials credentials) {
        RawTransaction rawTransaction = this.toRawTransaction();
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        this.signedTransaction = Numeric.toHexString(signedMessage);
    }
}
