package leaf.prod.app.adapter.market;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;
import leaf.prod.app.fragment.market.MarketsFragment;
import leaf.prod.walletsdk.manager.LoginDataManager;
import leaf.prod.walletsdk.model.market.Market;
import leaf.prod.walletsdk.model.market.MarketPair;
import leaf.prod.walletsdk.model.market.MarketsType;
import leaf.prod.walletsdk.util.CurrencyUtil;

public class MarketsAdapter extends BaseQuickAdapter<Market, BaseViewHolder> {

    private MarketsFragment fragment;

    private LoginDataManager manager;

    public MarketsAdapter(int layoutResId, @Nullable List<Market> data, MarketsFragment fragment) {
        super(layoutResId, data);
        this.fragment = fragment;
        this.manager = LoginDataManager.getInstance(mContext);
    }

    @Override
    protected void convert(BaseViewHolder helper, Market market) {
        if (market == null) {
            return;
        }
        setupChange(helper, market);
        setupFavIcon(helper, market);
        setupFavButton(helper, market);
        helper.setText(R.id.tv_token_s, market.getBaseSymbol());
        helper.setText(R.id.tv_token_b, market.getQuoteSymbol());
        helper.setText(R.id.tv_volume, market.getVolume());
        helper.setText(R.id.tv_currency, getCurrency(market));
        helper.setText(R.id.tv_price, market.getExchangeRate());
        helper.setText(R.id.tv_change, market.getChange());
    }

    private String getCurrency(Market market) {
        return CurrencyUtil.format(mContext, market.getTicker().getPrice());
    }

    private void setupFavIcon(BaseViewHolder helper, Market market) {
        if (manager.getLocalUser() != null) {
            List<MarketPair> favMarkets = manager.getLocalUser().getFavMarkets();
            if (favMarkets != null) {
                if (favMarkets.contains(market.getMarketPair())) {
                    helper.setBackgroundRes(R.id.btn_fav, R.mipmap.icon_favorite);
                } else {
                    helper.setBackgroundRes(R.id.btn_fav, R.mipmap.icon_unfavorite);
                }
            } else {
                helper.setBackgroundRes(R.id.btn_fav, R.mipmap.icon_unfavorite);
            }
        }
    }

    private void setupChange(BaseViewHolder helper, Market market) {
        if (market.getChange().contains("↑")) {
            helper.setBackgroundRes(R.id.tv_change, R.drawable.market_up);
        } else {
            helper.setBackgroundRes(R.id.tv_change, R.drawable.market_down);
        }
    }

    private void setupFavButton(BaseViewHolder helper, Market market) {
        helper.setOnClickListener(R.id.ll_favorite, v -> {
            if (manager.isFavorite(market.getMarketPair())) {
                manager.removeFavorite(market.getMarketPair());
            } else {
                manager.addFavorite(market.getMarketPair());
            }
            if (fragment.getMarketsType() == MarketsType.Favorite) {
                if (manager.isFavorite(market.getMarketPair())) {
                    helper.setBackgroundRes(R.id.btn_fav, R.mipmap.icon_favorite);
                } else {
                    helper.setBackgroundRes(R.id.btn_fav, R.mipmap.icon_unfavorite);
                }
            } else {
                fragment.updateAdapter();
            }
        });
    }
}
