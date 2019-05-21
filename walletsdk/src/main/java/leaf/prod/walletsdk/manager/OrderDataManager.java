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
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.Erc20TransactionManager;
import leaf.prod.walletsdk.R;
import leaf.prod.walletsdk.Transfer;
import leaf.prod.walletsdk.model.RandomWallet;
import leaf.prod.walletsdk.model.common.TradeType;
import leaf.prod.walletsdk.model.order.Erc1400Params;
import leaf.prod.walletsdk.model.order.FeeParams;
import leaf.prod.walletsdk.model.order.OrderParams;
import leaf.prod.walletsdk.model.order.RawOrder;
import leaf.prod.walletsdk.model.response.RelayError;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.sign.BitStream;
import leaf.prod.walletsdk.service.RelayService;
import leaf.prod.walletsdk.util.SignUtils;
import leaf.prod.walletsdk.util.StringUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import lombok.Getter;
import lombok.Setter;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Getter
@Setter
public abstract class OrderDataManager {

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

    protected RelayService relayService;

    OrderDataManager(Context context) {
        this.context = context;
        this.balanceInfo = new HashMap<>();
        this.relayService = new RelayService();
        this.owner = WalletUtil.getCurrentAddress(context);
        this.gas = GasDataManager.getInstance(context);
        this.token = TokenDataManager.getInstance(context);
        this.balance = BalanceDataManager.getInstance(context);
    }

