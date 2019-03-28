package leaf.prod.walletsdk.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import leaf.prod.walletsdk.model.market.Market;
import leaf.prod.walletsdk.model.market.MarketInterval;
import leaf.prod.walletsdk.model.market.MarketPair;
import leaf.prod.walletsdk.model.order.Fill;
import leaf.prod.walletsdk.model.response.relay.MarketHistoryResult;
import leaf.prod.walletsdk.model.response.relay.OrderBookResult;
import leaf.prod.walletsdk.model.setting.UserConfig;
import leaf.prod.walletsdk.service.RelayService;

public class MarketPriceDataManager {

    private Context context;

    private boolean isFiltering;

    private List<Market> markets;

    private List<Market> filteredMarkets;

    private Map<MarketInterval, List<MarketHistoryResult.Data>> trendMap;

    private OrderBookResult.OrderBook orderBook;

    private List<Fill> orderFills;

    private RelayService relayService;

    private static MarketPriceDataManager marketPriceManager;

    private MarketPriceDataManager(Context context) {
        this.context = context;
        this.isFiltering = false;
        this.trendMap = new HashMap<>();
        this.markets = new ArrayList<>();
        this.filteredMarkets = new ArrayList<>();
        this.relayService = new RelayService();
    }

    public static MarketPriceDataManager getInstance(Context context) {
        if (marketPriceManager == null) {
            marketPriceManager = new MarketPriceDataManager(context);
        }
        return marketPriceManager;
    }

    public void convertMarkets(List<Market> markets) {
        this.markets.clear();
        for (Market market : markets) {
            Market newMarket = market.convert();
            this.markets.add(newMarket);
        }
    }

    public RelayService getRelayService() {
        return relayService;
    }

    public List<Market> getMarkets() {
        return this.isFiltering ? this.filteredMarkets : this.markets;
    }

    public List<Market> getAllMarkets() {
        return markets;
    }

    public List<Market> getMarketsBy(String baseSymbol) {
        List<Market> result = new ArrayList<>();
        List<Market> markets = getMarkets();
        for (Market market : markets) {
            if (market.getBaseSymbol().equalsIgnoreCase(baseSymbol)) {
                result.add(market);
            }
        }
        return result;
    }

    public Market getMarketsBy(MarketPair pair) {
        Market result = null;
        List<Market> tickers = getMarkets();
        for (Market ticker : tickers) {
            if (ticker.getMarketPair().equals(pair)) {
                result = ticker;
                break;
            }
        }
        return result;
    }

    public List<Market> getFavTickers() {
        List<Market> result = new ArrayList<>();
        UserConfig config = LoginDataManager.getInstance(context).getLocalUser();
        if (config != null && config.getFavMarkets() != null) {
            for (MarketPair pair : config.getFavMarkets()) {
                if (getMarketsBy(pair) != null) {
                    result.add(getMarketsBy(pair));
                }
            }
        }
        return result;
    }

    public void setFiltering(boolean filtering) {
        isFiltering = filtering;
    }

