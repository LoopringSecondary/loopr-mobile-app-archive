/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-13 下午3:47
 * Cooperation: loopring.org 路印协议基金会
 */
package com.tomcat360.lyqb.manager;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;

import org.web3j.utils.Convert;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lyqb.walletsdk.service.LoopringService;
import com.tomcat360.lyqb.utils.NumberUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GasDataManager {

    static class GasLimit {

        String type;

        Integer gasLimit;
    }

    private Context context;

    private List<GasLimit> gasLimits;

    private BigDecimal recommendGasPrice; // in wei

    private BigDecimal customizeGasPrice; // in wei

    private Observable<BigDecimal> gasObservable;

    private LoopringService loopringService = new LoopringService();

    private static GasDataManager gasDataManager = null;

    private GasDataManager(Context context) {
        this.context = context;
        this.loadGasLimitsFromJson();
        this.getGasPriceFromRelay();
    }

    public static GasDataManager getInstance(Context context) {
        if (gasDataManager == null) {
            gasDataManager = new GasDataManager(context);
        }
        return gasDataManager;
    }

    // get price limit from local json file
    private void loadGasLimitsFromJson() {
        try {
            InputStream is = context.getAssets().open("json/gas_limit.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            parseJsonString(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonString(String jsonData) {
        Gson gson = new Gson();
        this.gasLimits = gson.fromJson(jsonData, new TypeToken<List<GasLimit>>() {
        }.getType());
    }

    // get recommend gas price through relay
    private void getGasPriceFromRelay() {
        if (this.gasObservable == null) {
            this.gasObservable = loopringService
                    .getEstimateGasPrice()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(str -> Convert.fromWei(str, Convert.Unit.WEI));
        }
        this.gasObservable.subscribe(gasPrice -> {
            recommendGasPrice = gasPrice;
        });
    }

    public void setCustomizeGasPriceString(String value) {
        this.customizeGasPrice = Convert.toWei(value, Convert.Unit.WEI);
    }

    public void setCustomizeGasPriceInGWei(Double value) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        this.customizeGasPrice = Convert.toWei(decimal, Convert.Unit.WEI);
    }

    public BigDecimal getRecommendGasPriceInWei() {
        return this.recommendGasPrice;
    }

    public BigDecimal getRecommendGasPriceInGWei() {
        return Convert.toWei(this.recommendGasPrice, Convert.Unit.GWEI);
    }

    public String getRecommendGasPriceString() {
        BigDecimal ether = Convert.toWei(this.recommendGasPrice, Convert.Unit.ETHER);
        int precision = TokenDataManager.getInstance(context).getPrecisionBySymbol("ETH");
        return NumberUtils.format1(ether.doubleValue(), precision);
    }

    public BigDecimal getCustomizeGasPriceInWei() {
        return this.customizeGasPrice;
    }

    public BigDecimal getCustomizeGasPriceInGWei() {
        return Convert.toWei(this.customizeGasPrice, Convert.Unit.GWEI);
    }

    public String getCustomizeGasPriceString() {
        BigDecimal ether = Convert.toWei(this.customizeGasPrice, Convert.Unit.ETHER);
        int precision = TokenDataManager.getInstance(context).getPrecisionBySymbol("ETH");
        return NumberUtils.format1(ether.doubleValue(), precision);
    }

    public double getGasPriceInWei() {
        BigDecimal price = this.customizeGasPrice == null ? recommendGasPrice : customizeGasPrice;
        return price.doubleValue();
    }

    public String getGasPriceString() {
        double gasPrice = getGasPriceInWei();
        int precision = TokenDataManager.getInstance(context).getPrecisionBySymbol("ETH");
        return NumberUtils.format1(gasPrice, precision);
    }

    public Integer getGasLimitByType(String type) {
        Integer result = null;
        for (GasLimit gasLimit : gasLimits) {
            if (gasLimit.type.equalsIgnoreCase(type)) {
                result = gasLimit.gasLimit;
            }
        }
        return result;
    }

    public Double getGasAmountInETH(String type) {
        Double result = null;
        if (getGasLimitByType(type) != null) {
            Double price = getGasPriceInWei();
            Integer limit = getGasLimitByType(type);
            result = price * limit;
        }
        return result;
    }

    public String getGasAmountString(String type) {
        String result = null;
        if (getGasLimitByType(type) != null) {
            Double amount = getGasPriceInWei() *getGasLimitByType(type);
            int precision = TokenDataManager.getInstance(context).getPrecisionBySymbol("ETH");
            result = NumberUtils.format1(amount, precision);
        }
        return result;
    }

    @SuppressLint("DefaultLocale")
    public String description(String type) {
        return String.format("GasPrice: %s * GasLimit: %6d = %s ETH",
                getGasPriceString(), getGasLimitByType(type), getGasAmountString(type));
    }
}
