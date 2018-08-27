package com.lyqb.walletsdk.service.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lyqb.walletsdk.model.loopr.response.BalanceResult;

import java.util.Arrays;

import io.socket.client.Socket;
import rx.Observable;
import rx.subjects.PublishSubject;

public class BalanceListener extends AbstractListener<BalanceResult> {

    private PublishSubject<BalanceResult> subject = PublishSubject.create();

    public BalanceListener(Socket socket) {
        super(socket);
    }

    @Override
    protected void registerEventHandler() {
        socket.on("balance_res", objects -> {
            BalanceResult balanceResult;
            try {
                JsonObject object = gson.fromJson(((String) objects[0]), JsonObject.class);
                JsonElement element = object.get("data");
                balanceResult = gson.fromJson(element, BalanceResult.class);

            } catch (Exception e) {
                e.printStackTrace();
                balanceResult = new BalanceResult();
            }
            System.out.println("send" + balanceResult.hashCode());
            subject.onNext(balanceResult);
        });
        socket.on("balance_end", data -> {
            System.out.println(Arrays.toString(data));
            subject.onCompleted();
        });
    }

    @Override
    public Observable<BalanceResult> start(Object param) {
        String s = gson.toJson(param);
//        System.out.println(s);
        socket.emit("balance_req", s);
        return subject;
    }

    @Override
    public void stop() {
        socket.emit("balance_end", null, args -> {
            System.out.println("balance end ack");
            subject.onCompleted();
        });
    }
}
