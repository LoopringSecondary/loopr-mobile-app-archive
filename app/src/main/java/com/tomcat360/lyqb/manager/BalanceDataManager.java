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

import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.lyqb.walletsdk.model.response.data.MarketcapResult;
import com.tomcat360.lyqb.utils.CurrencyUtil;
import com.tomcat360.lyqb.utils.NumberUtils;

import rx.Observable;

public class BalanceDataManager {

    private Context context;

    private List<BalanceResult.Asset> assets;

    private Observable<BalanceResult> balanceObservable;

    private static TokenDataManager tokenManager;

    private static MarketcapDataManager priceManager;

    private static BalanceDataManager balanceDataManager = null;

    private BalanceDataManager(Context context) {
        this.context = context;
        tokenManager = TokenDataManager.getInstance(context);
        priceManager = MarketcapDataManager.getInstance(context);
    }

    public static BalanceDataManager getInstance(Context context) {
        if (balanceDataManager == null) {
            balanceDataManager = new BalanceDataManager(context);
        }
        return balanceDataManager;
    }

    public List<BalanceResult.Asset> getAssets() {
        return assets;
    }

    public Observable<BalanceResult> getBalanceObservable() {
        return balanceObservable;
    }

    public BalanceResult.Asset getAssetBySymbol(String symbol) {
        BalanceResult.Asset result = null;
        for (BalanceResult.Asset asset : assets) {
            if (asset.getSymbol().equalsIgnoreCase(symbol)) {
                result = asset;
                break;
            }
        }
        return result;
    }

    public int getPrecisionBySymbol(String symbol) {
        int result = 4;
        for (BalanceResult.Asset asset : assets) {
            if (asset.getSymbol().equalsIgnoreCase(symbol)) {
                result = asset.getPrecision();
                break;
            }
        }
        return result;
    }

    // support for main fragment presenter
    public void mergeAssets(BalanceResult balance) {
        List<MarketcapResult.Token> tokens = priceManager.getMarketcapResult().getTokens();
        for (BalanceResult.Asset asset : balance.getTokens()) {
            if (!this.assets.contains(asset)) {
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
                    }
                }
            }
        }
    }
}
