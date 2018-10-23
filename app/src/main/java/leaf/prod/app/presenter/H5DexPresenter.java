/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-10-15 4:34 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter;

import java.math.BigInteger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import leaf.prod.app.activity.H5DexWebActivity;
import leaf.prod.app.utils.CurrencyUtil;
import leaf.prod.app.utils.FileUtils;
import leaf.prod.app.utils.LanguageUtil;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.utils.ToastUtils;
import leaf.prod.walletsdk.model.H5ScanType;
import leaf.prod.walletsdk.util.KeystoreUtils;
import leaf.prod.walletsdk.util.StringUtils;

public class H5DexPresenter extends BasePresenter<H5DexWebActivity> {

    private String signMessage;

    private JSONObject signTx;

    private String content;

    private String result;

    public String scanContent;

    private boolean isSignMessage = false;

    public H5ScanType type = H5ScanType.OTHER;

    public static final int SUCCESS = 1;

    public static final int ERROR = 2;

    private static final String KEY_METHOD = "method";

    private static final String KEY_DATA = "data";

    private static final String KEY_CALLBACK = "callback";

    private static final String FUNCTION_GET_ACCOUNT = "user.getCurrentAccount";

    private static final String FUNCTION_GET_LANGUAGE = "device.getCurrentLanguage";

    private static final String FUNCTION_GWT_CURRENCY = "device.getCurrentCurrency";

    private static final String FUNCTION_MESSAGE_SIGN = "message.sign";

    private static final String FUCTION_TRANSACTION_SIGN = "transaction.sign";

    public H5DexPresenter(H5DexWebActivity view, Context context) {
        super(view, context);
    }

    @SuppressLint("HandlerLeak")
    Handler handlerCreate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    view.hidePasswordDialog();
                    break;
                case ERROR:
                    ToastUtils.toast("密码错误，请核对后重新输入");
                    break;
            }
        }
    };

    @JavascriptInterface
    public void callApi(String content) {
        Log.d("agentweb", "content:" + content);
        try {
            this.content = content;
            JSONObject jsonObject = new JSONObject(content);
            String method = jsonObject.getString(KEY_METHOD);
            if (TextUtils.isEmpty(method)) {
                return;
            }
            switch (method) {
                case FUNCTION_GET_ACCOUNT:
                    getAddress();
                    send();
                    break;
                case FUNCTION_GET_LANGUAGE:
                    getLanguage();
                    send();
                    break;
                case FUNCTION_GWT_CURRENCY:
                    getCurrency();
                    send();
                    break;
                case FUNCTION_MESSAGE_SIGN:
                    isSignMessage = true;
                    JSONObject data = jsonObject.getJSONObject(KEY_DATA);
                    signMessage = data.getString("message");
                    view.showPasswordDialog();
                    break;
                case FUCTION_TRANSACTION_SIGN:
                    isSignMessage = false;
                    signTx = jsonObject.getJSONObject(KEY_DATA);
                    view.showPasswordDialog();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAddress() {
        result = (String) SPUtils.get(context, "address", "");
    }

    private void getCurrency() {
        result = CurrencyUtil.getCurrency(context).getText();
    }

    private void getLanguage() {
        result = LanguageUtil.getLanguage(context).getText();
    }

    public void sign(String password) {
        if (isSignMessage) {
            signMessage(password);
        } else {
            signTransaction(password);
        }
    }

    private void signTransaction(String password) {
        try {
            String keystore = FileUtils.getKeystoreFromSD(context);
            Credentials credentials = KeystoreUtils.unlock(password, keystore);
            BigInteger nonce = Numeric.toBigInt(signTx.getString("nonce"));
            BigInteger gasPrice = Numeric.toBigInt(signTx.getString("gasPrice"));
            BigInteger gasLimit = Numeric.toBigInt(signTx.getString("gasLimit"));
            String to = signTx.getString("to");
            BigInteger value = Numeric.toBigInt(signTx.getString("value"));
            String data = signTx.getString("data");
            byte chainId = (byte) signTx.getInt("chainId");
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
            result = Numeric.toHexString(signedMessage);
            handlerCreate.sendEmptyMessage(SUCCESS);
            send();
        } catch (Exception e) {
            handlerCreate.sendEmptyMessage(ERROR);
            e.printStackTrace();
        }
    }

    private void signMessage(String password) {
        try {
            String keystore = FileUtils.getKeystoreFromSD(context);
            Credentials credentials = KeystoreUtils.unlock(password, keystore);
            byte[] hash = Numeric.hexStringToByteArray(signMessage);
            byte[] prefix = ("\u0019Ethereum Signed Message:\n" + hash.length).getBytes();
            byte[] finalBytes = new byte[prefix.length + hash.length];
            System.arraycopy(prefix, 0, finalBytes, 0, prefix.length);
            System.arraycopy(hash, 0, finalBytes, prefix.length, hash.length);
            Sign.SignatureData sig = Sign.signMessage(finalBytes, credentials.getEcKeyPair());
            String r = Numeric.toHexString(sig.getR());
            String s = Numeric.toHexStringNoPrefix(sig.getS());
            String v = String.format("%02x", sig.getV());
            result = r + s + v;
            handlerCreate.sendEmptyMessage(SUCCESS);
            send();
        } catch (Exception e) {
            handlerCreate.sendEmptyMessage(ERROR);
            e.printStackTrace();
        }
    }

    private void sendP2PMessage() {
        try {
            JSONObject object = new JSONObject();
            object.put("result", StringUtils.trimAllWhitespace(scanContent));
            String cmd = "javascript:handleP2POrder(" + object.toString() + ")";
            if (view != null) {
                view.call(cmd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void postProcess() {
        try {
            JSONObject jsonObject = new JSONObject(content);
            String method = jsonObject.getString(KEY_METHOD);
            if (method.equalsIgnoreCase(FUNCTION_GWT_CURRENCY)) {
                switch (type) {
                    case P2P_ORDER:
                        sendP2PMessage();
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void send() {
        try {
            JSONObject jsonObject = new JSONObject(content);
            String callback = jsonObject.getString(KEY_CALLBACK);
            JSONObject object = new JSONObject();
            object.put("result", result);
            String cmd = "javascript:" + callback + "(" + object.toString() + ")";
            if (view != null) {
                view.call(cmd);
            }
            postProcess();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
