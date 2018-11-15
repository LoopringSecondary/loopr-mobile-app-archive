package leaf.prod.walletsdk.util;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
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

    public static Sign.SignatureData genSignMessage(Credentials credentials, String signMessage) {
        byte[] hash = Hash.sha3(signMessage.getBytes());
        byte[] prefix = ("\u0019Ethereum Signed Message:\n" + hash.length).getBytes();
        byte[] finalBytes = new byte[prefix.length + hash.length];
        System.arraycopy(prefix, 0, finalBytes, 0, prefix.length);
        System.arraycopy(hash, 0, finalBytes, prefix.length, hash.length);
        Sign.SignatureData signatureData = Sign.signMessage(finalBytes, credentials.getEcKeyPair());
        return signatureData;
    }
}
