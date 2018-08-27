package com.lyqb.walletsdk.service.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lyqb.walletsdk.model.loopr.request.param.GetBalance;
import com.lyqb.walletsdk.model.loopr.response.BalanceResult;

import io.socket.client.Socket;
import rx.Observable;
import rx.subjects.PublishSubject;

public class BalanceListener extends AbstractListener<BalanceResult, GetBalance> {

    public static final String TAG = "balance";

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
//            System.out.println("send" + balanceResult.hashCode());
            subject.onNext(balanceResult);
        });
        socket.on("balance_end", data -> {
//            System.out.println(Arrays.toString(data));
            subject.onCompleted();
        });
    }

    @Override
    public Observable<BalanceResult> start() {
        return subject;
    }

    @Override
    public void stop() {
        socket.emit("balance_end", null, args -> subject.onCompleted());
    }

    @Override
    public void send(GetBalance param) {
        String json = gson.toJson(param);
        socket.emit("balance_req", json);
    }
}
