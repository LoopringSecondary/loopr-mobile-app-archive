package com.tomcat360.lyqb.adapter;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.SPUtils;

/**
 *
 */
public class MainWalletAdapter extends BaseQuickAdapter<BalanceResult.Token, BaseViewHolder> {

    public MainWalletAdapter(int layoutResId, @Nullable List<BalanceResult.Token> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BalanceResult.Token item) {
        helper.setText(R.id.wallet_title, item.getSymbol());
        helper.setText(R.id.wallet_name, item.getSymbol());
        //        BigDecimal bigDecimal = UnitConverter.weiToEth(item.getBalance().toPlainString());
        //        String amount =  bigDecimal.toPlainString().length() > 8 ? bigDecimal.toPlainString().substring(0,8):bigDecimal.toPlainString();
        helper.setText(R.id.wallet_money, item.getValue() + "");
        if (SPUtils.get(mContext, "coin", "¥").equals("¥")) {
            helper.setText(R.id.wallet_count, "¥ " + item.getLegalValue());
        } else {
            helper.setText(R.id.wallet_count, "$ " + item.getLegalValue());
        }
    }
}
