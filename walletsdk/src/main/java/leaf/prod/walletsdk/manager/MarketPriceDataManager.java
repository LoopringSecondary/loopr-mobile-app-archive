package leaf.prod.walletsdk.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;

import org.apache.commons.lang3.ArrayUtils;

import leaf.prod.walletsdk.model.Depth;
import leaf.prod.walletsdk.model.Language;
import leaf.prod.walletsdk.model.OrderFill;
import leaf.prod.walletsdk.model.Ticker;
import leaf.prod.walletsdk.model.TradingPair;
import leaf.prod.walletsdk.model.Trend;
import leaf.prod.walletsdk.model.TrendInterval;
import leaf.prod.walletsdk.model.UserConfig;
import leaf.prod.walletsdk.service.LoopringService;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.DateUtil;
import leaf.prod.walletsdk.util.LanguageUtil;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.StringUtils;

public class MarketPriceDataManager {

    private Context context;

    private boolean isFiltering;

    private List<Ticker> tickers;

    private List<Ticker> filteredTickers;

    private List<Trend> trends;

    private Depth depth;

    private List<OrderFill> orderFills;

    protected LoopringService loopringService;

    private static MarketPriceDataManager marketPriceManager;

    private MarketPriceDataManager(Context context) {
        this.context = context;
        this.isFiltering = false;
        this.tickers = new ArrayList<>();
        this.trends = new ArrayList<>();
        this.filteredTickers = new ArrayList<>();
        this.loopringService = new LoopringService();
    }

    public static MarketPriceDataManager getInstance(Context context) {
        if (marketPriceManager == null) {
            marketPriceManager = new MarketPriceDataManager(context);
        }
        return marketPriceManager;
    }

    public void convertTickers(List<Ticker> tickers) {
        this.tickers.clear();
        for (Ticker ticker : tickers) {
            Ticker newTicker = Ticker.builder()
                    .market(ticker.getMarket())
                    .exchange(ticker.getExchange())
                    .tradingPair(getTradingPair(ticker))
                    .balanceShown(NumberUtils.format1(ticker.getLast(), ticker.getDecimals()))
                    .vol(ticker.getVol())
                    .change(getChange(ticker.getChange()))
                    .tag(ticker.getTag())
                    .market(ticker.getMarket())
                    .decimals(ticker.getDecimals())
                    .currencyShown(getCurrency(ticker))
                    .open(ticker.getOpen())
                    .close(ticker.getClose())
                    .last(ticker.getLast())
                    .low(ticker.getLow())
                    .high(ticker.getHigh())
                    .buy(ticker.getBuy())
                    .sell(ticker.getSell())
                    .build();
            this.tickers.add(newTicker);
        }
    }

    private TradingPair getTradingPair(Ticker ticker) {
        String[] tokens = ticker.getMarket().split("-");
        String tradingPair = tokens[0] + "-" + tokens[1];
        return TradingPair.builder().tokenA(tokens[0]).tokenB(tokens[1]).description(tradingPair).build();
    }

    private String getChange(String data) {
        String result = "--";
        if (data != null && !StringUtils.isEmpty(data)) {
            if (data.startsWith("-")) {
                result = "↓" + data.replace("-", "");
            } else {
                result = "↑" + data;
            }
        }
        return result;
    }

    private String getCurrency(Ticker ticker) {
        String tokenA = ticker.getMarket().split("-")[0];
        Double price = MarketcapDataManager.getInstance(context).getPriceBySymbol(tokenA);
        return CurrencyUtil.format(context, price);
    }

    public LoopringService getLoopringService() {
        return loopringService;
    }

    public List<Ticker> getTickers() {
        return this.isFiltering ? this.filteredTickers : this.tickers;
    }

    public List<Ticker> getAllTickers() {
        return tickers;
    }

    public List<Ticker> getTickersBy(String token) {
        List<Ticker> result = new ArrayList<>();
        List<Ticker> tickers = getTickers();
        for (Ticker ticker : tickers) {
            if (ticker.getTradingPair().getTokenB().equalsIgnoreCase(token)) {
                result.add(ticker);
            }
        }
        return result;
    }

    public Ticker getTickersBy(TradingPair pair) {
        Ticker result = null;
        List<Ticker> tickers = getTickers();
        for (Ticker ticker : tickers) {
            if (ticker.getTradingPair().equals(pair)) {
                result = ticker;
                break;
            }
        }
        return result;
    }

