/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-10 下午4:16
 * Cooperation: loopring.org 路印协议基金会
 */
package com.tomcat360.lyqb.presenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.lyqb.walletsdk.model.response.data.MarketcapResult;
import com.lyqb.walletsdk.model.response.data.Token;
import com.lyqb.walletsdk.util.UnitConverter;
import com.tomcat360.lyqb.fragment.MainFragment;
import com.tomcat360.lyqb.manager.TokenDataManager;
import com.tomcat360.lyqb.utils.CurrencyUtil;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.view.APP;

public class MainFragmentPresenter extends BasePresenter<MainFragment, MainFragment.MainFramentReceiver> {

    private Map<String, BalanceResult.Asset> tokenMap = new HashMap<>();

    public MainFragmentPresenter(MainFragment view, Context context) {
        super(view, context, view.getBroadcastReceiver());
        refreshTokens();
    }

    public void refreshTokens() {
        dataManager.refreshTokens();
    }

    public void setTokenLegalPrice(List<BalanceResult.Asset> assetList, List<Token> tokenList, MarketcapResult marketcapResult) {
        LyqbLogger.log(assetList.toString());
        LyqbLogger.log(tokenList.toString());
        LyqbLogger.log(marketcapResult.toString());
        TokenDataManager manager = TokenDataManager.getInstance(context);
        for (BalanceResult.Asset asset : assetList) {
            if (asset.getSymbol().equals("ETH"))
                asset.setValue(UnitConverter.weiToEth(asset.getBalance().toPlainString()).doubleValue());
            if (!asset.getBalance().equals(BigDecimal.ZERO) && manager.getTokenBySymbol(asset.getSymbol()) != null) {
                Log.d("", asset.getSymbol() + " " + manager.getTokenBySymbol(asset.getSymbol()));
                asset.setValue(asset.getBalance()
                        .divide(manager.getTokenBySymbol(asset.getSymbol()).getDecimals()).doubleValue());
            }
            if (getLegalPriceBySymbol(marketcapResult, asset.getSymbol()) != null) {
                asset.setLegalValue(getLegalPriceBySymbol(marketcapResult, asset.getSymbol()) * asset.getValue());
            }
            tokenMap.put(asset.getSymbol(), asset);
        }
        Collections.sort(assetList, (o1, o2) -> {
            if (o1.getLegalValue() < o2.getLegalValue()) {
                return 1;
            }
            if (o1.getLegalValue() == o2.getLegalValue()) {
                return 0;
            }
            return -1;
        });
        APP.setListAsset(assetList);
        List<BalanceResult.Asset> listChooseAsset = new ArrayList<>();
        List<String> listChooseSymbol = SPUtils.getDataList(this.context, "choose_token");
        double amount = 0;
        for (String symbol : listChooseSymbol) {
            listChooseAsset.add(tokenMap.get(symbol));
            amount += tokenMap.get(symbol).getLegalValue();
        }
        for (BalanceResult.Asset asset : assetList) {
            if (!listChooseSymbol.contains(asset.getSymbol()) && asset.getLegalValue() != 0) {
                listChooseAsset.add(asset);
                amount += asset.getLegalValue();
            }
        }
        SPUtils.put(this.context, "amount", String.valueOf(amount));
        view.setMoneyValue(BigDecimal.valueOf(amount));
        view.getmAdapter().setNewData(listChooseAsset);
        view.setWalletCount(CurrencyUtil.getCurrency(this.context).getSymbol() + amount);
        view.setListAsset(listChooseAsset);
        view.getmAdapter().notifyDataSetChanged();
    }
}
