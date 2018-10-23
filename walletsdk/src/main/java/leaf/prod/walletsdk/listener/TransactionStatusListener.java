package leaf.prod.walletsdk.listener;

import com.google.gson.JsonElement;

import leaf.prod.walletsdk.model.request.param.PendingTxParam;
import leaf.prod.walletsdk.model.response.data.Transaction;
import leaf.prod.walletsdk.util.Assert;
import rx.Observable;
import rx.subjects.PublishSubject;

public class TransactionStatusListener extends AbstractListener<Transaction[], PendingTxParam> {

    public static final String TAG = "TransactionStatus";

    private final PublishSubject<Transaction[]> subject = PublishSubject.create();

    @Override
    protected void registerEventHandler() {
        socket.on("transactionStatus_res", objects -> {
            JsonElement element = extractPayload(objects);
            Transaction[] Transaction = gson.fromJson(element, Transaction[].class);
            subject.onNext(Transaction);
        });
        socket.on("transactionStatus_end", objects -> subject.onCompleted());
    }

    @Override
    public Observable<Transaction[]> start() {
        return subject;
    }

    @Override
    public void stop() {
        socket.emit("transactionStatus_end", "");
    }

    @Override
    public void send(PendingTxParam param) {
        String s = gson.toJson(param);
        socket.emit("transactionStatus_req", s);
    }

    public void queryByHashes(String owner, String[] hashes) {
        Assert.hasText(owner);
        PendingTxParam param = PendingTxParam.builder()
                .owner(owner)
                .trxHashes(hashes)
                .build();
        send(param);
    }
}
