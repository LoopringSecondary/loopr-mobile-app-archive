package leaf.prod.app.fragment.market;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.activity.setting.LRCFeeRatioActivity;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.app.presenter.market.MarketTradeFragmentPresenter;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.MyViewUtils;
import leaf.prod.walletsdk.manager.MarketOrderDataManager;
import leaf.prod.walletsdk.model.TradeType;
import leaf.prod.walletsdk.util.StringUtils;

public class MarketTradeFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.ll_sell_token)
    public LinearLayout llSellToken;

    @BindView(R.id.ll_buy_token)
    public LinearLayout llBuyToken;

    @BindView(R.id.one_hour)
    public TextView oneHourView;

    @BindView(R.id.one_day)
    public TextView oneDayView;

    @BindView(R.id.one_month)
    public TextView oneMonthView;

    @BindView(R.id.custom)
    public TextView customView;

    @BindView(R.id.trade_price)
    public MaterialEditText tradePrice;

    @BindView(R.id.trade_amount)
    public MaterialEditText tradeAmount;

    @BindView(R.id.tv_sell_token_symbol)
    public TextView tvSellTokenSymbol;

    @BindView(R.id.tv_buy_token_symbol)
    public TextView tvBuyTokenSymbol;

    @BindView(R.id.tv_price_hint)
    public TextView tvPriceHint;

    @BindView(R.id.tv_amount_hint)
    public TextView tvAmountHint;

    @BindView(R.id.seek_bar)
    public BubbleSeekBar seekBar;

    @BindView(R.id.btn_buy)
    public Button buyButton;

    @BindView(R.id.btn_sell)
    public Button sellButton;

    private TradeType tradeType;

    private MarketTradeFragmentPresenter presenter;

    private MarketOrderDataManager marketManager;

    public final static int BALANCE_SUCCESS = 1;

    public static String PASSWORD_TYPE = "P2P_ORDER";

    @SuppressLint("HandlerLeak")
    Handler handlerBalance = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BALANCE_SUCCESS:
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_market_trade, container, false);
        unbinder = ButterKnife.bind(this, layout);
        marketManager = MarketOrderDataManager.getInstance(getContext());
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        presenter = new MarketTradeFragmentPresenter(this, getContext(), tradeType);
    }
    @Override
    protected void initView() {
        presenter.setSeekbar(0);
        oneHourView.setText(getResources().getString(R.string.hour, "1"));
        oneDayView.setText(getResources().getString(R.string.day, "1"));
        oneMonthView.setText(getResources().getString(R.string.month, "1"));
        setupPriceListener();
        presenter.setupAmountListener();
        setupButton();
    }

    private void setupPriceListener() {
        tradePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String value = editable.toString();
                seekBar.setEnabled(false);
                if (StringUtils.isEmpty(value)) {
                    presenter.setHint(0);
                } else if (value.equals(".") || 0d == Double.valueOf(value)) {
                    presenter.setHint(1);
                } else {
                    presenter.setHint(2);
                    seekBar.setEnabled(true);
                }
            }
        });
    }

    private void setupButton() {
        String title;
        switch (this.tradeType) {
            case buy:
                sellButton.setVisibility(View.GONE);
                buyButton.setVisibility(View.VISIBLE);
                title = getContext().getString(R.string.buy) + " " + marketManager.getTokenBuy();
                buyButton.setText(title);
                break;
            case sell:
                buyButton.setVisibility(View.GONE);
                sellButton.setVisibility(View.VISIBLE);
                title = getContext().getString(R.string.sell) + " " + marketManager.getTokenBuy();
                sellButton.setText(title);
                break;
        }
    }

    @OnClick({R.id.one_hour, R.id.one_day, R.id.one_month, R.id.custom, R.id.ll_sell_token, R.id.ll_buy_token, R.id.ll_lrc_fee, R.id.btn_buy, R.id.btn_sell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.one_hour:
                presenter.setInterval(1);
                break;
            case R.id.one_day:
                presenter.setInterval(24);
                break;
            case R.id.one_month:
                presenter.setInterval(24 * 30);
                break;
            case R.id.custom:
                presenter.setInterval(0);
                break;
            case R.id.ll_sell_token:
                break;
            case R.id.ll_buy_token:
                break;
            case R.id.ll_lrc_fee:
                getOperation().forward(LRCFeeRatioActivity.class);
                break;
            case R.id.btn_buy:
            case R.id.btn_sell:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    Double amount = (tradeAmount.getText().toString().isEmpty() || tradeAmount.getText()
                            .toString().equals(".") ? 0d : Double.valueOf(tradeAmount.getText().toString()));
                    Double price = (tradePrice.getText().toString().isEmpty() || tradePrice.getText()
                            .toString().equals(".") ? 0d : Double.valueOf(tradePrice.getText().toString()));
                    if (price == 0) {
                        presenter.setHint(1);
                    } else if (amount == 0) {
                        presenter.setHint(4);
                    } else {
                        MyViewUtils.hideInput(view);
                        presenter.showTradeDetailDialog();
                    }
                    break;
                }
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroyDialog();
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public Double getAmountBuy() {
        Double result = 0d;
        switch (this.tradeType) {
            case buy:
                result = Double.parseDouble(tradeAmount.getText().toString());
                break;
            case sell:
                result = Double.parseDouble(tradeAmount.getText().toString()) * Double.parseDouble(tradePrice.getText()
                        .toString());
                break;
        }
        return result;
    }

    public Double getAmountSell() {
        Double result = 0d;
        switch (this.tradeType) {
            case buy:
                result = Double.parseDouble(tradeAmount.getText().toString()) * Double.parseDouble(tradePrice.getText()
                        .toString());
                break;
            case sell:
                result = Double.parseDouble(tradeAmount.getText().toString());
                break;
        }
        return result;
    }
}
