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
import com.lyqb.walletsdk.util.UnitConverter;
import com.tomcat360.lyqb.fragment.MainFragment;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.view.APP;

public class MainFragmentPresenter extends BasePresenter<MainFragment> {

    private Map<String, BalanceResult.Asset> tokenMap = new HashMap<>();

    public MainFragmentPresenter(MainFragment view, Context context) {
        super(view, context);
    }

    public void setTokenLegalPrice(List<BalanceResult.Asset> assetList) {
        while (!initComplete()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LyqbLogger.log(assetList.toString());
        for (BalanceResult.Asset asset : assetList) {
            if (asset.getSymbol().equals("ETH"))
                asset.setValue(UnitConverter.weiToEth(asset.getBalance().toPlainString()).doubleValue());
            if (!asset.getBalance().equals(BigDecimal.ZERO) && getTokenBySymbol(asset.getSymbol()) != null) {
                asset.setValue(asset.getBalance()
                        .divide(getTokenBySymbol(asset.getSymbol()).getDecimals())
                        .doubleValue());
            }
            if (getLegalPriceBySymbol(asset.getSymbol()) != null) {
                asset.setLegalValue(getLegalPriceBySymbol(asset.getSymbol()) * asset.getValue());
            }
            tokenMap.put(asset.getSymbol(), asset);
        }
        for (BalanceResult.Asset asset : assetList) {
            if (asset.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                Log.d("", asset.toString());
            }
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
        updateListToken(assetList);
        view.getmAdapter().notifyDataSetChanged();
    }

    public void updateListToken(List<BalanceResult.Asset> assetList) {
        if (assetList != null) {
            List<BalanceResult.Asset> listChooseAsset = new ArrayList<>();
            List<String> listChooseSymbol = SPUtils.getDataList(this.context, "choose_token");
            for (String symbol : listChooseSymbol) {
                Log.d("", symbol);
            }
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
            view.getWalletCount().setText((String) SPUtils.get(this.context, "coin", "¥") + amount);
            view.setListAsset(listChooseAsset);
        }
    }
}
