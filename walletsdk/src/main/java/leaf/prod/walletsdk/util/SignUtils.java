package leaf.prod.walletsdk.util;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.model.SignedBody;

public class SignUtils {

    public static Sign.SignatureData genSignRawTx(Credentials credentials, RawTransaction rawTransaction) {
        byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction);
        return Sign.signMessage(encodedTransaction, credentials.getEcKeyPair());
    }

    public static SignedBody genSignMessage(Credentials credentials, byte[] hash) {
        byte[] prefix = ("\u0019Ethereum Signed Message:\n" + hash.length).getBytes();
        byte[] finalBytes = new byte[prefix.length + hash.length];
        System.arraycopy(prefix, 0, finalBytes, 0, prefix.length);
        System.arraycopy(hash, 0, finalBytes, prefix.length, hash.length);
        Sign.SignatureData sig = Sign.signMessage(finalBytes, credentials.getEcKeyPair());
        return SignedBody.builder().hash(Numeric.toHexString(hash)).sig(sig).build();
    }
}
