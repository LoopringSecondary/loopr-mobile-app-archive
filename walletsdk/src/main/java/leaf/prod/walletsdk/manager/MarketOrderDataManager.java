/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 3:47 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.util.List;
import java.util.Map;

import android.content.Context;

import leaf.prod.walletsdk.model.Order;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.TradeType;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;

public class MarketOrderDataManager extends OrderDataManager {

    private TradeType type;

    private List<Order> orders;

    private static MarketOrderDataManager marketOrderManager = null;

    private MarketOrderDataManager(Context context) {
        super(context);
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

    public String getTokenS() {
        return this.type == TradeType.buy ? tokenS : tokenB;
    }

    public String getTokenB() {
        return this.type == TradeType.buy ? tokenB : tokenS;
    }

    private void checkLRCEnough(OriginOrder order) {
        Double lrcFrozen = getLRCFrozenFromServer();
        Double lrcBalance = token.getDoubleFromWei("LRC", balance.getAssetBySymbol("LRC").getBalance());
        Double result = lrcBalance - order.getLrc() - lrcFrozen;
        if (result < 0) {
            balanceInfo.put("MINUS_LRC", -result);
        }
    }

    private void checkGasEnough(OriginOrder order, Boolean includingLRC) {
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

    private void checkLRCGasEnough(OriginOrder order) {
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

    private Double calculateGasForLRC(OriginOrder order) {
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
            return 0d;
        }
        return result;
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
    public Map verify(OriginOrder order) {
        balanceInfo.clear();
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

}
