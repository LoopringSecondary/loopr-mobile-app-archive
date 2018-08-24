package com.tomcat360.lyqb.core.service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tomcat360.lyqb.core.model.loopr.response.BalanceResult;
import com.tomcat360.lyqb.core.singleton.ObjectMapperInstance;

import org.json.JSONObject;

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
                JSONObject jsonObject = new JSONObject(((String) objects[0]));
                String data = jsonObject.getString("data");
                balanceResult = ObjectMapperInstance.getMapper().readValue(data, BalanceResult.class);
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
        String s = "";
        try {
            s = ObjectMapperInstance.getMapper().writeValueAsString(param);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(s);
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
