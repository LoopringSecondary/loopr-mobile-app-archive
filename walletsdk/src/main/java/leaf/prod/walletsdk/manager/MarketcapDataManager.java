package leaf.prod.walletsdk.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import leaf.prod.walletsdk.listener.MetadataListener;
import leaf.prod.walletsdk.model.common.Currency;
import leaf.prod.walletsdk.model.market.Market;
import leaf.prod.walletsdk.model.response.relay.MarketsResult;
import leaf.prod.walletsdk.model.response.relay.MetadataResult;
import leaf.prod.walletsdk.model.response.relay.TokensResult;
import leaf.prod.walletsdk.model.token.Token;
import leaf.prod.walletsdk.service.RelayService;
import leaf.prod.walletsdk.util.CurrencyUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MarketcapDataManager {

	private static MarketcapDataManager marketDataManager;

	private static TokenDataManager tokenDataManager;

	private MetadataResult metadataResult;

	private List<Market> markets;

	private Context context;

	private MetadataListener metadataListener = new MetadataListener();

	private RelayService relayService = new RelayService();

	private static Map<String, Observable<MarketsResult>> observableMap = new HashMap<>();

	private MarketcapDataManager(Context context) {
		this.context = context;
	}

	public static MarketcapDataManager getInstance(Context context) {
		if (marketDataManager == null) {
			marketDataManager = new MarketcapDataManager(context);
			tokenDataManager = TokenDataManager.getInstance(context);
		}
		return marketDataManager;
	}

	public Observable<MarketsResult> getObservable() {
		Currency currency = CurrencyUtil.getCurrency(context);
		if (observableMap.get(currency.getText()) == null) {
			observableMap.put(currency.getText(), relayService.getMarkets(true, true, CurrencyUtil.getCurrency(context), null));
		}
		return observableMap.get(currency.getText());
	}

	public void setMarkets(List<Market> markets) {
		for (Market market : markets) {
			market.convert();
		}
		this.markets = markets;
	}

	public MetadataResult getMetadataResult() {
		return metadataResult;
	}

	public List<Market> getMarkets() {
		return markets;
	}

	public void startSocket() {
		metadataListener.start().subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<MetadataResult>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
					}

					@Override
					public void onNext(MetadataResult metadataResult) {
						if (metadataResult.getMetadataChanged() == null)
							return;
						if (metadataResult.getMetadataChanged().isMarketMetadataChanged() ||
								metadataResult.getMetadataChanged().isTickerChanged()) {
							// 更新market信息
							relayService.getMarkets(metadataResult.getMetadataChanged().isMarketMetadataChanged(),
									metadataResult.getMetadataChanged()
											.isTickerChanged(), CurrencyUtil.getCurrency(context), null) // todo pairs
									.observeOn(AndroidSchedulers.mainThread())
									.subscribeOn(Schedulers.io())
									.subscribe(new Subscriber<MarketsResult>() {
										@Override
										public void onCompleted() {
											unsubscribe();
										}

										@Override
										public void onError(Throwable e) {
											unsubscribe();
										}

										@Override
										public void onNext(MarketsResult result) {
											if (result != null) {
												markets = result.getMarkets();
											}
											unsubscribe();
										}
									});
						}
						if (metadataResult.getMetadataChanged().isTokenMetadataChanged() ||
								metadataResult.getMetadataChanged().isTokenInfoChanged()) {
							// 更新token相关信息，不常变
							relayService.getTokens(metadataResult.getMetadataChanged()
									.isTokenMetadataChanged(), metadataResult.getMetadataChanged()
									.isTokenInfoChanged(), true, CurrencyUtil.getCurrency(context), null)
									.observeOn(AndroidSchedulers.mainThread())
									.subscribeOn(Schedulers.io())
									.subscribe(new Subscriber<TokensResult>() {
										@Override
										public void onCompleted() {
											unsubscribe();
										}

										@Override
										public void onError(Throwable e) {
											unsubscribe();
										}

										@Override
										public void onNext(TokensResult tokensResult) {
											if (tokensResult != null) {
												tokenDataManager.setTokens(tokensResult.getTokens());
											}
											unsubscribe();
										}
									});
						}
					}
				});
		metadataListener.send();
	}

	public Double getPriceBySymbol(String symbol) {
		Token token = tokenDataManager.getTokenBySymbol(symbol);
		if (token != null) {
			return token.getTicker().getPrice();
		}
		return 0d;
	}
}
