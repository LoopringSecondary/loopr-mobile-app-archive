/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-10 下午4:16
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter.wallet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import leaf.prod.app.fragment.wallet.MainWalletFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.MarketcapDataManager;
import leaf.prod.walletsdk.manager.PartnerDataManager;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.wallet.WalletEntity;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.model.response.relay.MarketcapResult;
import leaf.prod.walletsdk.model.token.Token;
import leaf.prod.walletsdk.service.RelayService;
import leaf.prod.walletsdk.service.RelayService;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainFragmentPresenter extends BasePresenter<MainWalletFragment> {

    private static RelayService loopringService;

    private static Observable<MarketcapResult> marketcapObservable;

    private static Observable<BalanceResult> balanceObservable;

    private Map<String, BalanceResult.Asset> tokenMap = new HashMap<>();

    private TokenDataManager tokenDataManager;

    private MarketcapDataManager marketcapDataManager;

    private BalanceDataManager balanceDataManager;

    private PartnerDataManager partnerDataManager;

    private List<BalanceResult.Asset> listAsset = new ArrayList<>(); //  返回的token列表

    private String moneyValue;

    private String address;

    private RelayService relayService;

    public MainFragmentPresenter(MainWalletFragment view, Context context) {
        super(view, context);
        marketcapDataManager = MarketcapDataManager.getInstance(context);
        tokenDataManager = TokenDataManager.getInstance(context);
        balanceDataManager = BalanceDataManager.getInstance(context);
        partnerDataManager = PartnerDataManager.getInstance(context);
        partnerDataManager.activatePartner();
        partnerDataManager.createPartner();
        relayService = new RelayService();
        //        test();
    }
    //
    //    public void test() {
    //        //        Observable<AccountBalance> observable = relayService.getAccounts(Collections.emptyList(), Collections.emptyList(), true);
    //        //        observable.subscribeOn(Schedulers.io())
    //        //                .observeOn(AndroidSchedulers.mainThread())
    //        //                .subscribe(new Subscriber<AccountBalance>() {
    //        //                    @Override
    //        //                    public void onCompleted() {
    //        //                    }
    //        //
    //        //                    @Override
    //        //                    public void onError(Throwable e) {
    //        //                        LyqbLogger.log(e.getMessage());
    //        //                    }
    //        //
    //        //                    @Override
    //        //                    public void onNext(AccountBalance accountBalance) {
    //        //                        accountBalance.convert();
    //        //                        LyqbLogger.log(accountBalance.toString());
    //        //                    }
    //        // });
    //        Observable<FillsResult> observable = relayService.getUserFills("", "", "", "", null);
    //        observable.subscribeOn(Schedulers.io())
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .subscribe(new Subscriber<FillsResult>() {
    //                    @Override
    //                    public void onCompleted() {
    //                    }
    //
    //                    @Override
    //                    public void onError(Throwable e) {
    //                        LyqbLogger.log(e.getMessage());
    //                    }
    //
    //                    @Override
    //                    public void onNext(FillsResult fillsResult) {
    //                        fillsResult.convert();
    //                        LyqbLogger.log(fillsResult.toString());
    //                    }
    //                });
    //    }

    public void initObservable() {
        LyqbLogger.log("initObservable: " + getAddress());
        if (loopringService == null)
            loopringService = new RelayService();
        Observable.zip(loopringService.getBalance(getAddress())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()), loopringService.getCustomToken(getAddress())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()), loopringService.getMarketcap(CurrencyUtil.getCurrency(context)
                        .getText())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                CombineObservable::getInstance)
                .subscribe(new Subscriber<CombineObservable>() {
                    @Override
                    public void onCompleted() {
                        view.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //                        handleNetworkError();
                    }

                    @Override
                    public void onNext(CombineObservable combineObservable) {
                        marketcapDataManager.setMarketcapResult(combineObservable.getMarketcapResult());
                        tokenDataManager.mergeTokens(combineObservable.getTokenList());
                        balanceDataManager.mergeAssets(combineObservable.getBalanceResult());
                        setTokenLegalPrice();
                        unsubscribe();
                    }
                });
    }

    public void initPushService() {
        if (marketcapObservable == null) {
            marketcapDataManager.getObservable().subscribe(marketcapResult -> {
                if (marketcapObservable != null) {
                    marketcapDataManager.setMarketcapResult(marketcapResult);
                    balanceDataManager.mergeAssets(balanceDataManager.getBalance());
                    setTokenLegalPrice();
                    view.finishRefresh();
                } else
                    marketcapObservable = marketcapDataManager.getObservable();
            }, error -> {
            });
        }
        if (balanceObservable == null) {
            balanceDataManager.getObservable().subscribe(balanceResult -> {
                if (balanceObservable != null) {
                    balanceDataManager.mergeAssets(balanceResult);
                    setTokenLegalPrice();
                    view.finishRefresh();
                } else {
                    balanceObservable = balanceDataManager.getBalanceObservable();
                }
            }, error -> {
            });
        }
    }

    public void destroy() {
        if (marketcapObservable != null) {
            marketcapObservable.unsubscribeOn(Schedulers.io());
            marketcapObservable = null;
        }
        if (balanceObservable != null) {
            balanceObservable.unsubscribeOn(Schedulers.io());
            balanceObservable = null;
        }
    }

    private void setTokenLegalPrice() {
        for (BalanceResult.Asset asset : balanceDataManager.getAssets()) {
            tokenMap.put(asset.getSymbol(), asset);
        }
        Collections.sort(balanceDataManager.getAssets(), (o1, o2) -> Double.compare(o2.getLegalValue(), o1.getLegalValue()));
        List<BalanceResult.Asset> listChooseAsset = new ArrayList<>(), positiveList = new ArrayList<>(), zeroList = new ArrayList<>();
        List<String> listChooseSymbol = WalletUtil.getChooseTokens(context);
        double amount = 0;
        for (String symbol : listChooseSymbol) {
            listChooseAsset.add(tokenMap.get(symbol));
        }
        for (BalanceResult.Asset asset : balanceDataManager.getAssets()) {
            if (!listChooseSymbol.contains(asset.getSymbol()) && asset.getLegalValue() != 0) {
                listChooseAsset.add(asset);
            }
        }
        // 根据金额拆分列表
        for (BalanceResult.Asset asset : listChooseAsset) {
            if (Arrays.asList("ETH", "WETH", "LRC").contains(asset.getSymbol()))
                continue;
            if (asset.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                positiveList.add(asset);
            } else {
                zeroList.add(asset);
            }
        }
        Collections.sort(positiveList, (asset, t1) -> asset.getSymbol().compareTo(t1.getSymbol()));
        Collections.sort(zeroList, (asset, t1) -> asset.getSymbol().compareTo(t1.getSymbol()));
        listChooseAsset.clear();
        for (String symbol : Arrays.asList("ETH", "WETH", "LRC")) {
            BalanceResult.Asset asset = tokenMap.get(symbol);
            listChooseAsset.add(asset);
            amount += asset.getLegalValue();
        }
        for (BalanceResult.Asset asset : positiveList) {
            listChooseAsset.add(asset);
            amount += asset.getLegalValue();
        }
        for (BalanceResult.Asset asset : zeroList) {
            listChooseAsset.add(asset);
            amount += asset.getLegalValue();
        }
        moneyValue = CurrencyUtil.format(context, amount);
        SPUtils.put(this.context, "amount", moneyValue);
        SPUtils.put(this.context, "amountValue", amount + "");
        listAsset = listChooseAsset;
        view.getmAdapter().setNewData(listChooseAsset);
        view.setWalletCount(moneyValue);
        view.getmAdapter().notifyDataSetChanged();
        /**
         * 更新钱包信息
         */
        WalletEntity myWallet = WalletUtil.getCurrentWallet(context);
        myWallet.setAmount(amount);
        myWallet.setAmountShow(moneyValue);
        WalletUtil.updateWallet(context, myWallet);
    }

    public List<BalanceResult.Asset> getListAsset() {
        return listAsset;
    }

    public String getAddress() {
        if (address == null) {
            WalletEntity wallet = WalletUtil.getCurrentWallet(context);
            return wallet != null ? wallet.getAddress() : "";
        }
        return address;
    }

    public String getWalletName() {
        WalletEntity walletEntity = WalletUtil.getCurrentWallet(context);
        if (walletEntity == null)
            return null;
        return walletEntity.getWalletname();
    }

    public String getMoneyValue() {
        return moneyValue;
    }

    private static class CombineObservable {

        private static CombineObservable combineObservable;

        private BalanceResult balanceResult;

        private List<Token> tokenList;

        private MarketcapResult marketcapResult;

        private CombineObservable() {
        }

        private CombineObservable(BalanceResult balanceResult, List<Token> tokenList, MarketcapResult marketcapResult) {
            this.balanceResult = balanceResult;
            this.tokenList = tokenList;
            this.marketcapResult = marketcapResult;
        }

        public static CombineObservable getInstance(BalanceResult balanceResult, List<Token> tokenList, MarketcapResult marketcapResult) {
            if (combineObservable == null) {
                return new CombineObservable(balanceResult, tokenList, marketcapResult);
            }
            combineObservable.setBalanceResult(balanceResult);
            combineObservable.setMarketcapResult(marketcapResult);
            combineObservable.setTokenList(tokenList);
            return combineObservable;
        }

        public BalanceResult getBalanceResult() {
            return balanceResult;
        }

        public void setBalanceResult(BalanceResult balanceResult) {
            this.balanceResult = balanceResult;
        }

        public List<Token> getTokenList() {
            return tokenList;
        }

        public void setTokenList(List<Token> tokenList) {
            this.tokenList = tokenList;
        }

        public MarketcapResult getMarketcapResult() {
            return marketcapResult;
        }

        public void setMarketcapResult(MarketcapResult marketcapResult) {
            this.marketcapResult = marketcapResult;
        }
    }
}
