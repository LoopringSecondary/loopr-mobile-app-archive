package com.tomcat360.lyqb.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.MnemonicWordAdapter;
import com.tomcat360.lyqb.adapter.MnemonicWordHintAdapter;
import com.tomcat360.lyqb.core.WalletHelper;
import com.tomcat360.lyqb.utils.DialogUtil;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.SpacesItemDecoration;
import com.tomcat360.lyqb.views.TitleView;

import org.web3j.crypto.Bip39Wallet;

import java.io.File;
import java.io.IOException;
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
        initPermissions();
        getFile();

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
        for (int i = 1; i < 13; i++) {
            listHint.add("mnemonic" + i);
        }
        mHintAdapter = new MnemonicWordHintAdapter(R.layout.adapter_mnemonic_word_hint, listHint);
        recyclerMnemonicHint.addItemDecoration(new SpacesItemDecoration(8));
        recyclerMnemonicHint.setAdapter(mHintAdapter);


        recyclerView.setLayoutManager(layoutManager);   //助记词选择列表
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            list.add("mnemonic" + i);
        }
        mAdapter = new MnemonicWordAdapter(R.layout.adapter_mnemonic_word, list);
        recyclerView.addItemDecoration(new SpacesItemDecoration(8));
        recyclerView.setAdapter(mAdapter);
    }

    private String nextStatus = "start";
    private int j = 0;

    @OnClick({R.id.wallet_name, R.id.btn_next, R.id.btn_confirm, R.id.btn_skip})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.wallet_name:

                break;
            case R.id.btn_next:

                if (nextStatus.equals("start")) {
                    nextStatus = "pasworrd";
                    walletName.setVisibility(View.GONE);
                    llPassword.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                } else if (nextStatus.equals("pasworrd")) {
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
//                            generatePartone.setVisibility(View.GONE);
//                            generateParttwo.setVisibility(View.VISIBLE);
                        ToastUtils.toast("成功");
                        createWallet();
                    } else {
                        ToastUtils.toast("两次输入密码不一样");
                    }
                }
                break;
            case R.id.btn_confirm:
                ++j;
                if (j == 1) {
                    rlMnemonic.setVisibility(View.GONE);
                    rlWord.setVisibility(View.GONE);
                    generatePartthree.setVisibility(View.VISIBLE);
                } else {
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

    private void createWallet() {
        try {
            String pas = repeatPassword.getText().toString();
            LyqbLogger.log(pas+"     ");
            Bip39Wallet bip39Wallet = WalletHelper.create(pas, getFile());
            LyqbLogger.log(pas+"     "+bip39Wallet.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private File getFile() {
//在SD卡下建目录
        String dir = Environment.getExternalStorageDirectory().getPath() + "/mnoe";
        LyqbLogger.log(dir);
        File mFile = new File(dir);
        LyqbLogger.log(String.valueOf(mFile.exists()));
        LyqbLogger.log(String.valueOf(mFile.isDirectory()));

        if (!mFile.exists())
            mFile.mkdirs();
//        File mFile = new File("/mnt/sdcard/work/mywork");
////判断文件夹是否存在，如果不存在就创建，否则不创建
//        if (!mFile.exists()) {
//            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
//            mFile.mkdirs();
////        }


        LyqbLogger.log(String.valueOf(mFile.exists()));
        LyqbLogger.log(String.valueOf(mFile.isDirectory()));
        return mFile;
    }


    private void initPermissions() {
        /**
         //		 * 6.0系统 获取权限
         //		 */
        List<String> list = new ArrayList<>();
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            list.add(Manifest.permission.CAMERA);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)) {
            list.add(Manifest.permission.READ_CONTACTS);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)) {
            list.add(Manifest.permission.CALL_PHONE);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.READ_LOGS)) {
            list.add(Manifest.permission.READ_LOGS);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            list.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.SET_DEBUG_APP)) {
            list.add(Manifest.permission.SET_DEBUG_APP);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            list.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
        if (list.size() > 0) {
            String[] mPermissionList = list.toArray(new String[]{});
            ActivityCompat.requestPermissions(this, mPermissionList, 100);
        }


    }

}
