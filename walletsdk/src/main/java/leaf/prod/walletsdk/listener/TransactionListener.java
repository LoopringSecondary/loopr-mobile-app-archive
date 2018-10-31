package leaf.prod.walletsdk.listener;

import com.google.gson.JsonElement;
import leaf.prod.walletsdk.model.request.param.TransactionParam;
import leaf.prod.walletsdk.model.response.data.TransactionPageWrapper;
import leaf.prod.walletsdk.util.Assert;

import rx.Observable;
import rx.subjects.PublishSubject;

public class TransactionListener extends AbstractListener<TransactionPageWrapper, TransactionParam> {

    public static final String TAG = "transaction";

    private final PublishSubject<TransactionPageWrapper> subject = PublishSubject.create();

    @Override
    protected void registerEventHandler() {
        socket.on("transaction_res", objects -> {
            JsonElement element = extractPayload(objects);
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

    public void queryBySymbol(String owner, String symbol, int pageIndex, int pageSize) {
        Assert.hasText(owner, "walletAddress can not be null");
        Assert.hasText(owner, "symbol can not be null");
        TransactionParam param = TransactionParam.builder()
                .owner(owner)
                .symbol(symbol)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        send(param);
    }

    public void queryBySymbolAndStatus(String owner, String symbol, String status, int pageIndex, int pageSize) {
        Assert.hasText(owner);
        Assert.hasText(symbol);
        Assert.hasText(status);
        TransactionParam param = TransactionParam.builder()
                .owner(owner)
                .symbol(symbol)
                .status(status)
                .pageSize(pageSize)
                .pageIndex(pageIndex)
                .build();
        send(param);
    }

    public void queryBySymbolAndTxType(String owner, String symbol, String txType, int pageIndex, int pageSize) {
        TransactionParam param = TransactionParam.builder()
                .owner(owner)
                .symbol(symbol)
                .txType(txType)
                .pageIndex(pageIndex)
                .pageSize(pageIndex)
                .build();
        send(param);
    }

    public void queryBySymbolAndStatusAndTxType(String owner, String symbol, String status, String txType, int pageIndex, int pageSize) {
        TransactionParam param = TransactionParam.builder()
                .owner(owner)
                .symbol(symbol)
                .status(status)
                .txType(txType)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        send(param);
    }

    public void queryByTxHash(String owner, String txHash) {
        Assert.hasText(owner, "walletAddress can not be null");
        Assert.hasText(txHash, "txHash can not be null");
        TransactionParam param = TransactionParam.builder()
                .owner(owner)
                .txHash(txHash)
                .build();
        send(param);
    }
}
