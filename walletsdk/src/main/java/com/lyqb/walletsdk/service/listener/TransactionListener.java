package com.lyqb.walletsdk.service.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lyqb.walletsdk.model.request.param.GetTransaction;
import com.lyqb.walletsdk.model.response.TransactionPageWrapper;

import io.socket.client.Socket;
import rx.Observable;
import rx.subjects.PublishSubject;

public class TransactionListener extends AbstractListener<TransactionPageWrapper, GetTransaction> {

    public static final String TAG = "transaction";

    private PublishSubject<TransactionPageWrapper> subject = PublishSubject.create();

    public TransactionListener(Socket socket) {
        super(socket);
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
    public void send(GetTransaction param) {
        String json = gson.toJson(param);
        socket.emit("transaction_req", json);
    }
}
