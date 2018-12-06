/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-17 下午3:14
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import leaf.prod.walletsdk.listener.BalanceListener;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.model.response.relay.MarketcapResult;
import leaf.prod.walletsdk.model.response.relay.Token;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BalanceDataManager {

    private static TokenDataManager tokenManager;

    private static MarketcapDataManager priceManager;

    private static BalanceDataManager balanceDataManager = null;

    private Context context;

    private BalanceResult balance;

    private Observable<BalanceResult> balanceObservable;

    private BalanceListener balanceListener;

    private String address;

    private BalanceDataManager(Context context) {
        this.context = context;
        this.balanceListener = new BalanceListener();
        tokenManager = TokenDataManager.getInstance(context);
        priceManager = MarketcapDataManager.getInstance(context);
        loadBalanceFromRelay();
    }

    public static BalanceDataManager getInstance(Context context) {
        if (balanceDataManager == null) {
            balanceDataManager = new BalanceDataManager(context);
        }
        return balanceDataManager;
    }

    private void loadBalanceFromRelay() {
        if (balanceObservable == null) {
            balanceObservable = balanceListener.start()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        balanceListener.queryByOwner(getAddress());
    }

    public BalanceResult getBalance() {
        return balance;
    }

    public List<BalanceResult.Asset> getAssets() {
        return balance != null ? balance.getTokens() : null;
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

    public String getFormattedBySymbol(String symbol, Double value) {
        int precision = getPrecision(symbol);
        return NumberUtils.format1(value, precision);
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

    public List<Token> getBalanceTokens() {
        List<Token> result = new LinkedList<>();
        for (BalanceResult.Asset asset : balance.getTokens()) {
            if (asset.getBalance() != null && asset.getBalance().doubleValue() != 0) {
                Token token = tokenManager.getTokenBySymbol(asset.getSymbol());
                result.add(token);
            }
        }
        return result;
    }

    // support for main fragment presenter
    public void mergeAssets(BalanceResult balance) {
        List<MarketcapResult.Token> tokens = priceManager.getMarketcapResult().getTokens();
        for (BalanceResult.Asset asset : balance.getTokens()) {
            for (MarketcapResult.Token token : tokens) {
                if (token.getSymbol().equalsIgnoreCase(asset.getSymbol())) {
                    int precision = NumberUtils.precision(token.getPrice());
                    if (tokenManager.getTokenBySymbol(asset.getSymbol()) != null) {
                        BigDecimal decimals = tokenManager.getTokenBySymbol(asset.getSymbol()).getDecimals();
                        double value = asset.getBalance().divide(decimals).doubleValue();
                        asset.setPrecision(precision);
                        asset.setValue(value);
                        asset.setValueShown(NumberUtils.format1(value, precision));
                        asset.setLegalValue(token.getPrice() * value);
                        asset.setLegalShown(CurrencyUtil.format(context, asset.getLegalValue()));
                    }
                    break;
                }
            }
        }
        this.balance = balance;
    }

    public Observable<BalanceResult> getObservable() {
        return balanceObservable;
    }

    public String getAddress() {
        if (address == null)
            //            address = (String) SPUtils.get(Objects.requireNonNull(context), "address", "");
            address = WalletUtil.getCurrentAddress(context);
        return address;
    }

    public static int getPrecision(String symbol) {
        BalanceResult.Asset asset = balanceDataManager.getAssetBySymbol(symbol);
        return asset != null ? asset.getPrecision() : 6;
    }
}
