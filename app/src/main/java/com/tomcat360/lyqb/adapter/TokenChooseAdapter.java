package com.tomcat360.lyqb.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyqb.walletsdk.model.loopr.response.BalanceResult;
import com.tomcat360.lyqb.R;

import java.util.List;

/**
 *
 */
public class TokenChooseAdapter extends BaseQuickAdapter<BalanceResult.Token, BaseViewHolder> {

    public TokenChooseAdapter(int layoutResId, @Nullable List<BalanceResult.Token> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BalanceResult.Token item) {
        helper.setText(R.id.wallet_name, item.getSymbol());
        helper.setText(R.id.wallet_amount, item.getSymbol());
    }
}
