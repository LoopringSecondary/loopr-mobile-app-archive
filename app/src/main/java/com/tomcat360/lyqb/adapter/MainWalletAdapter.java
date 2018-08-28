package com.tomcat360.lyqb.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyqb.walletsdk.model.response.BalanceResult;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.NumberUtils;
import com.tomcat360.lyqb.utils.SPUtils;

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
        helper.setText(R.id.wallet_money, NumberUtils.numberformat(item.getBalance().doubleValue())+"");
        if ((int)SPUtils.get(mContext,"coin",1) == 1){
            helper.setText(R.id.wallet_count, "¥ "+item.getBalance().doubleValue()*1);
        }else {
            helper.setText(R.id.wallet_count, "$ "+item.getBalance().doubleValue()*1);
        }


    }
}
