/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 5:42 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter.market;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
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
import com.vondear.rxtool.view.RxToast;
import com.xw.repo.BubbleSeekBar;

import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.trade.P2PErrorActivity;
import leaf.prod.app.activity.trade.P2PTradeQrActivity;
import leaf.prod.app.fragment.market.MarketTradeFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.app.utils.PasswordDialogUtil;
import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.MarketOrderDataManager;
import leaf.prod.walletsdk.manager.MarketcapDataManager;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.DateUtil;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MarketTradeFragmentPresenter extends BasePresenter<MarketTradeFragment> {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

    private MarketcapDataManager marketcapDataManager;

    private BalanceDataManager balanceDataManager;

    private TokenDataManager tokenDataManager;

    private MarketOrderDataManager marketOrderDataManager;

    private OptionsPickerView datePickerView;

    private AlertDialog marketTradeDialog;

    private View marketTradeDialogView;

    private Animation shakeAnimation;

    public MarketTradeFragmentPresenter(MarketTradeFragment view, Context context) {
        super(view, context);
        ButterKnife.bind(this, Objects.requireNonNull(view.getView()));
        marketcapDataManager = MarketcapDataManager.getInstance(context);
        balanceDataManager = BalanceDataManager.getInstance(context);
        tokenDataManager = TokenDataManager.getInstance(context);
        marketOrderDataManager = MarketOrderDataManager.getInstance(context);
        shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake_x);
        initTokens();
    }

    @SuppressLint("SetTextI18n")
    public void initTokens() {
        view.tvSellTokenSymbol.setText(marketOrderDataManager.getTokenSell());
        view.tvBuyTokenSymbol.setText(marketOrderDataManager.getTokenBuy());
        setInterval((int) SPUtils.get(context, "time_to_live", 1));
        setHint(0);
    }

    /**
     * 金额拖动条
     */
    public void setSeekbar(int progress) {
        view.seekBar.setProgress(progress);
        view.seekBar.setCustomSectionTextArray((sectionCount, array) -> {
            array.clear();
            array.put(0, "0%");
            array.put(1, "25%");
            array.put(2, "50%");
            array.put(3, "75%");
            array.put(4, "100%");
            return array;
        });
        view.seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                BalanceResult.Asset asset = balanceDataManager.getAssetBySymbol(marketOrderDataManager.getTokenSell());
                view.tradePrice.setText(NumberUtils.format1(asset.getValue() * progressFloat / 100, asset.getPrecision()));
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }
        });
    }

    public void setInterval(int hours) {
        if (hours != 0) {
            SPUtils.put(context, "time_to_live", hours);
        }
        view.oneHourView.setTextColor(view.getResources().getColor(R.color.colorNineText));
        view.oneDayView.setTextColor(view.getResources().getColor(R.color.colorNineText));
        view.oneMonthView.setTextColor(view.getResources().getColor(R.color.colorNineText));
        view.customView.setTextColor(view.getResources().getColor(R.color.colorNineText));
        view.oneHourView.setTypeface(null, Typeface.NORMAL);
        view.oneDayView.setTypeface(null, Typeface.NORMAL);
        view.oneMonthView.setTypeface(null, Typeface.NORMAL);
        view.customView.setTypeface(null, Typeface.NORMAL);
        switch (hours) {
            case 1:
                view.oneHourView.setTextColor(view.getResources().getColor(R.color.colorWhite));
                view.oneHourView.setTypeface(null, Typeface.BOLD);
                break;
            case 24:
                view.oneDayView.setTextColor(view.getResources().getColor(R.color.colorWhite));
                view.oneDayView.setTypeface(null, Typeface.BOLD);
                break;
            case 24 * 30:
                view.oneMonthView.setTextColor(view.getResources().getColor(R.color.colorWhite));
                view.oneMonthView.setTypeface(null, Typeface.BOLD);
                break;
            default:
                view.customView.setTextColor(view.getResources().getColor(R.color.colorWhite));
                view.customView.setTypeface(null, Typeface.BOLD);
                List<Integer> dates = new ArrayList<>();
                List<String> dateType = Arrays.asList(view.getResources().getString(R.string.hour, ""),
                        view.getResources().getString(R.string.day, ""),
                        view.getResources().getString(R.string.month, ""));
                for (int i = 1; i <= 24; ++i) {
                    dates.add(i);
                }
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
                                datePickerView.setNPicker(dates, dateType, null);
                                options1 = options1 > dates.size() - 1 ? 0 : options1;
                                datePickerView.setSelectOptions(options1, options2);
                                SPUtils.put(context, "time_to_live", dates.get(options1) * (options2 == 0 ? 1 : (options2 == 1 ? 24 : 24 * 30)));
                            }).build();
                    datePickerView.setNPicker(dates, dateType, null);
                }
                if (hours != 0) {
                    dates.clear();
                    if (hours / 24 <= 1) {
                        for (int i = 1; i <= 24; ++i) {
                            dates.add(i);
                        }
                        datePickerView.setNPicker(dates, dateType, null);
                        datePickerView.setSelectOptions((hours - 1) % 24, 0);
                    } else if ((hours / 24) <= 30) {
                        for (int i = 1; i <= 30; ++i) {
                            dates.add(i);
                        }
                        datePickerView.setNPicker(dates, dateType, null);
                        datePickerView.setSelectOptions(((hours / 24) - 1) % 30, 1);
                    } else {
                        for (int i = 1; i <= 12; ++i) {
                            dates.add(i);
                        }
                        datePickerView.setNPicker(dates, dateType, null);
                        datePickerView.setSelectOptions(((hours / 24 / 30) - 1) % 12, 2);
                    }
                } else {
                    datePickerView.show();
                }
                break;
        }
    }

    /**
     * 下单弹窗
     */
    @SuppressLint("SetTextI18n")
    public void showTradeDetailDialog() {
        setupDialog();
        OriginOrder order = constuctOrder();
        setToken(order);
        setPrice(order);
        setValidTime(order);
        marketTradeDialog.show();
    }

    private void setToken(OriginOrder order) {
        int tokenBID = tokenDataManager.getTokenBySymbol(order.getTokenB()).getImageResId();
        int tokenSID = tokenDataManager.getTokenBySymbol(order.getTokenS()).getImageResId();
        String tokenBTip = view.getResources().getString(R.string.buy) + " " + order.getTokenB();
        String tokenSTip = view.getResources().getString(R.string.sell) + " " + order.getTokenS();

        ((ImageView) marketTradeDialogView.findViewById(R.id.iv_token_b)).setImageDrawable(view.getResources().getDrawable(tokenBID));
        ((ImageView) marketTradeDialogView.findViewById(R.id.iv_token_s)).setImageDrawable(view.getResources().getDrawable(tokenSID));
        ((TextView) marketTradeDialogView.findViewById(R.id.tv_buy_token)).setText(tokenBTip);
        ((TextView) marketTradeDialogView.findViewById(R.id.tv_sell_token)).setText(tokenSTip);
    }

    private void setPrice(OriginOrder order) {
        String amountB = NumberUtils.format7(order.getAmountBuy(), 0, 6);
        String amountS = NumberUtils.format7(order.getAmountSell(), 0, 6);
        String amountBPrice = CurrencyUtil.format(context, marketcapDataManager
                .getPriceBySymbol(order.getTokenB()) * order.getAmountBuy());
        String amountSPrice = CurrencyUtil.format(context, marketcapDataManager
                .getPriceBySymbol(order.getTokenS()) * order.getAmountSell());
        String priceQuote = view.tradePrice.getText() + " " + marketOrderDataManager.getTradePair();
        String lrcFee = NumberUtils.format1(order.getLrc(), 3) +
                " LRC ≈ " + CurrencyUtil.format(context, marketcapDataManager.getAmountBySymbol("LRC", order.getLrc()));

        ((TextView) marketTradeDialogView.findViewById(R.id.tv_buy_amount)).setText(amountB);
        ((TextView) marketTradeDialogView.findViewById(R.id.tv_sell_amount)).setText(amountS);
        ((TextView) marketTradeDialogView.findViewById(R.id.tv_buy_price)).setText(amountBPrice);
        ((TextView) marketTradeDialogView.findViewById(R.id.tv_sell_price)).setText(amountSPrice);
        ((TextView) marketTradeDialogView.findViewById(R.id.tv_price)).setText(priceQuote);
        ((TextView) marketTradeDialogView.findViewById(R.id.tv_trading_fee)).setText(lrcFee);
    }

    private void setValidTime(OriginOrder order) {
        String validSince = sdf.format(order.getValidS());
        String validUntil = sdf.format(order.getValidU());
        ((TextView) marketTradeDialogView.findViewById(R.id.tv_live_time)).setText(validSince + " ~ " + validUntil);
    }

    private OriginOrder constuctOrder() {
        Double amountBuy = view.getAmountBuy();
        Double amountSell = view.getAmountSell();
        Date now = new Date();
        Integer validS = (int) (now.getTime() / 1000);
        int time = (int) SPUtils.get(context, "time_to_live", 1);
        Integer validU = (int) (DateUtil.addDateTime(now, time).getTime() / 1000);
        return marketOrderDataManager.constructOrder(amountBuy, amountSell, validS, validU);
    }

    private void setupDialog() {
        if (marketTradeDialog == null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
            marketTradeDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_p2p_trade_detail, null);
            marketTradeDialogView.findViewById(R.id.btn_cancel).setOnClickListener(view1 -> marketTradeDialog.hide());
            marketTradeDialogView.findViewById(R.id.btn_order).setOnClickListener(view1 -> {
                if (WalletUtil.needPassword(context)) {
                    PasswordDialogUtil.showPasswordDialog(view.getActivity(), MarketTradeFragment.PASSWORD_TYPE, v -> {
                        view.showProgress(view.getResources().getString(R.string.loading_default_messsage));
                        processOrder(PasswordDialogUtil.getInputPassword());
                    });
                } else {
                    processOrder("");
                }
            });
            builder.setView(marketTradeDialogView);
            builder.setCancelable(true);
            marketTradeDialog = builder.create();
            marketTradeDialog.setCancelable(true);
            marketTradeDialog.setCanceledOnTouchOutside(true);
            marketTradeDialog.getWindow().setGravity(Gravity.CENTER);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setHint(int flag) {
        switch (flag) {
            case 0: // empty
                view.tvPriceHint.setVisibility(View.INVISIBLE);
                break;
            case 1: // 0
                view.tvPriceHint.setVisibility(View.VISIBLE);
                view.tvPriceHint.setText(view.getResources().getString(R.string.input_valid_amount));
                view.tvPriceHint.setTextColor(view.getResources().getColor(R.color.colorRed));
                view.tvPriceHint.startAnimation(shakeAnimation);
                break;
            case 2: // shuzi
                view.tvPriceHint.setVisibility(View.VISIBLE);
                view.tvPriceHint.setText("≈" + CurrencyUtil.format(context, marketcapDataManager.getPriceBySymbol(marketOrderDataManager
                        .getTokenB()) * Double.parseDouble(view.tradePrice.getText().toString())));
                view.tvPriceHint.setTextColor(view.getResources().getColor(R.color.colorNineText));
                break;
            case 3: // empty
                view.tvAmountHint.setVisibility(View.INVISIBLE);
                break;
            case 4: // 0
                view.tvAmountHint.setVisibility(View.VISIBLE);
                view.tvAmountHint.setText(view.getResources().getString(R.string.input_valid_amount));
                view.tvAmountHint.setTextColor(view.getResources().getColor(R.color.colorRed));
                view.tvAmountHint.startAnimation(shakeAnimation);
                break;
            case 5: // shuzi
                view.tvAmountHint.setVisibility(View.VISIBLE);
                view.tvAmountHint.setText(view.getResources().getString(R.string.available_balance,
                        balanceDataManager.getAssetBySymbol(marketOrderDataManager.getTokenA())
                                .getValueShown()) + " " + marketOrderDataManager.getTokenA());
                view.tvAmountHint.setTextColor(view.getResources().getColor(R.color.colorNineText));
                break;
        }
    }

    public void destroyDialog() {
        if (marketTradeDialog != null) {
            marketTradeDialog.dismiss();
            PasswordDialogUtil.dismiss(MarketTradeFragment.PASSWORD_TYPE);
        }
    }

    public void processOrder(String password) {
        try {
            marketOrderDataManager.verify(password);
        } catch (Exception e) {
            view.hideProgress();
            PasswordDialogUtil.clearPassword();
            RxToast.error(context.getResources().getString(R.string.keystore_psw_error));
            e.printStackTrace();
            return;
        }
        if (!marketOrderDataManager.isBalanceEnough()) {
            Objects.requireNonNull(view.getActivity()).finish();
            view.getOperation().forward(P2PErrorActivity.class);
        } else {
            marketOrderDataManager.handleInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        view.getActivity().finish();
                        if (response.getError() == null) {
                            view.getOperation().forward(P2PTradeQrActivity.class);
                        } else {
                            view.getOperation().addParameter("error", response.getError().getMessage());
                            view.getOperation().forward(P2PErrorActivity.class);
                        }
                        view.hideProgress();
                    });
        }
    }
}
