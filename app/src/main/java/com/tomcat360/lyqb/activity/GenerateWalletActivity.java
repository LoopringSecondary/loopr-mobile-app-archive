package com.tomcat360.lyqb.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.base.Joiner;
import com.lyqb.walletsdk.WalletHelper;
import com.lyqb.walletsdk.model.WalletDetail;
import com.lyqb.walletsdk.service.LoopringService;
import com.lyqb.walletsdk.util.MnemonicUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.MnemonicWordAdapter;
import com.tomcat360.lyqb.adapter.MnemonicWordHintAdapter;
import com.tomcat360.lyqb.utils.ButtonClickUtil;
import com.tomcat360.lyqb.utils.DialogUtil;
import com.tomcat360.lyqb.utils.FileUtils;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.MyViewUtils;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.view.APP;
import com.tomcat360.lyqb.views.SpacesItemDecoration;
import com.tomcat360.lyqb.views.TitleView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class GenerateWalletActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.wallet_name)
    MaterialEditText walletName;
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


    private MnemonicWordHintAdapter mHintAdapter; //助记词提示adapter
    private MnemonicWordAdapter mAdapter;  //助记词选择adapter
    List<String> mneCheckedList = new LinkedList<>();//选中的助记词

    private String mnemonic;  //助记词
    List<String> listMnemonic = new ArrayList<>();  //正确顺序的助记词列表
    List<String> listRandomMnemonic = new ArrayList<>();  //打乱顺序的助记词列表
    private String nextStatus = "start"; //点击next时根据不同状态显示不同页面
    private String confirmStatus = "one";//点击confirm时根据不同状态显示不同页面

    private String address;//钱包地址
    private LoopringService loopringService = new LoopringService();


    public final static int MNEMONIC_SUCCESS = 1;
    public final static int CREATE_SUCCESS = 2;
    public final static int ERROR_ONE = 3;
    public final static int ERROR_TWO = 4;
    @SuppressLint("HandlerLeak")
    Handler handlerCreate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MNEMONIC_SUCCESS:
                    Bundle bundle = msg.getData();
                    String filename = (String) bundle.get("filename");
                    String mnemonic = (String) bundle.get("mnemonic");
                    getAddress();

                    break;
                case CREATE_SUCCESS:  //获取keystore中的address成功后，调用解锁钱包方法（unlockWallet）
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LyqbLogger.log(address);
                            loopringService.notifyCreateWallet(address)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<String>() {
                                        @Override
                                        public void onCompleted() {
                                            hideProgress();
                                            DialogUtil.showWalletCreateResultDialog(GenerateWalletActivity.this, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    DialogUtil.dialog.dismiss();
                                                    finish();
                                                    getOperation().forward(MainActivity.class);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ToastUtils.toast("创建失败，请重试");
                                            hideProgress();
                                        }

                                        @Override
                                        public void onNext(String s) {
                                        }
                                    });
                        }
                    }).start();
                    break;
                case ERROR_ONE:
                    hideProgress();
                    ToastUtils.toast("本地文件读取失败，请重试");
                    break;
                case ERROR_TWO:
                    hideProgress();
                    ToastUtils.toast("本地文件JSON解析失败，请重试");
                    break;

            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_generate_wallet);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        MyViewUtils.hideSoftInput(this, walletName);

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

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        GridLayoutManager layoutManagerHint = new GridLayoutManager(this, 3);


        recyclerMnemonicHint.setLayoutManager(layoutManagerHint);  //助记词提示列表
        mHintAdapter = new MnemonicWordHintAdapter(R.layout.adapter_item_mnemonic_word_hint, null);
        recyclerMnemonicHint.addItemDecoration(new SpacesItemDecoration(8));
        recyclerMnemonicHint.setAdapter(mHintAdapter);


        recyclerView.setLayoutManager(layoutManager);   //助记词选择列表
        mAdapter = new MnemonicWordAdapter(R.layout.adapter_item_mnemonic_word, null);
        recyclerView.addItemDecoration(new SpacesItemDecoration(8));
        recyclerView.setAdapter(mAdapter);
        final Joiner joiner = Joiner.on(" ");
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //选择助记词，进行验证
                CheckBox checkBox = view.findViewById(R.id.mnemonic_word);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    mneCheckedList.remove(listRandomMnemonic.get(position));
                    confirmMnemonicWordInfo.setText(joiner.join(mneCheckedList));
                } else {
                    checkBox.setChecked(true);
                    mneCheckedList.add(listRandomMnemonic.get(position));
                    confirmMnemonicWordInfo.setText(joiner.join(mneCheckedList));
                }
            }
        });

    }


    @OnClick({R.id.wallet_name, R.id.btn_next, R.id.btn_confirm, R.id.btn_skip})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.wallet_name:

                break;
            case R.id.btn_next:
                if (TextUtils.isEmpty(walletName.getText().toString())) {
                    ToastUtils.toast("请输入钱包名称");
                    return;
                }
                if (nextStatus.equals("start")) {
                    nextStatus = "password";
                    SPUtils.put(this, "walletname", walletName.getText().toString());
                    walletName.setVisibility(View.GONE);
                    llPassword.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                } else if (nextStatus.equals("password")) {
                    if (password.length() < 6) {
                        ToastUtils.toast("请输入6位以上密码");
                    } else {
                        nextStatus = "match";
                        llPassword.setVisibility(View.GONE);
                        password.setVisibility(View.GONE);
                        repeatPassword.setVisibility(View.VISIBLE);
                    }
                } else if (nextStatus.equals("match")) {
                    if (repeatPassword.getText().toString().equals(password.getText().toString())) {

                        if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                            mnemonic = MnemonicUtils.randomMnemonic();
                            String[] arrayMne = mnemonic.split(" ");

                            listMnemonic.clear();
                            for (int i = 0; i < arrayMne.length; i++) {
                                listMnemonic.add(arrayMne[i]);
                                listRandomMnemonic.add(arrayMne[i]);
                            }
                            mHintAdapter.setNewData(listMnemonic);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    FileUtils.keepFile(GenerateWalletActivity.this, "mnemonic.txt", mnemonic);

                                }
                            }).start();
