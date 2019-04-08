/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-13 下午1:55
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import org.web3j.utils.Convert;

import leaf.prod.walletsdk.model.common.Currency;
import leaf.prod.walletsdk.model.response.relay.TokensResult;
import leaf.prod.walletsdk.model.token.Token;
import leaf.prod.walletsdk.service.RelayService;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Observable;

public class TokenDataManager {

	private static TokenDataManager tokenDataManager = null;

	private List<Token> tokens;

	private Context context;

	private static Map<String, Observable<TokensResult>> observableMap = new HashMap<>();

	private RelayService relayService = new RelayService();

	private TokenDataManager(Context context) {
		this.context = context;
	}

	public static TokenDataManager getInstance(Context context) {
		if (tokenDataManager == null) {
			tokenDataManager = new TokenDataManager(context);
		}
		return tokenDataManager;
	}

	public Observable<TokensResult> getObservable() {
		Currency currency = CurrencyUtil.getCurrency(context);
		if (observableMap.get(currency.getText()) == null) {
			observableMap.put(currency.getText(), relayService.getTokens(true, true, true, CurrencyUtil.getCurrency(context), null));
		}
		return observableMap.get(currency.getText());
	}

	public List<Token> getTokens() {
		return this.tokens;
	}

	public void setTokens(List<Token> tokens) {
		List<String> chooseList = WalletUtil.getChooseTokens(context);
		List<Token> tTokens = new ArrayList<>();
		Map<String, Token> map = new HashMap<>();
		for (Token token : tokens) {
			String image = String.format("icon_token_%s", token.getSymbol().toLowerCase());
			int identifier = context.getResources().getIdentifier(image, "mipmap", context.getPackageName());
			token.setImageResId(identifier);
			map.put(token.getSymbol(), token);
		}
		Collections.sort(this.tokens, (t1, t2) -> t1.getSymbol().compareTo(t2.getSymbol()));
		tTokens.add(map.get("ETH"));
		tTokens.add(map.get("WETH"));
		tTokens.add(map.get("LRC"));
		if (chooseList != null) {
			Collections.sort(chooseList, String::compareTo);
			for (String symbol : chooseList) {
				if (!tTokens.contains(map.get(symbol))) {
					tTokens.add(map.get(symbol));
				}
			}
		}
		for (Token token : this.tokens) {
			if (!tTokens.contains(token)) {
				tTokens.add(token);
			}
		}
		this.tokens = tTokens;
	}

	public Token getTokenBySymbol(String symbol) {
		Token result = null;
		for (Token token : this.tokens) {
			if (token.getSymbol().equalsIgnoreCase(symbol)) {
				result = token;
				break;
			}
		}
		return result;
	}

	public Token getTokenByProtocol(String protocol) {
		Token result = null;
		for (Token token : this.tokens) {
			if (token.getProtocol().equalsIgnoreCase(protocol)) {
				result = token;
				break;
			}
		}
		return result;
	}

	public Double getDoubleFromWei(String symbol, BigDecimal valueInWei) {
		Double result = null;
		Token token = getTokenBySymbol(symbol);
		if (token != null) {
			BigDecimal decimals = token.getDecimals();
			result = valueInWei.divide(decimals).doubleValue();
		}
		return result;
	}

	public Double getDoubleFromWei(String symbol, String valueInWei) {
		try {
			BigDecimal bigDecimal = Convert.toWei(valueInWei, Convert.Unit.WEI);
			return getDoubleFromWei(symbol, bigDecimal);
		} catch (Exception e) {
			return 0d;
		}
	}

	public BigInteger getWeiFromDouble(String symbol, String doubleString) {
		Token token = getTokenBySymbol(symbol);
		return new BigDecimal(doubleString).multiply(token.getDecimals()).toBigInteger();
	}

	public BigInteger getWeiFromDouble(String symbol, Double doubleValue) {
		Token token = getTokenBySymbol(symbol);
		return new BigDecimal(doubleValue).multiply(token.getDecimals()).toBigInteger();
	}

	public static Token getTokenWithSymbol(String symbol) {
		return tokenDataManager.getTokenBySymbol(symbol);
	}

	public static Token getTokenWithProtocol(String protocol) {
		return tokenDataManager.getTokenByProtocol(protocol);
	}

	public static Double getDouble(String symbol, String valueInWei) {
		return tokenDataManager.getDoubleFromWei(symbol, valueInWei);
	}
}
