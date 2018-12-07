/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.MarketcapDataManager;
import leaf.prod.walletsdk.manager.P2POrderDataManager;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.Order;
import leaf.prod.walletsdk.model.OrderStatus;
import leaf.prod.walletsdk.model.OrderType;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.P2PSide;
import leaf.prod.walletsdk.util.DateUtil;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.SPUtils;

public class P2PRecordDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.iv_token_s)
    ImageView ivTokenS;

    @BindView(R.id.iv_token_b)
    ImageView ivTokenB;

    @BindView(R.id.tv_sell_token)
    TextView tvTokenS;

    @BindView(R.id.tv_buy_token)
    TextView tvTokenB;

    @BindView(R.id.tv_sell_amount)
    TextView tvAmountS;

    @BindView(R.id.tv_buy_amount)
    TextView tvAmountB;

    @BindView(R.id.tv_sell_price)
    TextView tvPriceS;

    @BindView(R.id.tv_buy_price)
    TextView tvPriceB;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.tv_price)
    TextView tvPrice;

    @BindView(R.id.tv_trading_fee)
    TextView tvTradingFee;

    @BindView(R.id.tv_filled)
    TextView tvFilled;

    @BindView(R.id.tv_id)
    TextView tvId;

    @BindView(R.id.tv_live_time)
    TextView tvLiveTime;

    private Order order;

    private TokenDataManager tokenDataManager;

    private BalanceDataManager balanceDataManager;

    private P2POrderDataManager p2pOrderManager;

    private MarketcapDataManager marketDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
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
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.order_detail));
        title.clickLeftGoBack(getWContext());
        order = (Order) getIntent().getSerializableExtra("order");
        if (order.getOriginOrder().getOrderType() == OrderType.P2P && order.getOriginOrder().getP2pSide() == P2PSide.MAKER
                && (order.getOrderStatus() == OrderStatus.OPENED || order.getOrderStatus() == OrderStatus.WAITED)) {
            title.setRightImageButton(R.mipmap.icon_title_qrcode, button -> {
                if (!(ButtonClickUtil.isFastDoubleClick(1))) {
                    String authAddr = order.getOriginOrder().getAuthAddr().toLowerCase();
                    String p2pContent = (String) SPUtils.get(getApplicationContext(), authAddr, "");
                    if (!p2pContent.isEmpty() && p2pContent.contains("-")) {
                        // TODO: yanyan
                        String qrCode = p2pOrderManager.generateQRCode(order.getOriginOrder());
                    } else {
                        RxToast.error(getString(R.string.detail_qr_error));
                    }
                }
            });
        }
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        if (order != null) {
            setOrderStatus();
            OriginOrder originOrder = order.getOriginOrder();
            if (originOrder != null) {
                setOverview(originOrder);
            }
        }
    }

    private void setOverview(OriginOrder order) {
        int resourceB = tokenDataManager.getTokenBySymbol(order.getTokenB()).getImageResId();
        int resourceS = tokenDataManager.getTokenBySymbol(order.getTokenS()).getImageResId();
        String amountB = balanceDataManager.getFormattedBySymbol(order.getTokenB(), order.getAmountBuy());
        String amountS = balanceDataManager.getFormattedBySymbol(order.getTokenS(), order.getAmountSell());
        String currencyB = marketDataManager.getCurrencyBySymbol(order.getTokenB(), order.getAmountBuy());
        String currencyS = marketDataManager.getCurrencyBySymbol(order.getTokenS(), order.getAmountSell());
        Double price = order.getAmountSell() / order.getAmountBuy();
        String priceStr = NumberUtils.format1(price, 6) + " " + order.getTokenS() + " / " + order.getTokenB();
        String lrcFee = balanceDataManager.getFormattedBySymbol("LRC", order.getLrc());
        String lrcCurrency = marketDataManager.getCurrencyBySymbol("LRC", order.getLrc());
        Double ratio = this.order.getDealtAmountSell() / order.getAmountSell();
        String ratioStr = NumberUtils.format1(ratio, 6) + "%";
        String validSince = DateUtil.formatDateTime(order.getValidS() * 1000, "MM-dd HH:mm");
        String validUntil = DateUtil.formatDateTime(order.getValidU() * 1000, "MM-dd HH:mm");

        ivTokenB.setImageDrawable(getResources().getDrawable(resourceB));
        ivTokenS.setImageDrawable(getResources().getDrawable(resourceS));
        tvTokenB.setText(getString(R.string.buy) + " " + order.getTokenB());
        tvTokenS.setText(getString(R.string.sell) + " " + order.getTokenS());
        tvAmountB.setText(amountB);
        tvAmountS.setText(amountS);
        tvPriceB.setText(currencyB);
        tvPriceS.setText(currencyS);
        tvPrice.setText(priceStr);
        tvTradingFee.setText(lrcFee + " LRC ≈ " + lrcCurrency);
        tvFilled.setText(ratioStr);
        tvId.setText(order.getHash());
        tvLiveTime.setText(validSince + " ~ " + validUntil);
    }

    private void setOrderStatus() {
        switch (order.getOrderStatus()) {
            case OPENED:
                tvStatus.setText(OrderStatus.OPENED.getDescription(this));
                break;
            case WAITED:
                tvStatus.setText(OrderStatus.WAITED.getDescription(this));
                break;
            case FINISHED:
                tvStatus.setText(OrderStatus.FINISHED.getDescription(this));
                break;
            case CUTOFF:
                tvStatus.setText(OrderStatus.CUTOFF.getDescription(this));
                break;
            case CANCELLED:
                tvStatus.setText(OrderStatus.CANCELLED.getDescription(this));
                break;
            case EXPIRED:
                tvStatus.setText(OrderStatus.EXPIRED.getDescription(this));
                break;
            case LOCKED:
                tvStatus.setText(OrderStatus.LOCKED.getDescription(this));
                break;
        }
    }

}
