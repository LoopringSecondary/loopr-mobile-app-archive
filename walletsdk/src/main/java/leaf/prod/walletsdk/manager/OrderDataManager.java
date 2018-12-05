/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-19 5:39 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.Transfer;
import leaf.prod.walletsdk.model.OrderType;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.RandomWallet;
import leaf.prod.walletsdk.model.SignedBody;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.service.LoopringService;
import leaf.prod.walletsdk.util.SignUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Observable;

@Getter
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

    public OriginOrder constructOrder(Double amountBuy, Double amountSell, Integer validS, Integer validU) {
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
                    .market(tradePair).orderType(OrderType.MARKET)
                    .tokenS(tokenS).tokenSell(tokenSell).tokenB(tokenB).tokenBuy(tokenBuy)
                    .amountS(amountS).amountSell(amountSell).amountB(amountB).amountBuy(amountBuy)
                    .validS(validS).validSince(validSince).validU(validU).validUntil(validUntil)
                    .lrc(0d).lrcFee(Numeric.toHexStringWithPrefix(BigInteger.ZERO))
                    .walletAddress(PartnerDataManager.getInstance(context).getWalletAddress())
                    .authAddr(randomWallet.getAddress())
                    .authPrivateKey(randomWallet.getPrivateKey())
                    .buyNoMoreThanAmountB(false).marginSplitPercentage(50)
                    .powNonce(1)
                    .build();
            order = signOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public OriginOrder signOrder(OriginOrder order) {
        String encoded = encodeOrder(order);
        byte[] hash = Hash.sha3(Numeric.hexStringToByteArray(encoded));
        SignedBody signedBody = SignUtils.genSignMessage(credentials, hash);
        String r = Numeric.toHexStringNoPrefix(signedBody.getSig().getR());
        String s = Numeric.toHexStringNoPrefix(signedBody.getSig().getS());
        Integer v = (int) signedBody.getSig().getV();
        order.setHash(signedBody.getHash());
        order.setR(r);
        order.setS(s);
        order.setV(v);
        return order;
    }

    private String encodeOrder(OriginOrder order) {
        List<? extends Type<? extends Serializable>> types = Arrays.asList(
                new Uint256(Numeric.toBigInt(order.getAmountS())),
                new Uint256(Numeric.toBigInt(order.getAmountB())),
                new Uint256(Numeric.toBigInt(order.getValidSince())),
                new Uint256(Numeric.toBigInt(order.getValidUntil())),
                new Uint256(Numeric.toBigInt(order.getLrcFee()))
        );
        String data = Numeric.cleanHexPrefix(order.getDelegate());
        data += Numeric.cleanHexPrefix(order.getOwner());
        data += Numeric.cleanHexPrefix(order.getTokenS());
        data += Numeric.cleanHexPrefix(order.getTokenB());
        data += Numeric.cleanHexPrefix(order.getWalletAddress());
        data += Numeric.cleanHexPrefix(order.getAuthAddr());
        for (Type<? extends Serializable> type : types) {
            data += TypeEncoder.encode(type);
        }
        data += order.getBuyNoMoreThanAmountB() ? "01" : "00";
        data += Numeric.toHexStringNoPrefix(BigInteger.valueOf(order.getMarginSplitPercentage()));
        return data;
    }

    protected Double getLRCFrozenFromServer() {
        String valueInWei = loopringService.getFrozenLRCFee(owner)
                .subscribeOn(Schedulers.io()).toBlocking().single();
        return token.getDoubleFromWei("LRC", valueInWei);
    }

    protected Double getAllowanceFromServer(String symbol) {
        String valueInWei = loopringService.getEstimatedAllocatedAllowance(owner, symbol)
                .subscribeOn(Schedulers.io()).toBlocking().single();
        return token.getDoubleFromWei(symbol, valueInWei);
    }

    public Observable<RelayResponseWrapper> handleInfo() {
        Observable<RelayResponseWrapper> result = null;
        try {
            if (needApprove()) {
                approve();
            }
            result = submit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isBalanceEnough() {
        for (String s : balanceInfo.keySet()) {
            if (s.startsWith("MINUS_")) {
                return false;
            }
        }
        return true;
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

    protected Observable<RelayResponseWrapper> submit() {
        return null;
    }

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
