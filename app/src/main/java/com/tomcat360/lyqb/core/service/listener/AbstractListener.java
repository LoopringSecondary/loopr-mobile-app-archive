package com.tomcat360.lyqb.core.service.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomcat360.lyqb.core.singleton.ObjectMapperInstance;

import io.socket.client.Socket;
import rx.Observable;

public abstract class AbstractListener<T> {

    Socket socket;
    ObjectMapper objectMapper = ObjectMapperInstance.getMapper();

    public AbstractListener(Socket socket) {
        this.socket = socket;
        registerEventHandler();
    }

    protected abstract void registerEventHandler();

    public abstract Observable<T> start(Object param);

    public abstract void stop();
}
