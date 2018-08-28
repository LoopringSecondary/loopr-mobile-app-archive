package com.lyqb.walletsdk.service;


import com.lyqb.walletsdk.Default;
import com.lyqb.walletsdk.model.request.param.GetBalance;
import com.lyqb.walletsdk.model.request.param.GetTransaction;
import com.lyqb.walletsdk.model.response.BalanceResult;
import com.lyqb.walletsdk.model.response.TransactionPageWrapper;
import com.lyqb.walletsdk.service.listener.BalanceListener;
import com.lyqb.walletsdk.service.listener.TransactionListener;

import io.socket.client.Socket;
import rx.Observable;

public class LooprSocketService {

    private BalanceListener balanceListener;
    private TransactionListener transactionListener;

    public LooprSocketService(Socket socket) {
        balanceListener = new BalanceListener(socket);
        transactionListener = new TransactionListener(socket);
    }

    public void close() {
        balanceListener.stop();
        transactionListener.stop();
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

    public Observable<TransactionPageWrapper> getTransactionDateStream() {
        return transactionListener.start();
    }

    public void requestTransaction(String owner, String symbol, int pageIndex, int pageSize) {
        GetTransaction getTransaction = GetTransaction.builder()
                .owner(owner)
                .symbol(symbol)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        transactionListener.send(getTransaction);
    }
}
