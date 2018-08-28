package com.lyqb.walletsdk;

import android.support.test.runner.AndroidJUnit4;

import com.lyqb.walletsdk.service.LooprSocketService;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SocketServiceTest {
    private Loopring loopring =  new Loopring();
    private LooprSocketService socketService = loopring.getSocketService();

    @Test
    public void getBalanceTest() throws InterruptedException {

        socketService.getBalanceDataStream().subscribe(new DebugSubscriber<>());
        socketService.requestBalance("0xb94065482ad64d4c2b9252358d746b39e820a585");

        socketService.requestBalance("0x847983c3a34afa192cfee860698584c030f4c9d1");

        Thread.sleep(100000);
    }

    @Test
    public void getTransactionTest() throws InterruptedException {
        socketService.getTransactionDateStream().subscribe(new DebugSubscriber<>());
        socketService.requestTransaction(
                "0xb94065482ad64d4c2b9252358d746b39e820a582",
                "WETH",
                1,
                30
        );

        socketService.requestTransaction(
                "0xb94065482ad64d4c2b9252358d746b39e820a582",
                "WETH",
                3,
                30
        );

        Thread.sleep(100000);

    }

}
