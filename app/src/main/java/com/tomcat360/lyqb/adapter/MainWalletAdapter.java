package com.tomcat360.lyqb.adapter;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.lyqb.walletsdk.model.response.data.Token;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.manager.TokenDataManager;
import com.tomcat360.lyqb.presenter.BasePresenter;

public class MainWalletAdapter extends BaseQuickAdapter<BalanceResult.Asset, BaseViewHolder> {

    private BasePresenter presenter;

    public MainWalletAdapter(int layoutResId, @Nullable List<BalanceResult.Asset> data, BasePresenter presenter) {
        super(layoutResId, data);
        this.presenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, BalanceResult.Asset item) {
        helper.setText(R.id.wallet_title, item.getSymbol());
        helper.setText(R.id.wallet_name, item.getSymbol());
        helper.setText(R.id.wallet_money, item.getValue() + "");
        helper.setText(R.id.wallet_count, item.getLegalShown());
        Token token = TokenDataManager.getInstance(mContext).getTokenBySymbol(item.getSymbol());
        helper.setImageResource(R.id.wallet_image, token.getImageResId());
    }
}
