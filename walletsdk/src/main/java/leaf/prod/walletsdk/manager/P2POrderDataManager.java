/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 3:47 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;
import com.google.gson.JsonObject;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.R;
import leaf.prod.walletsdk.Transfer;
import leaf.prod.walletsdk.model.order.RawOrder;
import leaf.prod.walletsdk.model.order.OrderType;
import leaf.prod.walletsdk.model.RawOrder;
import leaf.prod.walletsdk.model.order.P2PSide;
import leaf.prod.walletsdk.model.RandomWallet;
import leaf.prod.walletsdk.model.common.TradeType;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.walletsdk.util.SignUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import lombok.Getter;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Getter
public class P2POrderDataManager extends OrderDataManager {

    private static P2POrderDataManager p2pOrderManager = null;

    public final static String QRCODE_TYPE = "P2P";

    public final static String QRCODE_HASH = "hash";

    public final static String QRCODE_AUTH = "auth";

    public final static String SELL_COUNT = "count";

    private Map<String, String> errorMessage;

    private String makerHash;

    private String makerPrivateKey;

    private RawOrder[] orders;

    private List<Type> ringParameters;

    private Sign.SignatureData makerSignature;

    private Sign.SignatureData takerSignature;

    public BigInteger sellCount = BigInteger.valueOf(1);

    private int orderCount = 2;

    public boolean isTaker = false;

