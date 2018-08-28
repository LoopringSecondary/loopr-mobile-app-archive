//package com.lyqb.walletsdk;
//
//
//import org.web3j.crypto.Credentials;
//import org.web3j.crypto.RawTransaction;
//import org.web3j.crypto.TransactionEncoder;
//import org.web3j.utils.Numeric;
//
//import java.math.BigInteger;
//
//public class TransactionHelper {
//
////    private static final LooprHttpService httpService = new LooprHttpService(Default.RELAY_BASE);
//
//    public static String createEthTransferTransaction(String nonceStr, String gasPrice, String to, Credentials credentials, BigInteger value) {
//
////        String nonceStr = httpService.getNonce(credentials.getAddress()).toBlocking().first();
//        BigInteger nonce = Numeric.toBigInt(Numeric.cleanHexPrefix(nonceStr));
//
////        String gasPrice = httpService.getEstimateGasPrice().toBlocking().first();
//        String s = Numeric.cleanHexPrefix(gasPrice);
//        BigInteger bigInteger = Numeric.toBigInt(s);
//
//        RawTransaction rawTransaction = RawTransaction.createTransaction(
//                nonce,
//                bigInteger,
//                new BigInteger("21000"),
//                to,
//                value,
//                ""
//        );
//        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//        return Numeric.toHexString(signedMessage);
//    }
//
////    public static String sendTransaction(String signedData) throws IOException {
////        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
////        if (ethSendTransaction.hasError()) {
////            String message = ethSendTransaction.getError().getMessage();
////            throw new RuntimeException(message);
////        }else {
////            return ethSendTransaction.getTransactionHash();
////        }
////    }
//}
