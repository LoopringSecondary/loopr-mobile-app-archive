/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 3:47 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import com.google.gson.JsonObject;

import leaf.prod.walletsdk.Default;
import leaf.prod.walletsdk.R;
import leaf.prod.walletsdk.model.Order;
import leaf.prod.walletsdk.model.OrderType;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.P2PType;
import leaf.prod.walletsdk.model.TradeType;
import leaf.prod.walletsdk.service.LoopringService;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.walletsdk.util.WalletUtil;

public class P2POrderDataManager extends OrderDataManager {

    private Context context;

    private static P2POrderDataManager p2pOrderManager = null;

    private final static String QRCODE_TYPE = "P2P";

    private final static String QRCODE_HASH = "hash";

    private final static String QRCODE_AUTH = "auth";

    private final static String SELL_COUNT = "count";

    private Map balanceInfo;

    private Map<String, String> errorMessage;

    // token symbol, e.g. weth
    private String tokenS;

    // token symbol, e.g. lrc
    private String tokenB;

    private String tradePair;

    private String makerHash;

    private String makerPrivateKey;

    private OriginOrder[] orders;

    private LoopringService service;

    private Sign.SignatureData makerSignature;

    private Sign.SignatureData takerSignature;

    private TokenDataManager token;

    private BalanceDataManager balance;

    private BigInteger sellCount = BigInteger.valueOf(1);

    private int orderCount = 2;

    private boolean isTaker = false;

    private TradeType type = TradeType.buy;

    private int byteLength = Type.MAX_BYTE_LENGTH;

    private P2POrderDataManager(Context context) {
        this.tokenS = (String) SPUtils.get(context, "tokenS", "WETH");
        this.tokenB = (String) SPUtils.get(context, "tokenB", "LRC");
        service = new LoopringService();
        setupErrorMessage();
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

    private void swapToken() {
        String temp = this.tokenB;
        changeToTokenB(this.tokenS);
        changeToTokenS(temp);
    }

    private void changeToTokenS(String symbol) {
        this.tokenS = symbol;
        SPUtils.put(context, "tokenS", symbol);
    }

    private void changeToTokenB(String symbol) {
        this.tokenB = symbol;
        SPUtils.put(context, "tokenB", symbol);
    }

    private void handleResult(JsonObject scanning) {
        this.makerHash = scanning.get(QRCODE_HASH).getAsString();
        this.makerPrivateKey = scanning.get(QRCODE_AUTH).getAsString();
        this.sellCount = scanning.get(SELL_COUNT).getAsBigInteger();
        OriginOrder maker = getOrderBy(makerHash);
        OriginOrder taker = constructTakerBy(maker);
        this.orders = new OriginOrder[2];
        this.orders[0] = maker;
        this.orders[1] = taker;
    }

    private OriginOrder constructTakerBy(OriginOrder maker) {
        // tokens, market
        String tokenB = maker.getTokenS();
        String tokenBuy = token.getTokenByProtocol(tokenB).getSymbol();
        String tokenS = maker.getTokenB();
        String tokenSell = token.getTokenByProtocol(tokenS).getSymbol();
        String market = String.format("%s-%s", tokenSell, tokenBuy);
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
        String validSince = maker.getValidSince();
        String validUntil = maker.getValidUntil();
        Integer validS = Integer.parseInt(validSince, 16);
        Integer validU = Integer.parseInt(validUntil, 16);
        // build result
        return OriginOrder.builder().delegate(Default.DELEGATE_ADDRESS)
                .owner(WalletUtil.getCurrentAddress(context))
                .side("buy").market(market)
                .tokenS(tokenS).tokenSell(tokenSell)
                .tokenB(tokenB).tokenBuy(tokenBuy)
                .amountS(amountS).amountSell(amountSell)
                .amountB(amountB).amountBuy(amountBuy)
                .validS(validS).validSince(validSince)
                .validU(validU).validUntil(validUntil)
                .lrc(0d).lrcFee("0x0")
                .buyNoMoreThanAmountB(true)
                .marginSplitPercentage(50)
                .orderType(OrderType.P2P).p2pType(P2PType.TAKER).build();
    }

    private OriginOrder getOrderBy(String hash) {
        Order order = service.getOrderByHash(hash).toBlocking().single();
        return order.getOriginOrder();
    }

    @Override
    public Map verify(OriginOrder order) {
        return null;
    }
}
