package leaf.prod.app.presenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.fragment.P2PTradeFragment;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.PasswordDialogUtil;
import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.MarketcapDataManager;
import leaf.prod.walletsdk.manager.P2POrderDataManager;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.DateUtil;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 5:42 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class P2PTradePresenter extends BasePresenter<P2PTradeFragment> {

    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

    @BindView(R.id.first_token)
    TextView firstTokenView;

    @BindView(R.id.second_token)
    TextView secondTokenView;

    @BindView(R.id.token_s_price)
    TextView tokenSPrice;

    @BindView(R.id.token_b_price)
    TextView tokenBPrice;

    @BindView(R.id.sell_token)
    TextView sellTokenView;

    @BindView(R.id.buy_token)
    TextView buyTokenView;

    @BindView(R.id.seek_bar)
    BubbleSeekBar seekBar;

    @BindView(R.id.tv_sell_hint)
    TextView tvSellHint;

    @BindView(R.id.tv_buy_hint)
    TextView tvBuyHint;

    @BindView(R.id.sell_amount)
    MaterialEditText sellAmount;

    @BindView(R.id.buy_amount)
    MaterialEditText buyAmount;

    private String sellPrice = "0", buyPrice = "0";

    private Date validSince;

    private Date validUntil;

    private int timeToLive = 1;

    private List<TextView> intervalList;

    private MarketcapDataManager marketcapDataManager;

    private BalanceDataManager balanceDataManager;

    private TokenDataManager tokenDataManager;

    private P2POrderDataManager p2pOrderManager;

    private OptionsPickerView datePickerView;

    private AlertDialog p2pTradeDialog;

    private View p2pTradeDialogView;

    private Animation shakeAnimation;

    public P2PTradePresenter(P2PTradeFragment view, Context context) {
        super(view, context);
        ButterKnife.bind(this, Objects.requireNonNull(view.getView()));
        marketcapDataManager = MarketcapDataManager.getInstance(context);
        balanceDataManager = BalanceDataManager.getInstance(context);
        tokenDataManager = TokenDataManager.getInstance(context);
        p2pOrderManager = P2POrderDataManager.getInstance(context);
        initTokens("WETH", "LRC");
        shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake_x);
    }

    public void initTokens(String sell, String buy) {
        p2pOrderManager.changeToTokenS(sell);
        p2pOrderManager.changeToTokenB(buy);
        firstTokenView.setText(p2pOrderManager.getTokenS());
        secondTokenView.setText(p2pOrderManager.getTokenB());
        sellTokenView.setText(p2pOrderManager.getTokenS());
        buyTokenView.setText(p2pOrderManager.getTokenB());
        Double tokenPrice1 = marketcapDataManager.getPriceBySymbol(p2pOrderManager.getTokenS());
        Double tokenPrice2 = marketcapDataManager.getPriceBySymbol(p2pOrderManager.getTokenB());
        sellPrice = NumberUtils.format1(tokenPrice1 / tokenPrice2, 8);
        buyPrice = NumberUtils.format1(tokenPrice2 / tokenPrice1, 8);
        tokenSPrice.setText(" 1 " + p2pOrderManager.getTokenS() + " ≈ " + sellPrice + " " + p2pOrderManager.getTokenB());
        tokenBPrice.setText("1 " + p2pOrderManager.getTokenB() + " ≈ " + buyPrice + " " + p2pOrderManager.getTokenS());
        setHint(0);
    }

    public void switchToken() {
        p2pOrderManager.swapToken();
        String tPrice = sellPrice;
        firstTokenView.setText(p2pOrderManager.getTokenS());
        secondTokenView.setText(p2pOrderManager.getTokenB());
        tokenBPrice.setText(" 1 " + p2pOrderManager.getTokenS() + " ≈ " + sellPrice + " " + p2pOrderManager.getTokenB());
        tokenSPrice.setText("1 " + p2pOrderManager.getTokenB() + " ≈ " + buyPrice + " " + p2pOrderManager.getTokenS());
        sellTokenView.setText(p2pOrderManager.getTokenB());
        buyTokenView.setText(p2pOrderManager.getTokenS());
        sellPrice = buyPrice;
        buyPrice = tPrice;
        sellAmount.setText("");
        buyAmount.setText("");
        seekBar.setProgress(0);
    }

    /**
     * 金额拖动条
     */
    public void initSeekbar() {
        seekBar.setProgress(0);
        seekBar.setCustomSectionTextArray((sectionCount, array) -> {
            array.clear();
            array.put(0, "0%");
            array.put(1, "25%");
            array.put(2, "50%");
            array.put(3, "75%");
            array.put(4, "100%");
            return array;
        });
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                BalanceResult.Asset asset = balanceDataManager.getAssetBySymbol(p2pOrderManager.getTokenS());
                sellAmount.setText(NumberUtils.format1(asset.getValue() * progressFloat / 100, asset.getPrecision()));
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }
        });
    }

    public void setInterval(int index) {
        if (intervalList == null) {
            intervalList = new ArrayList<>();
            intervalList.add(view.getView().findViewById(R.id.one_hour));
            intervalList.add(view.getView().findViewById(R.id.one_day));
            intervalList.add(view.getView().findViewById(R.id.one_month));
            intervalList.add(view.getView().findViewById(R.id.custom));
        }
        List<Integer> dates = new ArrayList<>();
        List<String> dateType = Arrays.asList(view.getResources().getString(R.string.hour, ""),
                view.getResources().getString(R.string.day, ""),
                view.getResources().getString(R.string.month, ""));
        for (int i = 1; i <= 24; ++i) {
            dates.add(i);
        }
        for (int i = 0; i < intervalList.size(); ++i) {
            intervalList.get(i)
                    .setTextColor(i == index ? view.getResources().getColor(R.color.colorWhite) : view.getResources()
                            .getColor(R.color.colorNineText));
        }
        timeToLive = index == 0 ? 1 : (index == 1 ? 24 : (index == 2 ? 24 * 30 : 1));
        if (index == 3) {
            if (datePickerView == null) {
                datePickerView = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> {
                }).setBgColor(view.getResources().getColor(R.color.colorTitleBac))
                        .setTitleBgColor(view.getResources().getColor(R.color.colorTitleBac))
                        .setTitleText(view.getResources().getString(R.string.expiry_date))
                        .setTitleColor(view.getResources().getColor(R.color.colorNineText))
                        .setDividerColor(view.getResources().getColor(R.color.colorBg))
                        .setTextColorCenter(view.getResources().getColor(R.color.colorWhite))
                        .setCancelText(" ")
                        .setTitleSize(14)
                        .setSubCalSize(12)
                        .setContentTextSize(15)
                        .setLineSpacingMultiplier(2)
                        .setOptionsSelectChangeListener((options1, options2, options3) -> {
                            dates.clear();
                            int amount = options2 == 0 ? 24 : (options2 == 1 ? 30 : 12);
                            for (int i = 1; i <= amount; ++i) {
                                dates.add(i);
                            }
                            datePickerView.setSelectOptions(options1 > dates.size() - 1 ? 0 : options1, options2);
                            datePickerView.setNPicker(dates, dateType, null);
                            timeToLive = dates.get(options1) * (options2 == 0 ? 1 : (options2 == 1 ? 24 : 24 * 30));
                            LyqbLogger.log(timeToLive + "");
                        }).build();
                datePickerView.setNPicker(dates, dateType, null);
            }
            datePickerView.show();
        }
    }

    public void calMarketSell() {
        try {
            Double amountS = Double.parseDouble(sellAmount.getText().toString());
            Double priceS = Double.parseDouble(sellPrice);
            int precision = balanceDataManager.getAssetBySymbol(p2pOrderManager.getTokenB()).getPrecision();
            buyAmount.setText(NumberUtils.format1(amountS * priceS, precision));
        } catch (Exception e) {
            buyAmount.setText("0");
        }
    }

    /**
     * 下单弹窗
     */
    public void showTradeDetailDialog() {
        if (p2pTradeDialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);
            p2pTradeDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_p2p_trade_detail, null);
            p2pTradeDialogView.findViewById(R.id.btn_cancel).setOnClickListener(view1 -> p2pTradeDialog.hide());
            p2pTradeDialogView.findViewById(R.id.btn_order).setOnClickListener(view1 -> {
                if (WalletUtil.needPassword(context)) {
                    PasswordDialogUtil.showPasswordDialog(context, v -> processMaker(PasswordDialogUtil.getInputPassword()));
                } else {
                    processMaker("");
                }
            });
            builder.setView(p2pTradeDialogView);
            builder.setCancelable(true);
            p2pTradeDialog = builder.create();
            p2pTradeDialog.setCancelable(true);
            p2pTradeDialog.setCanceledOnTouchOutside(true);
            p2pTradeDialog.getWindow().setGravity(Gravity.CENTER);
        }
        ((ImageView) p2pTradeDialogView.findViewById(R.id.iv_token_s)).setImageDrawable(view.getResources()
                .getDrawable(tokenDataManager.getTokenBySymbol(p2pOrderManager.getTokenS()).getImageResId()));
        ((ImageView) p2pTradeDialogView.findViewById(R.id.iv_token_b)).setImageDrawable(view.getResources()
                .getDrawable(tokenDataManager.getTokenBySymbol(p2pOrderManager.getTokenB()).getImageResId()));
        ((TextView) p2pTradeDialogView.findViewById(R.id.tv_sell_token)).setText(view.getResources()
                .getString(R.string.sell) + " " + p2pOrderManager.getTokenS());
        ((TextView) p2pTradeDialogView.findViewById(R.id.tv_buy_token)).setText(view.getResources()
                .getString(R.string.buy) + " " + p2pOrderManager.getTokenB());
        ((TextView) p2pTradeDialogView.findViewById(R.id.tv_sell_price)).setText(CurrencyUtil.format(context, marketcapDataManager
                .getPriceBySymbol(p2pOrderManager.getTokenS()) * Double.parseDouble(sellAmount.getText()
                .toString())));
        ((TextView) p2pTradeDialogView.findViewById(R.id.tv_buy_price)).setText(CurrencyUtil.format(context, marketcapDataManager
                .getPriceBySymbol(p2pOrderManager.getTokenB()) * Double.parseDouble(buyAmount.getText()
                .toString())));
        ((TextView) p2pTradeDialogView.findViewById(R.id.tv_sell_amount)).setText(sellAmount.getText());
        ((TextView) p2pTradeDialogView.findViewById(R.id.tv_buy_amount)).setText(buyAmount.getText());
        ((TextView) p2pTradeDialogView.findViewById(R.id.tv_price)).setText(NumberUtils.format1(Double.parseDouble(sellAmount
                .getText().toString()) / Double.parseDouble(buyAmount.getText()
                .toString()), 8) + " " + p2pOrderManager.getTokenS() + "/" + p2pOrderManager.getTokenB());
        ((TextView) p2pTradeDialogView.findViewById(R.id.tv_trading_fee)).setText("");
        ((TextView) p2pTradeDialogView.findViewById(R.id.tv_margin_split)).setText("50%");
        validSince = new Date();
        validUntil = DateUtil.addDateTime(validSince, timeToLive);
        ((TextView) p2pTradeDialogView.findViewById(R.id.tv_live_time)).setText(sdf.format(validSince) + " ~ " +
                sdf.format(validUntil));
        p2pTradeDialog.show();
    }

    public void setHint(int flag) {
        tvBuyHint.setVisibility(View.INVISIBLE);
        switch (flag) {
            case 0:
                tvSellHint.setText(view.getResources().getString(R.string.available_balance,
                        balanceDataManager.getAssetBySymbol(p2pOrderManager.getTokenS())
                                .getValueShown()) + " " + p2pOrderManager.getTokenS());
                tvSellHint.setTextColor(view.getResources().getColor(R.color.colorNineText));
                break;
            case 1:
                tvSellHint.setText(view.getResources().getString(R.string.available_balance,
                        balanceDataManager.getAssetBySymbol(p2pOrderManager.getTokenS())
                                .getValueShown()) + " " + p2pOrderManager.getTokenS());
                tvSellHint.setTextColor(view.getResources().getColor(R.color.colorRed));
                tvSellHint.startAnimation(shakeAnimation);
                break;
            case 2:
                tvSellHint.setText(view.getResources().getString(R.string.input_valid_amount));
                tvSellHint.setTextColor(view.getResources().getColor(R.color.colorRed));
                tvSellHint.startAnimation(shakeAnimation);
                break;
            case 3:
                tvSellHint.setText(CurrencyUtil.format(context, marketcapDataManager.getPriceBySymbol(p2pOrderManager
                        .getTokenS()) *
                        Double.parseDouble(sellAmount.getText().toString())));
                tvSellHint.setTextColor(view.getResources().getColor(R.color.colorNineText));
                break;
            case 4:
                tvBuyHint.setText(view.getResources().getString(R.string.input_valid_amount));
                tvBuyHint.setTextColor(view.getResources().getColor(R.color.colorRed));
                tvBuyHint.setVisibility(View.VISIBLE);
                tvBuyHint.startAnimation(shakeAnimation);
        }
    }

    public double getMaxAmount() {
        return balanceDataManager.getAssetBySymbol(p2pOrderManager.getTokenS()).getValue();
    }

    public void processMaker(String password) {
        Double amountBuy = Double.parseDouble(buyAmount.getText().toString());
        Double amountSell = Double.parseDouble(sellAmount.getText().toString());
        Integer validS = (int) (validSince.getTime() / 1000);
        Integer validU = (int) (validUntil.getTime() / 1000);
        Integer sellCount = Integer.parseInt(view.sellCount.getText().toString());
        OriginOrder order = null;
        try {
            order = p2pOrderManager.constructMaker(amountBuy, amountSell, validS, validU, sellCount, password);
        } catch (Exception e) {
            // TODO: for yanyan: MUST handle exception of incorrect password
            e.printStackTrace();
        }
        p2pOrderManager.verify(order);
        if (!p2pOrderManager.isBalanceEnough()) {
            // TODO: for yanyan: balance not enough
            // p2pOrderManager.balanceInfo e.g. {"MINUS_ETH": 0.3974, "MINUS_LRC": 10.3974}
        }

        p2pOrderManager.handleInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getError() == null) {
                        // TODO: for yanyan submit order success
                    } else {
                        // TODO: for yanyan submit order failed
                    }
                });
    }
}
