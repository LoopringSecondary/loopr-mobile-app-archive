package com.lyqb.walletsdk.util;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UnitConverter {

    public static BigInteger ethToWei(String value) {
        return Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
    }

    public static BigDecimal weiToGwei(String value) {
        return Convert.fromWei(value, Convert.Unit.GWEI);
    }

    public static BigDecimal weiToEth(String value) {
        return Convert.fromWei(value, Convert.Unit.ETHER);
    }
}
