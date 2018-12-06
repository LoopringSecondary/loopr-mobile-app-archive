package leaf.prod.app.fragment;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.activity.P2PTokenListActivity;
import leaf.prod.app.presenter.P2PTradePresenter;
import leaf.prod.app.utils.ButtonClickUtil;

public class P2PTradeFragment extends BaseFragment {

    public final static int BALANCE_SUCCESS = 1;

    Unbinder unbinder;

    @BindView(R.id.ll_switch)
    LinearLayout llSwitch;

    @BindView(R.id.ll_sell_token)
    LinearLayout llSellToken;

    @BindView(R.id.ll_buy_token)
    LinearLayout llBuyToken;

    @BindView(R.id.one_hour)
    TextView oneHourView;

    @BindView(R.id.one_day)
    TextView oneDayView;

    @BindView(R.id.one_month)
    TextView oneMonthView;

    @BindView(R.id.custom)
    TextView customView;

    @BindView(R.id.sell_token)
    TextView sellTokenView;

    @BindView(R.id.buy_token)
    TextView buyTokenView;

    @BindView(R.id.sell_amount)
    MaterialEditText sellAmount;

    @BindView(R.id.buy_amount)
    MaterialEditText buyAmount;

    @BindView(R.id.sell_count)
    public MaterialEditText sellCount;

    @BindView(R.id.market_price)
    TextView marketPrice;

    private P2PTradePresenter presenter;

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
        presenter.initSeekbar();
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
                if (!editable.toString().isEmpty() && !editable.toString().equals(".")) {
                    presenter.setHint(5);
                } else {
                    presenter.setHint(6);
                }
            }
        });
        sellAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty() && !editable.toString().equals(".")) {
                    presenter.setHint(3);
                }
            }
        });
    }

    @OnClick({R.id.one_hour, R.id.one_day, R.id.one_month, R.id.custom, R.id.ll_switch, R.id.ll_sell_token, R.id.ll_buy_token, R.id.market_price, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.one_hour:
                presenter.setInterval(0);
                break;
            case R.id.one_day:
                presenter.setInterval(1);
                break;
            case R.id.one_month:
                presenter.setInterval(2);
                break;
            case R.id.custom:
                presenter.setInterval(3);
                break;
            case R.id.ll_switch:
                llSwitch.setOnClickListener(v -> presenter.switchToken());
                break;
            case R.id.ll_sell_token:
                Intent intent1 = new Intent(getContext(), P2PTokenListActivity.class);
                intent1.putExtra("ignoreSymbol", buyTokenView.getText());
                startActivityForResult(intent1, 0);
                break;
            case R.id.ll_buy_token:
                Intent intent2 = new Intent(getContext(), P2PTokenListActivity.class);
                intent2.putExtra("ignoreSymbol", sellTokenView.getText());
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
                        presenter.setHint(4);
                    } else {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            String result = bundle.getString("symbol");
            presenter.initTokens(requestCode == 0 ? result : "", requestCode == 1 ? result : "");
        }
    }
}
