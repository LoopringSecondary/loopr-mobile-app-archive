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
            marketDataManager.initMarketcap();
        }
        return marketDataManager;
    }

    public MarketcapResult getMarketcapResult() {
        return marketcapResult;
    }

    private void initMarketcap() {
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

    public void setMarketcapResult(MarketcapResult marketcapResult) {
        this.marketcapResult = marketcapResult;
    }

    public Double getPriceBySymbol(String symbol) {
        Double result = null;
        for (MarketcapResult.Token token : marketcapResult.getTokens()) {
            if (token.getSymbol().equalsIgnoreCase(symbol)) {
                result = token.getPrice();
                break;
            }
        }
        return result;
    }
}