    public List<Ticker> getFavTickers() {
        List<Ticker> result = new ArrayList<>();
        UserConfig config = LoginDataManager.getInstance(context).getLocalUser();
        if (config != null && config.getFavMarkets() != null) {
            for (TradingPair pair : config.getFavMarkets()) {
                if (getTickersBy(pair) != null) {
                    result.add(getTickersBy(pair));
                }
            }
        }
        return result;
    }

    public void setFiltering(boolean filtering) {
        isFiltering = filtering;
    }

    public void setFilteredTickers(List<Ticker> filteredTickers) {
        this.filteredTickers = filteredTickers;
    }

    public void convertTrend(List<Trend> trends) {
        this.trends.clear();
        for (Trend trend : trends) {
            getTrendValue(trend);
            getTrendInterval(trend);
            this.trends.add(trend);
        }
        Collections.sort(this.trends, (o1, o2) -> (int) (o1.getCreateTime() - o2.getCreateTime()));
    }

    private void getTrendInterval(Trend trend) {
        String start = "", end = "";
        TrendInterval interval = TrendInterval.getInterval(trend.getIntervals());
        switch (interval) {
            case ONE_HOUR:
            case TWO_HOURS:
            case FOUR_HOURS:
                if (LanguageUtil.getLanguage(context) == Language.zh_CN) {
                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "MM-dd HH:mm");
                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "HH:mm");
                } else {
                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "MMM dd HH:mm");
                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "HH:mm");
                }
                break;
            case ONE_DAY:
                if (LanguageUtil.getLanguage(context) == Language.zh_CN) {
                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "MM-dd HH:mm");
                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "MM-dd HH:mm");
                } else {
                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "MMM dd HH:mm");
                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "MMM dd HH:mm");
                }
                break;
            case ONE_WEEK:
                if (LanguageUtil.getLanguage(context) == Language.zh_CN) {
                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "yyyy-MM-dd");
                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "MM-dd");
                } else {
                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "MMM dd, yyyy");
                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "MMM dd");
                }
                break;
        }
        trend.setRange(start + " ~ " + end);
    }

    private void getTrendValue(Trend trend) {
        if (trend.getHigh() > 2 * Math.max(trend.getOpen(), trend.getClose())) {
            trend.setHigh(2 * Math.max(trend.getOpen(), trend.getClose()));
        }
        if (trend.getLow() < 0.5 * Math.min(trend.getOpen(), trend.getClose())) {
            trend.setLow(0.5 * Math.min(trend.getOpen(), trend.getClose()));
        }
        double change = (trend.getClose() - trend.getOpen()) / trend.getOpen();
        if (change == 0) {
            trend.setChange("--");
        } else if (change > 0) {
            trend.setChange("↑" + NumberUtils.format1(change * 100, 2) + "%");
        } else if (change < 0) {
            trend.setChange("↓" + NumberUtils.format1(-change * 100, 2) + "%");
        }
    }

    public List<Trend> getTrends() {
        return trends;
    }

    public void convertDepths(Depth result) {
        String[][] appendArray;
        String[][] buyArray = result.getDepth().getBuy();
        String[][] sellArray = result.getDepth().getSell();

        int length = Math.max(buyArray.length, sellArray.length);
        if (buyArray.length < length) {
            appendArray = constructAppendArray(length - buyArray.length);
            buyArray = ArrayUtils.addAll(buyArray, appendArray);
            result.getDepth().setBuy(buyArray);
        } else if (sellArray.length < length) {
            appendArray = constructAppendArray(length - sellArray.length);
            sellArray = ArrayUtils.addAll(sellArray, appendArray);
        }
        List<String[]> list = Arrays.asList(sellArray);
        Collections.sort(list, (o1, o2) -> o1[0].compareTo(o2[0]));
        result.getDepth().setSell((String[][]) list.toArray());
        this.depth = result;
    }

    private String[][] constructAppendArray(int length) {
        String[][] array = new String[length][3];
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < 3; ++j) {
                String field = "";
                array[i][j] = field;
            }
        }
        return array;
    }

    public List<String[]> getDepths(String side) {
        if (side.equals("buy")) {
            return Arrays.asList(depth.getDepth().getBuy());
        } else {
            return Arrays.asList(depth.getDepth().getSell());
        }
    }

    public void convertOrderFills(List<OrderFill> result) {
        this.orderFills = result;
    }

    public List<OrderFill> getOrderFills() {
        return orderFills;
    }
}
