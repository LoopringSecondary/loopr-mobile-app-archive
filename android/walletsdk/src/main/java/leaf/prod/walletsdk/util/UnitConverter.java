package leaf.prod.walletsdk.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.web3j.utils.Convert;

public class UnitConverter {

    public static BigInteger ethToWei(String value) {
        return Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
    }

    public static BigInteger ethToWei(BigDecimal value) {
        return Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
    }

    public static BigDecimal weiToGwei(String value) {
        return Convert.fromWei(value, Convert.Unit.GWEI);
    }

    public static BigDecimal weiToEth(String value) {
        return Convert.fromWei(value, Convert.Unit.ETHER);
    }

    public static BigDecimal weiToEth(BigInteger value) {
        return Convert.fromWei(new BigDecimal(value), Convert.Unit.ETHER);
    }
}
