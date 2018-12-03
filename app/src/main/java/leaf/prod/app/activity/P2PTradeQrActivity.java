package leaf.prod.app.activity;

import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.presenter.P2PTradeQrPresenter;
import leaf.prod.app.views.TitleView;

public class P2PTradeQrActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    private P2PTradeQrPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_p2p_qr);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        presenter = new P2PTradeQrPresenter(this, this);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.order_detail));
        title.clickLeftGoBack(getWContext());
        title.setRightImageButton(R.mipmap.icon_share, button -> {
        });
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
    }
}
