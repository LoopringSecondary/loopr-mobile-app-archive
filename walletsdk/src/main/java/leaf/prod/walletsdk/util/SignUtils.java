package leaf.prod.walletsdk.util;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.model.TransactionSignature;

public class SignUtils {

    public static TransactionSignature getSignature(Credentials credentials, RawTransaction rawTransaction) {
        byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction);
        Sign.SignatureData signatureData = Sign.signMessage(encodedTransaction, credentials.getEcKeyPair());
        String v = Numeric.toHexStringWithPrefix(BigInteger.valueOf(signatureData.getV()));
        String s = Numeric.toHexString(signatureData.getS());
        String r = Numeric.toHexString(signatureData.getR());
        return new TransactionSignature(v, r, s);
    }
}
