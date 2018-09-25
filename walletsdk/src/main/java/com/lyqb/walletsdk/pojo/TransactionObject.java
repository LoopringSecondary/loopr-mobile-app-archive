package com.lyqb.walletsdk.pojo;//package com.lyqb.walletsdk.pojo;
//
//
//import org.web3j.crypto.RawTransaction;
//import org.web3j.protocol.core.methods.request.Transaction;
//
//import java.math.BigInteger;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NonNull;
//
//@Data
//@AllArgsConstructor
//public class TransactionObject {
//    private byte chainId;
//
//    private String from;
//    @NonNull
//    private String to;
//
//    private BigInteger nonce;
//    private BigInteger gasPrice;
//    private BigInteger gasLimit;
//
//    @NonNull
//    private BigInteger value;
//    private String data;
//
//    public Transaction toTransaction() {
//        return new Transaction(from, nonce, gasPrice, gasLimit, to, value, data);
//    }
//
//    public RawTransaction toRawTransaction() {
//        return RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
//    }
//}
