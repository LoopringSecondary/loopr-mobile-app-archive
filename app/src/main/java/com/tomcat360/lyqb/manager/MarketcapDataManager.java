package com.tomcat360.lyqb.manager;

import android.content.Context;

import com.lyqb.walletsdk.listener.MarketcapListener;
import com.lyqb.walletsdk.model.Currency;
import com.lyqb.walletsdk.model.request.param.MarketcapParam;
import com.lyqb.walletsdk.model.response.data.MarketcapResult;
import com.tomcat360.lyqb.utils.CurrencyUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MarketcapDataManager {

    private static MarketcapDataManager marketDataManager;

    private MarketcapResult marketcapResult;

    private Context context;

    private MarketcapListener marketcapListener = new MarketcapListener();

    private Observable<MarketcapResult> observable;

    private MarketcapDataManager(Context context) {
        this.context = context;
    }

    public static MarketcapDataManager getInstance(Context context) {
        if (marketDataManager == null) {
            marketDataManager = new MarketcapDataManager(context);
            marketDataManager.updateMarketcap();
        }
        return marketDataManager;
    }

    public void refresh() {
        marketDataManager.updateMarketcap();
    }

    public MarketcapResult getMarketcapResult() {
        return marketcapResult;
    }

    private void updateMarketcap() {
        if (this.observable == null) {
            this.observable = marketcapListener.start()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            this.observable.subscribe(marketcapResult -> {
                this.marketcapResult = marketcapResult;
            }, error -> {
            });
        }
        Currency currency = CurrencyUtil.getCurrency(context);
        marketcapListener.send(MarketcapParam.builder().currency(currency.name()).build());
    }

    public Observable<MarketcapResult> getObservable() {
        return observable;
    }
}
