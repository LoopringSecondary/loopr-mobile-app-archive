package com.tomcat360.lyqb.adapter;

import java.util.List;

import android.support.annotation.Nullable;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyqb.walletsdk.model.response.data.Token;
import com.tomcat360.lyqb.R;

/**
 *
 */
public class TokenListAdapter extends BaseQuickAdapter<Token, BaseViewHolder> {

    private List<String> choose_token;

    public TokenListAdapter(int layoutResId, @Nullable List<Token> data, List<String> choose_token) {
        super(layoutResId, data);
        this.choose_token = choose_token;
    }

    @Override
    protected void convert(BaseViewHolder helper, Token item) {
        helper.setText(R.id.wallet_title, item.getSymbol());
        helper.setText(R.id.wallet_name, item.getSource());

        if (item.getImageResId() == 0) {
            helper.setVisible(R.id.wallet_symbol, true);
            helper.setVisible(R.id.wallet_image, false);
            helper.setText(R.id.wallet_symbol, item.getSymbol());
        } else {
            helper.setVisible(R.id.wallet_symbol, false);
            helper.setVisible(R.id.wallet_image, true);
            helper.setImageResource(R.id.wallet_image, item.getImageResId());
        }
        Switch aSwitch = helper.getView(R.id.s_v);
        aSwitch.setTag(item.getSymbol());
        if (choose_token.contains(item.getSymbol())) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }
    }

    public void setChoose_token(List<String> choose_token) {
        this.choose_token = choose_token;
    }
}
