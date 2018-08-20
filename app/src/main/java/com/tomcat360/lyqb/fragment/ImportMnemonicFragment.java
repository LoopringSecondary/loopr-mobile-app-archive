package com.tomcat360.lyqb.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.activity.MainActivity;
import com.tomcat360.lyqb.core.WalletHelper;
import com.tomcat360.lyqb.core.exception.KeystoreSaveException;
import com.tomcat360.lyqb.utils.ButtonClickUtil;
import com.tomcat360.lyqb.utils.DialogUtil;
import com.tomcat360.lyqb.utils.FileUtils;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.wheelPicker.picker.OptionPicker;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 *
 */
public class ImportMnemonicFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_mnemonic)
    MaterialEditText etMnemonic;
    @BindView(R.id.wallet_type)
    TextView walletType;
    @BindView(R.id.wallet_list)
    RelativeLayout walletList;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;
    @BindView(R.id.btn_unlock)
    Button btnUnlock;
    @BindView(R.id.et_dpath)
    MaterialEditText etDpath;
    @BindView(R.id.rl_dpath)
    RelativeLayout rlDpath;

    private String dpath;

    public final static int MNEMONIC_SUCCESS = 1;
    @SuppressLint("HandlerLeak")
    Handler handlerCreate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MNEMONIC_SUCCESS:
                    hideProgress();
                    Bundle bundle = msg.getData();
                    String filename = (String) bundle.get("filename");
                    LyqbLogger.log(filename);
                    DialogUtil.showWalletCreateResultDialog(getContext(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtil.dialog.dismiss();
//                            AppManager.finishAll();
                            getOperation().forward(MainActivity.class);
                        }
                    });
                    break;
                default:

                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_import_mnemonic, container, false);
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


    @OnClick({R.id.wallet_list, R.id.btn_unlock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wallet_list:
                walletChoose();
                break;
            case R.id.btn_unlock:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    if (TextUtils.isEmpty(etMnemonic.getText().toString())) {
                        ToastUtils.toast("请输入助记词");
                        return;
                    }
//                if (TextUtils.isEmpty(etPassword.getText().toString())) {
//                    ToastUtils.toast("");
//                    return;
//                }
//                if (etMnemonic.getText().toString().length() < 6){
//                    ToastUtils.toast("6位以上密码");
//                    return;
//                }
                    if (TextUtils.isEmpty(walletType.getText().toString())) {
                        ToastUtils.toast("请选择钱包类型");
                        return;
                    }
                    if (walletType.getText().toString().equals("其它")) {
                        if (TextUtils.isEmpty(etDpath.getText().toString())) {
                            ToastUtils.toast("请输入dpath");
                            return;
                        }
                        dpath = etDpath.getText().toString();
                    }
                    LyqbLogger.log(etMnemonic.getText().toString() + "   " + dpath + "   " + etPassword.getText().toString());
                    unlockWallet();

                }
                break;
        }
    }


    /**
     * 生成钱包
     */
    private void unlockWallet() {
        showProgress("加载中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bip39Wallet bip39Wallet;
                try {
                    bip39Wallet = WalletHelper.importFromMnemonic(etMnemonic.getText().toString(), dpath, etPassword.getText().toString(), FileUtils.getKeyStoreLocation(), 0);
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("filename", bip39Wallet.getFilename());
                    bundle.putString("mnemonic", bip39Wallet.getMnemonic());
                    LyqbLogger.log(bip39Wallet.getFilename() + "   " + bip39Wallet.getMnemonic());
                    msg.setData(bundle);
                    msg.what = MNEMONIC_SUCCESS;
                    handlerCreate.sendMessage(msg);
                }  catch (KeystoreSaveException e) {
                    ToastUtils.toast("钱包创建失败");
                    hideProgress();
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 选择钱包类型
     */
    public void walletChoose() {
        OptionPicker picker = new OptionPicker((Activity) getContext(), new String[]{
                "Loopring Wallet", "Imtoken", "MetaMask", "TREZOR (ETH)", "Digital Bitbox", "Exodus", "Jaxx",
                "Ledger (ETH)", "TREZOR (ETC)", "Ledger (ETC)", "SingularDTV", "Network: Testnets", "Network: Expanse",
                "Network: Ubiq", "Network: Ellaism", "other"
        });
        picker.setOffset(1);
        picker.setSelectedIndex(0);
        picker.setTextSize(18);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int position, String option) {
                LyqbLogger.log(position + "  ");
                rlDpath.setVisibility(View.GONE);
                if (position <= 6) {
                    dpath = "m/44'/60'/0'/0";
                } else if (position <= 7) {
                    dpath = "m/44'/60'/0'";
                } else if (position <= 8) {
                    dpath = "m/44'/61'/0'/0";
                } else if (position <= 9) {
                    dpath = "m/44'/60'/160720'/0";
                } else if (position <= 10) {
                    dpath = "m/0'/0'/0";
                } else if (position <= 11) {
                    dpath = "m/44'/1'/0'/0";
                } else if (position <= 12) {
                    dpath = "m/44'/40'/0'/0";
                } else if (position <= 13) {
                    dpath = "m/44'/108'/0'/0";
                } else if (position <= 14) {
                    dpath = "m/44'/163'/0'/0";
                } else if (position <= 15) {
                    dpath = "";
                    rlDpath.setVisibility(View.VISIBLE);
                }
                walletType.setText(option);

            }
        });
        picker.show();
    }
}
