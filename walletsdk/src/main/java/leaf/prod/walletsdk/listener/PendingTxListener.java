package leaf.prod.walletsdk.listener;

import com.google.gson.JsonElement;
import leaf.prod.walletsdk.model.request.param.PendingTxParam;
import leaf.prod.walletsdk.model.response.data.PendingTxResult;
import leaf.prod.walletsdk.util.Assert;

import leaf.prod.walletsdk.model.request.param.PendingTxParam;
import leaf.prod.walletsdk.model.response.data.PendingTxResult;
import leaf.prod.walletsdk.util.Assert;
import rx.Observable;
import rx.subjects.PublishSubject;

public class PendingTxListener extends AbstractListener<PendingTxResult, PendingTxParam> {

    public static final String TAG = "PendingTx";

    private final PublishSubject<PendingTxResult> subject = PublishSubject.create();

    @Override
    protected void registerEventHandler() {
        socket.on("pendingTx_res", objects -> {
            JsonElement element = extractPayload(objects);
            PendingTxResult pendingTxResult = gson.fromJson(element, PendingTxResult.class);
            subject.onNext(pendingTxResult);
        });
        socket.on("pendingTx_end", objects -> {
            subject.onCompleted();
        });
    }

    @Override
    public Observable<PendingTxResult> start() {
        return subject;
    }

    @Override
    public void stop() {
        socket.emit("pendingTx_end", "");
    }

    @Override
    public void send(PendingTxParam param) {
        String s = gson.toJson(param);
        socket.emit("pendingTx_req", s);
    }

    public void queryByOwner(String owner) {
        Assert.hasText(owner);
        PendingTxParam param = PendingTxParam.builder()
                .owner(owner)
                .build();
        send(param);
    }
}
