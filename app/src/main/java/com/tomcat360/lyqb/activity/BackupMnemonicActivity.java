package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.MnemonicWordHintAdapter;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.views.SpacesItemDecoration;
import com.tomcat360.lyqb.views.TitleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BackupMnemonicActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.backup_mnemonic)
    TextView backupMnemonic;
    @BindView(R.id.mnemonic_hint)
    TextView mnemonicHint;
    @BindView(R.id.rl_mnemonic)
    RelativeLayout rlMnemonic;
    @BindView(R.id.recycler_mnemonic_hint)
    RecyclerView recyclerMnemonicHint;
    @BindView(R.id.rl_word)
    RelativeLayout rlWord;

    private MnemonicWordHintAdapter mHintAdapter; //助记词提示adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_backup_mnemonic);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.generate_wallet));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        GridLayoutManager layoutManagerHint = new GridLayoutManager(this, 3);

        recyclerMnemonicHint.setLayoutManager(layoutManagerHint);  //助记词提示列表
        mHintAdapter = new MnemonicWordHintAdapter(R.layout.adapter_item_mnemonic_word_hint, SPUtils.getDataList(this,"mnemonic"));
        recyclerMnemonicHint.addItemDecoration(new SpacesItemDecoration(8));
        recyclerMnemonicHint.setAdapter(mHintAdapter);
    }
}
