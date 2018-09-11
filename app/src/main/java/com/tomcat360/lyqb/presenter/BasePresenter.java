package com.tomcat360.lyqb.presenter;

import java.util.List;

import android.content.Context;

import com.lyqb.walletsdk.listener.MarketcapListener;
import com.lyqb.walletsdk.model.request.param.MarketcapParam;
import com.lyqb.walletsdk.model.response.data.MarketcapResult;
import com.lyqb.walletsdk.model.response.data.Token;
import com.lyqb.walletsdk.service.LoopringService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BasePresenter<V> {

    protected V view;

    protected Context context;

    private List<Token> tokens;

    private MarketcapResult marketcapResult;

    private LoopringService loopringService = new LoopringService();

    private MarketcapListener marketcapListener = new MarketcapListener();

    public BasePresenter(V View, Context context) {
        this.attachView(View);
        this.context = context;
    }

    public void attachView(V view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    public boolean isViewAttached() {
        return view != null;
    }

    // for common usage
    private void updateTokens() {
        loopringService.getSupportedToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokens -> this.tokens = tokens);
        for (Token token : tokens) {
            String image = String.format("icon_token_%s", token.getSymbol());
            int identifier = context.getResources().getIdentifier(image, "mipmap", "android");
            token.setImageResId(identifier);
        }
    }

    private void updateMarketcap() {
        Observable<MarketcapResult> observable = marketcapListener.start();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(marketcapResult -> this.marketcapResult = marketcapResult);
        marketcapListener.send(MarketcapParam.builder().currency("CNY").build()); //TODO
    }

    public Double getLegalPriceBySymbol(String symbol) {
        Double result = null;
        List<MarketcapResult.Token> tokens = marketcapResult.getTokens();
        for (MarketcapResult.Token token : tokens) {
            if (token.getSymbol().equalsIgnoreCase(symbol)) {
                result = token.getPrice();
            }
        }
        return result;
    }

    public Token getTokenBySymbol(String symbol) {
        Token result = null;
        for (Token token : this.tokens) {
            if (token.getSymbol().equalsIgnoreCase(symbol)) {
                result = token;
            }
        }
        return result;
    }

    public Token getTokenByProtocol(String protocol) {
        Token result = null;
        for (Token token : this.tokens) {
            if (token.getProtocol().equalsIgnoreCase(protocol)) {
                result = token;
            }
        }
        return result;
    }
}
