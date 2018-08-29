package com.lyqb.walletsdk.listener;


import com.google.gson.Gson;
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

    protected abstract void registerEventHandler();

    public abstract Observable<R> start();

    public abstract void stop();

    public abstract void send(P param);
}
