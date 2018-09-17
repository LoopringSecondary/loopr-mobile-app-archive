/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-17 下午3:14
 * Cooperation: loopring.org 路印协议基金会
 */
package com.tomcat360.lyqb.manager;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;

import com.lyqb.walletsdk.listener.BalanceListener;
import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.lyqb.walletsdk.model.response.data.MarketcapResult;
import com.tomcat360.lyqb.utils.CurrencyUtil;
import com.tomcat360.lyqb.utils.NumberUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BalanceDataManager {

    private Context context;

    private BalanceResult balance;

    private Observable<BalanceResult> balanceObservable;

    private BalanceListener balanceListener;

    private static TokenDataManager tokenManager;

    private static MarketcapDataManager priceManager;

    private static BalanceDataManager balanceDataManager = null;

    private BalanceDataManager(Context context) {
        this.context = context;
        this.balanceListener = new BalanceListener();
        tokenManager = TokenDataManager.getInstance(context);
        priceManager = MarketcapDataManager.getInstance(context);
        loadBalanceFromRelay();
    }

    private void loadBalanceFromRelay() {
        if (balanceObservable == null) {
            balanceObservable = balanceListener.start()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public static BalanceDataManager getInstance(Context context) {
        if (balanceDataManager == null) {
            balanceDataManager = new BalanceDataManager(context);
        }
        return balanceDataManager;
    }

    public BalanceResult getBalance() {
        return balance;
    }

    public List<BalanceResult.Asset> getAssets() {
        return balance.getTokens();
    }

    public Observable<BalanceResult> getBalanceObservable() {
        return balanceObservable;
    }

    public BalanceResult.Asset getAssetBySymbol(String symbol) {
        BalanceResult.Asset result = null;
        for (BalanceResult.Asset asset : balance.getTokens()) {
            if (asset.getSymbol().equalsIgnoreCase(symbol)) {
                result = asset;
                break;
            }
        }
        return result;
    }

    public int getPrecisionBySymbol(String symbol) {
        int result = 4;
        for (BalanceResult.Asset asset : balance.getTokens()) {
            if (asset.getSymbol().equalsIgnoreCase(symbol)) {
                result = asset.getPrecision();
                break;
            }
        }
        return result;
    }

    // support for main fragment presenter
    public void mergeAssets(BalanceResult balance) {
        this.balance.getTokens().clear();
        List<MarketcapResult.Token> tokens = priceManager.getMarketcapResult().getTokens();
        for (BalanceResult.Asset asset : balance.getTokens()) {
            for (MarketcapResult.Token token : tokens) {
                if (token.getSymbol().equalsIgnoreCase(asset.getSymbol())) {
                    int precision = NumberUtils.integralLength(token.getPrice()) + 2; // TODO: 显示到当前货币最小单位
                    BigDecimal decimals = tokenManager.getTokenBySymbol(asset.getSymbol()).getDecimals();
                    double value = asset.getBalance().divide(decimals).doubleValue();
                    asset.setPrecision(precision);
                    asset.setValue(value);
                    asset.setValueShown(NumberUtils.format1(value, precision));
                    asset.setLegalValue(token.getPrice() * asset.getBalance().doubleValue());
                    asset.setLegalShown(CurrencyUtil.format(context, asset.getLegalValue()));
                    this.balance.getTokens().add(asset);
                }
            }
        }
    }
}
