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
import leaf.prod.app.views.TitleView;

public class P2PConfirmActivity extends BaseActivity {

    @BindView(R.id.iv_token_s)
    public ImageView ivTokenS;

    @BindView(R.id.iv_token_b)
    public ImageView ivTokenB;

    @BindView(R.id.tv_sell_token)
    public TextView tvSellToken;

    @BindView(R.id.tv_buy_token)
    public TextView tvBuyToken;

    @BindView(R.id.tv_sell_price)
    public TextView tvSellPrice;

    @BindView(R.id.tv_buy_price)
    public TextView tvBuyPrice;

    @BindView(R.id.tv_sell_amount)
    public TextView tvSellAmount;

    @BindView(R.id.tv_buy_amount)
    public TextView tvBuyAmount;

    @BindView(R.id.tv_price)
    public TextView tvPrice;

    @BindView(R.id.tv_trading_fee)
    public TextView tvTradingFee;

    @BindView(R.id.tv_live_time)
    public TextView tvLiveTime;

    @BindView(R.id.title)
    public TitleView title;

    private P2PConfirmPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_p2p_confirm);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
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
        title.setBTitle(getResources().getString(R.string.trade_confirmation));
        title.clickLeftGoBack(getWContext());
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
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
