package leaf.prod.walletsdk.manager;

import java.util.List;

import android.content.Context;

import leaf.prod.walletsdk.model.Ticker;
import leaf.prod.walletsdk.service.LoopringService;

public class MarketPriceDataManager {

    private static MarketPriceDataManager marketDataManager;

    private List<Ticker> tickers;

    private Context context;

    protected LoopringService loopringService;

    private MarketPriceDataManager(Context context) {
        this.context = context;
        this.loopringService = new LoopringService();
    }

    public static MarketPriceDataManager getInstance(Context context) {
        if (marketDataManager == null) {
            marketDataManager = new MarketPriceDataManager(context);
        }
        return marketDataManager;
    }

    public void convert(List<Ticker> tickers) {

    }

    public LoopringService getLoopringService() {
        return loopringService;
    }

    public List<Ticker> getTickers() {
        return tickers;
    }
}
