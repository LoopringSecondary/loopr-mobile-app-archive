/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity.market;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.adapter.ViewPageAdapter;
import leaf.prod.app.fragment.market.MarketSelectFragment;
import leaf.prod.app.presenter.market.MarketSelectActivityPresenter;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.market.Market;
import leaf.prod.walletsdk.model.market.MarketsType;

public class MarketSelectActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.cancel_text)
    TextView cancelText;

    @BindView(R.id.ll_search)
    LinearLayout llSearch;

    @BindView(R.id.market_tab)
    TabLayout marketTab;

    @BindView(R.id.cl_loading)
    public ConstraintLayout clLoading;

    @BindView(R.id.left_btn1)
    public ImageView leftBtn1;

    @BindView(R.id.view_pager)
    public ViewPager viewPager;

    private List<Market> list;

    private List<Market> listSearch = new ArrayList<>();

    private boolean isFirstTime = true;

    private MarketSelectActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_market_select);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
        presenter = new MarketSelectActivityPresenter(this, this);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.markets));
        title.clickLeftGoBack(R.mipmap.icon_remove, getWContext());
        title.setRightImageButton(R.mipmap.icon_search, button -> {
            title.setVisibility(View.GONE);
            llSearch.setVisibility(View.VISIBLE);
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listSearch.clear();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getMarketPair().toString().contains(s.toString().toUpperCase())) {
                        listSearch.add(list.get(i));
                    }
                }
                presenter.updateAdapter(true, listSearch);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void initView() {
        List<Fragment> fragments = new ArrayList<>();
        String[] titles = new String[MarketsType.values().length];
        for (MarketsType type : MarketsType.values()) {
            MarketSelectFragment fragment = new MarketSelectFragment();
            fragment.setMarketsType(type);
            fragments.add(type.ordinal(), fragment);
            titles[type.ordinal()] = type.name();
        }
        titles[0] = getString(R.string.Favorites);
        presenter.setFragments(fragments);
        setupViewPager(fragments, titles);
    }

    private void setupViewPager(List<Fragment> fragments, String[] titles) {
        marketTab.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(titles.length - 1);
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), fragments, titles));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isFirstTime) {
                    isFirstTime = false;
                    MarketSelectFragment fragment = (MarketSelectFragment) fragments.get(position);
                    fragment.updateAdapter();
                }
            }

            @Override
            public void onPageSelected(int position) {
                MarketSelectFragment fragment = (MarketSelectFragment) fragments.get(position);
                fragment.updateAdapter();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void initData() {
        list = MarketPriceDataManager.getInstance(this).getAllMarkets();
    }

    @OnClick({R.id.left_btn1, R.id.cancel_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_text:
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                title.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.GONE);
                etSearch.setText("");
                presenter.updateAdapter(false, list);
                break;
            case R.id.left_btn1:
                finish();
                break;
        }
    }
}
