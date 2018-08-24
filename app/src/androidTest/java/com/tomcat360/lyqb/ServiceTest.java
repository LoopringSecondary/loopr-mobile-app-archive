package com.tomcat360.lyqb;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tomcat360.lyqb.core.model.loopr.response.BalanceResult;
import com.tomcat360.lyqb.core.service.LooprSocketService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;
import rx.Subscriber;

@RunWith(AndroidJUnit4.class)
public class ServiceTest {

    @Test
    public void test() throws InterruptedException, JsonProcessingException {
        Context context = InstrumentationRegistry.getTargetContext();
        Assert.assertEquals("com.tomcat360.lyqb", context.getPackageName());
        LooprSocketService socketService = new LooprSocketService("https://relay1.loopring.io/");
        while (!socketService.connected) {
            System.out.println(socketService.connected);
            Thread.sleep(1000);
        }
        Observable<BalanceResult> balance = socketService.balance();
        socketService.listenBalance("0xb94065482ad64d4c2b9252358d746b39e820a582");

        balance.subscribe(new Subscriber<BalanceResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(BalanceResult balanceResult) {
                System.out.println(balanceResult.toString());
            }
        });

        System.out.println("end");
    }

}
