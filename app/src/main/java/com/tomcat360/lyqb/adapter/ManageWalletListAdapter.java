package com.tomcat360.lyqb.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.SPUtils;

import java.util.List;

/**
 *
 */
public class ManageWalletListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ManageWalletListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.wallet_name, (String) SPUtils.get(mContext,"walletname","name"));
        helper.setText(R.id.wallet_count, (String) SPUtils.get(mContext,"amount","$392.27"));
        helper.setText(R.id.wallet_address, (String) SPUtils.get(mContext,"address","address"));

        helper.setVisible(R.id.icon_status,true);

    }
}
