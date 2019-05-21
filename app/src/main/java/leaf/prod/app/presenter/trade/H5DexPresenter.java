///**
// * Created with IntelliJ IDEA.
// * User: kenshin wangchen@loopring.org
// * Time: 2018-10-15 4:34 PM
// * Cooperation: loopring.org 路印协议基金会
// */
//package leaf.prod.app.presenter.trade;
//
//import java.math.BigInteger;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//import android.webkit.JavascriptInterface;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.web3j.crypto.Credentials;
//import org.web3j.crypto.RawTransaction;
//import org.web3j.crypto.Sign;
//import org.web3j.crypto.TransactionEncoder;
//import org.web3j.utils.Numeric;
//import com.vondear.rxtool.view.RxToast;
//
//import leaf.prod.app.R;
//import leaf.prod.app.activity.trade.H5DexWebActivity;
//import leaf.prod.app.presenter.BasePresenter;
//import leaf.prod.walletsdk.manager.PartnerDataManager;
//import leaf.prod.walletsdk.model.H5ScanType;
//import leaf.prod.walletsdk.util.CurrencyUtil;
//import leaf.prod.walletsdk.util.LanguageUtil;
//import leaf.prod.walletsdk.util.StringUtils;
//import leaf.prod.walletsdk.util.WalletUtil;
//
//public class H5DexPresenter extends BasePresenter<H5DexWebActivity> {
//
//    public static final int SUCCESS = 1;
//
//    public static final int ERROR = 2;
//
//    private static final String KEY_METHOD = "method";
//
//    private static final String KEY_DATA = "data";
//
//    private static final String KEY_CALLBACK = "callback";
//
//    private static final String FUNCTION_GET_ACCOUNT = "user.getCurrentAccount";
//
//    private static final String FUNCTION_GET_REWARD = "user.getRewardAddress";
//
//    private static final String FUNCTION_GET_LANGUAGE = "device.getCurrentLanguage";
//
//    private static final String FUNCTION_GET_CURRENCY = "device.getCurrentCurrency";
//
//    private static final String FUCTION_P2P_SHARE = "device.share";
//
//    private static final String FUNCTION_MESSAGE_SIGN = "message.sign";
//
//    private static final String FUCTION_TRANSACTION_SIGN = "transaction.sign";
//
//    public String scanContent;
//
//    public H5ScanType type = H5ScanType.OTHER;
//
//    @SuppressLint("HandlerLeak")
//    Handler handlerCreate = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case SUCCESS:
//                    view.hidePasswordDialog();
//                    break;
//                case ERROR:
//                    RxToast.error(view.getResources().getString(R.string.h5dex_psw_error));
//                    break;
//            }
//        }
//    };
//
//    private String signMessage;
//
//    private JSONObject signTx;
//
//    private JSONObject p2pOrder;
//
//    private String content;
//
//    private String result;
//
//    private boolean isSignMessage = false;
//
//    private PartnerDataManager partnerManager;
//
//    public H5DexPresenter(H5DexWebActivity view, Context context) {
//        super(view, context);
//        partnerManager = PartnerDataManager.getInstance(context);
//    }
//
//    @JavascriptInterface
//    public void callApi(String content) {
//        Log.d("agentweb", "content:" + content);
//        try {
//            this.content = content;
//            JSONObject jsonObject = new JSONObject(content);
//            String method = jsonObject.getString(KEY_METHOD);
//            if (TextUtils.isEmpty(method)) {
//                return;
//            }
//            switch (method) {
//                case FUNCTION_GET_ACCOUNT:
//                    getAddress();
//                    send();
//                    break;
//                case FUNCTION_GET_REWARD:
//                    getRewardAddress();
//                    send();
//                    break;
//                case FUNCTION_GET_LANGUAGE:
//                    getLanguage();
//                    send();
//                    break;
//                case FUNCTION_GET_CURRENCY:
//                    getCurrency();
//                    send();
//                    break;
//                case FUNCTION_MESSAGE_SIGN:
//                    isSignMessage = true;
//                    JSONObject data = jsonObject.getJSONObject(KEY_DATA);
//                    signMessage = data.getString("message");
//                    if (WalletUtil.needPassword(context)) {
//                        view.showPasswordDialog();
//                    } else {
//                        try {
//                            sign(WalletUtil.getCredential(context, ""));
//                        } catch (Exception e) {
//                            RxToast.error(context.getResources().getString(R.string.keystore_psw_error));
//                        }
//                    }
//                    break;
//                case FUCTION_TRANSACTION_SIGN:
//                    isSignMessage = false;
//                    signTx = jsonObject.getJSONObject(KEY_DATA);
//                    if (WalletUtil.needPassword(context)) {
//                        view.showPasswordDialog();
//                    } else {
//                        try {
//                            sign(WalletUtil.getCredential(context, ""));
//                        } catch (Exception e) {
//                            RxToast.error(context.getResources().getString(R.string.keystore_psw_error));
//                        }
//                    }
//                    break;
//                case FUCTION_P2P_SHARE:
//                    p2pOrder = jsonObject.getJSONObject(KEY_DATA);
//                    handleP2POrder();
//                    break;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleP2POrder() {
//        view.handleP2PShare(p2pOrder);
//    }
//
//    private void getAddress() {
//        result = WalletUtil.getCurrentAddress(context);
//    }
//
//    private void getRewardAddress() {
//        result = partnerManager.getWalletAddress();
//    }
//
//    private void getCurrency() {
//        result = CurrencyUtil.getCurrency(context).getText();
//    }
//
//    private void getLanguage() {
//        result = LanguageUtil.getLanguage(context).getText();
//    }
//
//    public void sign(Credentials credentials) {
//        if (isSignMessage) {
//            signMessage(credentials);
//        } else {
//            signTransaction(credentials);
//        }
//    }
//
//    private void signTransaction(Credentials credentials) {
//        try {
//            BigInteger nonce = Numeric.toBigInt(signTx.getString("nonce"));
//            BigInteger gasPrice = Numeric.toBigInt(signTx.getString("gasPrice"));
//            BigInteger gasLimit = Numeric.toBigInt(signTx.getString("gasLimit"));
//            String to = signTx.getString("to");
//            BigInteger value = Numeric.toBigInt(signTx.getString("value"));
//            String data = signTx.getString("data");
//            byte chainId = (byte) signTx.getInt("chainId");
//            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
//            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
//            result = Numeric.toHexString(signedMessage);
//            handlerCreate.sendEmptyMessage(SUCCESS);
//            send();
//        } catch (Exception e) {
//            handlerCreate.sendEmptyMessage(ERROR);
//            e.printStackTrace();
//        }
//    }
//
//    private void signMessage(Credentials credentials) {
//        try {
//            byte[] hash = Numeric.hexStringToByteArray(signMessage);
//            byte[] prefix = ("\u0019Ethereum Signed Message:\n" + hash.length).getBytes();
//            byte[] finalBytes = new byte[prefix.length + hash.length];
//            System.arraycopy(prefix, 0, finalBytes, 0, prefix.length);
//            System.arraycopy(hash, 0, finalBytes, prefix.length, hash.length);
//            Sign.SignatureData sig = Sign.signMessage(finalBytes, credentials.getEcKeyPair());
//            String r = Numeric.toHexString(sig.getR());
//            String s = Numeric.toHexStringNoPrefix(sig.getS());
//            String v = String.format("%02x", sig.getV());
//            result = r + s + v;
//            handlerCreate.sendEmptyMessage(SUCCESS);
//            send();
//        } catch (Exception e) {
//            handlerCreate.sendEmptyMessage(ERROR);
//            e.printStackTrace();
//        }
//    }
//
//    private void sendP2PMessage() {
//        try {
//            JSONObject object = new JSONObject();
//            object.put("result", StringUtils.trimAllWhitespace(scanContent));
//            String cmd = "javascript:handleP2POrder(" + object.toString() + ")";
//            if (view != null) {
//                view.call(cmd);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void postProcess() {
//        try {
//            JSONObject jsonObject = new JSONObject(content);
//            String method = jsonObject.getString(KEY_METHOD);
//            if (method.equalsIgnoreCase(FUNCTION_GET_CURRENCY)) {
//                switch (type) {
//                    case P2P_ORDER:
//                        sendP2PMessage();
//                        break;
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void send() {
//        try {
//            JSONObject jsonObject = new JSONObject(content);
//            String callback = jsonObject.getString(KEY_CALLBACK);
//            JSONObject object = new JSONObject();
//            object.put("result", result);
//            String cmd = "javascript:" + callback + "(" + object.toString() + ")";
//            if (view != null) {
//                view.call(cmd);
//            }
//            postProcess();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//}
