package com.lyqb.walletsdk;

import android.support.test.runner.AndroidJUnit4;

import com.lyqb.walletsdk.service.LooprSocketService;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SocketServiceTest {

    @Test
    public void getBalanceTest() throws InterruptedException {
        LooprSocketService service = new LooprSocketService("https://relay1.loopring.io");

        while (!service.connected) {
            System.out.println("waiting");
            Thread.sleep(1000);
        }

        service.getBalance("0xb94065482ad64d4c2b9252358d746b39e820a585")
                .subscribe(new DebugSubscriber<>());


        Thread.sleep(100000);
    }

}
