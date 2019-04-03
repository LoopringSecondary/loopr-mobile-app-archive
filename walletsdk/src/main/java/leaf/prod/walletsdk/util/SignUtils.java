package leaf.prod.walletsdk.util;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.model.request.relayParam.NotifyScanParam;

public class SignUtils {

    public static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    public static final String Eip191Header = "\u0019\u0001";

    public static final String Eip712DomainHash = "0xaea25658c273c666156bd427f83a666135fcde6887a6c25fc1cd1562bc4f3f34";

    public static final String Eip712OrderSchemaHash = "0x40b942178d2a51f1f61934268590778feb8114db632db7d88537c98d2b05c5f2";

    public static SignatureData genSignRawTx(Credentials credentials, RawTransaction rawTransaction) {
        byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction);
        return Sign.signMessage(encodedTransaction, credentials.getEcKeyPair());
    }

    public static SignatureData genSignMessage(Credentials credentials, byte[] message) {
        byte[] prefix = (MESSAGE_PREFIX + message.length).getBytes();
        byte[] finalBytes = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, finalBytes, 0, prefix.length);
        System.arraycopy(message, 0, finalBytes, prefix.length, message.length);
        return Sign.signMessage(finalBytes, credentials.getEcKeyPair());
    }

    public static NotifyScanParam.SignParam genSignParam(Credentials credentials, String owner) {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        byte[] hash = Hash.sha3(timeStamp.getBytes());
        SignatureData sig = SignUtils.genSignMessage(credentials, hash);
        return NotifyScanParam.SignParam.builder().timestamp(timeStamp).owner(owner)
                .r(Numeric.toHexStringNoPrefix(sig.getR()))
                .s(Numeric.toHexStringNoPrefix(sig.getS()))
                .v(BigInteger.valueOf(sig.getV()))
                .build();
    }
}
