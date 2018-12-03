/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-19 5:39 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.Transfer;
import leaf.prod.walletsdk.model.OrderType;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.P2PType;
import leaf.prod.walletsdk.model.RandomWallet;
import leaf.prod.walletsdk.model.SignedBody;
import leaf.prod.walletsdk.service.LoopringService;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.SignUtils;
import leaf.prod.walletsdk.util.WalletUtil;

public class OrderDataManager {

    protected String owner;

    // token symbol, e.g. weth
    protected String tokenSell;

    // token symbol, e.g. lrc
    protected String tokenBuy;

    // e.g. lrc-weth
    protected String tradePair;

    protected Context context;

    protected GasDataManager gas;

    protected Credentials credentials;

    protected TokenDataManager token;

    protected BalanceDataManager balance;

    protected Map<String, Double> balanceInfo;

    protected LoopringService loopringService;

    OrderDataManager(Context context) {
        this.context = context;
        this.balanceInfo = new HashMap<>();
        this.loopringService = new LoopringService();
        this.owner = WalletUtil.getCurrentAddress(context);
        this.gas = GasDataManager.getInstance(context);
        this.token = TokenDataManager.getInstance(context);
        this.balance = BalanceDataManager.getInstance(context);
    }

    public OriginOrder constructOrder(Credentials credentials, Double amountBuy, Double amountSell, Integer validS, Integer validU) {
        OriginOrder order = null;
        try {
            String tokenB = token.getTokenBySymbol(tokenBuy).getProtocol();
            String tokenS = token.getTokenBySymbol(tokenSell).getProtocol();
            String amountB = Numeric.toHexStringWithPrefix(token.getWeiFromDouble(tokenBuy, amountBuy));
            String amountS = Numeric.toHexStringWithPrefix(token.getWeiFromDouble(tokenSell, amountSell));
            String validSince = Numeric.toHexStringWithPrefix(BigInteger.valueOf(validS));
            String validUntil = Numeric.toHexStringWithPrefix(BigInteger.valueOf(validU));
            RandomWallet randomWallet = WalletUtil.getRandomWallet(context);

            order = OriginOrder.builder()
                    .delegate(Default.DELEGATE_ADDRESS)
                    .owner(WalletUtil.getCurrentAddress(context))
                    .side("buy").market(tradePair)
                    .tokenS(tokenS).tokenSell(tokenSell).tokenB(tokenB).tokenBuy(tokenBuy)
                    .amountS(amountS).amountSell(amountSell).amountB(amountB).amountBuy(amountBuy)
                    .validS(validS).validSince(validSince).validU(validU).validUntil(validUntil)
                    .lrc(0d).lrcFee(Numeric.toHexStringWithPrefix(BigInteger.ZERO))
                    .walletAddress(PartnerDataManager.getInstance(context).getWalletAddress())
                    .authAddr(randomWallet.getAddress())
                    .authPrivateKey(randomWallet.getPrivateKey())
                    .buyNoMoreThanAmountB(false).marginSplitPercentage(50)
                    .orderType(OrderType.P2P).p2pType(P2PType.MAKER).powNonce(1)
                    .build();
            order = signOrder(credentials, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public OriginOrder signOrder(Credentials credentials, OriginOrder order) {
        byte[] encoded = encodeOrder(order);
        SignedBody signedBody = SignUtils.genSignMessage(credentials, encoded);
        String r = Numeric.toHexStringNoPrefix(signedBody.getSig().getR());
        String s = Numeric.toHexStringNoPrefix(signedBody.getSig().getS());
        Integer v = (int) signedBody.getSig().getV();
        order.setHash(signedBody.getHash());
        order.setR(r);
        order.setS(s);
        order.setV(v);
        return order;
    }

    private byte[] encodeOrder(OriginOrder order) {
        byte[] array = Numeric.hexStringToByteArray(order.getDelegate());
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getOwner()));
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getOwner()));
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getTokenS()));
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getTokenB()));
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getWalletAddress()));
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getAuthAddr()));
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getAmountS()));
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getAmountB()));
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getValidSince()));
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getValidUntil()));
        array = NumberUtils.append(array, Numeric.hexStringToByteArray(order.getLrcFee()));
        byte[] temp = order.getBuyNoMoreThanAmountB() ? new byte[]{1} : new byte[]{0};
        array = NumberUtils.append(array, temp);
        temp = new byte[]{order.getMarginSplitPercentage().byteValue()};
        array = NumberUtils.append(array, temp);
        return array;
    }

    protected Double getLRCFrozenFromServer() {
        String valueInWei = loopringService.getFrozenLRCFee(owner).toBlocking().single();
        return token.getDoubleFromWei("LRC", valueInWei);
    }

    protected Double getAllowanceFromServer(String symbol) {
        String valueInWei = loopringService.getEstimatedAllocatedAllowance(owner, symbol).toBlocking().single();
        return token.getDoubleFromWei(symbol, valueInWei);
    }

    public void handleInfo() throws Exception {
        if (isBalanceEnough()) {
            if (needApprove()) {
                approve();
            } else {
                submit();
            }
        } else {
            return;
        }
    }

    private boolean isBalanceEnough() {
        for (String s : balanceInfo.keySet()) {
            if (s.startsWith("MINUS_")) {
                return true;
            }
        }
        return false;
    }

    private boolean needApprove() {
        for (String s : balanceInfo.keySet()) {
            if (s.startsWith("GAS_")) {
                return true;
            }
        }
        return false;
    }

    private void approve() throws Exception {
        for (Map.Entry<String, Double> entry : balanceInfo.entrySet()) {
            if (entry.getKey().startsWith("GAS_")) {
                if (entry.getValue() != 1 && entry.getValue() != 2) {
                    return;
                }
                String token = entry.getKey().split("_")[1];
                if (entry.getValue() == 1) {
                    approveOnce(token);
                } else if (entry.getValue() == 2) {
                    approveTwice(token);
                }
            }
        }
    }

    private void submit() {}

    private void approveOnce(String symbol) throws Exception {
        Transfer transfer = new Transfer(credentials);
        String contract = token.getTokenBySymbol(symbol).getProtocol();
        BigInteger value = token.getWeiFromDouble(symbol, (double) Integer.MAX_VALUE);
        BigInteger gasPrice = gas.getCustomizeGasPriceInWei().toBigInteger();
        BigInteger gasLimit = gas.getGasLimitByType("approve");
        transfer.erc20(contract, gasPrice, gasLimit)
                .approve(credentials, contract, Default.DELEGATE_ADDRESS, value);
    }

    private void approveTwice(String symbol) throws Exception {
        Transfer transfer = new Transfer(credentials);
        String contract = token.getTokenBySymbol(symbol).getProtocol();
        BigInteger value = BigInteger.ZERO;
        BigInteger gasPrice = gas.getCustomizeGasPriceInWei().toBigInteger();
        BigInteger gasLimit = gas.getGasLimitByType("approve");
        transfer.erc20(contract, gasPrice, gasLimit)
                .approve(credentials, contract, Default.DELEGATE_ADDRESS, value);
        value = token.getWeiFromDouble(symbol, (double) Integer.MAX_VALUE);
        transfer.erc20(contract, gasPrice, gasLimit)
                .approve(credentials, contract, Default.DELEGATE_ADDRESS, value);
    }
}
