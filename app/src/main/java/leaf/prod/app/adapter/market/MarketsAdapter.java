package leaf.prod.app.adapter.market;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;
import leaf.prod.app.fragment.market.MarketsFragment;
import leaf.prod.walletsdk.manager.LoginDataManager;
import leaf.prod.walletsdk.model.MarketsType;
import leaf.prod.walletsdk.model.Ticker;
import leaf.prod.walletsdk.model.TradingPair;
import leaf.prod.walletsdk.util.NumberUtils;

public class MarketsAdapter extends BaseQuickAdapter<Ticker, BaseViewHolder> {

    private MarketsFragment fragment;

    private LoginDataManager manager;

    public MarketsAdapter(int layoutResId, @Nullable List<Ticker> data, MarketsFragment fragment) {
        super(layoutResId, data);
        this.fragment = fragment;
        this.manager = LoginDataManager.getInstance(mContext);
    }

    @Override
    protected void convert(BaseViewHolder helper, Ticker ticker) {
        if (ticker == null) {
            return;
        }
        setupChange(helper, ticker);
        setupFavIcon(helper, ticker);
        setupFavButton(helper, ticker);
        helper.setText(R.id.tv_token_s, ticker.getTradingPair().getTokenA());
        helper.setText(R.id.tv_token_b, ticker.getTradingPair().getTokenB());
        helper.setText(R.id.tv_volume, "Vol " + NumberUtils.numberformat2(ticker.getVol()));
        helper.setText(R.id.tv_currency, ticker.getCurrencyShown());
        helper.setText(R.id.tv_price, ticker.getBalanceShown());
        helper.setText(R.id.tv_change, ticker.getChange());
    }

    private void setupFavIcon(BaseViewHolder helper, Ticker ticker) {
        if (manager.getLocalUser() != null) {
            List<TradingPair> favMarkets = manager.getLocalUser().getFavMarkets();
            if (favMarkets != null) {
                if (favMarkets.contains(ticker.getTradingPair())) {
                    helper.setBackgroundRes(R.id.btn_fav, R.mipmap.icon_favorite);
                } else {
                    helper.setBackgroundRes(R.id.btn_fav, R.mipmap.icon_unfavorite);
                }
            } else {
                helper.setBackgroundRes(R.id.btn_fav, R.mipmap.icon_unfavorite);
            }
        }
    }

    private void setupChange(BaseViewHolder helper, Ticker ticker) {
        if (ticker.getChange().contains("↑")) {
            helper.setBackgroundRes(R.id.tv_change, R.drawable.market_up);
        } else {
            helper.setBackgroundRes(R.id.tv_change, R.drawable.market_down);
        }
    }

    private void setupFavButton(BaseViewHolder helper, Ticker ticker) {
        helper.setOnClickListener(R.id.ll_favorite, v -> {
            if (manager.isFavorite(ticker.getTradingPair())) {
                manager.removeFavorite(ticker.getTradingPair());
            } else {
                manager.addFavorite(ticker.getTradingPair());
            }
            if (fragment.getMarketsType() == MarketsType.Favorite) {
                if (manager.isFavorite(ticker.getTradingPair())) {
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
