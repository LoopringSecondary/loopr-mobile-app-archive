package com.lyqb.walletsdk.service;


import com.lyqb.walletsdk.Default;
import com.lyqb.walletsdk.model.loopr.request.param.GetBalance;
import com.lyqb.walletsdk.model.loopr.response.BalanceResult;
import com.lyqb.walletsdk.service.listener.BalanceListener;

import java.net.URISyntaxException;
import java.util.Arrays;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;
import rx.Observable;

public class LooprSocketService {

    private Socket socket;
    public boolean connected = false;

    private BalanceListener balanceListener;


    public LooprSocketService(String serviceUrl) {
        OkHttpClient okHttpClient = new OkHttpClient();
        IO.Options opt = new IO.Options();
        opt.reconnection = true;
        opt.reconnectionAttempts = 10;
        opt.transports = new String[]{"websocket"};
        opt.callFactory = okHttpClient;
        opt.webSocketFactory = okHttpClient;
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

        balanceListener = new BalanceListener(socket);
    }

    public void close() {
        balanceListener.stop();

        socket.close();
    }

    public Observable<BalanceResult> getBalanceDataStream() {
        return balanceListener.start();
    }

    public void requestBalance(String owner) {
        GetBalance getBalance = GetBalance.builder()
                .owner(owner)
                .delegateAddress(Default.DELEGATE_ADDRESS)
                .build();
        balanceListener.send(getBalance);
    }
}
