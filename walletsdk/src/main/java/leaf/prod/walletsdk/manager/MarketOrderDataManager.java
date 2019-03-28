/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 3:47 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.math.BigInteger;
import java.util.Map;

import android.content.Context;

import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.model.common.TradeType;
import leaf.prod.walletsdk.model.order.OrderType;
import leaf.prod.walletsdk.model.order.RawOrder;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Observable;

public class MarketOrderDataManager extends OrderDataManager {

    private TradeType type;

    private RawOrder order;

    private String priceFromDepth;

    private static MarketOrderDataManager marketOrderManager = null;

    private MarketOrderDataManager(Context context) {
        super(context);
        this.type = TradeType.buy;
    }

    public static MarketOrderDataManager getInstance(Context context) {
        if (marketOrderManager == null) {
            marketOrderManager = new MarketOrderDataManager(context);
        }
        return marketOrderManager;
    }

    public void setType(TradeType type) {
        this.type = type;
    }

    public TradeType getType() {
        return this.type;
    }

    public RawOrder getOrder() {
        return order;
    }

    public String getTokenA() {
        return this.tokenSell;
    }

    public String getTokenB() {
        return this.tokenBuy;
    }

    public String getTokenSell() {
        return this.type == TradeType.buy ? tokenBuy : tokenSell;
    }

    public String getTokenBuy() {
        return this.type == TradeType.buy ? tokenSell : tokenBuy;
    }

    public String getTradePair() {
        return this.tokenSell + "-" + this.tokenBuy;
    }

    public String getPriceFromDepth() {
        return priceFromDepth;
    }

    public void setPriceFromDepth(String priceFromDepth) {
        this.priceFromDepth = priceFromDepth;
    }

    public RawOrder constructOrder(Double amountBuy, Double amountSell, Integer validS, Integer validU) {
        RawOrder originOrder = super.constructOrder(amountBuy, amountSell, validS, validU);
        originOrder.setSide(type.name());
        originOrder.setOrderType(OrderType.MARKET);
        Double lrcFee = calculateLrcFee(originOrder);
        String lrcFeeHex = calculateLrcFeeString(lrcFee);
        originOrder.setLrc(lrcFee);
        originOrder.setLrcFee(lrcFeeHex);
        this.order = originOrder;
        return originOrder;
    }

    private Double calculateLrcFee(RawOrder order) {
        GasDataManager gasManager = GasDataManager.getInstance(context);
        SettingDataManager settingManager = SettingDataManager.getInstance(context);
        MarketcapDataManager marketManager = MarketcapDataManager.getInstance(context);
        Double priceLRC = marketManager.getPriceBySymbol("LRC");
        Double priceETH = marketManager.getPriceBySymbol("ETH");
        Double priceTokenS = marketManager.getPriceBySymbol(order.getTokenS());
        Double lrcFeeMin = gasManager.getGasAmountInETH("eth_transfer") * priceETH;
        Double lrcFeeCalc = priceTokenS * order.getAmountSell() * settingManager.getLrcFeeFloat();
        Double lrcFee = lrcFeeMin > lrcFeeCalc ? lrcFeeMin : lrcFeeCalc;
        return lrcFee / priceLRC;
    }

    private String calculateLrcFeeString(Double lrcFee) {
        BigInteger valueInWei = TokenDataManager.getInstance(context).getWeiFromDouble("LRC", lrcFee);
        return Numeric.toHexStringWithPrefix(valueInWei);
    }

    private void checkLRCEnough(RawOrder order) {
        Double lrcFrozen = getLRCFrozenFromServer();
        Double lrcBalance = token.getDoubleFromWei("LRC", balance.getAssetBySymbol("LRC").getBalance());
        Double result = lrcBalance - order.getLrc() - lrcFrozen;
        if (result < 0) {
            balanceInfo.put("MINUS_LRC", -result);
        }
    }

    private void checkGasEnough(RawOrder order, Boolean includingLRC) {
        Double result;
        Double ethBalance = balance.getAssetBySymbol("ETH").getBalance().doubleValue();
        Double tokenGas = calculateGas(order.getTokenS(), order.getAmountSell(), order.getLrc());
        if (includingLRC) {
            Double lrcGas = calculateGas("LRC", order.getAmountSell(), order.getLrc());
            result = ethBalance - lrcGas - tokenGas;
        } else {
            result = ethBalance - tokenGas;
        }
        if (result < 0) {
            balanceInfo.put("MINUS_ETH", -result);
        }
    }

