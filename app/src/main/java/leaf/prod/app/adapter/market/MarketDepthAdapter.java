package leaf.prod.app.adapter.market;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.StringUtils;

public class MarketDepthAdapter extends BaseQuickAdapter<String[], BaseViewHolder> {

    private String side;

    public MarketDepthAdapter(int layoutResId, @Nullable List<String[]> data, String side) {
        super(layoutResId, data);
        this.side = side;
    }

    @Override
    protected void convert(BaseViewHolder helper, String[] depth) {
        if (depth == null || depth.length != 3 || StringUtils.isEmpty(side)) { return; }
        helper.setText(R.id.tv_price, NumberUtils.format1(Double.valueOf(depth[0]), 8));
        helper.setText(R.id.tv_amount, NumberUtils.format7(Double.valueOf(depth[1]), 0, 2));
        if (side.equals("buy")) {
            helper.setTextColor(R.id.tv_price, mContext.getResources().getColor(R.color.colorGreen));
        } else {
            helper.setTextColor(R.id.tv_price, mContext.getResources().getColor(R.color.colorRed));
        }
    }
}
