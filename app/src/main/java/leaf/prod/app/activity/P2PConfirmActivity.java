/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-05 4:09 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.presenter.P2PConfirmPresenter;

public class P2PConfirmActivity extends BaseActivity {

    @BindView(R.id.iv_token_s)
    public ImageView tokenSImage;

    @BindView(R.id.iv_token_b)
    public ImageView tokenBImage;

    @BindView(R.id.tv_sell_token)
    public TextView tokenSText;

    @BindView(R.id.tv_buy_token)
    public TextView tokenBText;

    @BindView(R.id.tv_sell_price)
    public TextView sellPriceText;

    @BindView(R.id.tv_buy_price)
    public TextView buyPriceText;

    @BindView(R.id.tv_sell_amount)
    public TextView sellAmountText;

    @BindView(R.id.tv_buy_amount)
    public TextView buyAmountText;

    @BindView(R.id.tv_price)
    public TextView priceText;

    @BindView(R.id.tv_trading_fee)
    public TextView tradingFeeText;

    @BindView(R.id.tv_margin_split)
    public TextView marginSplitText;

    @BindView(R.id.tv_live_time)
    public TextView liveTimeText;

    private P2PConfirmPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_p2p_trade_detail);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化P层
     */
    @Override
    protected void initPresenter() {
        presenter = new P2PConfirmPresenter(this, this);
    }

    /**
     * 初始化标题
     */
    @Override
    public void initTitle() {
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
    }

    /**
     * 0
     * 初始化数据
     */
    @Override
    public void initData() {
        String p2pOrder = getIntent().getStringExtra("p2p_order");
        if (p2pOrder != null && !p2pOrder.isEmpty()) {
            JsonObject object = new JsonParser().parse(p2pOrder).getAsJsonObject();
            presenter.p2pContent = object.get("value").getAsJsonObject();
            presenter.handleResult();
        }
    }

    @OnClick({R.id.btn_order, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_order:
                presenter.processTaker();
                break;
            case R.id.cancel:
                break;
        }
    }
}
