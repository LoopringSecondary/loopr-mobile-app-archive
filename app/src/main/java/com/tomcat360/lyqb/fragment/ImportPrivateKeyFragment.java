package com.tomcat360.lyqb.fragment;


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

import com.lyqb.walletsdk.WalletHelper;
import com.lyqb.walletsdk.exception.InvalidPrivateKeyException;
import com.lyqb.walletsdk.exception.KeystoreSaveException;
import com.lyqb.walletsdk.model.WalletDetail;
import com.lyqb.walletsdk.service.LooprHttpService;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.activity.MainActivity;
import com.tomcat360.lyqb.model.eventbusData.PrivateKeyData;
import com.tomcat360.lyqb.utils.ButtonClickUtil;
import com.tomcat360.lyqb.utils.DialogUtil;
import com.tomcat360.lyqb.utils.FileUtils;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.view.APP;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


/**
 *
 */
public class ImportPrivateKeyFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.et_password)
    MaterialEditText etPassword;
    @BindView(R.id.et_repeat_password)
    MaterialEditText etRepeatPassword;
    @BindView(R.id.btn_unlock)
    Button btnUnlock;
    @BindView(R.id.et_private_key)
    MaterialEditText etPrivateKey;

    private String address;//钱包地址
    private LooprHttpService looprHttpService;

    public final static int MNEMONIC_SUCCESS = 1;
    public final static int CREATE_SUCCESS = 2;
    public final static int ERROR_ONE = 3;
    public final static int ERROR_TWO = 4;
    public final static int ERROR_THREE = 5;
    public final static int ERROR_FOUR = 6;
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
                    getAddress();
                    break;
                case CREATE_SUCCESS:  //获取keystore中的address成功后，调用解锁钱包方法（unlockWallet）
                    LyqbLogger.log(address);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LyqbLogger.log("22222222" + address);
                            looprHttpService.unlockWallet(address)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<String>() {
                                        @Override
                                        public void onCompleted() {
                                            hideProgress();

                                            DialogUtil.showWalletCreateResultDialog(getContext(), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    DialogUtil.dialog.dismiss();
//                                                    AppManager.finishAll();
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
                    ToastUtils.toast("私钥错误");
                    hideProgress();
                    break;
                case ERROR_TWO:
                    ToastUtils.toast("钱包创建失败");
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
//        looprHttpService = new LooprHttpService(G.RELAY_URL);
        looprHttpService = APP.getLooprHttpService();
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


    @OnClick(R.id.btn_unlock)
    public void onViewClicked() {
        if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
            if (TextUtils.isEmpty(etPrivateKey.getText().toString())) {
                ToastUtils.toast("请输入私钥");
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
            if (TextUtils.isEmpty(etRepeatPassword.getText().toString())) {
                ToastUtils.toast("请再次输入密码");
                return;
            }
            if (!etRepeatPassword.getText().toString().equals(etPassword.getText().toString())) {
                ToastUtils.toast("两次输入密码不一致");
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
                String fileName = null;
                WalletDetail walletDetail = null;
                try {
                    walletDetail = APP.getLoopring().importFromPrivateKey(etPrivateKey.getText().toString().startsWith("0x") ? etPrivateKey.getText().toString().substring(2) : etPrivateKey.getText().toString(), etPassword.getText().toString(), FileUtils.getKeyStoreLocation(getContext()));
                    fileName = walletDetail.getFilename();
                    SPUtils.put(getContext(), "filename", fileName);
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("filename", fileName);
                    LyqbLogger.log(fileName);
                    msg.setData(bundle);
                    msg.what = MNEMONIC_SUCCESS;
                    handlerCreate.sendMessage(msg);
                } catch (InvalidPrivateKeyException e) {
                    handlerCreate.sendEmptyMessage(ERROR_ONE);
                    e.printStackTrace();
                } catch (KeystoreSaveException e) {
                    handlerCreate.sendEmptyMessage(ERROR_ONE);
                    e.printStackTrace();
                }

            }
        }).start();
    }

}
