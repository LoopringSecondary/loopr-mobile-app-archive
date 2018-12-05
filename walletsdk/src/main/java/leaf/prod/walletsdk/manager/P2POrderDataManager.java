/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 3:47 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;
import com.google.gson.JsonObject;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.R;
import leaf.prod.walletsdk.Transfer;
import leaf.prod.walletsdk.model.Order;
import leaf.prod.walletsdk.model.OrderType;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.P2PSide;
import leaf.prod.walletsdk.model.TradeType;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.walletsdk.util.SignUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import lombok.Getter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Getter
public class P2POrderDataManager extends OrderDataManager {

    private static P2POrderDataManager p2pOrderManager = null;

    private final static String QRCODE_HASH = "hash";

    private final static String QRCODE_AUTH = "auth";

    private final static String SELL_COUNT = "count";

    private Map<String, String> errorMessage;

    private String makerHash;

    private String makerPrivateKey;

    private OriginOrder[] orders;

    private Sign.SignatureData makerSignature;

    private Sign.SignatureData takerSignature;

    public BigInteger sellCount = BigInteger.valueOf(1);

    private int orderCount = 2;

    public boolean isTaker = false;

    private P2POrderDataManager(Context context) {
        super(context);
        this.tokenS = (String) SPUtils.get(context, "tokenS", "WETH");
        this.tokenB = (String) SPUtils.get(context, "tokenB", "LRC");
        this.updatePair();
        this.setupErrorMessage();
    }

    public static P2POrderDataManager getInstance(Context context) {
        if (p2pOrderManager == null) {
            p2pOrderManager = new P2POrderDataManager(context);
        }
        return p2pOrderManager;
    }

    private void setupErrorMessage() {
        this.errorMessage = new HashMap<>();
        errorMessage.put("10001", context.getString(R.string.R10001));
        errorMessage.put("50001", context.getString(R.string.R50001));
        errorMessage.put("50002", context.getString(R.string.R50002));
        errorMessage.put("50003", context.getString(R.string.R50003));
        errorMessage.put("50004", context.getString(R.string.R50004));
        errorMessage.put("50005", context.getString(R.string.R50005));
        errorMessage.put("50006", context.getString(R.string.R50006));
        errorMessage.put("50007", context.getString(R.string.R50007));
        errorMessage.put("50008", context.getString(R.string.R50008));
    }

    private void updatePair() {
        this.tradePair = String.format("%s-%s", this.tokenS, this.tokenB);
    }

    // use for pressing switch button in p2p trade activity
    public void swapToken() {
        String temp = this.tokenB;
        changeToTokenB(this.tokenS);
        changeToTokenS(temp);
    }

    // use for pressing switch tokenS in p2p trade activity
    public void changeToTokenS(String symbol) {
        this.tokenS = symbol;
        SPUtils.put(context, "tokenS", symbol);
        updatePair();
    }

    // use for pressing switch tokenB in p2p trade activity
    public void changeToTokenB(String symbol) {
        this.tokenB = symbol;
        SPUtils.put(context, "tokenB", symbol);
        updatePair();
    }

    public void handleResult(JsonObject scanning) {
        this.makerHash = scanning.get(QRCODE_HASH).getAsString();
        this.sellCount = scanning.get(SELL_COUNT).getAsBigInteger();
        this.makerPrivateKey = scanning.get(QRCODE_AUTH).getAsString();
        OriginOrder maker = getOrderBy(makerHash);
        OriginOrder taker = constructTaker(maker);
        this.isTaker = true;
        this.orders = new OriginOrder[2];
        this.orders[0] = maker;
        this.orders[1] = taker;
    }

    private OriginOrder constructTaker(OriginOrder maker) {
        // tokens, tokenb
        this.tokenB = maker.getTokenS();
        this.tokenS = maker.getTokenB();
        // amountB, amountBuy
        BigInteger divide = Numeric.toBigInt(maker.getAmountS()).divide(sellCount);
        String amountB = Numeric.toHexStringWithPrefix(divide);
        Double amountBuy = token.getDoubleFromWei(tokenB, amountB);
        // amountS, amountSell
        String amountS;
        divide = Numeric.toBigInt(maker.getAmountB()).divide(sellCount);
        BigInteger mod = Numeric.toBigInt(maker.getAmountB()).mod(sellCount);
        if (mod.equals(BigInteger.valueOf(0))) {
            amountS = Numeric.toHexStringWithPrefix(divide);
        } else {
            amountS = Numeric.toHexStringWithPrefix(divide.add(BigInteger.valueOf(1)));
        }
        Double amountSell = token.getDoubleFromWei(tokenS, amountS);
        // validSince, validUntil
        Integer validS = Integer.parseInt(maker.getValidSince(), 16);
        Integer validU = Integer.parseInt(maker.getValidUntil(), 16);
        // construct order
        OriginOrder order = constructOrder(amountBuy, amountSell, validS, validU);
        order.setSide(TradeType.sell.name());
        order.setOrderType(OrderType.P2P);
        order.setP2pSide(P2PSide.TAKER);
        return order;
    }

