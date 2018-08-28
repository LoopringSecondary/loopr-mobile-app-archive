package com.lyqb.walletsdk;

import android.support.test.runner.AndroidJUnit4;

import com.lyqb.walletsdk.service.LooprHttpService;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HttpServiceTest {

    private static LooprHttpService httpService;

    static {
        Loopring.create();
        httpService = Loopring.getHttpService();
    }

    @Test
    public void supportedTokenTest() {
        httpService.getSupportedToken().subscribe(new DebugSubscriber<>());
    }

    @Test
    public void getNonceTest() {
        httpService.getNonce("0x71c079107b5af8619d54537a93dbf16e5aab4900").subscribe(new DebugSubscriber<>());
    }

    @Test
    public void getEstimateGasPriceTest() {
        httpService.getEstimateGasPrice().subscribe(new DebugSubscriber<>());
    }

    @Test
    public void unlockWalletTest() {
        httpService.unlockWallet("0xb94065482ad64d4c2b9252358d746b39e820a585").subscribe(new DebugSubscriber<>());
    }

    @Test
    public void getBalanceTest() {
        httpService.getBalance("0xb94065482ad64d4c2b9252358d746b39e820a585").subscribe(new DebugSubscriber<>());
    }
}
