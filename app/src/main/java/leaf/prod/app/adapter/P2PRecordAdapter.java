package leaf.prod.app.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;
import leaf.prod.walletsdk.model.Order;
import leaf.prod.walletsdk.util.NumberUtils;

public class P2PRecordAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public P2PRecordAdapter(int layoutResId, @Nullable List<Order> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order order) {
        if (order == null)
            return;
        helper.setText(R.id.tv_token_s, order.getTradingPair().split("-")[0]);
        helper.setText(R.id.tv_token_b, order.getTradingPair().split("-")[1]);
        helper.setGone(R.id.tv_sell_icon, false);
        helper.setGone(R.id.tv_buy_icon, false);
        switch (order.getOriginOrder().getP2pType()) {
            case MAKER:
                helper.setVisible(R.id.tv_sell_icon, true);
                break;
            case TAKER:
                helper.setVisible(R.id.tv_buy_icon, true);
                break;
        }
        helper.setText(R.id.tv_price, String.valueOf(order.getPrice()));
        helper.setText(R.id.tv_amount, String.valueOf(order.getOriginOrder().getAmountSell()));
        helper.setText(R.id.tv_filled, NumberUtils.format1(order.getDealtAmountS() / order.getOriginOrder()
                .getAmountSell(), 2) + "%");
        helper.setTextColor(R.id.tv_operate, mContext.getResources().getColor(R.color.colorNineText));
        switch (order.getOrderStatus()) {
            case OPENED:
                helper.setVisible(R.id.tv_cancel, true);
                break;
            case WAITED:
                helper.setText(R.id.tv_operate, R.string.order_submitted);
                helper.setVisible(R.id.tv_operate, true);
                break;
            case FINISHED:
                helper.setText(R.id.tv_operate, R.string.order_completed);
                helper.setTextColor(R.id.tv_operate, mContext.getResources().getColor(R.color.colorGreen));
                helper.setVisible(R.id.tv_operate, true);
                break;
            case CUTOFF:
                helper.setText(R.id.tv_operate, R.string.order_cut_off);
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
        helper.setText(R.id.tv_date, sdf.format(new Date(new Long(order.getOriginOrder()
                .getValidS()).longValue() * 1000)));
    }
}