    public void constructMaker(Double amountBuy, Double amountSell, Integer validS, Integer validU, Integer sellCount) {
        OriginOrder order = constructOrder(amountBuy, amountSell, validS, validU);
        order.setSide(TradeType.buy.name());
        order.setOrderType(OrderType.P2P);
        order.setP2pSide(P2PSide.MAKER);
        preserveMaker(order, sellCount);
    }

    private void preserveMaker(OriginOrder order, Integer sellCount) {
        this.isTaker = false;
        this.orders = new OriginOrder[]{order};
        String value = String.format("%s-%s", order.getAuthPrivateKey(), sellCount);
        SPUtils.put(context, order.getHash(), value);
        order.setAuthPrivateKey("");
    }

    private Boolean validate() {
        Boolean result = false;
        if (orders.length >= 2) {
            OriginOrder maker = orders[0];
            OriginOrder taker = orders[1];
            if (makerPrivateKey != null && !makerPrivateKey.isEmpty() &&
                    taker.getAuthPrivateKey() != null && maker.getHash() != null && taker.getHash() != null) {
                result = true;
            }
        }
        return result;
    }

    private String generate() throws Exception {
        this.signRingHash();
        String data = encodeRing();
        Transfer transfer = new Transfer(credentials);
        BigInteger gasLimit = gas.getGasLimitByType("submitRing");
        BigInteger gasPrice = gas.getCustomizeGasPriceInWei().toBigInteger();
        RawTransaction rawTx = transfer.eth(gasPrice, gasLimit)
                .getRawTransaction(credentials, Default.PROTOCOL_ADDRESS, data, BigInteger.ZERO);
        byte[] bytes = TransactionEncoder.signMessage(rawTx, credentials);
        return Numeric.toHexString(bytes);
    }

    private void signRingHash() {
        byte[] hash = generateHash();
        Credentials makerCredentials = Credentials.create(makerPrivateKey);
        Credentials takerCredentials = Credentials.create(orders[1].getAuthPrivateKey());
        this.makerSignature = SignUtils.genSignMessage(makerCredentials, hash).getSig();
        this.takerSignature = SignUtils.genSignMessage(takerCredentials, hash).getSig();
    }

    private byte[] generateHash() {
        byte[] makerHash = Numeric.hexStringToByteArray(orders[0].getHash());
        byte[] takerHash = Numeric.hexStringToByteArray(orders[1].getHash());
        byte[] result = new byte[makerHash.length];
        for (int i = 0; i < makerHash.length; ++i) {
            result[i] = (byte) (makerHash[i] ^ takerHash[i]);
        }
        return result;
    }

    private String encodeRing() {
        String data = "0xe78aadb2";
        data += generateOffset();
        data += generateFee();
        data += insertOrderCounts();
        data += generateAddresses();
        data += insertOrderCounts();
        data += generateValues();
        data += insertOrderCounts();
        data += generateMargin();
        data += insertOrderCounts();
        data += generateFlag();
        data += insertListCounts();
        data += generateVList();
        data += insertListCounts();
        data += generateRList();
        data += insertListCounts();
        data += generateSList();
        return data;
    }