    public RawOrder constructOrder(Double amountBuy, Double amountSell, Integer validSince, Integer validUntil) {
        RawOrder order = null;
        try {
            String tokenB = token.getTokenBySymbol(tokenBuy).getProtocol();
            String tokenS = token.getTokenBySymbol(tokenSell).getProtocol();
            String tokenFee = token.getTokenBySymbol("LRC").getProtocol();
            String amountB = Numeric.toHexStringWithPrefix(token.getWeiFromDouble(tokenBuy, amountBuy));
            String amountS = Numeric.toHexStringWithPrefix(token.getWeiFromDouble(tokenSell, amountSell));
            String owner = WalletUtil.getCurrentAddress(context);
            RandomWallet randomWallet = WalletUtil.getRandomWallet();
            OrderParams orderParams = OrderParams.builder()
                    .sig("")
                    .broker("")
                    .orderInterceptor("")
                    .allOrNone(false)
                    .validUntil(validUntil)
                    .dualAuthAddr(randomWallet.getAddress())
                    .dualAuthPrivateKey(randomWallet.getPrivateKey())
                    .wallet(PartnerDataManager.getInstance(context).getWalletAddress())
                    .build();
            FeeParams feeParams = FeeParams.builder()
                    .tokenFee(tokenFee)
                    .amountFee(Numeric.toHexStringWithPrefix(BigInteger.ZERO))
                    .tokenRecipient(owner)
                    .tokenSFeePercentage(0)
                    .tokenBFeePercentage(0)
                    .walletSplitPercentage(50)
                    .build();
            Erc1400Params erc1400Params = Erc1400Params.builder().build();
            order = RawOrder.builder()
                    .owner(owner).version(0)
                    .tokenB(tokenB).tokenBuy(tokenBuy)
                    .tokenS(tokenS).tokenSell(tokenSell)
                    .amountB(amountB).amountBuy(amountBuy)
                    .amountS(amountS).amountSell(amountSell)
                    .validSince(validSince)
                    .params(orderParams)
                    .feeParams(feeParams)
                    .erc1400Params(erc1400Params)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public RawOrder signOrder(RawOrder order) {
        String hash = encodeOrder(order);
        String sign = getSignature(hash);
        order.setHash(hash);
        order.getParams().setSig(sign);
        return order;
    }

    private String getSignature(String hash) {
        byte[] data = Numeric.hexStringToByteArray(hash);
        SignatureData sigData = SignUtils.genSignMessage(credentials, data);
        BitStream bitStream = new BitStream();
        bitStream.addNumber(BigInteger.ONE, 1, true);
        bitStream.addNumber(BigInteger.valueOf(32 + 32 + 1), 1, true);
        bitStream.addNumber(BigInteger.valueOf((int) sigData.getV()), 1, true);
        bitStream.addRawBytes(sigData.getR(), true);
        bitStream.addRawBytes(sigData.getS(), true);
        return bitStream.getData();
    }

    private String encodeOrder(RawOrder order) {
        BitStream bitStream = new BitStream();
        FeeParams feeParams = order.getFeeParams();
        OrderParams orderParams = order.getParams();
        Erc1400Params erc1400Params = order.getErc1400Params();
        byte[] transferDataBytes = erc1400Params.getTransferDataS().getBytes();
        String transferDataHash = Numeric.toHexString(Hash.sha3(transferDataBytes));
        bitStream.addBytes32(SignUtils.Eip712OrderSchemaHash, true);
        bitStream.addUint(Numeric.toBigInt(order.getAmountS()), true);
        bitStream.addUint(Numeric.toBigInt(order.getAmountB()), true);
        bitStream.addUint(Numeric.toBigInt(feeParams.getAmountFee()), true);
        bitStream.addUint(BigInteger.valueOf(order.getValidSince()), true);
        bitStream.addUint(BigInteger.valueOf(orderParams.getValidUntil()), true);
        bitStream.addAddress(order.getOwner(), 32, true);
        bitStream.addAddress(order.getTokenS(), 32, true);
        bitStream.addAddress(order.getTokenB(), 32, true);
        bitStream.addAddress(orderParams.getDualAuthAddr(), 32, true);
        bitStream.addAddress(orderParams.getBroker(), 32, true);
        bitStream.addAddress(orderParams.getOrderInterceptor(), 32, true);
        bitStream.addAddress(orderParams.getWallet(), 32, true);
        bitStream.addAddress(feeParams.getTokenRecipient(), 32, true);
        bitStream.addAddress(feeParams.getTokenFee(), true);
        bitStream.addUint(BigInteger.valueOf(feeParams.getWalletSplitPercentage()), true);
        bitStream.addUint(BigInteger.valueOf(feeParams.getTokenSFeePercentage()), true);
        bitStream.addUint(BigInteger.valueOf(feeParams.getTokenBFeePercentage()), true);
        bitStream.addBoolean(orderParams.getAllOrNone(), true);
        bitStream.addUint(BigInteger.valueOf(erc1400Params.getTokenStandardS()), true);
        bitStream.addUint(BigInteger.valueOf(erc1400Params.getTokenStandardB()), true);
        bitStream.addUint(BigInteger.valueOf(erc1400Params.getTokenStandardFee()), true);
        bitStream.addBytes32(erc1400Params.getTrancheS(), true);
        bitStream.addBytes32(erc1400Params.getTrancheB(), true);
        bitStream.addBytes32(transferDataHash, true);
        String orderDataHash = Numeric.toHexString(Hash.sha3(bitStream.getBytes()));
        BitStream outerStream = new BitStream();
        outerStream.addHex(StringUtils.toHex(SignUtils.Eip191Header), true);
        outerStream.addBytes32(SignUtils.Eip712DomainHash, true);
        outerStream.addBytes32(orderDataHash, true);
        return Numeric.toHexString(Hash.sha3(outerStream.getBytes()));
    }

    public TradeType getSide(RawOrder order) {
        MarketPriceDataManager instance = MarketPriceDataManager.getInstance(context);
        return instance.getOrderSide(order.getTokenBuy(), order.getTokenSell());
    }

    public Observable<RelayResponseWrapper> handleInfo() {
        Observable<RelayResponseWrapper> result;
        if (needApprove()) {
            result = approve().observeOn(Schedulers.io())
                    .flatMap((Func1<String, Observable<RelayResponseWrapper>>) hash -> {
                        if (!hash.equals("failed")) {
                            return submit();
                        }
                        RelayError failed = RelayError.builder()
                                .message(context.getString(R.string.approve_failed)).build();
                        RelayResponseWrapper<Object> response = RelayResponseWrapper.builder().error(failed).build();
                        return Observable.just(response);
                    });
        } else {
            result = submit();
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

    private Observable<String> approve() {
        Observable<String> result = Observable.just("failed");
        for (Map.Entry<String, Double> entry : balanceInfo.entrySet()) {
            if (entry.getKey().startsWith("GAS_")) {
                if (entry.getValue() != 1 && entry.getValue() != 2) {
                    continue;
                }
                String token = entry.getKey().split("_")[1];
                if (entry.getValue() == 1) {
                    result = approveOnce(token);
                } else if (entry.getValue() == 2) {
                    result = approveTwice(token);
                }
            }
        }
        return result;
    }

    protected Observable<String> approve(String symbol, Double value) {
        Transfer transfer = new Transfer(credentials);
        String contract = token.getTokenBySymbol(symbol).getProtocol();
        BigInteger amount = token.getWeiFromDouble(symbol, value);
        BigInteger gasPrice = gas.getCustomizeGasPriceInWei().toBigInteger();
        BigInteger gasLimit = gas.getGasLimitByType("approve");
        return transfer.erc20(contract, gasPrice, gasLimit)
                .approve(credentials, contract, Default.DELEGATE_ADDRESS, amount);
    }

    private Observable<String> approveOnce(String symbol) {
        Transfer transfer = new Transfer(credentials);
        String contract = token.getTokenBySymbol(symbol).getProtocol();
        BigInteger value = token.getWeiFromDouble(symbol, (double) Integer.MAX_VALUE);
        BigInteger gasPrice = gas.getCustomizeGasPriceInWei().toBigInteger();
        BigInteger gasLimit = gas.getGasLimitByType("approve");
        return transfer.erc20(contract, gasPrice, gasLimit)
                .approve(credentials, contract, Default.DELEGATE_ADDRESS, value);
    }

    private Observable<String> approveTwice(String symbol) {
        Transfer transfer = new Transfer(credentials);
        String contract = token.getTokenBySymbol(symbol).getProtocol();
        BigInteger value = BigInteger.ZERO;
        BigInteger gasPrice = gas.getCustomizeGasPriceInWei().toBigInteger();
        BigInteger gasLimit = gas.getGasLimitByType("approve");
        final Erc20TransactionManager manager = transfer.erc20(contract, gasPrice, gasLimit);
        return manager.approve(credentials, contract, Default.DELEGATE_ADDRESS, value)
                .observeOn(Schedulers.io())
                .flatMap((Func1<String, Observable<String>>) s -> {
                    BigInteger value1 = token.getWeiFromDouble(symbol, (double) Integer.MAX_VALUE);
                    return manager.approve(credentials, contract, Default.DELEGATE_ADDRESS, value1);
                });
    }

    protected abstract Observable<RelayResponseWrapper> submit();
}
