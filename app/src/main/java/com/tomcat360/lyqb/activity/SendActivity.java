package com.tomcat360.lyqb.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyqb.walletsdk.TransactionHelper;
import com.lyqb.walletsdk.WalletHelper;
import com.lyqb.walletsdk.exception.IllegalCredentialException;
import com.lyqb.walletsdk.exception.InvalidKeystoreException;
import com.lyqb.walletsdk.exception.TransactionException;
import com.lyqb.walletsdk.model.Account;
import com.lyqb.walletsdk.model.TransactionObject;
import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.lyqb.walletsdk.service.EthereumService;
import com.lyqb.walletsdk.service.LoopringService;
import com.lyqb.walletsdk.util.UnitConverter;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.ButtonClickUtil;
import com.tomcat360.lyqb.utils.DialogUtil;
import com.tomcat360.lyqb.utils.FileUtils;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.NumberUtils;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.view.APP;
import com.tomcat360.lyqb.views.RangeSeekBar;
import com.tomcat360.lyqb.views.TitleView;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

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
    private static int TOKEN_CODE = 2;  //选择币种code
    private String errorMes;  //异常错误信息
    private double amountTotal; //选中币的值
    private double amountSend; //输入转币金额
    private double gasFee = 0.0002; //基础邮费
    private String address; //钱包地址
    private BigInteger nonce;
    private BigInteger gasPrice;
    private LoopringService loopringService = new LoopringService();
    private static EthereumService ethereumService = new EthereumService();

    public final static int SEND_SUCCESS = 3;
    public final static int SEND_FAILED = 4;
    public final static int ERROR_ONE = 5;
    public final static int ERROR_TWO = 6;
    public final static int ERROR_THREE = 7;
    public final static int ERROR_FOUR = 8;
    @SuppressLint("HandlerLeak")
    Handler handlerCreate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND_SUCCESS:
                    hideProgress();
                    String txhash = (String) msg.getData().get("txhash");
                    ToastUtils.toast("发送成功");
                    passwordDialog.dismiss();
                    break;
                case SEND_FAILED:
                    hideProgress();
                    ToastUtils.toast("转账失败，请重试"+errorMes);
                    LyqbLogger.log(errorMes);
                    break;
                case ERROR_ONE:
                    hideProgress();
                    ToastUtils.toast("密码输入错误");
                    break;
                case ERROR_TWO:
                    hideProgress();
                    ToastUtils.toast("转账失败" + errorMes);
                    break;
                case ERROR_THREE:
                    hideProgress();
                    ToastUtils.toast("信息获取失败");
                    break;
                case ERROR_FOUR:
                    hideProgress();
                    ToastUtils.toast("keystore获取失败");
                    break;

            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
        LyqbLogger.log((String) SPUtils.get(this, "filename", ""));
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.send));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        address = (String) SPUtils.get(SendActivity.this, "address", "");
    }

    @Override
    public void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {


//                File file = new File(SendActivity.this.getFilesDir().getAbsolutePath() + "/keystore/" + (String) SPUtils.get(SendActivity.this, "filename", ""));

                String nonceStr = loopringService.getNonce(address).toBlocking().single();
                String gasPriceStr = loopringService.getEstimateGasPrice().toBlocking().single();

                long nonceLong = Long.valueOf(nonceStr, 16);   //d=255
                long gasPriceLong = Long.valueOf(gasPriceStr.substring(2), 16);   //d=255
                nonce = BigInteger.valueOf(nonceLong);
                gasPrice = BigInteger.valueOf(gasPriceLong);
                LyqbLogger.log(nonce + "    " + gasPrice);

//            BigInteger estimateGasLimit = ethereumService.estimateGasLimit(transactionDetail);


            }
        }).start();


        String sendChoose = (String) SPUtils.get(this, "send_choose", "LRC");
        List<BalanceResult.Token> listToken = APP.getListToken();
        for (int i = 0; i < listToken.size(); i++) {
            if (listToken.get(i).equals(sendChoose)) {
                amountTotal = listToken.get(i).getBalance().doubleValue();
            }
        }
