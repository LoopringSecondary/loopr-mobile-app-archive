package leaf.prod.app.adapter.trade;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.R;
import leaf.prod.app.fragment.trade.P2PRecordsFragment;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.PasswordDialogUtil;
import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.P2POrderDataManager;
import leaf.prod.walletsdk.model.CancelOrder;
import leaf.prod.walletsdk.model.CancelType;
import leaf.prod.walletsdk.model.order.RawOrder;
import leaf.prod.walletsdk.model.request.relayParam.NotifyScanParam;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.SignUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class P2PRecordAdapter extends BaseQuickAdapter<RawOrder, BaseViewHolder> {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private P2PRecordsFragment fragment;

    private P2POrderDataManager p2POrderDataManager;

    public P2PRecordAdapter(int layoutResId, @Nullable List<RawOrder> data, P2PRecordsFragment fragment) {
        super(layoutResId, data);
        this.fragment = fragment;
        p2POrderDataManager = P2POrderDataManager.getInstance(fragment.getContext());
    }

    @Override
    protected void convert(BaseViewHolder helper, RawOrder rawOrder) {
        if (rawOrder == null)
            return;
        helper.setText(R.id.tv_token_s, rawOrder.getOriginOrder().getTokenS());
        helper.setText(R.id.tv_token_b, rawOrder.getOriginOrder().getTokenB());
        helper.setGone(R.id.tv_sell_icon, false);
        helper.setGone(R.id.tv_buy_icon, false);
        if (rawOrder.getOriginOrder().getP2pSide() != null) {
            switch (rawOrder.getOriginOrder().getP2pSide()) {
                case MAKER:
                    helper.setVisible(R.id.tv_sell_icon, true);
                    break;
                case TAKER:
                    helper.setVisible(R.id.tv_buy_icon, true);
                    break;
            }
        }
        helper.setText(R.id.tv_price, rawOrder.getSellPrice());
        helper.setText(R.id.tv_amount, NumberUtils.format1(rawOrder.getOriginOrder()
                .getAmountSell(), BalanceDataManager.getPrecision(rawOrder.getOriginOrder().getTokenS())));
        helper.setText(R.id.tv_filled, rawOrder.getFilled());
        helper.setTextColor(R.id.tv_operate, mContext.getResources().getColor(R.color.colorNineText));
        helper.setGone(R.id.tv_cancel, false);
        helper.setGone(R.id.tv_operate, false);
        switch (rawOrder.getOrderStatus()) {
            case OPENED:
            case WAITED:
                helper.setVisible(R.id.tv_cancel, true);
                helper.setOnClickListener(R.id.tv_cancel, view -> {
                    String hash = rawOrder.getOriginOrder().getHash();
                    PasswordDialogUtil.showPasswordDialog(fragment.getContext(), P2PRecordsFragment.PASSWORD_TYPE + "_" + hash, listener -> {
                        NotifyScanParam.SignParam signParam = null;
                        try {
                            signParam = SignUtils.genSignParam(WalletUtil.getCredential(fragment.getContext(), PasswordDialogUtil
                                    .getInputPassword()), WalletUtil
                                    .getCurrentAddress(fragment.getContext()));
                        } catch (Exception e) {
                            PasswordDialogUtil.clearPassword();
                            RxToast.error(fragment.getResources().getString(R.string.keystore_psw_error));
                            e.printStackTrace();
                        }
                        if (signParam != null) {
                            CancelOrder cancelOrder = CancelOrder.builder()
                                    .type(CancelType.hash)
                                    .orderHash(hash)
                                    .build();
                            p2POrderDataManager.getLoopringService().cancelOrderFlex(cancelOrder, signParam)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<String>() {
                                        @Override
                                        public void onCompleted() {
                                            unsubscribe();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            LyqbLogger.log(e.getMessage());
                                            RxToast.error(fragment.getResources().getString(R.string.cancel_failed));
                                            unsubscribe();
                                        }

                                        @Override
                                        public void onNext(String s) {
                                            RxToast.success(fragment.getResources().getString(R.string.cancel_success));
                                            fragment.refreshOrders(0);
                                            PasswordDialogUtil.dismiss(P2PRecordsFragment.PASSWORD_TYPE + "_" + hash);
                                            unsubscribe();
                                        }
                                    });
                        }
                    });
                });
                break;
            case FINISHED:
                helper.setText(R.id.tv_operate, R.string.order_completed);
                helper.setTextColor(R.id.tv_operate, mContext.getResources().getColor(R.color.colorGreen));
                helper.setVisible(R.id.tv_operate, true);
                break;
            case CUTOFF:
                helper.setText(R.id.tv_operate, R.string.order_cutoff);
                helper.setVisible(R.id.tv_operate, true);
                break;
            case CANCELLED:
                helper.setText(R.id.tv_operate, R.string.order_cancelled);
                helper.setVisible(R.id.tv_operate, true);
                break;
            case EXPIRED:
                helper.setText(R.id.tv_operate, R.string.order_expired);
                helper.setVisible(R.id.tv_operate, true);
                break;
            case LOCKED:
                helper.setText(R.id.tv_operate, R.string.order_locked);
                helper.setVisible(R.id.tv_operate, true);
                break;
            default:
                break;
        }
        helper.setText(R.id.tv_date, sdf.format(new Date(Long.valueOf(rawOrder.getOriginOrder().getValidS()) * 1000)));
    }
}
