package com.tomcat360.lyqb.presenter;

import java.util.List;

import android.content.Context;

import com.lyqb.walletsdk.listener.MarketcapListener;
import com.lyqb.walletsdk.model.Currency;
import com.lyqb.walletsdk.model.request.param.MarketcapParam;
import com.lyqb.walletsdk.model.response.data.MarketcapResult;
import com.lyqb.walletsdk.model.response.data.Token;
import com.lyqb.walletsdk.service.LoopringService;
import com.tomcat360.lyqb.utils.CurrencyUtil;

import rx.Observable;
import rx.schedulers.Schedulers;

public class BasePresenter<V> {

    protected V view;

    protected Context context;

    private static List<Token> tokens;

    private static MarketcapResult marketcapResult;

    private LoopringService loopringService = new LoopringService();

    private MarketcapListener marketcapListener = new MarketcapListener();

    private static boolean tokensGet = false;

    private static boolean marketcapGet = false;

    public BasePresenter(V View, Context context) {
        this.attachView(View);
        this.context = context;
        this.updateMarketcap();
        this.updateTokens();
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
                .subscribe(tokens -> {
                    for (Token token : tokens) {
                        String image = String.format("icon_token_%s", token.getSymbol());
                        int identifier = context.getResources().getIdentifier(image, "mipmap", "android");
                        token.setImageResId(identifier);
                    }
                    BasePresenter.tokens = tokens;
                    tokensGet = true;
                });
    }

    private void updateMarketcap() {
        Observable<MarketcapResult> observable = marketcapListener.start();
        observable.subscribeOn(Schedulers.io())
                .subscribe(marketcapResult -> {
                    BasePresenter.marketcapResult = marketcapResult;
                    marketcapGet = true;
                });
        Currency currency = CurrencyUtil.getCurrency(context);
        marketcapListener.send(MarketcapParam.builder().currency(currency.name()).build());
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

    public boolean initComplete() {
        return tokensGet && marketcapGet;
    }

}
