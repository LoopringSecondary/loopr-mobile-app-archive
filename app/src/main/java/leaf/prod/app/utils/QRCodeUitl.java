package leaf.prod.app.utils;

import org.web3j.crypto.WalletUtils;
import com.google.gson.Gson;

import leaf.prod.walletsdk.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan chenwang34@creditease.cn
 * Time: 2018-10-12 上午10:42
 * Cooperation: CreditEase©2017 普信恒业科技发展(北京)有限公司
 */
public class QRCodeUitl {

    public enum QRCodeType {
        KEY_STORE, TRANSFER, P2P_ORDER;
    }

    public static boolean isKeyStore(String content) {
        if (StringUtils.isEmpty(content) || !content.contains("ciphertext"))
            return false;
        try {
            new Gson().fromJson(content, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isTransfer(String content) {
        if (StringUtils.isEmpty(content))
            return false;
        return WalletUtils.isValidAddress(content);
    }

    public static boolean isP2POrder(String content) {
        return false;
    }

    public static boolean isValidQRCode(String content, String restricts) {
        QRCodeType qrCodeType = getQRCodeType(content);
        return qrCodeType != null && (StringUtils.isEmpty(restricts) || restricts.contains(qrCodeType.name()));
    }

    public static QRCodeType getQRCodeType(String content) {
        if (isKeyStore(content))
            return QRCodeType.KEY_STORE;
        if (isTransfer(content))
            return QRCodeType.TRANSFER;
        if (isP2POrder(content))
            return QRCodeType.P2P_ORDER;
        return null;
    }
}