    private String generateOffset() {
        String result = "";
        int byteLength = Type.MAX_BYTE_LENGTH;
        result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(byteLength * 9));
        result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(byteLength * 18));
        result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(byteLength * 31));
        result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(byteLength * 34));
        result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(byteLength * 37));
        result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(byteLength * 42));
        result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(byteLength * 47));
        return result;
    }

    private String generateFee() {
        String result = "";
        result += new Address(orders[0].getWalletAddress()).getValue();
        result += Numeric.toHexStringNoPrefix(BigInteger.ZERO);
        return result;
    }

    private String insertOrderCounts() {
        return Numeric.toHexStringNoPrefix(BigInteger.valueOf(orderCount));
    }

    private String insertListCounts() {
        return Numeric.toHexStringNoPrefix(BigInteger.valueOf(orderCount * 2));
    }

    private String generateAddresses() {
        String result = "";
        for (OriginOrder order : orders) {
            result += new Address(order.getOwner()).getValue();
            result += new Address(order.getTokenS()).getValue();
            result += new Address(order.getWalletAddress()).getValue();
            result += new Address(order.getAuthAddr()).getValue();
        }
        return result;
    }

    private String generateValues() {
        String result = "";
        for (OriginOrder order : orders) {
            result += order.getAmountS();
            result += order.getAmountB();
            result += order.getValidSince();
            result += order.getValidUntil();
            result += order.getLrcFee();
            result += order.getAmountS();
        }
        return result;
    }

    private String generateMargin() {
        String result = "";
        for (OriginOrder order : orders) {
            result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(order.getMargin()));
        }
        return result;
    }

    private String generateFlag() {
        String result = "";
        for (OriginOrder order : orders) {
            int flag = order.getBuyNoMoreThanAmountB() ? 1 : 0;
            result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(flag));
        }
        return result;
    }

    private String generateVList() {
        String result = "";
        if (makerSignature != null && takerSignature != null) {
            for (OriginOrder order : orders) {
                result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(order.getV()));
            }
            result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(makerSignature.getV()));
            result += Numeric.toHexStringNoPrefix(BigInteger.valueOf(takerSignature.getV()));
        }
        return result;
    }

    private String generateRList() {
        String result = "";
        if (makerSignature != null && takerSignature != null) {
            for (OriginOrder order : orders) {
                result += order.getR();
            }
            result += makerSignature.getR();
            result += takerSignature.getR();
        }
        return result;
    }

    private String generateSList() {
        String result = "";
        if (makerSignature != null && takerSignature != null) {
            for (OriginOrder order : orders) {
                result += order.getS();
            }
            result += makerSignature.getS();
            result += takerSignature.getS();
        }
        return result;
    }

    private Observable<RelayResponseWrapper> submitRing() {
        if (!validate()) {
            return null;
        }
        try {
            String rawTx = generate();
            orders[1] = signOrder(orders[1]);
            String makerHash = orders[0].getHash();
            String takerHash = orders[1].getHash();
            return loopringService.submitRing(makerHash, takerHash, rawTx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Observable<RelayResponseWrapper> submit() {
        Observable<RelayResponseWrapper> result = null;
        if (!isTaker) {
            if (orders.length == 1 && orders[0] != null) {
                result = loopringService.submitOrder(orders[0]);
            }
        } else if (orders.length == 2 && makerHash != null) {
            result = loopringService.submitOrderForP2P(orders[1], makerHash)
                    .observeOn(Schedulers.io())
                    .flatMap((Func1<RelayResponseWrapper, Observable<RelayResponseWrapper>>) response -> {
                        if (response.getError() == null) {
                            return submitRing();
                        } else {
                            return Observable.just(response);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return result;
    }

    private OriginOrder getOrderBy(String hash) {
        Order order = loopringService.getOrderByHash(hash)
                .subscribeOn(Schedulers.io()).toBlocking().single();
        return order.getOriginOrder();
    }

    public void verify(String password) throws Exception {
        OriginOrder order = completeOrder(password);
        balanceInfo.clear();
        checkGasEnough(order);
        checkBalanceEnough(order);
    }

    private OriginOrder completeOrder(String password) throws Exception {
        this.credentials = WalletUtil.getCredential(context, password);
        int index = isTaker ? 1 : 0;
        OriginOrder order = orders[index];
        order = signOrder(order);
        orders[index] = order;
        return order;
    }

    private void checkGasEnough(OriginOrder order) {
        Double result;
        Double ethBalance = token.getDoubleFromWei("ETH", balance.getAssetBySymbol("ETH").getBalance());
        Double tokenGas = calculateGas(order.getTokenS(), order.getAmountSell());
        if (isTaker) {
            Double gasAmount = gas.getGasAmountInETH("submitRing");
            result = ethBalance - tokenGas - gasAmount;
        } else {
            result = ethBalance - tokenGas;
        }
        if (result < 0) {
            balanceInfo.put("MINUS_ETH", -result);
        }
    }

    private Double calculateGas(String tokenSell, Double amountSell) {
        Double result;
        BalanceResult.Asset asset = this.balance.getAssetBySymbol(tokenSell);
        Double allowance = token.getDoubleFromWei(asset.getSymbol(), asset.getAllowance());
        Double tokenFrozen = getAllowanceFromServer(tokenSell);
        if (allowance >= amountSell + tokenFrozen) {
            return 0d;
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

    private void checkBalanceEnough(OriginOrder order) {
        if (isTaker) {
            BigDecimal balanceDecimal = balance.getAssetBySymbol(order.getTokenS()).getBalance();
            Double tokensBalance = token.getDoubleFromWei(order.getTokenS(), balanceDecimal);
            Double result = tokensBalance - order.getAmountSell();
            if (result < 0) {
                String key = String.format("MINUS_%s", order.getTokenS());
                balanceInfo.put(key, -result);
            }
        }
    }
}
