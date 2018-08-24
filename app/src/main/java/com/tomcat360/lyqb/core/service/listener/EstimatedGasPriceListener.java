package com.tomcat360.lyqb.core.service.listener;

import com.tomcat360.lyqb.core.model.loopr.response.EstimateGasPriceResult;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import rx.Observable;
import rx.subjects.PublishSubject;

public class EstimatedGasPriceListener extends AbstractListener<EstimateGasPriceResult> {

    private PublishSubject<EstimateGasPriceResult> subject = PublishSubject.create();

    public EstimatedGasPriceListener(Socket socket) {
        super(socket);
    }


    @Override
    protected void registerEventHandler() {
        socket.on("estimatedGasPrice_res", args -> {

            try {
                JSONObject jsonObject = new JSONObject(((String) args[0]));
                String dataStr = jsonObject.getString("data");
                EstimateGasPriceResult estimateGasPriceResult = new EstimateGasPriceResult(dataStr);
                subject.onNext(estimateGasPriceResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Observable<EstimateGasPriceResult> start(Object param) {
        return null;
    }

    @Override
    public void stop() {

    }
}
