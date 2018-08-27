package com.tomcat360.lyqb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyqb.walletsdk.KeystoreHelper;
import com.lyqb.walletsdk.TransactionHelper;
import com.lyqb.walletsdk.WalletHelper;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.model.eventbusData.KeystoreData;
import com.tomcat360.lyqb.model.eventbusData.MnemonicData;
import com.tomcat360.lyqb.model.eventbusData.PrivateKeyData;
import com.tomcat360.lyqb.utils.ButtonClickUtil;
import com.tomcat360.lyqb.utils.DialogUtil;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.RangeSeekBar;
import com.tomcat360.lyqb.views.TitleView;
import com.vondear.rxtool.RxTool;

import org.greenrobot.eventbus.EventBus;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.wallet_image)
    ImageView walletImage;
    @BindView(R.id.send_wallet_name)
    TextView sendWalletName;
    @BindView(R.id.send_wallet_count)
    TextView sendWalletCount;
    @BindView(R.id.ll_manager_wallet)
    LinearLayout llManagerWallet;
    @BindView(R.id.wallet_address)
    MaterialEditText walletAddress;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.address_toast)
    TextView addressToast;
    @BindView(R.id.money_amount)
    MaterialEditText moneyAmount;
    @BindView(R.id.wallet_name2)
    TextView walletName2;
    @BindView(R.id.amount_toast)
    TextView amountToast;
    @BindView(R.id.seekBar)
    RangeSeekBar seekBar;
    @BindView(R.id.transacition_fee)
    TextView transacitionFee;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.ll_show_fee)
    LinearLayout llShowFee;

    private static int REQUEST_CODE = 1;  //二维码扫一扫code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        LyqbLogger.log((String) SPUtils.get(this, "filename", ""));
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.send));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.ll_manager_wallet, R.id.iv_scan, R.id.btn_send, R.id.ll_show_fee})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_manager_wallet:
                getOperation().forward(TokenListActivity.class);
                break;
            case R.id.iv_scan:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    startActivityForResult(new Intent(this, ActivityScanerCode.class), REQUEST_CODE);
                }
                break;
            case R.id.ll_show_fee:
                showFeeDialog(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case R.id.btn_send:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    checkInfo();
                }
                break;
        }
    }

    private void checkInfo() {
        if (TextUtils.isEmpty(walletAddress.getText().toString())) {
            ToastUtils.toast("请输入钱包地址");
            return;
        }
        if (TextUtils.isEmpty(moneyAmount.getText().toString())) {
            ToastUtils.toast("请输入额度");
            return;
        }
        if (TextUtils.isEmpty(moneyAmount.getText().toString())) {
            ToastUtils.toast("请输入额度");
            return;
        }
        send();
    }

    private void send() {
        showProgress("加载中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WalletFile walletFile = null;
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/keystore/" + (String) SPUtils.get(SendActivity.this, "filename", ""));
                    walletFile = KeystoreHelper.loadFromFile(file);
                    Credentials credentials = WalletHelper.unlock("qqqqqq", walletFile);
                    String ethTrasnferTransaction = TransactionHelper.createEthTrasnferTransaction("0x75a6543F96e4177128f8CaA35db739e5088489B0", credentials, BigInteger.valueOf(0));
                    String s = TransactionHelper.sendTransaction(ethTrasnferTransaction);
                    LyqbLogger.log(s);
                    hideProgress();
                } catch (FileNotFoundException e) {
                    ToastUtils.toast("本地文件读取失败，请重试");
                    e.printStackTrace();
                } catch (IOException e) {
                    ToastUtils.toast("本地文件读取失败，请重试");
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {

                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = bundle.getString("result");
                LyqbLogger.log(result);
                walletAddress.setText(result);
            }
        }
    }

    public AlertDialog dialog;
    TextView dialogTitle;
    TextView tvAmount;
    TextView tvWalletInfo;
    ImageView cancel;
    RangeSeekBar dialogSeekBar;

    /**
     * @param context
     * @param listener
     */
    public void showFeeDialog(Context context, View.OnClickListener listener) {
        if (dialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_fee, null);
            builder.setView(view);
            dialogTitle = (TextView) view.findViewById(R.id.title);
            tvAmount = (TextView) view.findViewById(R.id.tv_amount);
            tvWalletInfo = (TextView) view.findViewById(R.id.tv_wallet_info);
            cancel = (ImageView) view.findViewById(R.id.cancel);
            dialogSeekBar = (RangeSeekBar) view.findViewById(R.id.seekBar);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);
            dialog = builder.create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
        } else {
            dialog.show();
        }


    }

}
