package com.lyqb.walletsdk;

import android.support.test.runner.AndroidJUnit4;

import com.lyqb.walletsdk.service.LooprSocketService;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SocketServiceTest {

    @Test
    public void getBalanceTest() throws InterruptedException {
//        Loopring.create();
//        LooprSocketService socketService = Loopring.getSocketService();

        Loopring1 loopring1 = new Loopring1();
        LooprSocketService socketService = loopring1.getSocketService();

        socketService.getBalanceDataStream().subscribe(new DebugSubscriber<>());
        socketService.requestBalance("0xb94065482ad64d4c2b9252358d746b39e820a585");

        socketService.requestBalance("0x847983c3a34afa192cfee860698584c030f4c9d1");

        Thread.sleep(100000);
    }

}
