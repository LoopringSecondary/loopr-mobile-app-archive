package com.lyqb.walletsdk;

import android.support.test.runner.AndroidJUnit4;

import com.lyqb.walletsdk.listener.BalanceListener;
import com.lyqb.walletsdk.model.request.param.BalanceParam;
import com.lyqb.walletsdk.model.response.data.BalanceResult;

import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;

@RunWith(AndroidJUnit4.class)
public class ListenerTest {

    @Test
    public void balanceListerTest() throws InterruptedException {
        SDK.initSDK();
        BalanceListener balanceListener = new BalanceListener();
        Observable<BalanceResult> start = balanceListener.start();
        start.subscribe(new DebugSubscriber<>());
        BalanceParam balanceParam = BalanceParam.builder()
                .owner("0xb94065482ad64d4c2b9252358d746b39e820a582")
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .build();
        balanceListener.send(balanceParam);
        Thread.sleep(50000);
    }
}
