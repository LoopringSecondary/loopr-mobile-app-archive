package leaf.prod.app.fragment;

import java.io.IOException;
import java.util.List;

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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.web3j.crypto.Credentials;
import leaf.prod.walletsdk.exception.InvalidPrivateKeyException;
import leaf.prod.walletsdk.exception.KeystoreCreateException;
import leaf.prod.walletsdk.service.LoopringService;
import leaf.prod.walletsdk.util.CredentialsUtils;
import leaf.prod.walletsdk.util.KeystoreUtils;
import leaf.prod.walletsdk.util.MnemonicUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import leaf.prod.app.R;
import leaf.prod.app.activity.MainActivity;
import leaf.prod.app.model.WalletEntity;
import leaf.prod.app.model.eventbusData.MnemonicData;
import leaf.prod.app.utils.AppManager;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.DialogUtil;
import leaf.prod.app.utils.FileUtils;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.utils.ToastUtils;
import leaf.prod.app.views.wheelPicker.picker.OptionPicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import leaf.prod.walletsdk.exception.InvalidPrivateKeyException;
import leaf.prod.walletsdk.exception.KeystoreCreateException;
import leaf.prod.walletsdk.service.LoopringService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 *
 */
public class ImportMnemonicFragment extends BaseFragment {

    public final static int MNEMONIC_SUCCESS = 1;

    public final static int CREATE_SUCCESS = 2;

    public final static int ERROR_ONE = 3;

    public final static int ERROR_TWO = 4;

    public final static int ERROR_THREE = 5;

    public final static int ERROR_FOUR = 6;

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

    private String address;//钱包地址

    private String filename;//钱包名称

    private LoopringService loopringService = new LoopringService();

    @SuppressLint("HandlerLeak")
    Handler handlerCreate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MNEMONIC_SUCCESS:
                    hideProgress();
                    getAddress();
                    break;
                case CREATE_SUCCESS:  //获取keystore中的address成功后，调用解锁钱包方法（unlockWallet）
                    SPUtils.put(getContext(), "pas", etPassword.getText().toString());
                    SPUtils.put(getContext(), "hasWallet", true);
                    SPUtils.put(getContext(), "address", "0x" + address);
                    List<WalletEntity> list = SPUtils.getWalletDataList(getContext(), "walletlist", WalletEntity.class);//多钱包，将钱包信息存在本地
                    list.add(new WalletEntity("", filename, "0x" + address, etMnemonic.getText().toString()));
                    SPUtils.setDataList(getContext(), "walletlist", list);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LyqbLogger.log("22222222" + address);
                            loopringService.notifyCreateWallet(address)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<String>() {
                                        @Override
                                        public void onCompleted() {
                                            hideProgress();
                                            DialogUtil.showWalletCreateResultDialog(getContext(), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    DialogUtil.dialog.dismiss();
                                                    AppManager.finishAll();
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
                    ToastUtils.toast("钱包创建失败");
                    hideProgress();
                    break;
                case ERROR_TWO:
                    break;
                case ERROR_THREE:
                    hideProgress();
                    ToastUtils.toast("本地文件读取失败，请重试");
                    break;
                case ERROR_FOUR:
                    hideProgress();
                    ToastUtils.toast("本地文件JSON解析失败，请重试");
                    break;
            }
        }
    };

    public void getAddress() {
        new Thread() {
            @Override
            public void run() {
                Message msg = handlerCreate.obtainMessage();
                try {
                    address = FileUtils.getFileFromSD(getContext());
                    msg.obj = address;
                    msg.what = CREATE_SUCCESS;
                    handlerCreate.sendMessage(msg);
                } catch (IOException e) {
                    handlerCreate.sendEmptyMessage(ERROR_THREE);
                    e.printStackTrace();
                } catch (JSONException e) {
                    handlerCreate.sendEmptyMessage(ERROR_FOUR);
                    e.printStackTrace();
                }
            }
        }.start();
    }

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MnemonicData event) {
        /**
         * 将扫描的结果存到输入框中
         */
        etMnemonic.setText(event.getMnemonic());
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        //        looprHttpService = new LooprHttpService(G.RELAY_URL);
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
                    if (TextUtils.isEmpty(etPassword.getText().toString())) {
                        ToastUtils.toast("请输入密码");
                        return;
                    }
                    if (etPassword.getText().toString().length() < 6) {
                        ToastUtils.toast("请输入6位以上密码");
                        return;
                    }
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
                    LyqbLogger.log(etMnemonic.getText().toString() + "   " + dpath + "   " + etPassword.getText()
                            .toString());
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
                Credentials credentials = null;
                try {
                    credentials = MnemonicUtils.calculateCredentialsFromMnemonic(etMnemonic.getText()
                            .toString(), dpath, etPassword.getText().toString());
                    String privateKeyHexString = CredentialsUtils.toPrivateKeyHexString(credentials.getEcKeyPair()
                            .getPrivateKey());
                    filename = KeystoreUtils.createFromPrivateKey(privateKeyHexString, etPassword.getText()
                            .toString(), FileUtils.getKeyStoreLocation(getContext()));
                    SPUtils.put(getContext(), "filename", filename);
                    handlerCreate.sendEmptyMessage(MNEMONIC_SUCCESS);
                } catch (KeystoreCreateException e) {
                    handlerCreate.sendEmptyMessage(ERROR_ONE);
                    e.printStackTrace();
                } catch (InvalidPrivateKeyException e) {
                    handlerCreate.sendEmptyMessage(ERROR_ONE);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 选择钱包类型
     */
    public void walletChoose() {
        OptionPicker picker = new OptionPicker((Activity) getContext(), new String[]{"Loopring Wallet", "Imtoken", "MetaMask", "TREZOR (ETH)", "Digital Bitbox", "Exodus", "Jaxx", "Ledger (ETH)", "TREZOR (ETC)", "Ledger (ETC)", "SingularDTV", "Network: Testnets", "Network: Expanse", "Network: Ubiq", "Network: Ellaism", "other"});
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
