/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity.trade;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.presenter.trade.P2PRecordDetailPresenter;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.MarketcapDataManager;
import leaf.prod.walletsdk.manager.P2POrderDataManager;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.order.RawOrder;
//import leaf.prod.walletsdk.model.RawOrder;

public class
P2PRecordDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    public TitleView title;

    @BindView(R.id.iv_token_s)
    public ImageView ivTokenS;

    @BindView(R.id.tv_token_s)
    public TextView tvTokenS;

    @BindView(R.id.iv_token_b)
    public ImageView ivTokenB;

    @BindView(R.id.tv_token_b)
    public TextView tvTokenB;

    @BindView(R.id.tv_sell_token)
    public TextView tvTokenSell;

    @BindView(R.id.tv_buy_token)
    public TextView tvTokenBuy;

    @BindView(R.id.tv_sell_amount)
    public TextView tvAmountS;

    @BindView(R.id.tv_buy_amount)
    public TextView tvAmountB;

    @BindView(R.id.tv_sell_price)
    public TextView tvPriceS;

    @BindView(R.id.tv_buy_price)
    public TextView tvPriceB;

    @BindView(R.id.tv_status)
    public TextView tvStatus;

    @BindView(R.id.tv_price)
    public TextView tvPrice;

    @BindView(R.id.tv_trading_fee)
    public TextView tvTradingFee;

    @BindView(R.id.tv_filled)
    public TextView tvFilled;

    @BindView(R.id.tv_id)
    public TextView tvId;

    @BindView(R.id.tv_live_time)
    public TextView tvLiveTime;

    @BindView(R.id.ll_share_view)
    public ConstraintLayout shareView;

    @BindView(R.id.qrcode_image)
    public ImageView qrCodeImage;

    @BindView(R.id.sell_info)
    public TextView sellInfo;

    @BindView(R.id.buy_info)
    public TextView buyInfo;

    @BindView(R.id.valid_until)
    public TextView tvValidUntil;

    @BindView(R.id.price_A_buy)
    public TextView priceABuy;

    @BindView(R.id.price_A_sell)
    public TextView priceASell;

    @BindView(R.id.price_B_buy)
    public TextView priceBBuy;

    @BindView(R.id.price_B_sell)
    public TextView priceBSell;

    P2PRecordDetailPresenter presenter;

    private RawOrder rawOrder;

    private TokenDataManager tokenDataManager;

    private BalanceDataManager balanceDataManager;

    private P2POrderDataManager p2pOrderManager;

    private MarketcapDataManager marketDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_p2p_record_detail);
        ButterKnife.bind(this);
        tokenDataManager = TokenDataManager.getInstance(this);
        balanceDataManager = BalanceDataManager.getInstance(this);
        p2pOrderManager = P2POrderDataManager.getInstance(this);
        marketDataManager = MarketcapDataManager.getInstance(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
        presenter = new P2PRecordDetailPresenter(this, this);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.order_detail));
        title.clickLeftGoBack(getWContext());
        rawOrder = (RawOrder) getIntent().getSerializableExtra("order");
        //todo order