    public void setFilteredMarkets(List<Market> filteredMarkets) {
        this.filteredMarkets = filteredMarkets;
    }

//    public void convertTrend(List<Trend> trends) {
//        List<Trend> trendList = new ArrayList<>();
//        for (Trend trend : trends) {
//            getTrendValue(trend);
//            getTrendRange(trend);
//            trendList.add(trend);
//        }
//        MarketInterval interval = MarketInterval.getInterval(trends.get(0).getIntervals());
//        Collections.sort(trendList, (o1, o2) -> (int) (o1.getCreateTime() - o2.getCreateTime()));
//        trendMap.put(interval, trendList);
//    }

//    private void getTrendRange(Trend trend) {
//        String start = "", end = "";
//        MarketInterval interval = MarketInterval.getInterval(trend.getIntervals());
//        switch (interval) {
//            case ONE_HOUR:
//            case TWO_HOURS:
//            case FOUR_HOURS:
//                if (LanguageUtil.getLanguage(context) == Language.zh_CN) {
//                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "MM-dd HH:mm");
//                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "HH:mm");
//                } else {
//                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "MMM d HH:mm", Locale.US);
//                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "HH:mm", Locale.US);
//                }
//                break;
//            case ONE_DAY:
//                if (LanguageUtil.getLanguage(context) == Language.zh_CN) {
//                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "MM-dd HH:mm");
//                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "MM-dd HH:mm");
//                } else {
//                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "MMM d HH:mm", Locale.US);
//                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "MMM d HH:mm", Locale.US);
//                }
//                break;
//            case ONE_WEEK:
//                if (LanguageUtil.getLanguage(context) == Language.zh_CN) {
//                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "yyyy-MM-dd");
//                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "MM-dd");
//                } else {
//                    start = DateUtil.formatDateTime(trend.getStart() * 1000L, "MMM d, yyyy", Locale.US);
//                    end = DateUtil.formatDateTime(trend.getEnd() * 1000L, "MMM d", Locale.US);
//                }
//                break;
//        }
//        trend.setRange(start + " ~ " + end);
//    }

//    private void getTrendValue(Trend trend) {
//        if (trend.getHigh() > 2 * Math.max(trend.getOpen(), trend.getClose())) {
//            trend.setHigh(2 * Math.max(trend.getOpen(), trend.getClose()));
//        }
//        if (trend.getLow() < 0.5 * Math.min(trend.getOpen(), trend.getClose())) {
//            trend.setLow(0.5 * Math.min(trend.getOpen(), trend.getClose()));
//        }
//        double change = (trend.getClose() - trend.getOpen()) / trend.getOpen();
//        if (change == 0) {
//            trend.setChange("--");
//        } else if (change > 0) {
//            trend.setChange("↑" + NumberUtils.format1(change * 100, 2) + "%");
//        } else if (change < 0) {
//            trend.setChange("↓" + NumberUtils.format1(-change * 100, 2) + "%");
//        }
//    }

//    public void convertOrderBook(OrderBookResult.OrderBook result) {
//        String[][] appendArray;
//        List<OrderBookResult.OrderBook.Order> buyList = result.getBuys();
//        List<OrderBookResult.OrderBook.Order> sellList = result.getSells();
//
//        int length = 25;
//        List<String[]> list = Arrays.asList(sellArray);
//        Collections.sort(list, (o1, o2) -> o1[0].compareTo(o2[0]));
//        sellArray = (String[][]) list.toArray();
//        if (buyArray.length < length) {
//            appendArray = constructAppendArray(length - buyArray.length);
//            buyArray = ArrayUtils.addAll(buyArray, appendArray);
//            result.getDepth().setBuy(buyArray);
//        } else if (buyArray.length > length) {
//            buyArray = ArrayUtils.subarray(buyArray, 0, length);
//            result.getDepth().setBuy(buyArray);
//        }
//        if (sellArray.length < length) {
//            appendArray = constructAppendArray(length - sellArray.length);
//            sellArray = ArrayUtils.addAll(sellArray, appendArray);
//        } else if (sellArray.length > length) {
//            sellArray = ArrayUtils.subarray(sellArray, 0, length);
//        }
//        result.getDepth().setSell(sellArray);
//        this.orderBook = result;
//    }
//
//    private String[][] constructAppendArray(int length) {
//        String[][] array = new String[length][3];
//        for (int i = 0; i < length; ++i) {
//            for (int j = 0; j < 3; ++j) {
//                String field = "";
//                array[i][j] = field;
//            }
//        }
//        return array;
//    }

    public List<OrderBookResult.OrderBook.Order> getDepths(String side) {
        if (side.equals("buy")) {
            return orderBook.getBuys();
        } else {
            return orderBook.getSells();
        }
    }

    public void convertFills(List<Fill> result) {
        this.orderFills = result;
    }

    public List<Fill> getFills() {
        return orderFills;
    }
}
