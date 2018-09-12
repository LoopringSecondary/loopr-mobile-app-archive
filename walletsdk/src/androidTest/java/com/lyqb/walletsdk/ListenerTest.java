package com.lyqb.walletsdk;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.lyqb.walletsdk.listener.BalanceListener;
import com.lyqb.walletsdk.model.response.data.BalanceResult;

import rx.Observable;

@RunWith(AndroidJUnit4.class)
public class ListenerTest {

    @Test
    public void balanceListerTest() throws InterruptedException {
        SDK.initSDK();
        BalanceListener balanceListener = new BalanceListener();
        Observable<BalanceResult> start = balanceListener.start();
        start.subscribe(new DebugSubscriber<>());
        balanceListener.queryByOwner("0xb94065482ad64d4c2b9252358d746b39e820a582");
        balanceListener.stop();
        Thread.sleep(50000);
    }
}
