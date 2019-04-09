/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-17 下午3:14
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;

import leaf.prod.walletsdk.listener.BalanceListener;
import leaf.prod.walletsdk.model.response.relay.AccountBalance;
import leaf.prod.walletsdk.model.response.relay.AccountBalanceWrapper;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.model.token.Token;
import leaf.prod.walletsdk.service.RelayService;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BalanceDataManager {

	private static BalanceDataManager balanceDataManager = null;

	private Context context;

	private BalanceListener balanceListener;

	private List<AccountBalance> accountBalances;

	private static RelayService relayService;

	private static Map<String, Observable<AccountBalanceWrapper>> observableMap = new HashMap<>();

	private BalanceDataManager(Context context) {
		this.context = context;
		this.balanceListener = new BalanceListener();
	}

	public static BalanceDataManager getInstance(Context context) {
		if (balanceDataManager == null) {
			balanceDataManager = new BalanceDataManager(context);
			relayService = new RelayService();
		}
		return balanceDataManager;
	}

	public Observable<AccountBalanceWrapper> getObservable() {
		String address = WalletUtil.getCurrentAddress(context);
		if (observableMap.get(address) == null) {
			observableMap.put(address, relayService.getAccounts(Arrays.asList(WalletUtil.getCurrentAddress(context)), null, true));
		}
		return observableMap.get(address);
	}

	/**
	 * 由mainfragment初始化当前解锁钱包的账户数据
	 *
	 * @param accountBalanceWrapper
	 */
	public void setAccountBalance(AccountBalanceWrapper accountBalanceWrapper) {
		List<AccountBalance> accountBalances = new ArrayList<>();
		Map<String, AccountBalanceWrapper.TokenBalanceMap.TokenBalance> tokenBalanceMap = accountBalanceWrapper.getAccountBalances()
				.get(WalletUtil.getCurrentAddress(context))
				.getTokenBalanceMap();
		for (String tokenAddr : tokenBalanceMap.keySet()) {
			accountBalances.add(new AccountBalance(context, tokenBalanceMap.get(tokenAddr)));
		}
		this.accountBalances = accountBalances;
	}

	/**
	 * 触发更新账户
	 */
	public void startSocket(String address) {
		balanceListener.start().subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<BalanceResult>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
					}

					@Override
					public void onNext(BalanceResult balanceResult) {
						if (accountBalances == null)
							return;
						for (AccountBalance accountBalance : accountBalances) {
							if (accountBalance.getToken().equalsIgnoreCase(balanceResult.getAccount().getAddress())) {
								accountBalance.update(context, balanceResult.getAccount().getTokenBalance());
							}
						}
					}
				});
		balanceListener.queryByOwner(address);
	}

	public List<AccountBalance> getAccountBalances() {
		return accountBalances;
	}

	public AccountBalance getAssetBySymbol(String symbol) {
		for (AccountBalance asset : accountBalances) {
			if (asset.getTokenSymbol().equalsIgnoreCase(symbol)) {
				return asset;
			}
		}
		return null;
	}

	public String getFormattedBySymbol(String symbol, Double value) {
		int precision = getPrecision(symbol);
		return NumberUtils.format1(value, precision);
	}

	public int getPrecisionBySymbol(String symbol) {
		int result = 4;
		for (AccountBalance asset : accountBalances) {
			if (asset.getTokenSymbol().equalsIgnoreCase(symbol)) {
				result = asset.getPrecision();
				break;
			}
		}
		return result;
	}

	public List<Token> getBalanceTokens() {
		List<Token> result = new LinkedList<>();
		for (AccountBalance asset : accountBalances) {
			if (asset.getBalance() != null && asset.getBalanceDouble() != 0) {
				Token token = TokenDataManager.getTokenWithSymbol(asset.getTokenSymbol());
				result.add(token);
			}
		}
		return result;
	}

	public static int getPrecision(String symbol) {
		AccountBalance asset = balanceDataManager.getAssetBySymbol(symbol);
		return asset != null ? asset.getPrecision() : 6;
	}

	public Double getFrozenFee(String symbol) {
		AccountBalance accountBalance = getAssetBySymbol(symbol);
		return accountBalance != null ? accountBalance.getBalanceDouble() - accountBalance.getAvailableBalanceDouble() : 0;
	}

	public Double getAllowanceFee(String symbol) {
		AccountBalance accountBalance = getAssetBySymbol(symbol);
		return accountBalance != null ? accountBalance.getAvailableBalanceDouble() : 0;
	}
}
