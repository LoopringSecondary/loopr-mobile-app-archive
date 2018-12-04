package leaf.prod.app.activity;

import android.os.Bundle;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.views.TitleView;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class P2PRecordDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

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
    }
}
