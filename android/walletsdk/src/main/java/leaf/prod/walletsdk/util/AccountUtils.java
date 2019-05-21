package leaf.prod.walletsdk.util;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

public class AccountUtils {

    public static String generateKeystoreFilename(String address) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'", Locale.CHINA);
        return dateFormat.format(new Date()) + address + ".json";
    }

    public static String publicKeyToAddress(String publicKey) {
        return Keys.getAddress(publicKey);
    }

    public static String privateKeyToAddress(String privateKey) {
        BigInteger publicKey = Sign.publicKeyFromPrivate(Numeric.toBigInt(privateKey));
        return Keys.getAddress(publicKey);
    }

    public static String privateKeyToPublicKey(String privateKey) {
        BigInteger publicKey = Sign.publicKeyFromPrivate(Numeric.toBigInt(privateKey));
        return Numeric.toHexStringWithPrefix(publicKey);
    }
}
