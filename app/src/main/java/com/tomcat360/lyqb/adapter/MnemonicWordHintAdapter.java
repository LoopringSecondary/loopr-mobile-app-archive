package com.tomcat360.lyqb.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tomcat360.lyqb.R;

import java.util.List;

/**
 *
 */
public class MnemonicWordHintAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MnemonicWordHintAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.mnemonic_word, item);

    }
}
