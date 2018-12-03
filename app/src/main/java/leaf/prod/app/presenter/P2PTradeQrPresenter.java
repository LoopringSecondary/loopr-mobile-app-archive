package leaf.prod.app.presenter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.P2PTradeQrActivity;
import leaf.prod.walletsdk.manager.TokenDataManager;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-03 7:42 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class P2PTradeQrPresenter extends BasePresenter<P2PTradeQrActivity> {

    @BindView(R.id.tv_sell_token)
    TextView tvSellToken;

    @BindView(R.id.iv_token_s)
    ImageView ivTokenS;

    @BindView(R.id.iv_token_b)
    ImageView ivTokenB;

    @BindView(R.id.tv_sell_amount)
    TextView tvSellAmount;

    @BindView(R.id.tv_sell_price)
    TextView tvSellPrice;

    @BindView(R.id.tv_buy_token)
    TextView tvBuyToken;

    @BindView(R.id.tv_buy_amount)
    TextView tvBuyAmount;

    @BindView(R.id.tv_buy_price)
    TextView tvBuyPrice;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.tv_live_time)
    TextView tvLiveTime;

    private TokenDataManager tokenDataManager;

    public P2PTradeQrPresenter(P2PTradeQrActivity view, Context context) {
        super(view, context);
        ButterKnife.bind(this, view);
        tokenDataManager = TokenDataManager.getInstance(context);
        tvSellToken.setText(view.getIntent().getStringExtra("sellToken"));
        tvBuyToken.setText(view.getIntent().getStringExtra("buyToken"));
        tvSellAmount.setText(view.getIntent().getStringExtra("sellAmount"));
        tvBuyAmount.setText(view.getIntent().getStringExtra("buyAmount"));
        tvSellPrice.setText(view.getIntent().getStringExtra("sellPrice"));
        tvBuyPrice.setText(view.getIntent().getStringExtra("buyPrice"));
        ivTokenS.setImageDrawable(view.getResources()
                .getDrawable(tokenDataManager.getTokenBySymbol(tvSellToken.getText().toString()).getImageResId()));
        ivTokenB.setImageDrawable(view.getResources()
                .getDrawable(tokenDataManager.getTokenBySymbol(tvBuyToken.getText().toString()).getImageResId()));
        tvLiveTime.setText(view.getIntent().getStringExtra("liveTime"));
    }
}
