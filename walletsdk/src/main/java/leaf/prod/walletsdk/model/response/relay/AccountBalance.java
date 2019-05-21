package leaf.prod.walletsdk.model.response.relay;

import android.content.Context;

import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.token.Token;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.StringUtils;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-04-09 3:38 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class AccountBalance {

	private String token;

	private String tokenSymbol;

	private String balance;

	private double balanceDouble;

	private String valueShow;

	private double legalValue;

	private String legalShown;

	private String allowance;

	private double allowanceDouble;

	private String availableBalance;

	private double availableBalanceDouble;

	private String availableAllowance;

	private double availableAllowanceDouble;

	private int precision;

	public AccountBalance() {
	}

	public AccountBalance(Context context, AccountBalanceWrapper.TokenBalanceMap.TokenBalance tokenBalance) {
		this.token = tokenBalance.getToken();
		Token tokenBean = TokenDataManager.getTokenWithProtocol(token);
		// todo this.tokenSymbol =
		this.balance = tokenBalance.getBalance();
		this.balanceDouble = !StringUtils.isEmpty(balance) ? Numeric.toBigInt(balance).doubleValue() : 0;
		this.precision = NumberUtils.precision(tokenBean.getTicker().getPrice());
		this.valueShow = NumberUtils.format1(balanceDouble, precision);
		this.legalValue = tokenBean.getTicker().getPrice() * balanceDouble;
		this.allowance = tokenBalance.getAllowance();
		this.legalShown = CurrencyUtil.format(context, legalValue);
		this.allowanceDouble = !StringUtils.isEmpty(allowance) ? Numeric.toBigInt(allowance).doubleValue() : 0;
		this.availableBalance = tokenBalance.getAvailableBalance();
		this.availableBalanceDouble = !StringUtils.isEmpty(availableBalance) ? Numeric.toBigInt(availableBalance)
				.doubleValue() : 0;
		this.availableAllowance = tokenBalance.getAvailableAllowance();
		this.availableAllowanceDouble = !StringUtils.isEmpty(availableAllowance) ? Numeric.toBigInt(availableAllowance)
				.doubleValue() : 0;
	}

	public void update(Context context, BalanceResult.Account.TokenBalance tokenBalance) {
		Token tokenBean = TokenDataManager.getTokenWithProtocol(token);
		this.balance = tokenBalance.getBalance();
		this.balanceDouble = !StringUtils.isEmpty(balance) ? Numeric.toBigInt(balance).doubleValue() : 0;
		this.valueShow = NumberUtils.format1(balanceDouble, NumberUtils.precision(tokenBean.getTicker().getPrice()));
		this.legalValue = tokenBean.getTicker().getPrice() * balanceDouble;
		this.legalShown = CurrencyUtil.format(context, legalValue);
		this.allowance = tokenBalance.getAllowance();
		this.allowanceDouble = !StringUtils.isEmpty(allowance) ? Numeric.toBigInt(allowance).doubleValue() : 0;
	}
}
