package com.lyqb.walletsdk.service;


import com.lyqb.walletsdk.Default;
import com.lyqb.walletsdk.model.loopr.request.param.GetBalance;
import com.lyqb.walletsdk.model.loopr.response.BalanceResult;
import com.lyqb.walletsdk.service.listener.BalanceListener;

import io.socket.client.Socket;
import rx.Observable;

public class LooprSocketService {

//    private Socket socket;

    private BalanceListener balanceListener;

    public LooprSocketService(Socket socket) {

        balanceListener = new BalanceListener(socket);
    }

    public void close() {
        balanceListener.stop();

//        socket.close();
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
