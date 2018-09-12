package com.lyqb.walletsdk.util;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;
import com.lyqb.walletsdk.model.Account;
import com.lyqb.walletsdk.model.TransactionObject;
import com.lyqb.walletsdk.model.TransactionSignature;

public class SignUtils {

    public static String signTransaction(TransactionObject transactionObject, Account account) {
        return signTransaction(transactionObject, account.getPrivateKey());
    }

    public static String signTransaction(TransactionObject transactionObject, String privateKey) {
        RawTransaction rawTransaction = transactionObject.toRawTransaction();
        Credentials credentials = Credentials.create(privateKey);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, transactionObject.getChainId(), credentials);
        return Numeric.toHexString(signedMessage);
    }

    public static TransactionSignature getSignature(TransactionObject transactionObject, String privateKey) {
        RawTransaction rawTransaction = transactionObject.toRawTransaction();
        Credentials credentials = Credentials.create(privateKey);
        byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction, transactionObject.getChainId());
        Sign.SignatureData signatureData = Sign.signMessage(encodedTransaction, credentials.getEcKeyPair());
        String v = Numeric.toHexStringWithPrefix(BigInteger.valueOf(signatureData.getV()));
        String s = Numeric.toHexString(signatureData.getS());
        String r = Numeric.toHexString(signatureData.getR());
        return new TransactionSignature(v, r, s);
    }
}
