package leaf.prod.app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxfeature.tool.RxQRCode;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.presenter.P2PTradeQrPresenter;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.MarketcapDataManager;
import leaf.prod.walletsdk.manager.P2POrderDataManager;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.OrderStatus;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.util.DateUtil;

public class P2PTradeQrActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.tv_sell_token)
    public TextView tvSellToken;

    @BindView(R.id.iv_token_s)
    public ImageView ivTokenS;

    @BindView(R.id.iv_token_b)
    public ImageView ivTokenB;

    @BindView(R.id.iv_qr_code)
    public ImageView ivQrCode;

    @BindView(R.id.tv_sell_amount)
    public TextView tvSellAmount;

    @BindView(R.id.tv_sell_price)
    public TextView tvSellPrice;

    @BindView(R.id.tv_buy_token)
    public TextView tvBuyToken;

    @BindView(R.id.tv_buy_amount)
    public TextView tvBuyAmount;

    @BindView(R.id.tv_buy_price)
    public TextView tvBuyPrice;

    @BindView(R.id.tv_status)
    public TextView tvStatus;

    @BindView(R.id.tv_live_time)
    public TextView tvLiveTime;

    private P2PTradeQrPresenter presenter;

    private TokenDataManager tokenManager;

    private BalanceDataManager balanceManager;

    private MarketcapDataManager marketManager;

    private P2POrderDataManager p2pOrderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_p2p_qr);
        ButterKnife.bind(this);
        tokenManager = TokenDataManager.getInstance(this);
        balanceManager = BalanceDataManager.getInstance(this);
        marketManager = MarketcapDataManager.getInstance(this);
        p2pOrderManager = P2POrderDataManager.getInstance(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        presenter = new P2PTradeQrPresenter(this, this);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.order_detail));
        title.clickLeftGoBack(getWContext());
        title.setRightImageButton(R.mipmap.icon_share, button -> {
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        OriginOrder order = p2pOrderManager.getOrder();
        if (order != null) {
            int resourceB = tokenManager.getTokenBySymbol(order.getTokenB()).getImageResId();
            int resourceS = tokenManager.getTokenBySymbol(order.getTokenS()).getImageResId();
            String amountB = balanceManager.getFormattedBySymbol(order.getTokenB(), order.getAmountBuy());
            String amountS = balanceManager.getFormattedBySymbol(order.getTokenS(), order.getAmountSell());
            String currencyB = marketManager.getCurrencyBySymbol(order.getTokenB(), order.getAmountBuy());
            String currencyS = marketManager.getCurrencyBySymbol(order.getTokenS(), order.getAmountSell());
            String validSince = DateUtil.formatDateTime(order.getValidS() * 1000, "MM-dd HH:mm");
            String validUntil = DateUtil.formatDateTime(order.getValidU() * 1000, "MM-dd HH:mm");

            ivTokenB.setImageDrawable(getResources().getDrawable(resourceB));
            ivTokenS.setImageDrawable(getResources().getDrawable(resourceS));
            tvBuyToken.setText(order.getTokenB());
            tvSellToken.setText(order.getTokenS());
            tvBuyAmount.setText(amountB);
            tvSellAmount.setText(amountS);
            tvBuyPrice.setText(currencyB);
            tvSellPrice.setText(currencyS);
            tvStatus.setText(OrderStatus.OPENED.getDescription(this));
            tvLiveTime.setText(validSince + " ~ " + validUntil);

            generateQRCode();
        }
    }

    @Override
    public void initData() {
    }

    private void generateQRCode() {
        OriginOrder order = p2pOrderManager.getOrder();
        String content = p2pOrderManager.generateQRCode(order);
        RxQRCode.Builder builder = RxQRCode.builder(content);
        builder.into(ivQrCode);
    }
}
