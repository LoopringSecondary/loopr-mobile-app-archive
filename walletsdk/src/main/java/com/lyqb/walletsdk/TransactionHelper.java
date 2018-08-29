package com.lyqb.walletsdk;

import com.lyqb.walletsdk.model.TransactionDetail;
import com.lyqb.walletsdk.service.EthereumService;
import com.lyqb.walletsdk.service.LoopringService;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class TransactionHelper {

    private static LoopringService httpService = new LoopringService();
    private static EthereumService ethereumService = new EthereumService();

    public static TransactionDetail createTransaction(
            String to,
            BigInteger value,
            Credentials credentials
    ) throws IOException {

        String nonceStr = httpService.getNonce(credentials.getAddress()).toBlocking().single();
        String gasPriceStr = httpService.getEstimateGasPrice().toBlocking().single();

        BigInteger nonce = Numeric.toBigInt(Numeric.cleanHexPrefix(nonceStr));
        BigInteger gasPrice = Numeric.toBigInt(Numeric.cleanHexPrefix(gasPriceStr));

        TransactionDetail transactionDetail = new TransactionDetail(
                (byte) 1,
                credentials.getAddress(),
                to,
                nonce,
                gasPrice,
                BigInteger.ZERO,
                value,
                ""
        );
        EthEstimateGas ethEstimateGas = ethereumService.estimateGasLimit(transactionDetail);
        BigInteger amountUsed;
        if (ethEstimateGas.hasError()){
            String message = ethEstimateGas.getError().getMessage();
            System.out.println(message);
            throw new RuntimeException(message);
        }else {
            amountUsed = ethEstimateGas.getAmountUsed();
        }
        transactionDetail.setGasLimit(amountUsed);
        return transactionDetail;
    }


    public static TransactionDetail createTransaction(
            String to,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimited,
            BigInteger value,
            String data,
            Credentials credentials
    ) {
        String dataInHex = Numeric.toHexString(data.getBytes());
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimited,
                to,
                value,
                dataInHex
        );
        return create(((byte) 1), rawTransaction, credentials);
    }

    public static TransactionDetail createTransaction(
            String to,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimited,
            BigInteger value,
            String data,
            String privateKey
    ) {
        String dataInHex = Numeric.toHexString(data.getBytes());
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimited,
                to,
                value,
                dataInHex
        );
        Credentials credentials = Credentials.create(privateKey);
        return create(((byte) 1), rawTransaction, credentials);
    }

    public static TransactionDetail createTransaction(
            byte chainId,
            String to,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimited,
            BigInteger value,
            String data,
            Credentials credentials
    ) {
        String dataInHex = Numeric.toHexString(data.getBytes());
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimited,
                to,
                value,
                dataInHex
        );
        return create(chainId, rawTransaction, credentials);
    }

    public static TransactionDetail createTransaction(
            byte chainId,
            String to,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimited,
            BigInteger value,
            String data,
            String privateKey
    ) {
        String dataInHex = Numeric.toHexString(data.getBytes());
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimited,
                to,
                value,
                dataInHex
        );
        Credentials credentials = Credentials.create(privateKey);
        return create(chainId, rawTransaction, credentials);
    }

    private static TransactionDetail create(byte chainId, RawTransaction rawTransaction, Credentials credentials) {
        BigInteger nonce = rawTransaction.getNonce();
        BigInteger gasPrice = rawTransaction.getGasPrice();
        BigInteger gasLimit = rawTransaction.getGasLimit();
        String to = rawTransaction.getTo();
        BigInteger value = rawTransaction.getValue();
        String data = rawTransaction.getData();
        String from = credentials.getAddress();

        byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction, chainId);
        Sign.SignatureData signatureData = Sign.signMessage(encodedTransaction, credentials.getEcKeyPair());
        String v = Numeric.toHexStringWithPrefix(BigInteger.valueOf(signatureData.getV()));
        String r = Numeric.toHexString(signatureData.getR());
        String s = Numeric.toHexString(signatureData.getS());

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        String signedTransaction = Numeric.toHexString(signedMessage);

//        return TransactionDetail.builder()
//                .nonce(nonce)
//                .gasPrice(gasPrice)
//                .gasLimit(gasLimit)
//                .to(to)
//                .value(value)
//                .data(data)
//                .chainId(chainId)
//                .from(from)
//                .v(v)
//                .r(r)
//                .s(s)
//                .signedTransaction(signedTransaction)
//                .build();
        return new TransactionDetail();
    }

}
