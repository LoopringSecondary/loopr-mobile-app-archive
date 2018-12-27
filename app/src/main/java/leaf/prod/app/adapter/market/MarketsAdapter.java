package leaf.prod.app.adapter.market;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.fragment.market.MarketsFragment;
import leaf.prod.walletsdk.manager.MarketOrderDataManager;
import leaf.prod.walletsdk.model.Ticker;

public class MarketsAdapter extends BaseQuickAdapter<Ticker, BaseViewHolder> {

    private MarketsFragment fragment;

    private MarketOrderDataManager marketOrderManager;

    public MarketsAdapter(int layoutResId, @Nullable List<Ticker> data, MarketsFragment fragment) {
        super(layoutResId, data);
        this.fragment = fragment;
        marketOrderManager = MarketOrderDataManager.getInstance(fragment.getContext());
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, Ticker item) {
    }
    //    @Override
    //    protected void convert(BaseViewHolder helper, Order order) {
    //        if (order == null)
    //            return;
    //        helper.setText(R.id.tv_token_s, order.getOriginOrder().getTokenS());
    //        helper.setText(R.id.tv_token_b, order.getOriginOrder().getTokenB());
    //        helper.setGone(R.id.tv_sell_icon, false);
    //        helper.setGone(R.id.tv_buy_icon, false);
    //        if (order.getOriginOrder().getP2pSide() != null) {
    //            switch (order.getOriginOrder().getP2pSide()) {
    //                case MAKER:
    //                    helper.setVisible(R.id.tv_sell_icon, true);
    //                    break;
    //                case TAKER:
    //                    helper.setVisible(R.id.tv_buy_icon, true);
    //                    break;
    //            }
    //        }
    //        helper.setText(R.id.tv_price, order.getPrice());
    //        helper.setText(R.id.tv_amount, NumberUtils.format1(order.getOriginOrder()
    //                .getAmountSell(), BalanceDataManager.getPrecision(order.getOriginOrder().getTokenS())));
    //        helper.setText(R.id.tv_filled, order.getFilled());
    //        helper.setTextColor(R.id.tv_operate, mContext.getResources().getColor(R.color.colorNineText));
    //        helper.setGone(R.id.tv_cancel, false);
    //        helper.setGone(R.id.tv_operate, false);
    //
    //    }
}
