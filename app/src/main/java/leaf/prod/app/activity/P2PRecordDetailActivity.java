package leaf.prod.app.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.P2POrderDataManager;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.Order;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import leaf.prod.walletsdk.model.response.relay.Token;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.NumberUtils;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
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

    private TokenDataManager tokenDataManager;

    private BalanceDataManager balanceDataManager;

    private P2POrderDataManager p2POrderDataManager;

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
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        Order order = (Order) getIntent().getSerializableExtra("order");
        if (order != null) {
            tokenDataManager = TokenDataManager.getInstance(this);
            balanceDataManager = BalanceDataManager.getInstance(this);
            p2POrderDataManager = P2POrderDataManager.getInstance(this);
            Token tokens = tokenDataManager.getTokenBySymbol(p2POrderDataManager.getTokenS());
            Token tokenb = tokenDataManager.getTokenBySymbol(p2POrderDataManager.getTokenB());
            BalanceResult.Asset assets = balanceDataManager.getAssetBySymbol(p2POrderDataManager.getTokenS());
            BalanceResult.Asset assetb = balanceDataManager.getAssetBySymbol(p2POrderDataManager.getTokenB());
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
                    tvStatus.setText(getResources().getString(R.string.order_matching));
                    break;
                case WAITED:
                    tvStatus.setText(getResources().getString(R.string.order_submitted));
                    break;
                case FINISHED:
                    tvStatus.setText(getResources().getString(R.string.completed));
                    break;
                case CUTOFF:
                    tvStatus.setText(getResources().getString(R.string.order_cutoff));
                    break;
                case CANCELLED:
                    tvStatus.setText(getResources().getString(R.string.order_cancelled));
                    break;
                case EXPIRED:
                    tvStatus.setText(getResources().getString(R.string.order_expired));
                    break;
                case LOCKED:
                    tvStatus.setText(getResources().getString(R.string.order_locked));
                    break;
            }
            tvPrice.setText(order.getPrice() + " " + p2POrderDataManager.getTokenS() + "/" + p2POrderDataManager.getTokenB());
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
