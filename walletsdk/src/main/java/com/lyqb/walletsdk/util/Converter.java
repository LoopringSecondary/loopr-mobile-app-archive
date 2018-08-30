package com.lyqb.walletsdk.util;

import org.web3j.utils.Convert;

import java.math.BigInteger;

public class Converter {

    public static BigInteger ethToWei(String value) {
        return Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
    }
}
