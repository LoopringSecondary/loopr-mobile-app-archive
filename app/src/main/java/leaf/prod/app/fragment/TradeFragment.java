package leaf.prod.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.activity.H5DexWebActivity;

/**
 *
 */
public class TradeFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.ddex_layout)
    LinearLayout llDex;

    @BindView(R.id.p2p_layout)
    LinearLayout llP2P;

    @BindView(R.id.order_layout)
    LinearLayout llOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_trade, container, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ddex_layout, R.id.p2p_layout, R.id.order_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ddex_layout:
                getOperation().addParameter("url", "http://10.137.111.175:8000/#/auth/tpwallet?to=/face2face");
                getOperation().forward(H5DexWebActivity.class);
                break;
            case R.id.p2p_layout:
                getOperation().addParameter("url", "http://embeddex.upwallet.io/#/face2face");
                getOperation().forward(H5DexWebActivity.class);
                break;
            case R.id.order_layout:
                break;
        }
    }
}
