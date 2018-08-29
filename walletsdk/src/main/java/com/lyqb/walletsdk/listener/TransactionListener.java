package com.lyqb.walletsdk.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lyqb.walletsdk.model.request.param.TransactionParam;
import com.lyqb.walletsdk.model.response.data.TransactionPageWrapper;

import rx.Observable;
import rx.subjects.PublishSubject;

public class TransactionListener extends AbstractListener<TransactionPageWrapper, TransactionParam> {

    public static final String TAG = "transaction";

    private PublishSubject<TransactionPageWrapper> subject = PublishSubject.create();

    public TransactionListener() {
        super();
    }

    @Override
    protected void registerEventHandler() {
        socket.on("transaction_res", objects -> {
            JsonObject object = gson.fromJson(((String) objects[0]), JsonObject.class);
            JsonElement element = object.get("data");
            TransactionPageWrapper transactionPageWrapper = gson.fromJson(element, TransactionPageWrapper.class);
            subject.onNext(transactionPageWrapper);
        });
        socket.on("transaction_end", data -> {
            subject.onCompleted();
        });
    }

    @Override
    public Observable<TransactionPageWrapper> start() {
        return subject;
    }

    @Override
    public void stop() {
        socket.emit("transaction_end", "");
    }

    @Override
    public void send(TransactionParam param) {
        String json = gson.toJson(param);
        socket.emit("transaction_req", json);
    }
}
