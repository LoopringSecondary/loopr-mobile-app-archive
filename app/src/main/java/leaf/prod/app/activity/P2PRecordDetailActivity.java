/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.P2POrderDataManager;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.Order;
import leaf.prod.walletsdk.model.OrderStatus;
import leaf.prod.walletsdk.model.OrderType;
import leaf.prod.walletsdk.model.P2PSide;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.model.response.relay.Token;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.SPUtils;

public class P2PRecordDetailActivity extends BaseActivity {

    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_p2p_record_detail);
        ButterKnife.bind(this);
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
                String walletAddress = order.getOriginOrder().getWalletAddress();
                String p2pContent = (String) SPUtils.get(this, walletAddress, "");
                if (p2pContent.isEmpty()) {
                    RxToast.error(getString(R.string.detail_qr_error));
                } else {
                    // TODO: yanyan
                    String qrCode = p2pOrderManager.generateQRCode(order.getOriginOrder());
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
            tokenDataManager = TokenDataManager.getInstance(this);
            balanceDataManager = BalanceDataManager.getInstance(this);
            p2pOrderManager = P2POrderDataManager.getInstance(this);
            Token tokens = tokenDataManager.getTokenBySymbol(p2pOrderManager.getTokenS());
            Token tokenb = tokenDataManager.getTokenBySymbol(p2pOrderManager.getTokenB());
            BalanceResult.Asset assets = balanceDataManager.getAssetBySymbol(p2pOrderManager.getTokenS());
            BalanceResult.Asset assetb = balanceDataManager.getAssetBySymbol(p2pOrderManager.getTokenB());
            if (tokens != null) {
                ivTokenS.setImageDrawable(getResources().getDrawable(tokens.getImageResId()));
                tvTokenS.setText(getResources().getString(R.string.sell) + " " + tokens.getSymbol());
                tvAmountS.setText(NumberUtils.format1(order.getOriginOrder()
                        .getAmountSell(), BalanceDataManager.getPrecision(order.getOriginOrder().getTokenS())));
                if (assets != null) {
                    tvPriceS.setText(CurrencyUtil.format(this, assets.getLegalValue() * order.getOriginOrder()
                            .getAmountSell()));
                }
            }
            if (tokenb != null) {
                ivTokenB.setImageDrawable(getResources().getDrawable(tokenb.getImageResId()));
                tvTokenB.setText(getResources().getString(R.string.buy) + " " + tokenb.getSymbol());
                tvAmountB.setText(NumberUtils.format1(order.getOriginOrder()
                        .getAmountBuy(), BalanceDataManager.getPrecision(order.getOriginOrder().getTokenB())));
                if (assetb != null) {
                    tvPriceB.setText(CurrencyUtil.format(this, assetb.getLegalValue() * order.getOriginOrder()
                            .getAmountBuy()));
                }
            }
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
                    tvStatus.setText(getResources().getString(R.string.order_locked));
                    break;
            }
            tvPrice.setText(order.getPrice() + " " + p2pOrderManager.getTokenS() + "/" + p2pOrderManager.getTokenB());
            tvTradingFee.setText(order.getOriginOrder().getLrc() + " LRC");
            tvFilled.setText(NumberUtils.format1(order.getDealtAmountSell() / order.getOriginOrder()
                    .getAmountSell(), 2) + "%");
            tvId.setText(order.getOriginOrder().getHash());
            tvLiveTime.setText(sdf.format(new Date(new Long(order.getOriginOrder()
                    .getValidS()).longValue() * 1000)) + " ~ "
                    + sdf.format(new Date(new Long(order.getOriginOrder().getValidU()).longValue() * 1000)));
        }
    }
}
