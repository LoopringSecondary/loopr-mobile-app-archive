package leaf.prod.app.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.presenter.P2PPresenter;
import leaf.prod.app.views.TitleView;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class P2PActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.p2p_tab)
    TabLayout p2pTab;
    //    @BindView(R.id.trade_item)
    //    TabItem tradeItem;
    //
    //    @BindView(R.id.records_item)
    //    TabItem recordsItem;

    private P2PPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_p2p);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
        presenter = new P2PPresenter(this, this);
        presenter.setTabSelect(0);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.p2p_title));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        p2pTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                presenter.setTabSelect(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void initData() {
    }
}
