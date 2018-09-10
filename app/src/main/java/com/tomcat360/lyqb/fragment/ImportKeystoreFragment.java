package com.tomcat360.lyqb.fragment;

import java.io.IOException;
import java.util.List;

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
import com.lyqb.walletsdk.WalletHelper;
import com.lyqb.walletsdk.exception.IllegalCredentialException;
import com.lyqb.walletsdk.exception.InvalidKeystoreException;
import com.lyqb.walletsdk.exception.KeystoreSaveException;
import com.lyqb.walletsdk.model.WalletDetail;
import com.lyqb.walletsdk.service.LoopringService;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.activity.MainActivity;
import com.tomcat360.lyqb.model.WalletEntity;
import com.tomcat360.lyqb.model.eventbusData.KeystoreData;
import com.tomcat360.lyqb.utils.AppManager;
import com.tomcat360.lyqb.utils.ButtonClickUtil;
import com.tomcat360.lyqb.utils.DialogUtil;
import com.tomcat360.lyqb.utils.FileUtils;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 *
 */
public class ImportKeystoreFragment extends BaseFragment {

    public final static int MNEMONIC_SUCCESS = 1;

    public final static int CREATE_SUCCESS = 2;

    public final static int ERROR_ONE = 3;

    public final static int ERROR_TWO = 4;

    public final static int ERROR_THREE = 5;

    public final static int ERROR_FOUR = 6;

    Unbinder unbinder;

    @BindView(R.id.et_keystore)
    MaterialEditText etKeystore;

    @BindView(R.id.et_password)
    MaterialEditText etPassword;

    @BindView(R.id.btn_unlock)
    Button btnUnlock;

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
                    list.add(new WalletEntity("", filename, "0x" + address, ""));
                    SPUtils.setDataList(getContext(), "walletlist", list);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
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
                    ToastUtils.toast("身份验证失败");
                    hideProgress();
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
        layout = inflater.inflate(R.layout.fragment_import_keystore, container, false);
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
    public void onMessageEvent(KeystoreData event) {
        /**
         * 将扫描的结果存到输入框中
         */
        etKeystore.setText(event.getKeystory());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_unlock)
    public void onViewClicked() {
        if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
            if (TextUtils.isEmpty(etKeystore.getText().toString())) {
                ToastUtils.toast("请输入keystore文件");
                return;
            }
            if (etPassword.getText().toString().length() < 6) {
                ToastUtils.toast("请输入6位以上密码");
                return;
            }
            if (TextUtils.isEmpty(etPassword.getText().toString())) {
                ToastUtils.toast("请输入keystore密码");
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                WalletDetail walletDetail = null;
                try {
                    walletDetail = WalletHelper.createFromKeystore(etKeystore.getText().toString(), etPassword.getText()
                            .toString(), FileUtils.getKeyStoreLocation(getContext()));
                    //                    walletDetail = APP.getLoopring().importFromKeystore(etKeystore.getText().toString(), etPassword.getText().toString(), FileUtils.getKeyStoreLocation(getContext()));
                    filename = walletDetail.getFilename();
                    SPUtils.put(getContext(), "filename", filename);
                    handlerCreate.sendEmptyMessage(MNEMONIC_SUCCESS);
                } catch (KeystoreSaveException e) {
                    handlerCreate.sendEmptyMessage(ERROR_ONE);
                    e.printStackTrace();
                } catch (InvalidKeystoreException e) {
                    handlerCreate.sendEmptyMessage(ERROR_TWO);
                    e.printStackTrace();
                } catch (IllegalCredentialException e) {
                    handlerCreate.sendEmptyMessage(ERROR_ONE);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