//        if (rawOrder.getOrderType() == OrderType.P2P && rawOrder
//                .getP2pSide() == P2PSide.MAKER
//                && (rawOrder.getOrderStatus() == OrderStatus.OPENED || rawOrder.getOrderStatus() == OrderStatus.WAITED)) {
//            title.setRightImageButton(R.mipmap.icon_title_qrcode, button -> {
//                if (!(ButtonClickUtil.isFastDoubleClick(1))) {
//                    String authAddr = rawOrder.getAuthAddr().toLowerCase();
//                    String p2pContent = (String) SPUtils.get(getApplicationContext(), authAddr, "");
//                    if (!p2pContent.isEmpty() && p2pContent.contains("-")) {
//                        String qrCode = p2pOrderManager.generateQRCode(rawOrder.getOriginOrder());
//                        presenter.showShareDialog(qrCode);
//                    } else {
//                        RxToast.error(getString(R.string.detail_qr_error));
//                    }
//                }
//            });
//        }
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        if (rawOrder != null) {
            setOrderStatus();
            RawOrder originOrder = rawOrder;
            if (originOrder != null) {
                setOverview(originOrder);
            }
        }
    }

    private void setOverview(RawOrder order) {
        //todo order
//        int resourceB = tokenDataManager.getTokenBySymbol(order.getTokenB()).getImageResId();
//        int resourceS = tokenDataManager.getTokenBySymbol(order.getTokenS()).getImageResId();
//        String amountB = balanceDataManager.getFormattedBySymbol(order.getTokenB(), order.getAmountBuy());
//        String amountS = balanceDataManager.getFormattedBySymbol(order.getTokenS(), order.getAmountSell());
//        String currencyB = marketDataManager.getCurrencyBySymbol(order.getTokenB(), order.getAmountBuy());
//        String currencyS = marketDataManager.getCurrencyBySymbol(order.getTokenS(), order.getAmountSell());
//        Double price = order.getAmountSell() / order.getAmountBuy();
//        String priceStr = NumberUtils.format1(price, 6) + " " + order.getTokenS() + " / " + order.getTokenB();
//        String lrcFee = balanceDataManager.getFormattedBySymbol("LRC", order.getLrc());
//        String lrcCurrency = marketDataManager.getCurrencyBySymbol("LRC", order.getLrc());
//        Double ratio = this.rawOrder.getDealtAmountSell() / order.getAmountSell();
//        // Use NumberFormat
//        NumberFormat formatter = NumberFormat.getPercentInstance();
//        formatter.setMaximumFractionDigits(2);
//        formatter.setMinimumFractionDigits(2);
//        String ratioStr = formatter.format(ratio);
//        String validSince = DateUtil.formatDateTime(order.getValidS() * 1000L, "MM-dd HH:mm");
//        String validUntil = DateUtil.formatDateTime(order.getValidU() * 1000L, "MM-dd HH:mm");
//
//        if (resourceB == 0) {
//            ivTokenB.setVisibility(View.INVISIBLE);
//            tvTokenB.setVisibility(View.VISIBLE);
//            tvTokenB.setText(order.getTokenB());
//        } else {
//            ivTokenB.setVisibility(View.VISIBLE);
//            tvTokenB.setVisibility(View.INVISIBLE);
//            ivTokenB.setImageResource(resourceB);
//        }
//        if (resourceS == 0) {
//            ivTokenS.setVisibility(View.INVISIBLE);
//            tvTokenS.setVisibility(View.VISIBLE);
//            tvTokenS.setText(order.getTokenS());
//        } else {
//            ivTokenS.setVisibility(View.VISIBLE);
//            tvTokenS.setVisibility(View.INVISIBLE);
//            ivTokenS.setImageResource(resourceS);
//        }
//
//        tvTokenBuy.setText(getString(R.string.buy) + " " + order.getTokenB());
//        tvTokenSell.setText(getString(R.string.sell) + " " + order.getTokenS());
//        tvAmountB.setText(amountB);
//        tvAmountS.setText(amountS);
//        tvPriceB.setText(currencyB);
//        tvPriceS.setText(currencyS);
//        tvPrice.setText(priceStr);
//        tvTradingFee.setText(lrcFee + " LRC ≈ " + lrcCurrency);
//        tvFilled.setText(ratioStr);
//        tvId.setText(order.getHash());
//        tvLiveTime.setText(validSince + " ~ " + validUntil);
//        sellInfo.setText(amountS + " " + order.getTokenS());
//        buyInfo.setText(amountB + " " + order.getTokenB());
//        tvValidUntil.setText(validUntil);
//        double priceBuy = order.getAmountBuy() / order.getAmountSell();
//        double priceSell = order.getAmountSell() / order.getAmountBuy();
//        priceABuy.setText("1 " + order.getTokenB());
//        priceASell.setText(NumberUtils.format1(priceSell, 4) + " " + order.getTokenS());
//        priceBSell.setText("1 " + order.getTokenS());
//        priceBBuy.setText(NumberUtils.format1(priceBuy, 4) + " " + order.getTokenB());
    }

    private void setOrderStatus() {
        //todo order
//        switch (rawOrder.getOrderStatus()) {
//            case OPENED:
//                tvStatus.setText(OrderStatus.OPENED.getDescription(this));
//                break;
//            case WAITED:
//                tvStatus.setText(OrderStatus.WAITED.getDescription(this));
//                break;
//            case FINISHED:
//                tvStatus.setText(OrderStatus.FINISHED.getDescription(this));
//                break;
//            case CUTOFF:
//                tvStatus.setText(OrderStatus.CUTOFF.getDescription(this));
//                break;
//            case CANCELLED:
//                tvStatus.setText(OrderStatus.CANCELLED.getDescription(this));
//                break;
//            case EXPIRED:
//                tvStatus.setText(OrderStatus.EXPIRED.getDescription(this));
//                break;
//            case LOCKED:
//                tvStatus.setText(OrderStatus.LOCKED.getDescription(this));
//                break;
//        }
    }
}
