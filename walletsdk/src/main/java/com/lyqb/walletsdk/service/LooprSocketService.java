//package com.lyqb.walletsdk.service;
//
//
//import com.lyqb.walletsdk.Default;
//import com.lyqb.walletsdk.model.request.param.BalanceParam;
//import com.lyqb.walletsdk.model.request.param.TransactionParam;
//import com.lyqb.walletsdk.model.response.data.BalanceResult;
//import com.lyqb.walletsdk.model.response.data.TransactionPageWrapper;
//import com.lyqb.walletsdk.listener.BalanceListener;
//import com.lyqb.walletsdk.listener.TransactionListener;
//
//import io.socket.client.Socket;
//import rx.Observable;
//
//public class LooprSocketService {
//
//    private BalanceListener balanceListener;
//    private TransactionListener transactionListener;
//
//    public LooprSocketService(Socket socket) {
//        balanceListener = new BalanceListener();
//        transactionListener = new TransactionListener();
//    }
//
//    public void close() {
//        balanceListener.stop();
//        transactionListener.stop();
//    }
//
//    public Observable<BalanceResult> getBalanceDataStream() {
//        return balanceListener.start();
//    }
//
//    public void requestBalance(String owner) {
//        BalanceParam balanceParam = BalanceParam.builder()
//                .owner(owner)
//                .delegateAddress(Default.DELEGATE_ADDRESS)
//                .build();
//        balanceListener.send(balanceParam);
//    }
//
//    public Observable<TransactionPageWrapper> getTransactionDateStream() {
//        return transactionListener.start();
//    }
//
//    public void requestTransaction(String owner, String symbol, int pageIndex, int pageSize) {
//        TransactionParam transactionParam = TransactionParam.builder()
//                .owner(owner)
//                .symbol(symbol)
//                .pageIndex(pageIndex)
//                .pageSize(pageSize)
//                .build();
//        transactionListener.send(transactionParam);
//    }
//}
