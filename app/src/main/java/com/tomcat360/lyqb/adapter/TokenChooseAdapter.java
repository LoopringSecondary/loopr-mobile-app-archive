package com.tomcat360.lyqb.adapter;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyqb.walletsdk.model.response.data.Token;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.SPUtils;

/**
 *
 */
public class TokenChooseAdapter extends BaseQuickAdapter<Token, BaseViewHolder> {

    public TokenChooseAdapter(int layoutResId, @Nullable List<Token> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Token item) {
        if (SPUtils.get(mContext, "send_choose", "").equals(item.getSymbol())) {
            helper.setVisible(R.id.iv_checked, true);
        } else {
            helper.setVisible(R.id.iv_checked, false);
        }
        helper.setText(R.id.wallet_name, item.getSymbol());
        helper.setText(R.id.wallet_amount, item.getSource());

        if (item.getImageResId() == 0) {
            helper.setVisible(R.id.wallet_symbol, true);
            helper.setVisible(R.id.wallet_image, false);
            helper.setText(R.id.wallet_symbol, item.getSymbol());
        } else {
            helper.setVisible(R.id.wallet_symbol, false);
            helper.setVisible(R.id.wallet_image, true);
            helper.setImageResource(R.id.wallet_image, item.getImageResId());
        }
    }
}
