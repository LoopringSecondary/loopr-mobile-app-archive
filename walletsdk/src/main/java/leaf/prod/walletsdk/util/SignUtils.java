package leaf.prod.walletsdk.util;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;

public class SignUtils {

    public static Sign.SignatureData genSignRawTx(Credentials credentials, RawTransaction rawTransaction) {
        byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction);
        return Sign.signMessage(encodedTransaction, credentials.getEcKeyPair());
    }

    public static Sign.SignatureData genSignMessage(Credentials credentials, String signMessage) {
        byte[] hash = Hash.sha3(signMessage.getBytes());
        byte[] prefix = ("\u0019Ethereum Signed Message:\n" + hash.length).getBytes();
        byte[] finalBytes = new byte[prefix.length + hash.length];
        System.arraycopy(prefix, 0, finalBytes, 0, prefix.length);
        System.arraycopy(hash, 0, finalBytes, prefix.length, hash.length);
        return Sign.signMessage(finalBytes, credentials.getEcKeyPair());
    }
}
