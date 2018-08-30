package com.lyqb.walletsdk;

import com.lyqb.walletsdk.model.TransactionObject;
import com.lyqb.walletsdk.service.EthereumService;

import org.junit.Test;

import java.math.BigInteger;

public class TransactionTest {

    @Test
    public void test() {
        SDK.initSDK();

        EthereumService ethereumService = new EthereumService();

        TransactionObject transactionObject = new TransactionObject(
                ((byte) 1),
                "",
                "",
                BigInteger.ONE,
                BigInteger.ZERO,
                BigInteger.ZERO,
                new BigInteger("100"),
                "hello world"
        );

        BigInteger bigInteger = ethereumService.estimateGasLimit(transactionObject);
        System.out.println(bigInteger);
    }
}
