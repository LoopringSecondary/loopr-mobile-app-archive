package com.lyqb.walletsdk;

import android.support.test.runner.AndroidJUnit4;

import com.lyqb.walletsdk.model.TransactionDetail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.math.BigInteger;

@RunWith(AndroidJUnit4.class)
public class CommonTest {
    @Test
    public void test() {
        long time1 = System.currentTimeMillis();
        TransactionDetail transaction = TransactionHelper.createTransaction(
                "0x07a7191de1ba70dbe875f12e744b020416a5712b",
                BigInteger.valueOf(10L),
                BigInteger.valueOf(30L),
                BigInteger.valueOf(30L),
                BigInteger.ONE,
                "",
                "6385615bc43c0accaa331291a48e71e4de9dd021aaaa6b1238fe2c82dbce3c50"
        );
        System.out.println(transaction.toString());
        long time = System.currentTimeMillis() - time1;
        System.out.println(time);
    }

    @Test
    public void test1() throws IOException {
        SDK.initSDK();
        Credentials credentials = Credentials.create("c34b6b238327f46db73d0be38cd283734aea6014a41f7b937d0ab720e82de61f");

        TransactionDetail transactionDetail = TransactionHelper.createTransaction(
                "0xb94065482ad64d4c2b9252358d746b39e820a582",
                BigInteger.ZERO,
                credentials
        );
        System.out.println(transactionDetail.toString());
        transactionDetail.sign(credentials);
        System.out.println(transactionDetail.toString());
        System.out.println();
    }
}
