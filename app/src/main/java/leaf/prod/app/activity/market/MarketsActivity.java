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
import leaf.prod.app.presenter.market.MarketActivityPresenter;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.Ticker;

public class MarketsActivity extends BaseActivity {

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

    private List<Ticker> list;

    private List<Ticker> listSearch = new ArrayList<>();

    private MarketActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_markets);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
        presenter.setTabSelect(0);
    }

    @Override
    protected void initPresenter() {
        presenter = new MarketActivityPresenter(this, this);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.markets));
        title.clickLeftGoBack(getWContext());
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
                    if (list.get(i).getTradingPair().getDescription().contains(s.toString().toUpperCase())) {
                        listSearch.add(list.get(i));
                    }
                }
                presenter.updateAdapter(true, listSearch);

//                mAdapter.setNewData(listSearch);
//                mAdapter.setOnItemClickListener((adapter, view, position) -> {
//                    String symbol = listSearch.get(position).getSymbol();
//                    SPUtils.put(SendListChooseActivity.this, "send_choose", symbol);
//                    Intent intent = new Intent();
//                    intent.putExtra("symbol", symbol);
//                    setResult(1, intent);
//                    finish();
//                });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void initView() {
        marketTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        list = MarketPriceDataManager.getInstance(this).getAllTickers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.refreshTickers();
        presenter.updateAdapter(false, list);
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
