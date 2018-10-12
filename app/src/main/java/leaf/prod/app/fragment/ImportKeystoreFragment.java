package leaf.prod.app.fragment;

import java.io.IOException;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.activity.SetWalletNameActivity;
import leaf.prod.app.model.WalletEntity;
import leaf.prod.app.model.eventbusData.KeystoreData;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.FileUtils;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.utils.ToastUtils;
import leaf.prod.walletsdk.exception.IllegalCredentialException;
import leaf.prod.walletsdk.exception.InvalidKeystoreException;
import leaf.prod.walletsdk.exception.KeystoreCreateException;
import leaf.prod.walletsdk.service.LoopringService;
import leaf.prod.walletsdk.util.KeystoreUtils;
import leaf.prod.walletsdk.util.StringUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    @BindView(R.id.btn_next)
    Button btnNext;

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
                    WalletEntity newWallet = new WalletEntity("", filename, "0x" + address, "");
                    loopringService.notifyCreateWallet(address)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {
                                    hideProgress();
                                    getActivity().getIntent().putExtra("newWallet", newWallet);
                                    getActivity().getIntent().setClass(getActivity(), SetWalletNameActivity.class);
                                    getActivity().startActivity(getActivity().getIntent());
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
        EventBus.getDefault().register(this);
        if (getArguments() != null && !StringUtils.isEmpty(getArguments().getString("result"))) {
            etKeystore.setText(getArguments().getString("result"));
        }
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    @OnClick(R.id.btn_next)
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
        new Thread(() -> {
            try {
                filename = KeystoreUtils.createFromKeystoreJson(etKeystore.getText()
                        .toString(), etPassword.getText().toString(), FileUtils.getKeyStoreLocation(getContext()));
                SPUtils.put(getContext(), "filename", filename);
                handlerCreate.sendEmptyMessage(MNEMONIC_SUCCESS);
            } catch (InvalidKeystoreException e) {
                handlerCreate.sendEmptyMessage(ERROR_TWO);
                e.printStackTrace();
            } catch (IllegalCredentialException | KeystoreCreateException e) {
                handlerCreate.sendEmptyMessage(ERROR_ONE);
                e.printStackTrace();
            }
        }).start();
    }
}
