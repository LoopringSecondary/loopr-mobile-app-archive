package leaf.prod.app.adapter.market;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;
import leaf.prod.walletsdk.model.Ticker;

public class MarketSelectAdapter extends BaseQuickAdapter<Ticker, BaseViewHolder> {

    public MarketSelectAdapter(int layoutResId, @Nullable List<Ticker> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Ticker ticker) {
        if (ticker == null)  return;
        setupChange(helper, ticker);
        helper.setText(R.id.tv_token_s, ticker.getTradingPair().getTokenA());
        helper.setText(R.id.tv_token_b, ticker.getTradingPair().getTokenB());
        helper.setText(R.id.tv_price, ticker.getBalanceShown());
        helper.setText(R.id.tv_change, ticker.getChange());
    }

    private void setupChange(BaseViewHolder helper, Ticker ticker) {
        if (ticker.getChange().contains("↑")) {
            helper.setTextColor(R.id.tv_change, mContext.getResources().getColor(R.color.colorRed));
        } else {
            helper.setTextColor(R.id.tv_change, mContext.getResources().getColor(R.color.colorGreen));
        }
    }
}
