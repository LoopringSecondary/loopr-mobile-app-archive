package leaf.prod.walletsdk.util;

import java.math.BigInteger;

import org.web3j.crypto.WalletUtils;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.SDK;

public class CredentialsUtils {

    public static String toPrivateKeyHexString(BigInteger privateKey) {
        return Numeric.toHexStringWithPrefixSafe(privateKey);
    }

    public static boolean isValidAddress(String address) {
        return isHexAddress(address) || isHexAddress(getENSAddress(address));
    }

    public static boolean isHexAddress(String address) {
        return WalletUtils.isValidAddress(address);
    }

    public static String getENSAddress(String address) {
        if (!address.endsWith(".eth")) {
            address += ".eth";
        }
        try {
            Web3j web3j = SDK.getWeb3j();
            EnsResolver ensResolver = new EnsResolver(web3j);
            return ensResolver.resolve(address);
        } catch (Exception e) {
            return "";
        }
    }
}
