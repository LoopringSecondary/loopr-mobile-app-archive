package leaf.prod.app.fragment.trade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vondear.rxtool.view.RxToast;
import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.activity.trade.P2PTokenListActivity;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.app.presenter.trade.P2PTradePresenter;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.MyViewUtils;
import leaf.prod.walletsdk.manager.P2POrderDataManager;
import leaf.prod.walletsdk.model.TradeType;
import leaf.prod.walletsdk.util.StringUtils;

public class P2PTradeFragment extends BaseFragment {

    public final static int BALANCE_SUCCESS = 1;

    public static String PASSWORD_TYPE = "P2P_ORDER";

    @BindView(R.id.ll_switch)
    public LinearLayout llSwitch;

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

    @BindView(R.id.sell_amount)
    public MaterialEditText sellAmount;

    @BindView(R.id.buy_amount)
    public MaterialEditText buyAmount;

    @BindView(R.id.sell_count)
    public MaterialEditText sellCount;

    @BindView(R.id.market_price)
    public TextView marketPrice;

    @BindView(R.id.tv_sell_token_symbol)
    public TextView tvSellTokenSymbol;

    @BindView(R.id.tv_buy_token_symbol)
    public TextView tvBuyTokenSymbol;

    @BindView(R.id.tv_sell_token_symbol2)
    public TextView tvSellTokenSymbol2;

    @BindView(R.id.tv_buy_token_symbol2)
    public TextView tvBuyTokenSymbol2;

    @BindView(R.id.tv_sell_token_price)
    public TextView tvSellTokenPrice;

    @BindView(R.id.tv_buy_token_price)
    public TextView tvBuyTokenPrice;

    @BindView(R.id.tv_sell_hint)
    public TextView tvSellHint;

    @BindView(R.id.tv_buy_hint)
    public TextView tvBuyHint;

    @BindView(R.id.seek_bar)
    public BubbleSeekBar seekBar;

    Unbinder unbinder;

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

    private P2PTradePresenter presenter;

    private P2POrderDataManager p2pManager;

    private String currentToken, tradeType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_p2p_trade, container, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        presenter = new P2PTradePresenter(this, getContext());
    }

    @Override
    protected void initView() {
        presenter.setSeekbar(0);
        oneHourView.setText(getResources().getString(R.string.hour, "1"));
        oneDayView.setText(getResources().getString(R.string.day, "1"));
        oneMonthView.setText(getResources().getString(R.string.month, "1"));
        sellAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double sellAmountDouble = (editable.toString().isEmpty() || editable.toString()
                        .equals(".") ? 0d : Double.valueOf(editable.toString()));
                if (sellAmountDouble == 0) {
                    presenter.setHint(0);
                } else if (sellAmountDouble > presenter.getMaxAmount()) {
                    presenter.setHint(1);
                } else {
                    presenter.setHint(3);
                }
            }
        });
        buyAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String value = editable.toString();
                if (StringUtils.isEmpty(value)) {
                    presenter.setHint(4);
                } else if (value.equals(".")) {
                    presenter.setHint(5);
                } else {
                    presenter.setHint(6);
                }
            }
        });
    }

    @OnClick({R.id.one_hour, R.id.one_day, R.id.one_month, R.id.custom, R.id.ll_switch, R.id.ll_sell_token, R.id.ll_buy_token, R.id.market_price, R.id.btn_next})
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
            case R.id.ll_switch:
                llSwitch.setOnClickListener(v -> presenter.switchToken());
                break;
            case R.id.ll_sell_token:
                Intent intent1 = new Intent(getContext(), P2PTokenListActivity.class);
                currentToken = tvSellTokenSymbol2.getText().toString();
                tradeType = TradeType.sell.name();
                intent1.putExtra("ignoreSymbol", tvBuyTokenSymbol2.getText().toString());
                intent1.putExtra("tokenType", tradeType);
                startActivityForResult(intent1, 0);
                break;
            case R.id.ll_buy_token:
                Intent intent2 = new Intent(getContext(), P2PTokenListActivity.class);
                currentToken = tvBuyTokenSymbol2.getText().toString();
                tradeType = TradeType.buy.name();
                intent2.putExtra("ignoreSymbol", tvSellTokenSymbol2.getText().toString());
                intent2.putExtra("tokenType", tradeType);
                startActivityForResult(intent2, 1);
                break;
            case R.id.market_price:
                presenter.calMarketSell();
                break;
            case R.id.btn_next:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    Double amountB = (buyAmount.getText().toString().isEmpty() || buyAmount.getText()
                            .toString().equals(".") ? 0d : Double.valueOf(buyAmount.getText().toString()));
                    Double amountS = (sellAmount.getText().toString().isEmpty() || sellAmount.getText()
                            .toString().equals(".") ? 0d : Double.valueOf(sellAmount.getText().toString()));
                    if (amountS == 0) {
                        presenter.setHint(2);
                    } else if (amountS > presenter.getMaxAmount()) {
                        presenter.setHint(1);
                    } else if (amountB == 0) {
                        presenter.setHint(5);
                    } else if (sellCount.getText().toString().isEmpty()) {
                        RxToast.warning(getResources().getString(R.string.minimal_count_error));
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
        p2pManager = P2POrderDataManager.getInstance(getContext());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (TradeType.sell.name().equals(tradeType) && !p2pManager.getTokenSell().equals(currentToken)) {
            presenter.setSeekbar(0);
            buyAmount.setText("");
            presenter.setHint(0);
            tvBuyHint.setText("");
        } else if (TradeType.buy.name().equals(tradeType) && !p2pManager.getTokenBuy().equals(currentToken)) {
            buyAmount.setText("");
            tvBuyHint.setText("");
        }
        presenter.initTokens();
    }
}
