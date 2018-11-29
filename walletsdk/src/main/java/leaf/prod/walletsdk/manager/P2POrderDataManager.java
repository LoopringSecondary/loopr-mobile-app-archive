/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 3:47 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.util.Map;

import android.content.Context;

import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Sign;

import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.TradeType;

public class P2POrderDataManager extends OrderDataManager {

    private Context context;

    private static P2POrderDataManager p2pOrderManager = null;

    private static String QRCODE_TYPE = "P2P";
    private static String QRCODE_HASH = "hash";
    private static String QRCODE_AUTH  = "auth";
    private static String SELL_COUNT = "count";

    private Map balanceInfo;
    private Map errorMessage;
    private String tradePair;
    private String makerHash;
    private String makerPrivateKey;
    private OriginOrder[] orders;
    private Sign.SignatureData makerSignature;
    private Sign.SignatureData takerSignature;

    private int sellCount = 1;
    private int orderCount = 2;
    private boolean isTaker = false;
    private TradeType type = TradeType.buy;
    private int byteLength = Type.MAX_BYTE_LENGTH;

    private TokenDataManager token;
    private BalanceDataManager balance;

    private P2POrderDataManager(Context context) {

    }

    public static P2POrderDataManager getInstance(Context context) {
        if (p2pOrderManager == null) {
            p2pOrderManager = new P2POrderDataManager(context);
        }
        return p2pOrderManager;
    }

    @Override
    public Map verify(OriginOrder order) {
        return null;
    }
}
