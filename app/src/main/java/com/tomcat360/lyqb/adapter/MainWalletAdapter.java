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
        Token token = TokenDataManager.getInstance(mContext).getTokenBySymbol(item.getSymbol());
        helper.setText(R.id.wallet_title, item.getSymbol());
        helper.setText(R.id.wallet_name, token.getSource());
        helper.setText(R.id.wallet_money, item.getValueShown());
        helper.setText(R.id.wallet_count, item.getLegalShown());
        if (token.getImageResId() == 0) {
            helper.setVisible(R.id.wallet_symbol, true);
            helper.setVisible(R.id.wallet_image, false);
            helper.setText(R.id.wallet_symbol, item.getSymbol());
        } else {
            helper.setVisible(R.id.wallet_symbol, false);
            helper.setVisible(R.id.wallet_image, true);
            helper.setImageResource(R.id.wallet_image, token.getImageResId());
        }
    }
}
