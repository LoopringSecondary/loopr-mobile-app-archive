package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.MnemonicWordAdapter;
import com.tomcat360.lyqb.adapter.MnemonicWordHintAdapter;
import com.tomcat360.lyqb.utils.DialogUtil;
import com.tomcat360.lyqb.views.SpacesItemDecoration;
import com.tomcat360.lyqb.views.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GenerateWalletActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.wallet_name)
    TextView walletName;
    @BindView(R.id.good)
    TextView good;
    @BindView(R.id.password)
    MaterialEditText password;
    @BindView(R.id.repeat_password)
    MaterialEditText repeatPassword;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.password_match)
    TextView passwordMatch;
    @BindView(R.id.generate_partone)
    LinearLayout generatePartone;
    @BindView(R.id.generate_parttwo)
    LinearLayout generateParttwo;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;
    @BindView(R.id.backup_mnemonic)
    TextView backupMnemonic;
    @BindView(R.id.mnemonic_hint)
    TextView mnemonicHint;
    @BindView(R.id.rl_mnemonic)
    RelativeLayout rlMnemonic;
    @BindView(R.id.rl_word)
    RelativeLayout rlWord;
    @BindView(R.id.confirm_mnemonic_word)
    TextView confirmMnemonicWord;
    @BindView(R.id.confirm_mnemonic_word_info)
    TextView confirmMnemonicWordInfo;
    @BindView(R.id.recycler_mnemonic_hint)
    RecyclerView recyclerMnemonicHint;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.generate_partthree)
    LinearLayout generatePartthree;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_skip)
    Button btnSkip;


    private MnemonicWordAdapter mAdapter;
    private MnemonicWordHintAdapter mHintAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_generate_wallet);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initTitle() {
        title.setBTitle("Generate Wallet");
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        GridLayoutManager layoutManagerHint = new GridLayoutManager(this, 3);


        recyclerMnemonicHint.setLayoutManager(layoutManagerHint);  //助记词提示列表
        List<String> listHint = new ArrayList<>();
        for (int i = 1;i<13;i++){
            listHint.add("mnemonic"+i);
        }
        mHintAdapter = new MnemonicWordHintAdapter(R.layout.adapter_mnemonic_word_hint, listHint);
        recyclerMnemonicHint.addItemDecoration(new SpacesItemDecoration(8));
        recyclerMnemonicHint.setAdapter(mHintAdapter);


        recyclerView.setLayoutManager(layoutManager);   //助记词选择列表
        List<String> list = new ArrayList<>();
        for (int i = 1;i<13;i++){
            list.add("mnemonic"+i);
        }
        mAdapter = new MnemonicWordAdapter(R.layout.adapter_mnemonic_word, list);
        recyclerView.addItemDecoration(new SpacesItemDecoration(8));
        recyclerView.setAdapter(mAdapter);
    }

    private int i = 0;
    private int j = 0;

    @OnClick({R.id.wallet_name, R.id.btn_next,R.id.btn_confirm, R.id.btn_skip})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.wallet_name:

                break;
            case R.id.btn_next:
                ++i;
                if (i == 1) {
                    walletName.setVisibility(View.GONE);
                    llPassword.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                } else if (i == 2) {
                    llPassword.setVisibility(View.GONE);
                    password.setVisibility(View.GONE);
                    repeatPassword.setVisibility(View.VISIBLE);
                } else {
                    generatePartone.setVisibility(View.GONE);
                    generateParttwo.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_confirm:
                ++j;
                if (j == 1) {
                    rlMnemonic.setVisibility(View.GONE);
                    rlWord.setVisibility(View.GONE);
                    generatePartthree.setVisibility(View.VISIBLE);
                }else {
                    DialogUtil.showWalletCreateResultDialog(this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtil.dialog.dismiss();
                            finish();
                            getOperation().forward(MainActivity.class);
                        }
                    });
                }
                break;
            case R.id.btn_skip:
                break;


        }
    }


}