    private void checkLRCGasEnough(RawOrder order) {
        Double ethBalance = token.getDoubleFromWei("ETH", balance.getAssetBySymbol("ETH").getBalance());
        Double lrcGas = calculateGasForLRC(order);
        Double result = ethBalance - lrcGas;
        if (result < 0) {
            balanceInfo.put("MINUS_ETH", -result);
        }
    }

    private Double calculateGas(String symbol, Double amount, Double lrcFee) {
        Double result;
        BalanceResult.Asset asset = balance.getAssetBySymbol(symbol);
        Double allowance = token.getDoubleFromWei(asset.getSymbol(), asset.getAllowance());
        if (symbol.equalsIgnoreCase("LRC")) {
            Double lrcFrozen = getLRCFrozenFromServer();
            Double sellingFrozen = getAllowanceFromServer("LRC");
            if (allowance >= lrcFee + lrcFrozen + sellingFrozen) {
                return 0d;
            }
        } else {
            Double tokenFrozen = getAllowanceFromServer(symbol);
            if (allowance >= amount + tokenFrozen) {
                return 0d;
            }
        }
        Double gasAmount = gas.getGasAmountInETH("approve");
        String key = String.format("GAS_%s", asset.getSymbol());
        if (allowance == 0) {
            result = gasAmount;
            balanceInfo.put(key, 1d);
        } else {
            result = gasAmount * 2;
            balanceInfo.put(key, 2d);
        }
        return result;
    }

    private Double calculateGasForLRC(RawOrder order) {
        Double result;
        BalanceResult.Asset asset = balance.getAssetBySymbol("LRC");
        Double allowance = token.getDoubleFromWei("LRC", asset.getAllowance());
        Double lrcFrozen = getLRCFrozenFromServer();
        Double sellingFrozen = getAllowanceFromServer("LRC");
        if (order.getLrc() + lrcFrozen + sellingFrozen + order.getAmountSell() > allowance) {
            Double gasAmount = gas.getGasAmountInETH("approve");
            if (allowance == 0) {
                result = gasAmount;
                balanceInfo.put("GAS_LRC", 1d);
            } else {
                result = gasAmount * 2;
                balanceInfo.put("GAS_LRC", 2d);
            }
        } else {
            result = 0d;
        }
        return result;
    }

    private void completeOrder(String password) throws Exception {
        this.credentials = WalletUtil.getCredential(context, password);
        this.order = signOrder(order);
    }

    /*
     1. LRC FEE 比较的是当前订单lrc fee + getFrozenLrcfee() <> 账户lrc 余额 不够失败
     2. 如果够了，看lrc授权够不够，够则成功，如果不够需要授权是否等于=0，如果不是，先授权lrc = 0， 再授权lrc = max，
        是则直接授权lrc = max。看两笔授权支付的eth gas够不够，如果eth够则两次授权，不够失败
     3. 比较当前订单amounts + loopring_getEstimatedAllocatedAllowance() <> 账户授权tokens，够则成功，
        不够则看两笔授权支付的eth gas够不够，如果eth够则两次授权，不够失败。如果是sell lrc，
        需要lrc fee + getFrozenLrcfee() + amounts(lrc) + loopring_getEstimatedAllocatedAllowance() <> 账户授权lrc
     4. buy lrc不看前两点，只要3满足即可
     */
    public Map verify(String password) throws Exception {
        balanceInfo.clear();
        completeOrder(password);
        if (order.getSide().equalsIgnoreCase("buy")) {
            if (order.getTokenB().equalsIgnoreCase("LRC")) {
                checkGasEnough(order, false);
            } else {
                checkLRCEnough(order);
                checkGasEnough(order, true);
            }
        } else {
            if (order.getTokenS().equalsIgnoreCase("LRC")) {
                checkLRCEnough(order);
                checkLRCGasEnough(order);
            } else {
                checkLRCEnough(order);
                checkGasEnough(order, true);
            }
        }
        return balanceInfo;
    }

    @Override
    protected Observable<RelayResponseWrapper> submit() {
        Observable<RelayResponseWrapper> result = null;
        if (order != null) {
            result = relayService.submitOrder(order);
        }
        return result;
    }
}
