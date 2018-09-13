package com.lyqb.walletsdk.listener;

import android.util.Log;

import com.lyqb.walletsdk.model.request.param.MarketcapParam;
import com.lyqb.walletsdk.model.response.data.MarketcapResult;

import rx.Observable;
import rx.subjects.PublishSubject;

public class MarketcapListener extends AbstractListener<MarketcapResult, MarketcapParam> {

    private final PublishSubject<MarketcapResult> subject = PublishSubject.create();

    @Override
    protected void registerEventHandler() {
        socket.on("marketcap_res", objects -> {
            MarketcapResult marketcapResult = gson.fromJson(extractPayload(objects), MarketcapResult.class);
            subject.onNext(marketcapResult);
        });
        socket.on("marketcap_end", data -> subject.onCompleted());
    }

    @Override
    public Observable<MarketcapResult> start() {
        return subject;
    }

    @Override
    public void stop() {
        socket.emit("marketcap_end", null, args -> subject.onCompleted());
    }

    @Override
    public void send(MarketcapParam param) {
        Log.d("", "marketcap send===============================>");
        String json = gson.toJson(param);
        socket.emit("marketcap_req", json);
    }
}
