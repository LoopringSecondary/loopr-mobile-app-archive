package leaf.prod.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.presenter.P2PPresenter;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.model.QRCodeType;

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

    private P2PPresenter presenter;

    private static int REQUEST_CODE = 1;

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
        title.setRightImageButton(R.mipmap.icon_scan, button -> {
            Intent intent = new Intent(P2PActivity.this, ActivityScanerCode.class);
            intent.putExtra("restrict", QRCodeType.P2P_ORDER.name());
            startActivityForResult(intent, REQUEST_CODE);
        });
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
        p2pTab.getTabAt(getIntent().getIntExtra("tag", 0)).select();
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = bundle.getString("result");
                getOperation().addParameter("p2p_order", result);
                getOperation().forward(P2PConfirmActivity.class);
            }
        }
    }
}
