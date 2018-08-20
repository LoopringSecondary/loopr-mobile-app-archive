package com.tomcat360.lyqb.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 *
 */
public class TokenListSearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TokenListSearchAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
//        helper.setText(R.id.mnemonic_word, Integer.valueOf(helper.getPosition())+1+"."+item);

    }
}