    private P2POrderDataManager(Context context) {
        super(context);
        this.tokenSell = (String) SPUtils.get(context, "tokenSell", "WETH");
        this.tokenBuy = (String) SPUtils.get(context, "tokenBuy", "LRC");
        this.updatePair();
        this.setupErrorMessage();
        this.ringParameters = new ArrayList<>();
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

    public String getLocaleError(String errorCode) {
        String result = "";
        if (errorMessage.keySet().contains(errorCode)) {
            result = errorMessage.get(errorCode);
        }
        return result;
    }

    private void updatePair() {
        this.tradePair = String.format("%s-%s", this.tokenSell, this.tokenBuy);
    }

    // use for pressing switch button in p2p trade activity
    public void swapToken() {
        String temp = this.tokenBuy;
        changeToTokenB(this.tokenSell);
        changeToTokenS(temp);
    }

    // use for pressing switch tokenSell in p2p trade activity
    public void changeToTokenS(String symbol) {
        this.tokenSell = symbol;
        SPUtils.put(context, "tokenSell", symbol);
        updatePair();
    }

    // use for pressing switch tokenBuy in p2p trade activity
    public void changeToTokenB(String symbol) {
        this.tokenBuy = symbol;
        SPUtils.put(context, "tokenBuy", symbol);
        updatePair();
    }

    public void handleResult(JsonObject scanning) {
        this.makerHash = scanning.get(QRCODE_HASH).getAsString();
        this.sellCount = scanning.get(SELL_COUNT).getAsBigInteger();
        this.makerPrivateKey = scanning.get(QRCODE_AUTH).getAsString();
        RawOrder maker = getOrderBy(makerHash);
        RawOrder taker = constructTaker(maker);
        maker.convert();
        this.isTaker = true;
        this.orders = new RawOrder[2];
        this.orders[0] = maker;
        this.orders[1] = taker;
    }

    public RawOrder getOrder() {
        RawOrder result = null;
        if (!isTaker) {
            if (orders.length == 1) {
                result = orders[0];
            }
        } else {
            if (orders.length == 2) {
                result = orders[1];
            }
        }
        return result;
    }

    private RawOrder constructTaker(RawOrder maker) {
        // tokenSell, tokenBuy
        this.tokenBuy = maker.getTokenS();
        this.tokenSell = maker.getTokenB();
        String tokenBuy = token.getTokenBySymbol(this.tokenBuy).getProtocol();
        String tokenSell = token.getTokenBySymbol(this.tokenSell).getProtocol();
        updatePair();

        // amountB, amountBuy, amountS, amountSell
        BigInteger amountB = Numeric.toBigInt(maker.getAmountS()).divide(sellCount);
        Double amountBuy = token.getDoubleFromWei(this.tokenBuy, new BigDecimal(amountB));
        BigInteger amountS = Numeric.toBigInt(maker.getAmountB()).divide(sellCount);
        BigInteger mod = Numeric.toBigInt(maker.getAmountB()).mod(sellCount);
        if (!mod.equals(BigInteger.ZERO)) {
            amountS.add(BigInteger.ONE);
        }
        Double amountSell = token.getDoubleFromWei(this.tokenSell, new BigDecimal(amountS));

        // validSince, validUntil
        Integer validS = Numeric.toBigInt(maker.getValidSince()).intValue();
        Integer validU = Numeric.toBigInt(maker.getValidUntil()).intValue();
        String validSince = Numeric.toHexStringWithPrefix(BigInteger.valueOf(validS));
        String validUntil = Numeric.toHexStringWithPrefix(BigInteger.valueOf(validU));

        // construct order
        RawOrder order = null;
        try {
            RandomWallet randomWallet = WalletUtil.getRandomWallet();
            order = RawOrder.builder().delegate(Default.DELEGATE_ADDRESS)
                    .owner(WalletUtil.getCurrentAddress(context)).market(tradePair)
                    .tokenS(this.tokenSell).tokenSell(tokenSell).tokenB(this.tokenBuy).tokenBuy(tokenBuy)
                    .amountB(Numeric.toHexStringWithPrefix(amountB)).amountBuy(amountBuy)
                    .amountS(Numeric.toHexStringWithPrefix(amountS)).amountSell(amountSell)
                    .validS(validS).validSince(validSince).validU(validU).validUntil(validUntil)
                    .lrc(0d).lrcFee(Numeric.toHexStringWithPrefix(BigInteger.ZERO))
                    .walletAddress(PartnerDataManager.getInstance(context).getWalletAddress())
                    .authAddr(randomWallet.getAddress()).authPrivateKey(randomWallet.getPrivateKey())
                    .buyNoMoreThanAmountB(false).marginSplitPercentage("0x32").margin(50).powNonce(1)
                    .side(TradeType.sell.name()).orderType(OrderType.P2P).p2pSide(P2PSide.TAKER)
                    .build();
        } catch (Exception e) {
            Log.e("", e.getLocalizedMessage());
        }
        return order;
    }

    public void constructMaker(Double amountBuy, Double amountSell, Integer validS, Integer validU, Integer sellCount) {
        RawOrder order = constructOrder(amountBuy, amountSell, validS, validU);
        preserveMaker(order, sellCount);
        order.setSide(TradeType.buy.name());
        order.setOrderType(OrderType.P2P);
        order.setP2pSide(P2PSide.MAKER);
        order.setAuthPrivateKey("");
    }

    private void preserveMaker(RawOrder order, Integer sellCount) {
        this.isTaker = false;
        this.orders = new RawOrder[]{order};
        String value = String.format("%s-%s", order.getAuthPrivateKey(), sellCount);
        SPUtils.put(context.getApplicationContext(), order.getAuthAddr().toLowerCase(), value);
    }

    private Boolean validate() {
        Boolean result = false;
        if (orders.length >= 2) {
            RawOrder maker = orders[0];
            RawOrder taker = orders[1];
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
        String hexString = Numeric.toHexString(result);
        hexString += Numeric.cleanHexPrefix(orders[0].getWalletAddress());
        hexString += "0000";
        return Hash.sha3(Numeric.hexStringToByteArray(hexString));
    }

    private String encodeRing() {
        ringParameters.clear();
        generateOffset();
        generateFee();
        insertOrderCounts();
        generateAddresses();
        insertOrderCounts();
        generateValues();
        insertOrderCounts();
        generateMargin();
        insertOrderCounts();
        generateFlag();
        insertListCounts();
        generateVList();
        insertListCounts();
        generateRList();
        insertListCounts();
        generateSList();
        String result = "0xe78aadb2";
        for (Type param : ringParameters) {
            result += TypeEncoder.encode(param);
        }
        return result;
    }

    private void generateOffset() {
        int byteLength = Type.MAX_BYTE_LENGTH;
        ringParameters.add(new Uint256(byteLength * 9));
        ringParameters.add(new Uint256(byteLength * 18));
        ringParameters.add(new Uint256(byteLength * 31));
        ringParameters.add(new Uint256(byteLength * 34));
        ringParameters.add(new Uint256(byteLength * 37));
        ringParameters.add(new Uint256(byteLength * 42));
        ringParameters.add(new Uint256(byteLength * 47));
    }

    private void generateFee() {
        ringParameters.add(new Address(orders[0].getWalletAddress()));
        ringParameters.add(new Uint256(BigInteger.ZERO)); // fee selection
    }

    private void insertOrderCounts() {
        ringParameters.add(new Uint256(orderCount));
    }

    private void insertListCounts() {
        ringParameters.add(new Uint256(orderCount * 2));
    }

    private void generateAddresses() {
        for (RawOrder order : orders) {
            ringParameters.add(new Address(order.getOwner()));
            ringParameters.add(new Address(order.getTokenSell()));
            ringParameters.add(new Address(order.getWalletAddress()));
            ringParameters.add(new Address(order.getAuthAddr()));
        }
    }

    private void generateValues() {
        for (RawOrder order : orders) {
            ringParameters.add(new Address(order.getAmountS()));
            ringParameters.add(new Address(order.getAmountB()));
            ringParameters.add(new Address(order.getValidSince()));
            ringParameters.add(new Address(order.getValidUntil()));
            ringParameters.add(new Address(order.getLrcFee()));
            ringParameters.add(new Address(order.getAmountS()));
        }
    }

    private void generateMargin() {
        for (RawOrder order : orders) {
            ringParameters.add(new Address(order.getMarginSplitPercentage()));
        }
    }

    private void generateFlag() {
        for (RawOrder order : orders) {
            int flag = order.getBuyNoMoreThanAmountB() ? 1 : 0;
            ringParameters.add(new Uint256(flag));
        }
    }

    private void generateVList() {
        if (makerSignature != null && takerSignature != null) {
            for (RawOrder order : orders) {
                ringParameters.add(new Uint256(Numeric.toBigInt(order.getV())));
            }
            ringParameters.add(new Uint256(makerSignature.getV()));
            ringParameters.add(new Uint256(takerSignature.getV()));
        }
    }

    private void generateRList() {
        if (makerSignature != null && takerSignature != null) {
            for (RawOrder order : orders) {
                ringParameters.add(new Uint256(Numeric.toBigInt(order.getR())));
            }
            ringParameters.add(new Uint256(Numeric.toBigInt(makerSignature.getR())));
            ringParameters.add(new Uint256(Numeric.toBigInt(takerSignature.getR())));
        }
    }

    private void generateSList() {
        if (makerSignature != null && takerSignature != null) {
            for (RawOrder order : orders) {
                ringParameters.add(new Uint256(Numeric.toBigInt(order.getS())));
            }
            ringParameters.add(new Uint256(Numeric.toBigInt(makerSignature.getS())));
            ringParameters.add(new Uint256(Numeric.toBigInt(takerSignature.getS())));
        }
    }

    private Observable<RelayResponseWrapper> submitRing() {
        if (!validate()) {
            return null;
        }
        try {
            String rawTx = generate();
            String makerHash = orders[0].getHash();
            String takerHash = orders[1].getHash();
            return relayService.submitRing(makerHash, takerHash, rawTx);
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
                result = relayService.submitOrder(orders[0]);
            }
        } else if (orders.length == 2 && makerHash != null) {
            result = relayService.submitOrderForP2P(orders[1], makerHash)
                    .observeOn(Schedulers.io())
                    .flatMap((Func1<RelayResponseWrapper, Observable<RelayResponseWrapper>>) response -> {
                        if (response.getError() == null) {
                            return submitRing();
                        } else {
                            return Observable.just(response);
                        }
                    });
        }
        return result;
    }

    private RawOrder getOrderBy(String hash) {
        RawOrder rawOrder = relayService.getOrderByHash(hash)
                .subscribeOn(Schedulers.io()).toBlocking().single();
        return rawOrder;
    }

    public void verify(String password) throws Exception {
        RawOrder order = completeOrder(password);
        balanceInfo.clear();
        checkGasEnough(order);
        checkBalanceEnough(order);
    }

    private RawOrder completeOrder(String password) throws Exception {
        this.credentials = WalletUtil.getCredential(context, password);
        int index = isTaker ? 1 : 0;
        RawOrder order = orders[index];
        order = signOrder(order);
        orders[index] = order;
        return order;
    }

    private void checkGasEnough(RawOrder order) {
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

    private void checkBalanceEnough(RawOrder order) {
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

    public String generateQRCode(RawOrder order) {
        String result = null;
        if (order != null) {
            String address = order.getAuthAddr().toLowerCase();
            String params = (String) SPUtils.get(context.getApplicationContext(), address, "");
            if (!params.isEmpty() && params.contains("-")) {
                String auth = params.split("-")[0];
                String count = params.split("-")[1];
                JsonObject value = new JsonObject();
                value.addProperty(QRCODE_AUTH, auth);
                value.addProperty(SELL_COUNT, count);
                value.addProperty(QRCODE_HASH, order.getHash());
                JsonObject content = new JsonObject();
                content.add("value", value);
                content.addProperty("type", P2POrderDataManager.QRCODE_TYPE);
                result = content.toString();
            }
        }
        return result;
    }
}
