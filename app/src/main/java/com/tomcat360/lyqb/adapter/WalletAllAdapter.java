package com.tomcat360.lyqb.adapter;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyqb.walletsdk.model.response.data.Transaction;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.manager.BalanceDataManager;
import com.tomcat360.lyqb.manager.MarketcapDataManager;
import com.tomcat360.lyqb.manager.TokenDataManager;
import com.tomcat360.lyqb.utils.CurrencyUtil;
import com.tomcat360.lyqb.utils.DateUtil;
import com.tomcat360.lyqb.utils.NumberUtils;

public class WalletAllAdapter extends BaseQuickAdapter<Transaction, BaseViewHolder> {

    private String symbol;

    private TokenDataManager tokenManager;

    private MarketcapDataManager priceManager;

    private BalanceDataManager balanceManager;

    public WalletAllAdapter(int layoutResId, @Nullable List<Transaction> data, String symbol) {
        super(layoutResId, data);
        this.symbol = symbol;
        tokenManager = TokenDataManager.getInstance(mContext);
        priceManager = MarketcapDataManager.getInstance(mContext);
        balanceManager = BalanceDataManager.getInstance(mContext);
    }

    @Override
    protected void convert(BaseViewHolder helper, Transaction item) {
        int precision = balanceManager.getPrecisionBySymbol(item.getSymbol());
        Double value = tokenManager.getDoubleFromWei(item.getSymbol(), item.getValue());
        String valueShown = NumberUtils.format1(value, precision);
        Double price = priceManager.getPriceBySymbol(item.getSymbol());
        String currency = CurrencyUtil.format(mContext, price * value);
        String image = String.format("icon_tx_%s", item.getType().getDescription().toLowerCase());
        int identifier = mContext.getResources().getIdentifier(image, "mipmap", mContext.getPackageName());
        helper.setImageResource(R.id.wallet_image, identifier);
        helper.setText(R.id.wallet_name, DateUtil.timeStampToDateTime3(item.getCreateTime()));
        helper.setText(R.id.wallet_count, currency);
        setTxType(helper, item, valueShown);
        setTxStatus(helper, item);
    }

    private void setTxType(BaseViewHolder helper, Transaction item, String value) {
        switch (item.getType()) {
            case SEND:
                helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.send) + " " + symbol);
                helper.setText(R.id.wallet_money, "-" + value + " " + symbol);
                helper.setTextColor(R.id.wallet_money, mContext.getResources().getColor(R.color.colorRed));
                break;
            case RECEIVE:
                helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.receive) + " " + symbol);
                helper.setText(R.id.wallet_money, "+" + value + " " + symbol);
                helper.setTextColor(R.id.wallet_money, mContext.getResources().getColor(R.color.colorGreen));
                break;
            case SELL:
                helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.sell) + " " + symbol);
                helper.setText(R.id.wallet_money, "-" + value + " " + symbol);
                helper.setTextColor(R.id.wallet_money, mContext.getResources().getColor(R.color.colorRed));
                break;
            case BUY:
                helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.buy) + " " + symbol);
                helper.setText(R.id.wallet_money, "+" + value + " " + symbol);
                helper.setTextColor(R.id.wallet_money, mContext.getResources().getColor(R.color.colorGreen));
                break;
            case APPROVE:
                helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.approve));
                helper.setVisible(R.id.wallet_money, false);
                helper.setVisible(R.id.wallet_count, false);
                break;
            case CANCEL:
                helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.cancel));
                helper.setVisible(R.id.wallet_money, false);
                helper.setVisible(R.id.wallet_count, false);
                break;
            case CONVERT_INCOME:
                this.updateIncome(helper, item, value);
                break;
            case CONVERT_OUTCOME:
                this.updateOutcome(helper, item, value);
                break;
            case UNSUPPORTED:
                helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.other));
                helper.setVisible(R.id.wallet_money, false);
                helper.setVisible(R.id.wallet_count, false);
                break;
            default:
                System.out.println(item.getType());
        }
    }

    private void setTxStatus(BaseViewHolder helper, Transaction item) {
        switch (item.getStatus()) {
            case SUCCESS:
                helper.setImageResource(R.id.iv_status, R.mipmap.icon_tx_success);
                break;
            case PENDING:
                helper.setImageResource(R.id.iv_status, R.mipmap.icon_tx_pending);
                break;
            case FAILED:
                helper.setImageResource(R.id.iv_status, R.mipmap.icon_tx_failed);
                break;
        }
    }

    private void updateIncome(BaseViewHolder helper, Transaction item, String value) {
        if (item.getSymbol().equalsIgnoreCase("WETH")) {
            helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.convert_weth));
        } else if (item.getSymbol().equalsIgnoreCase("ETH")) {
            helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.convert_eth));
        }
        helper.setText(R.id.wallet_money, "+" + value + " " + symbol);
        helper.setTextColor(R.id.wallet_money, mContext.getResources().getColor(R.color.colorGreen));
    }

    private void updateOutcome(BaseViewHolder helper, Transaction item, String value) {
        if (item.getSymbol().equalsIgnoreCase("WETH")) {
            helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.convert_eth));
        } else if (item.getSymbol().equalsIgnoreCase("ETH")) {
            helper.setText(R.id.wallet_title, mContext.getResources().getString(R.string.convert_weth));
        }
        helper.setText(R.id.wallet_money, "-" + value + " " + symbol);
        helper.setTextColor(R.id.wallet_money, mContext.getResources().getColor(R.color.colorRed));
    }
}
