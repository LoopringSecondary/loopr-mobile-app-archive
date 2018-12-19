/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-18 10:16 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.presenter.AirdropPresenter;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.model.QRCodeType;

public class AirdropActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.airdrop_addr)
    public MaterialEditText airdropAddress;

    @BindView(R.id.airdrop_amount)
    public MaterialEditText airdropAmount;

    @BindView(R.id.btn_claim)
    public Button claimButton;

    private AirdropPresenter presenter;

    private static int REQUEST_CODE = 1;

    /**
     * 初始化P层
     */
    @Override
    protected void initPresenter() {
        presenter = new AirdropPresenter(this, this);
    }

    /**
     * 初始化标题
     */
    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.airdrop_title));
        title.clickLeftGoBack(getWContext());
        title.setRightImageButton(R.mipmap.icon_scan, button -> {
            Intent intent = new Intent(this, ActivityScanerCode.class);
            intent.putExtra("restrict", QRCodeType.TRANSFER.name());
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        int color = this.getResources().getColor(R.color.colorBg);
        if (!presenter.isClaimTimeValid()) {
            claimButton.setBackgroundColor(color);
            claimButton.setOnClickListener(v -> RxToast.error(getString(R.string.airdrop_time_invalid)));
        }
    }

    /**
     * 0
     * 初始化数据
     */
    @Override
    public void initData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_airdrop);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @OnClick({R.id.btn_claim})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_claim:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) {
                    presenter.handleClaim();
                }
                break;
        }
    }
}
