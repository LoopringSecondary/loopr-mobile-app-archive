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

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.activity.GenerateWalletActivity;
import com.tomcat360.lyqb.activity.MainActivity;
import com.tomcat360.lyqb.core.WalletHelper;
import com.tomcat360.lyqb.core.exception.IllegalCredentialException;
import com.tomcat360.lyqb.core.exception.InvalidKeystoreException;
import com.tomcat360.lyqb.core.exception.KeystoreSaveException;
import com.tomcat360.lyqb.utils.AppManager;
import com.tomcat360.lyqb.utils.ButtonClickUtil;
import com.tomcat360.lyqb.utils.DialogUtil;
import com.tomcat360.lyqb.utils.FileUtils;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.ToastUtils;

import org.web3j.crypto.Bip39Wallet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 *
 */
public class ImportKeystoreFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_keystore)
    MaterialEditText etKeystore;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;
    @BindView(R.id.btn_unlock)
    Button btnUnlock;

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
                String fileName = null;
                try {
                    fileName = WalletHelper.importFromKeystore(etKeystore.getText().toString(),etPassword.getText().toString(), FileUtils.getKeyStoreLocation());
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("filename", fileName);
                    LyqbLogger.log(fileName);
                    msg.setData(bundle);
                    msg.what = MNEMONIC_SUCCESS;
                    handlerCreate.sendMessage(msg);
                } catch (KeystoreSaveException e) {
                    ToastUtils.toast("钱包创建失败");
                    e.printStackTrace();
                } catch (InvalidKeystoreException e) {
                    ToastUtils.toast("私钥错误");
                    e.printStackTrace();
                } catch (IllegalCredentialException e) {
                    ToastUtils.toast("身份验证失败");
                    e.printStackTrace();
                }

            }
        }).start();
    }


}
