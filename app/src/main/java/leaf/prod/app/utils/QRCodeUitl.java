package leaf.prod.app.utils;

import java.util.Hashtable;

import android.graphics.Bitmap;

import org.web3j.crypto.WalletUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import leaf.prod.walletsdk.model.QRCodeType;
import leaf.prod.walletsdk.util.StringUtils;

public class QRCodeUitl {

    private static Gson gson = new Gson();

    /**
     * 判断二维码是否是keystore
     *
     * @param content
     * @return
     */
    public static boolean isKeyStore(String content) {
        if (StringUtils.isEmpty(content) || !content.contains("ciphertext"))
            return false;
        try {
            gson.fromJson(content, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断二维码是否是转出地址
     *
     * @param content
     * @return
     */
    public static boolean isTransfer(String content) {
        if (StringUtils.isEmpty(content))
            return false;
        return WalletUtils.isValidAddress(content);
    }

    private static boolean isContentOf(String content, String type) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(content).getAsJsonObject();
            JsonElement jsonType = jsonObject.get("type");
            return jsonType.getAsString().equalsIgnoreCase(type);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断二维码是否是P2P订单
     *
     * @param content
     * @return
     */
    public static boolean isP2POrder(String content) {
        return isContentOf(content, "P2P");
    }

    /**
     * 判断二维码是不是登录信息
     * ex: {"type":"UUID","value":"xxx"}
     *
     * @param content
     * @return
     */
    public static boolean isLogin(String content) {
        return isContentOf(content, "UUID");
    }

    /**
     * 判断二维码是不是授权信息
     * ex: {"type":"approve","value":"xxx"}
     *
     * @param content
     * @return
     */
    public static boolean isApprove(String content) {
        return isContentOf(content, "AUTH");
    }

    /**
     * 判断二维码是不是转换信息
     * ex: {"type":"CONVERT","value":"xxx"}
     *
     * @param content
     * @return
     */
    public static boolean isConvert(String content) {
        return isContentOf(content, "CONVERT");
    }

    /**
     * 判断二维码是不是下单信息
     * ex: {"type":"SIGN","value":"xxx"}
     *
     * @param content
     * @return
     */
    public static boolean isOrder(String content) {
        return isContentOf(content, "SIGN");
    }

    /**
     * 判断二维码是不是取消订单信息
     * ex: {"type":"CANCEL_ORDER","value":"xxx"}
     *
     * @param content
     * @return
     */
    public static boolean isCancelOrder(String content) {
        return isContentOf(content, "CANCEL_ORDER");
    }

    /**
     * 判断二维码是否是app支持的三种二维码
     *
     * @param content
     * @param restricts
     * @return
     */
    public static boolean isValidQRCode(String content, String restricts) {
        QRCodeType qrCodeType = getQRCodeType(content);
        return qrCodeType != null && (StringUtils.isEmpty(restricts) || restricts.contains(qrCodeType.name()));
    }

    /**
     * 根据内容生成二维码
     *
     * @param content
     * @return
     */
    public static Bitmap createQRCodeBitmap(String content, int size) {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix;
            bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static QRCodeType getQRCodeType(String content) {
        if (isKeyStore(content))
            return QRCodeType.KEY_STORE;
        if (isTransfer(content))
            return QRCodeType.TRANSFER;
        if (isP2POrder(content))
            return QRCodeType.P2P_ORDER;
        if (isLogin(content))
            return QRCodeType.LOGIN;
        if (isApprove(content))
            return QRCodeType.APPROVE;
        if (isOrder(content))
            return QRCodeType.ORDER;
        if (isConvert(content))
            return QRCodeType.CONVERT;
        if (isCancelOrder(content))
            return QRCodeType.CANCEL_ORDER;
        return null;
    }
}
