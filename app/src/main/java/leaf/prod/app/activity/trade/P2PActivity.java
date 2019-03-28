/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity.trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.WindowManager;

import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.activity.wallet.ActivityScanerCode;
import leaf.prod.app.presenter.trade.P2PPresenter;
import leaf.prod.app.utils.QRCodeUitl;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.model.common.QRCodeType;

public class P2PActivity extends BaseActivity {

    private static int REQUEST_CODE = 1;

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.p2p_tab)
    TabLayout p2pTab;

    private P2PPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = bundle.getString("result");
                if (QRCodeUitl.getQRCodeType(result) == QRCodeType.P2P_ORDER) {
                    getOperation().addParameter("p2p_order", result);
                    getOperation().forward(P2PConfirmActivity.class);
                } else {
                    RxToast.error(getString(R.string.qr_p2p_error_tip));
                }
            }
        }
    }
}
