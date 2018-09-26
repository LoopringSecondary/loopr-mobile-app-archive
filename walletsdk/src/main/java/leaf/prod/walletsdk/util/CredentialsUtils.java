package leaf.prod.walletsdk.util;

import java.math.BigInteger;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

public class CredentialsUtils {

    public static String publicKeyToAddress(String publicKey) {
        return Keys.getAddress(publicKey);
    }

    public static String privateKeyToAddress(String privateKey) {
        BigInteger publicKey = Sign.publicKeyFromPrivate(Numeric.toBigInt(privateKey));
        return Keys.getAddress(publicKey);
    }

    public static String privateKeyToPublicKey(String privateKey) {
        BigInteger publicKey = Sign.publicKeyFromPrivate(Numeric.toBigInt(privateKey));
        return Numeric.toHexStringWithPrefixSafe(publicKey);
    }

    public static String toPrivateKeyHexString(BigInteger privateKey) {
        return Numeric.toHexStringWithPrefixSafe(privateKey);
    }

    public static boolean isPrivateKey(String privateKey) {
        return WalletUtils.isValidPrivateKey(privateKey);
    }

    public static boolean isAddress(String address) {
        return WalletUtils.isValidAddress(address);
    }
}
