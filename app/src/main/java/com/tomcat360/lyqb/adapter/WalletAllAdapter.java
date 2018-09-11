package com.tomcat360.lyqb.adapter;

import java.math.BigDecimal;
import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyqb.walletsdk.model.response.data.Transaction;
import com.lyqb.walletsdk.util.UnitConverter;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.DateUtil;
import com.tomcat360.lyqb.utils.SPUtils;

/**
 *
 */
public class WalletAllAdapter extends BaseQuickAdapter<Transaction, BaseViewHolder> {

    private String symbol;

    public WalletAllAdapter(int layoutResId, @Nullable List<Transaction> data, String symbol) {
        super(layoutResId, data);
        this.symbol = symbol;
    }

    @Override
    protected void convert(BaseViewHolder helper, Transaction item) {
        BigDecimal value = UnitConverter.weiToEth(item.getValue()); //wei转成eth
        String amount = value.toPlainString().length() > 8 ? value.toPlainString()
                .substring(0, 8) : value.toPlainString();
        String coin = (String) SPUtils.get(mContext, "coin", "￥");
        if (item.getType().equals("send")) {
            helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.send) + " " + symbol);
            helper.setText(R.id.wallet_money, "-" + amount + " " + symbol);
        } else {
            helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.receive) + " " + symbol);
            helper.setText(R.id.wallet_money, "+" + amount + " " + symbol);
        }
        helper.setText(R.id.wallet_name, DateUtil.timeStampToDateTime3(item.getCreateTime()));
        if (item.getStatus().equals("success")) {
            helper.setImageResource(R.id.iv_status, R.mipmap.icon_warning);
        } else {
            helper.setImageResource(R.id.iv_status, R.mipmap.icon_warning_lg);
        }
        helper.setText(R.id.wallet_count, coin + amount);
    }
}
