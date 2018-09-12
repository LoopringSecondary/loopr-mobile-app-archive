package com.lyqb.walletsdk.listener;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lyqb.walletsdk.SDK;

import io.socket.client.Socket;
import rx.Observable;

public abstract class AbstractListener<R, P> {

    Socket socket;

    Gson gson = new Gson();

    //    public AbstractListener(Socket socket) {
    //        this.socket = socket;
    //        registerEventHandler();
    //    }
    AbstractListener() {
        this.socket = SDK.getSocketClient();
        registerEventHandler();
    }

    protected JsonElement extractPayload(Object[] objects) {
        JsonObject object = gson.fromJson(((String) objects[0]), JsonObject.class);
        return object.get("data");
    }

    protected abstract void registerEventHandler();

    public abstract Observable<R> start();

    public abstract void stop();

    public abstract void send(P param);
}
