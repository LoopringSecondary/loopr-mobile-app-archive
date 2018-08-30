package com.tomcat360.lyqb.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.lyqb.walletsdk.util.UnitConverter;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.NumberUtils;
import com.tomcat360.lyqb.utils.SPUtils;

import java.math.BigDecimal;
import java.util.List;

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

        BigDecimal bigDecimal = UnitConverter.weiToEth(item.getBalance().toPlainString());
        helper.setText(R.id.wallet_money, bigDecimal.toPlainString());

        if (SPUtils.get(mContext,"coin","¥").equals("¥")){
            helper.setText(R.id.wallet_count, "¥ "+bigDecimal.toPlainString());
        }else {
            helper.setText(R.id.wallet_count, "$ "+bigDecimal.toPlainString());
        }


    }
}
