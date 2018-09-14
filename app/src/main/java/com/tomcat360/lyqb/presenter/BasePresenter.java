package com.tomcat360.lyqb.presenter;

import java.util.List;

import android.content.Context;

import com.lyqb.walletsdk.model.response.data.MarketcapResult;
import com.lyqb.walletsdk.model.response.data.Token;

public class BasePresenter<V> {

    protected V view;

    protected Context context;

    public BasePresenter(V view, Context context) {
        this.attachView(view);
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

//    public Double getLegalPriceBySymbol(String symbol) {
//        return getLegalPriceBySymbol(marketDataManager.getMarketcapResult(), symbol);
//    }

    public double getLegalPriceBySymbol(MarketcapResult marketcapResult, String symbol) {
        double result = 0.0;
        List<MarketcapResult.Token> tokens = marketcapResult.getTokens();
        for (MarketcapResult.Token token : tokens) {
            if (token.getSymbol().equalsIgnoreCase(symbol)) {
                result = token.getPrice();
            }
        }
        return result;
    }
//
//    public Token getTokenBySymbol(String symbol) {
//        return getTokenBySymbol(marketDataManager.getTokens(), symbol);
//    }

    public Token getTokenBySymbol(List<Token> tokenList, String symbol) {
        Token result = null;
        for (Token token : tokenList) {
            if (token.getSymbol().equalsIgnoreCase(symbol)) {
                result = token;
            }
        }
        return result;
    }
//
//    public Token getTokenByProtocol(String protocol) {
//        Token result = null;
//        for (Token token : marketDataManager.getTokens()) {
//            if (token.getProtocol().equalsIgnoreCase(protocol)) {
//                result = token;
//            }
//        }
//        return result;
//    }
}
