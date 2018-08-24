package com.lyqb.walletsdk.service;


import com.lyqb.walletsdk.model.loopr.request.param.GetBalance;
import com.lyqb.walletsdk.model.loopr.response.BalanceResult;
import com.lyqb.walletsdk.service.listener.AbstractListener;
import com.lyqb.walletsdk.service.listener.BalanceListener;
import com.lyqb.walletsdk.singleton.OkHttpInstance;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import rx.Observable;

public class LooprSocketService {

    private Socket socket;
    public boolean connected = false;

    private List<AbstractListener> listeners = new LinkedList<>();

    public LooprSocketService(String serviceUrl) {
        IO.Options opt = new IO.Options();
        opt.reconnection = true;
        opt.reconnectionAttempts = 10;
        opt.transports = new String[]{"websocket"};
        opt.callFactory = OkHttpInstance.getClient();
        opt.webSocketFactory = OkHttpInstance.getClient();
        try {
            socket = IO.socket(serviceUrl, opt);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, args -> {
            System.out.println("connected! " + Arrays.toString(args));
            this.connected = true;
        });
        socket.connect();
    }

    public void close() {
        for (AbstractListener listener : listeners) {
            listener.stop();
        }
        socket.close();
    }

    public Observable<BalanceResult> getBalance(String owner) {
        BalanceListener balanceListener = new BalanceListener(socket);
        listeners.add(balanceListener);
        GetBalance getBalance = GetBalance.builder()
                .owner(owner)
                .delegateAddress("0x17233e07c67d086464fD408148c3ABB56245FA64")
                .build();
        return balanceListener.start(getBalance);
    }
}
