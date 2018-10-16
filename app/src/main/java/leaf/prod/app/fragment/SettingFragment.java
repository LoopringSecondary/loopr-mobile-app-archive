package leaf.prod.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.activity.ContractVersionActivity;
import leaf.prod.app.activity.CurrencyActivity;
import leaf.prod.app.activity.LRCFeeRatioActivity;
import leaf.prod.app.activity.LanguageActivity;
import leaf.prod.app.activity.MainActivity;
import leaf.prod.app.activity.ManageWalletActivity;
import leaf.prod.app.activity.MarginSplitActivity;
import leaf.prod.app.activity.ShareActivity;
import leaf.prod.app.utils.FingerprintUtil;
import leaf.prod.app.utils.SPUtils;

/**
 *
 */
public class SettingFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.ll_share)
    LinearLayout llShare;

    @BindView(R.id.ll_manager_wallet)
    LinearLayout llManagerWallet;

    @BindView(R.id.ll_money_type)
    LinearLayout llMoneyType;

    @BindView(R.id.ll_language)
    LinearLayout llLanguage;

    @BindView(R.id.ll_id_touch)
    LinearLayout llIdTouch;

    @BindView(R.id.ll_contract_version)
    LinearLayout llContractVersion;

    @BindView(R.id.ll_lrc_proportion)
    LinearLayout llLrcProportion;

    @BindView(R.id.ll_margin_split)
    LinearLayout llMarginSplit;

    @BindView(R.id.app_version)
    TextView appVersion;

    @BindView(R.id.ll_app_version)
    LinearLayout llAppVersion;

    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
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
        if (!FingerprintUtil.isEnable(getContext())) {
            llIdTouch.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_share, R.id.ll_manager_wallet, R.id.ll_money_type, R.id.ll_language, R.id.ll_id_touch, R.id.ll_contract_version, R.id.ll_lrc_proportion, R.id.ll_margin_split, R.id.ll_app_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_share:  //分享
                getOperation().forward(ShareActivity.class);
                break;
            case R.id.ll_manager_wallet:  //管理钱包
                getOperation().forward(ManageWalletActivity.class);
                break;
            case R.id.ll_money_type://货币
                getOperation().forward(CurrencyActivity.class);
                break;
            case R.id.ll_language://语言
                startActivity(new Intent(mainActivity, LanguageActivity.class));
                getOperation().forward(LanguageActivity.class);
                break;
            case R.id.ll_id_touch://触控id
                SwitchButton aSwitch = view.findViewById(R.id.s_v);
                aSwitch.toggle();
                SPUtils.put(getContext(), "touch_id", aSwitch.isChecked());
                break;
            case R.id.ll_contract_version://合约版本
                getOperation().forward(ContractVersionActivity.class);
                break;
            case R.id.ll_lrc_proportion://LRC费用比例
                getOperation().forward(LRCFeeRatioActivity.class);
                break;
            case R.id.ll_margin_split:// 差价分成
                getOperation().forward(MarginSplitActivity.class);
                break;
            case R.id.ll_app_version:// app版本
                break;
        }
    }
}
