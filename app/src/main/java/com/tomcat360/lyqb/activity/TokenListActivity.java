package com.tomcat360.lyqb.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lyqb.walletsdk.model.response.data.Token;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.TokenListAdapter;
import com.tomcat360.lyqb.manager.TokenDataManager;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.views.RecyclerViewBugLayoutManager;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TokenListActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.cancel_text)
    TextView cancelText;

    @BindView(R.id.ll_search)
    LinearLayout llSearch;

    List<String> choose_token;

    private TokenListAdapter mAdapter;

    private List<Token> list;

    private List<Token> listSearch = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_token_list);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.tokens));
        title.clickLeftGoBack(getWContext());
        title.setRightImageButton(R.mipmap.icon_search, new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                llSearch.setVisibility(View.VISIBLE);
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LyqbLogger.log(s.toString() + "   " + s);
                listSearch.clear();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getSymbol().contains(s.toString().toUpperCase())) {
                        listSearch.add(list.get(i));
                        LyqbLogger.log(listSearch.toString());
                    }
                }
                mAdapter.setNewData(listSearch);
                if (s == null) {
                    mAdapter.setNewData(list);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        choose_token = SPUtils.getDataList(TokenListActivity.this, "choose_token");
        LyqbLogger.log(choose_token.toString());
        RecyclerViewBugLayoutManager layoutManager = new RecyclerViewBugLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        list = TokenDataManager.getInstance(this).getTokens();
        mAdapter = new TokenListAdapter(R.layout.adapter_item_token_list, list, choose_token);
        recyclerView.setAdapter(mAdapter);
        /**
         *代币选中状态
         * */
        recyclerView.setClickable(false);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Switch aSwitch = view.findViewById(R.id.s_v);
                if (aSwitch.isChecked()) {
//                    aSwitch.setChecked(false);

                    aSwitch.setSwitchTextAppearance(TokenListActivity.this, R.style.s_false);

                    choose_token.remove(list.get(position).getSymbol());
                } else {
//                    aSwitch.setChecked(true);

                    aSwitch.setSwitchTextAppearance(TokenListActivity.this, R.style.s_true);

                    choose_token.add(list.get(position).getSymbol());
                }
                mAdapter.setChoose_token(choose_token);
                mAdapter.notifyDataSetChanged();
                SPUtils.setDataList(TokenListActivity.this, "choose_token", choose_token);
            }
        });
    }

    @OnClick({R.id.cancel_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_text:
                llSearch.setVisibility(View.GONE);
                etSearch.setText("");
                mAdapter.setNewData(list);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
