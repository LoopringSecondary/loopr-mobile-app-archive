/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-10 下午4:16
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter.wallet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import leaf.prod.app.fragment.wallet.MainWalletFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.MarketcapDataManager;
import leaf.prod.walletsdk.manager.PartnerDataManager;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.response.relay.AccountBalance;
import leaf.prod.walletsdk.model.response.relay.AccountBalanceWrapper;
import leaf.prod.walletsdk.model.response.relay.MarketsResult;
import leaf.prod.walletsdk.model.response.relay.TokensResult;
import leaf.prod.walletsdk.model.wallet.WalletEntity;
import leaf.prod.walletsdk.service.RelayService;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainFragmentPresenter extends BasePresenter<MainWalletFragment> {

	private static RelayService relayService;

	private Map<String, AccountBalance> tokenMap = new HashMap<>();

	private TokenDataManager tokenDataManager;

	private MarketcapDataManager marketcapDataManager;

	private BalanceDataManager balanceDataManager;

	private PartnerDataManager partnerDataManager;

	private List<AccountBalance> listAsset = new ArrayList<>(); //  返回的token列表

	private String moneyValue;

	private String address;

	public MainFragmentPresenter(MainWalletFragment view, Context context) {
		super(view, context);
		marketcapDataManager = MarketcapDataManager.getInstance(context);
		tokenDataManager = TokenDataManager.getInstance(context);
		balanceDataManager = BalanceDataManager.getInstance(context);
		partnerDataManager = PartnerDataManager.getInstance(context);
		partnerDataManager.activatePartner();
		partnerDataManager.createPartner();
		relayService = new RelayService();
		address = WalletUtil.getCurrentAddress(context);
	}

	/**
	 * 初始化当前解锁钱包的token、balance和marketcap信息
	 */
	public void initObservable() {
		LyqbLogger.log("initObservable: " + address);
		if (relayService == null)
			relayService = new RelayService();
		tokenDataManager.getObservable()
				.flatMap((Func1<TokensResult, Observable<AccountBalanceWrapper>>) tokensResult -> {
					tokenDataManager.setTokens(tokensResult.getTokens());
					return balanceDataManager.getObservable();
				})
				.flatMap((Func1<AccountBalanceWrapper, Observable<MarketsResult>>) balance -> {
					balanceDataManager.setAccountBalance(balance);
					balanceDataManager.startSocket(address);
					return marketcapDataManager.getObservable();
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
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
						marketcapDataManager.setMarkets(result.getMarkets());
						marketcapDataManager.startSocket();
						setTokenLegalPrice();
						unsubscribe();
					}
				});
	}

	/**
	 * 刷新获得最新的balance和marketcap数据并且显示
	 */
	public void initPushService() {
		marketcapDataManager.getObservable()
				.flatMap((Func1<MarketsResult, Observable<AccountBalanceWrapper>>) result -> {
					marketcapDataManager.setMarkets(result.getMarkets());
					return balanceDataManager.getObservable();
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<AccountBalanceWrapper>() {
					@Override
					public void onCompleted() {
						unsubscribe();
					}

					@Override
					public void onError(Throwable e) {
						unsubscribe();
					}

					@Override
					public void onNext(AccountBalanceWrapper accountBalanceWrapper) {
						balanceDataManager.setAccountBalance(accountBalanceWrapper);
						setTokenLegalPrice();
						unsubscribe();
						view.finishRefresh();
					}
				});
	}

	private void setTokenLegalPrice() {
		for (AccountBalance accountBalance : balanceDataManager.getAccountBalances()) {
			tokenMap.put(accountBalance.getToken(), accountBalance);
		}
		Collections.sort(balanceDataManager.getAccountBalances(), (o1, o2) -> Double.compare(o2.getLegalValue(), o1.getLegalValue()));
		List<AccountBalance> listChooseAsset = new ArrayList<>(), positiveList = new ArrayList<>(), zeroList = new ArrayList<>();
		List<String> listChooseSymbol = WalletUtil.getChooseTokens(context);
		double amount = 0;
		for (String symbol : listChooseSymbol) {
			listChooseAsset.add(tokenMap.get(symbol));
		}
		for (AccountBalance asset : balanceDataManager.getAccountBalances()) {
			if (!listChooseSymbol.contains(asset.getToken()) && asset.getLegalValue() != 0) {
				listChooseAsset.add(asset);
			}
		}
		// 根据金额拆分列表
		for (AccountBalance asset : listChooseAsset) {
			if (Arrays.asList("ETH", "WETH", "LRC").contains(asset.getToken()))
				continue;
			if (asset.getBalanceDouble() > 0) {
				positiveList.add(asset);
			} else {
				zeroList.add(asset);
			}
		}
		Collections.sort(positiveList, (asset, t1) -> asset.getTokenSymbol().compareTo(t1.getTokenSymbol()));
		Collections.sort(zeroList, (asset, t1) -> asset.getTokenSymbol().compareTo(t1.getTokenSymbol()));
		listChooseAsset.clear();
		for (String symbol : Arrays.asList("ETH", "WETH", "LRC")) {
			AccountBalance asset = tokenMap.get(symbol);
			listChooseAsset.add(asset);
			amount += asset.getLegalValue();
		}
		for (AccountBalance asset : positiveList) {
			listChooseAsset.add(asset);
			amount += asset.getLegalValue();
		}
		for (AccountBalance asset : zeroList) {
			listChooseAsset.add(asset);
			amount += asset.getLegalValue();
		}
		moneyValue = CurrencyUtil.format(context, amount);
		SPUtils.put(this.context, "amount", moneyValue);
		SPUtils.put(this.context, "amountValue", amount + "");
		listAsset = listChooseAsset;
		view.getmAdapter().setNewData(listChooseAsset);
		view.setWalletCount(moneyValue);
		view.getmAdapter().notifyDataSetChanged();
		/**
		 * 更新钱包信息
		 */
		WalletEntity myWallet = WalletUtil.getCurrentWallet(context);
		myWallet.setAmount(amount);
		myWallet.setAmountShow(moneyValue);
		WalletUtil.updateWallet(context, myWallet);
	}

	public List<AccountBalance> getListAsset() {
		return listAsset;
	}

	public String getAddress() {
		if (address == null) {
			WalletEntity wallet = WalletUtil.getCurrentWallet(context);
			return wallet != null ? wallet.getAddress() : "";
		}
		return address;
	}

	public String getWalletName() {
		WalletEntity walletEntity = WalletUtil.getCurrentWallet(context);
		if (walletEntity == null)
			return null;
		return walletEntity.getWalletname();
	}

	public String getMoneyValue() {
		return moneyValue;
	}
}
