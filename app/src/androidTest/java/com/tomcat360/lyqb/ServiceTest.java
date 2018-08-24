package com.tomcat360.lyqb;

import android.support.test.espresso.core.internal.deps.guava.collect.Maps;
import android.support.test.runner.AndroidJUnit4;

import com.tomcat360.lyqb.core.model.loopr.request.RequestWrapper;
import com.tomcat360.lyqb.core.model.loopr.request.param.GetBalance;
import com.tomcat360.lyqb.core.model.loopr.response.BalanceResult;
import com.tomcat360.lyqb.core.rpc.LooprRpc;
import com.tomcat360.lyqb.core.service.LooprSocketService;
import com.tomcat360.lyqb.core.singleton.ObjectMapperInstance;
import com.tomcat360.lyqb.core.singleton.OkHttpInstance;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;
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

        service.getBalance("0xb94065482ad64d4c2b9252358d746b39e820a585")
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

        service.getBalance("0xb94065482ad64d4c2b9252358d746b39e820a582");

        Thread.sleep(30000);
    }

    @Test
    public void httpTest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://relay1.loopring.io/")
                .addConverterFactory(JacksonConverterFactory.create(ObjectMapperInstance.getMapper()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(OkHttpInstance.getClient())
                .build();
        LooprRpc looprRpc = retrofit.create(LooprRpc.class);

        GetBalance getBalance = GetBalance.builder()
                .owner("0xb94065482ad64d4c2b9252358d746b39e820a582")
                .delegateAddress("0x17233e07c67d086464fD408148c3ABB56245FA64")
                .build();

        RequestWrapper wrapper = new RequestWrapper("loopring_getSupportedTokens", Maps.newHashMap());
        Observable<Map> observable = looprRpc.send(wrapper);
        observable.subscribe(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                System.out.println("hello");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Map map) {
                System.out.println(map.toString());
            }
        });
    }



}
