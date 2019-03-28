package leaf.prod.app.fragment.setupwallet;

import java.io.IOException;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.activity.setting.SetWalletNameActivity;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.walletsdk.exception.InvalidPrivateKeyException;
import leaf.prod.walletsdk.exception.KeystoreCreateException;
import leaf.prod.walletsdk.model.wallet.ImportWalletType;
import leaf.prod.walletsdk.model.wallet.WalletEntity;
import leaf.prod.walletsdk.model.wallet.eventbusData.PrivateKeyData;
import leaf.prod.walletsdk.service.RelayService;
import leaf.prod.walletsdk.util.FileUtils;
import leaf.prod.walletsdk.util.KeystoreUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class ImportPrivateKeyFragment extends BaseFragment {

    public final static int MNEMONIC_SUCCESS = 1;

    public final static int CREATE_SUCCESS = 2;

    public final static int ERROR_ONE = 3;

    public final static int ERROR_TWO = 4;

    public final static int ERROR_THREE = 5;

    public final static int ERROR_FOUR = 6;

    Unbinder unbinder;

    @BindView(R.id.et_password)
    MaterialEditText etPassword;

    @BindView(R.id.et_repeat_password)
    MaterialEditText etRepeatPassword;

    @BindView(R.id.btn_next)
    Button btnNext;

    @BindView(R.id.et_private_key)
    MaterialEditText etPrivateKey;

    private String address;//钱包地址

    private String filename;//钱包名称

    private RelayService loopringService = new RelayService();

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
                    WalletEntity newWallet = WalletEntity.builder()
                            .filename(filename)
                            .address(address.toLowerCase().startsWith("0x") ? address : "0x" + address)
                            .walletType(ImportWalletType.KEY_STORE)
                            .chooseTokenList(Arrays.asList("ETH", "WETH", "LRC"))
                            .build();
                    //                    WalletEntity newWallet = new WalletEntity("", filename, address, "", null, "", "", ImportWalletType.PRIVATE_KEY);
                    loopringService.notifyCreateWallet(address)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {
                                    hideProgress();
                                    if (WalletUtil.isWalletExisted(getContext(), newWallet)) {
                                        RxToast.error(getResources().getString(R.string.wallet_existed));
                                    } else {
                                        getOperation().addParameter("newWallet", newWallet);
                                        getOperation().forward(SetWalletNameActivity.class);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    RxToast.error(getResources().getString(R.string.add_wallet_error));
                                    hideProgress();
                                }

                                @Override
                                public void onNext(String s) {
                                }
                            });
                    break;
                case ERROR_ONE:
                    RxToast.error(getResources().getString(R.string.private_key_error));
                    hideProgress();
                    break;
                case ERROR_TWO:
                    hideProgress();
                    RxToast.error(getResources().getString(R.string.add_wallet_error));
                    break;
                case ERROR_THREE:
                case ERROR_FOUR:
                    hideProgress();
                    RxToast.error(getResources().getString(R.string.local_file_error));
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
                    address = FileUtils.getFileFromSD(getContext(), filename);
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
        layout = inflater.inflate(R.layout.fragment_import_private_key, container, false);
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
    public void onMessageEvent(PrivateKeyData event) {
        /**
         * 将扫描的结果存到输入框中
         */
        etPrivateKey.setText(event.getPrivateKey());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
            if (TextUtils.isEmpty(etPrivateKey.getText().toString())) {
                RxToast.warning(getResources().getString(R.string.private_key_hint));
                return;
            }
            if (TextUtils.isEmpty(etPassword.getText().toString())) {
                RxToast.warning(getResources().getString(R.string.put_password));
                return;
            }
            if (etPassword.getText().toString().length() < 6) {
                RxToast.warning(getResources().getString(R.string.valid_password));
                return;
            }
            if (TextUtils.isEmpty(etRepeatPassword.getText().toString())) {
                RxToast.warning(getResources().getString(R.string.repeat_password));
                return;
            }
            if (!etRepeatPassword.getText().toString().equals(etPassword.getText().toString())) {
                RxToast.error(getResources().getString(R.string.password_match_toast));
                return;
            }
            unlockWallet();
        }
    }

    /**
     * 生成钱包
     */
    private void unlockWallet() {
        showProgress("加载中...");
        new Thread(() -> {
            try {
                filename = KeystoreUtils.createFromPrivateKey(etPrivateKey.getText()
                        .toString().trim(), etPassword.getText()
                        .toString(), FileUtils.getKeyStoreLocation(getContext()));
                //                SPUtils.put(getContext(), "filename", filename);
                handlerCreate.sendEmptyMessage(MNEMONIC_SUCCESS);
            } catch (KeystoreCreateException e) {
                handlerCreate.sendEmptyMessage(ERROR_TWO);
                e.printStackTrace();
            } catch (InvalidPrivateKeyException e) {
                handlerCreate.sendEmptyMessage(ERROR_ONE);
                e.printStackTrace();
            }
        }).start();
    }
}
