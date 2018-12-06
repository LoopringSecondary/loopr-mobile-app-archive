/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-05 5:39 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.R;
import leaf.prod.app.activity.P2PConfirmActivity;
import leaf.prod.app.activity.P2PTradeQrActivity;
import leaf.prod.app.utils.PasswordDialogUtil;
import leaf.prod.walletsdk.manager.P2POrderDataManager;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class P2PConfirmPresenter extends BasePresenter<P2PConfirmActivity> {

    public JsonObject p2pContent;

    private P2POrderDataManager p2pManager;

    public P2PConfirmPresenter(P2PConfirmActivity view, Context context) {
        super(view, context);
        p2pManager = P2POrderDataManager.getInstance(context);
    }

    public void handleResult() {
        p2pManager.handleResult(p2pContent);
        OriginOrder taker = p2pManager.getOrders()[1];
        /*
        view.tokenBText.setText(taker.getTokenB());  TODO: yanyan
        */
    }

    public void processTaker() {
        if (WalletUtil.needPassword(context)) {
            PasswordDialogUtil.showPasswordDialog(context, v -> processTaker(PasswordDialogUtil.getInputPassword()));
        } else {
            processTaker("");
        }
    }

    private void processTaker(String password) {
        try {
            p2pManager.verify(password);
        } catch (Exception e) {
            // TODO: for yanyan: MUST handle exception of incorrect password
            RxToast.error(view.getResources().getString(R.string.keystore_psw_error));
            e.printStackTrace();
        }
        if (!p2pManager.isBalanceEnough()) {
            // TODO: for yanyan: balance not enough
            // p2pOrderManager.balanceInfo e.g. {"MINUS_ETH": 0.3974, "MINUS_LRC": 10.3974}
        }
        p2pManager.handleInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getError() == null) {
                        view.getOperation().forward(P2PTradeQrActivity.class);
                    } else {
                        String message = p2pManager.getLocaleError(response.getError().getMessage());
                        RxToast.error(message);
                    }
                });
    }
}
