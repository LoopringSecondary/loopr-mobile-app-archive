package com.tomcat360.lyqb;

import android.support.test.runner.AndroidJUnit4;

import com.tomcat360.lyqb.core.model.loopr.response.BalanceResult;
import com.tomcat360.lyqb.core.service.LooprSocketService;

import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Subscriber;

@RunWith(AndroidJUnit4.class)
public class ServiceTest {

    @Test
    public void test() throws InterruptedException {
        LooprSocketService service = new LooprSocketService("https://relay1.loopring.io");

        while (!service.connected) {
            System.out.println("waiting");
            Thread.sleep(1000);
        }

        service.getBalance("0xb94065482ad64d4c2b9252358d746b39e820a582")
                .subscribe(new Subscriber<BalanceResult>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("completed");
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

//        service.getBalance("0xb94065482ad64d4c2b9252358d746b39e820a582");

        Thread.sleep(30000);
    }
}