//                            SPUtils.setDataList(this, "mnemonic", listMnemonic);
                            mHintAdapter.notifyDataSetChanged();

                            generatePartone.setVisibility(View.GONE);
                            generateParttwo.setVisibility(View.VISIBLE);
                            passwordMatch.setVisibility(View.VISIBLE);
                            MyViewUtils.hideSoftInput(this, repeatPassword);
                        }
                    } else {
                        ToastUtils.toast("两次输入密码不一致");
                        passwordMatch.setVisibility(View.VISIBLE);
                        MyViewUtils.hideSoftInput(this, repeatPassword);
                    }
                }
                break;
            case R.id.btn_confirm:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击

                    if (confirmStatus.equals("one")) {
                        confirmStatus = "two";
                        rlMnemonic.setVisibility(View.GONE);
                        rlWord.setVisibility(View.GONE);
                        generatePartthree.setVisibility(View.VISIBLE);
                        LyqbLogger.log(listMnemonic.toString());

                        Collections.shuffle(listRandomMnemonic); //打乱助记词
                        mAdapter.setNewData(listRandomMnemonic);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        matchMnemonic();

                    }
                }
                break;
            case R.id.btn_skip:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    startThread();
                }
                break;
        }
    }

    /**
     * 生成钱包
     */
    private void startThread() {
        showProgress("加载中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WalletDetail walletDetail = createWallet();//生成助记词
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("filename", walletDetail.getFilename());
                bundle.putString("mnemonic", walletDetail.getMnemonic());
                SPUtils.put(GenerateWalletActivity.this, "filename", walletDetail.getFilename());
                LyqbLogger.log(walletDetail.getMnemonic() + "   " + walletDetail.getFilename());
                msg.setData(bundle);
                msg.what = MNEMONIC_SUCCESS;
                handlerCreate.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 助记词匹配
     */
    private void matchMnemonic() {
        String str = Joiner.on(" ").join(mneCheckedList);
        if (str.trim().equals(mnemonic)) {
            startThread();
        } else {
            ToastUtils.toast("助记词不匹配");
        }
    }

    /**
     * 判断两个集合是否相等
     */
    public boolean compare(List<String> a, List<String> b) {
        if (a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    /**
     * 创建钱包
     */
    private WalletDetail createWallet() {
        WalletDetail walletDetail = null;
        try {
            String pas = repeatPassword.getText().toString();
            walletDetail = WalletHelper.createFromMnemonic(mnemonic,null,pas, FileUtils.getKeyStoreLocation(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return walletDetail;
    }


    /**
     * 获取本地keystore中的address
     */
    public void getAddress() {
        new Thread() {
            @Override
            public void run() {
                Message msg = handlerCreate.obtainMessage();
                try {
                    address = FileUtils.getFileFromSD(GenerateWalletActivity.this);
                } catch (IOException e) {
                    handlerCreate.sendEmptyMessage(ERROR_ONE);
                    e.printStackTrace();
                } catch (JSONException e) {
                    handlerCreate.sendEmptyMessage(ERROR_TWO);
                    e.printStackTrace();
                }
                msg.obj = address;
                msg.what = CREATE_SUCCESS;
                handlerCreate.sendMessage(msg);
            }
        }.start();

    }

}
