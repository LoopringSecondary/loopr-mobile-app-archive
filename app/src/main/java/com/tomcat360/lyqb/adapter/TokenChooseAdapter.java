package com.tomcat360.lyqb.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyqb.walletsdk.model.response.BalanceResult;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.activity.SendListChooseActivity;
import com.tomcat360.lyqb.utils.SPUtils;

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
        if ( SPUtils.get(mContext,"send_choose","").equals(item.getSymbol())){
            helper.setVisible(R.id.iv_checked,true);
        }else {
            helper.setVisible(R.id.iv_checked,false);
        }
        helper.setText(R.id.wallet_name, item.getSymbol());
        helper.setText(R.id.wallet_amount, item.getSymbol());
    }
}