//        amount = (double)SPUtils.get(this,"send_amount",0.00);

        sendWalletName.setText(sendChoose);
        walletName2.setText(sendChoose);
        sendWalletCount.setText(amountTotal + " " + sendChoose);
    }

    @OnClick({R.id.ll_manager_wallet, R.id.iv_scan, R.id.btn_send, R.id.ll_show_fee})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_manager_wallet:
                getOperation().forwardForResult(SendListChooseActivity.class, TOKEN_CODE);
                break;
            case R.id.iv_scan:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    startActivityForResult(new Intent(this, ActivityScanerCode.class), REQUEST_CODE);
                }
                break;
            case R.id.ll_show_fee:
                showFeeDialog(this);
                break;
            case R.id.btn_send:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    checkInfo();
                }
                break;
        }
    }

    private void checkInfo() {
        String amount = moneyAmount.getText().toString();
        if (TextUtils.isEmpty(walletAddress.getText().toString())) {
            ToastUtils.toast("请输入钱包地址");
            return;
        }
        if (TextUtils.isEmpty(amount)) {
            ToastUtils.toast("请输入额度");
            return;
        }
        amountSend = Double.parseDouble(amount);
        if (amountSend < amountTotal) {
            ToastUtils.toast("可用余额不足");
            return;
        }
        showConfirmDialog(this);
    }


    /**
     * 确认转出dialog
     */
    private AlertDialog confirmDialog;

    public void showConfirmDialog(Context context) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_send_confirm, null);
        builder.setView(view);
        TextView payAmount = (TextView) view.findViewById(R.id.pay_amount);
        TextView toAddress = (TextView) view.findViewById(R.id.to_address);
        TextView formAddress = (TextView) view.findViewById(R.id.form_address);
        TextView tvGassFee = (TextView) view.findViewById(R.id.gass_fee);
        Button confirm = (Button) view.findViewById(R.id.btn_confirm);

        payAmount.setText(moneyAmount.getText().toString());
        toAddress.setText(walletAddress.getText().toString());
        formAddress.setText(address);


        BigInteger gas = gasPrice.multiply(new BigInteger("21000"));
        BigDecimal bigDecimal = UnitConverter.weiToEth(gas.toString());
        tvGassFee.setText(bigDecimal.toPlainString().substring(0,8) + " ETH = " + NumberUtils.formatTwo(Double.toString(gasFee), Integer.toString(1)));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                showPasswordDialog();
            }
        });
        builder.setCancelable(true);
        confirmDialog = null;
        confirmDialog = builder.create();
        confirmDialog.setCancelable(true);
        confirmDialog.setCanceledOnTouchOutside(true);
        confirmDialog.show();
    }

    /**
     * 输入密码dialog
     *
     * @param context
     * @param listener
     */
    private AlertDialog passwordDialog;

    public void showPasswordDialog() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.DialogTheme);//
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_put_password, null);
        builder.setView(view);
        TextView title = (TextView) view.findViewById(R.id.title);
        final EditText passwordInput = (EditText) view.findViewById(R.id.password_input);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(passwordInput.getText().toString())) {
                    ToastUtils.toast("请输入密码");
                    return;
                } else {
                    send(passwordInput.getText().toString());
                }

            }
        });
        builder.setCancelable(true);
        passwordDialog = null;
        passwordDialog = builder.create();
        passwordDialog.setCancelable(true);
        passwordDialog.setCanceledOnTouchOutside(true);
        passwordDialog.show();

    }


    private void send(String password) {
        showProgress("加载中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Account account = null;
                    String keystore = FileUtils.getKeystoreFromSD(SendActivity.this);
                    account = WalletHelper.unlockWallet(password, keystore);

                    Integer chanid = new Integer(1);
                    BigInteger value = UnitConverter.ethToWei(moneyAmount.getText().toString());

                    TransactionObject transaction = TransactionHelper.createTransaction(chanid.byteValue(), address, walletAddress.getText().toString(), nonce, gasPrice, BigInteger.valueOf(21000), value, "");
                    String txhash = TransactionHelper.sendTransaction(transaction, account.getPrivateKey());
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("txhash",txhash);
                    msg.setData(bundle);
                    msg.what = SEND_SUCCESS;
                    handlerCreate.sendMessage(msg);
//                    LyqbLogger.log(s);
//                    handlerCreate.sendEmptyMessage(SEND_SUCCESS);
                } catch (TransactionException e) {
                    errorMes = e.getMessage();
                    handlerCreate.sendEmptyMessage(SEND_FAILED);
                    e.printStackTrace();
                } catch (InvalidKeystoreException e) {
                    handlerCreate.sendEmptyMessage(ERROR_THREE);
                    e.printStackTrace();
                } catch (IllegalCredentialException e) {
                    handlerCreate.sendEmptyMessage(ERROR_THREE);
                    e.printStackTrace();
                } catch (JSONException e) {
                    handlerCreate.sendEmptyMessage(ERROR_FOUR);
                    e.printStackTrace();
                } catch (IOException e) {
                    handlerCreate.sendEmptyMessage(ERROR_FOUR);
                    e.printStackTrace();
                }
//                handlerCreate.sendEmptyMessage(SEND_SUCCESS);

//                String s = "";
//                try {
//                    File file = new File(SendActivity.this.getFilesDir().getAbsolutePath()  + "/keystore/" + (String) SPUtils.get(SendActivity.this, "filename", ""));
//                    walletFile = KeystoreHelper.loadFromFile(file);
//                    Credentials credentials = WalletHelper.unlockWallet("qqqqqq", walletFile);
//                    String ethTrasnferTransaction = TransactionHelper.createEthTrasnferTransaction("0x75a6543F96e4177128f8CaA35db739e5088489B0", credentials, BigInteger.valueOf(0));
//                    String s = TransactionHelper.sendTransaction(ethTrasnferTransaction);

//                    Credentials credentials = APP.getLoopring().unlockWallet("qqqqqq", FileUtils.getKeystoreFile(SendActivity.this));
//                    String s = APP.getLoopring().sendTransaction("0x75a6543F96e4177128f8CaA35db739e5088489B0", BigInteger.valueOf(0), credentials);
//                    if (s != null) {
//                        handlerCreate.sendEmptyMessage(SEND_SUCCESS);
//                    } else {
//                        handlerCreate.sendEmptyMessage(SEND_FAILED);
//                    }
//                    LyqbLogger.log(s);
//                } catch (CipherException e) {
//                    handlerCreate.sendEmptyMessage(ERROR_ONE);
//                    e.printStackTrace();
//                } catch (TransactionFailureException e) {
//                    errorMes = e.getMessage();
//                    handlerCreate.sendEmptyMessage(ERROR_TWO);
//                    e.printStackTrace();
//                }
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
        } else if (requestCode == TOKEN_CODE) {
            if (resultCode == 1) {
                String symbol = data.getStringExtra("symbol");
                amountTotal = data.getDoubleExtra("amount", 0.00);
                sendWalletName.setText(symbol);
                walletName2.setText(symbol);
                sendWalletCount.setText(amountTotal + " " + symbol);
            }
        }
    }

    private AlertDialog dialog;
    private TextView dialogTitle;
    private TextView tvAmount;
    private TextView tvWalletInfo;
    private ImageView cancel;
    private RangeSeekBar dialogSeekBar;
    private int value;

    /**
     * @param context
     */

    public void showFeeDialog(Context context) {
        if (dialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_fee, null);
            builder.setView(view);
            dialogTitle = (TextView) view.findViewById(R.id.title);
            tvAmount = (TextView) view.findViewById(R.id.tv_amount);
            tvWalletInfo = (TextView) view.findViewById(R.id.tv_wallet_info);
            cancel = (ImageView) view.findViewById(R.id.cancel);
            dialogSeekBar = (RangeSeekBar) view.findViewById(R.id.seekBar);

            tvAmount.setText(NumberUtils.formatSix(Double.toString(gasFee), Integer.toString(1)) + " ETH = " + NumberUtils.formatTwo(Double.toString(gasFee), Integer.toString(1)));
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialogSeekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
                @Override
                public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                    value = (int) min;

                    String dd = NumberUtils.formatSix(Double.toString(0.000200), Integer.toString(value));
                    gasFee = Double.parseDouble(dd);
                    tvAmount.setText(dd + " ETH = " + NumberUtils.formatTwo(Double.toString(0.06), Integer.toString(value)));
                    tvWalletInfo.setText("Gas limit(100000) * Gas Price(" + NumberUtils.numberformat1((double) value) + " Gwei)");
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
