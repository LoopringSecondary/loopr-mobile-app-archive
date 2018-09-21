package com.tomcat360.lyqb.activity;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import com.lyqb.walletsdk.TransactionHelper;
import com.lyqb.walletsdk.WalletHelper;
import com.lyqb.walletsdk.exception.IllegalCredentialException;
import com.lyqb.walletsdk.exception.InvalidKeystoreException;
import com.lyqb.walletsdk.exception.TransactionException;
import com.lyqb.walletsdk.model.Account;
import com.lyqb.walletsdk.model.TransactionObject;
import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.lyqb.walletsdk.model.response.data.Token;
import com.lyqb.walletsdk.service.EthereumService;
import com.lyqb.walletsdk.service.LoopringService;
import com.lyqb.walletsdk.util.UnitConverter;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.manager.BalanceDataManager;
import com.tomcat360.lyqb.manager.GasDataManager;
import com.tomcat360.lyqb.manager.MarketcapDataManager;
import com.tomcat360.lyqb.manager.TokenDataManager;
import com.tomcat360.lyqb.utils.ButtonClickUtil;
import com.tomcat360.lyqb.utils.CurrencyUtil;
import com.tomcat360.lyqb.utils.FileUtils;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.NumberUtils;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.TitleView;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendActivity extends BaseActivity {

    public final static int SEND_SUCCESS = 3;

    public final static int SEND_FAILED = 4;

    public final static int ERROR_ONE = 5;

    public final static int ERROR_TWO = 6;

    public final static int ERROR_THREE = 7;

    public final static int ERROR_FOUR = 8;

    private static int REQUEST_CODE = 1;  //二维码扫一扫code

    private static int TOKEN_CODE = 2;  //选择币种code

    private static EthereumService ethereumService = new EthereumService();

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.wallet_image)
    ImageView walletImage;

    @BindView(R.id.wallet_symbol)
    TextView walletSymbol;

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
    IndicatorSeekBar seekBar;

    @BindView(R.id.transacition_fee)
    TextView transacitionFee;

    @BindView(R.id.btn_send)
    Button btnSend;

    @BindView(R.id.ll_show_fee)
    LinearLayout llShowFee;

    private String errorMes;  //异常错误信息

    private double amountTotal; //选中币的值

    private double amountSend; //输入转币金额

    private double gasFee = 0.0002; //基础邮费

    private String address; //钱包地址

    private BigInteger nonce;

    private BigInteger gasPrice;

    private String sendChoose;

    private LoopringService loopringService = new LoopringService();

    private BalanceDataManager balanceManager;

    private TokenDataManager tokenDataManager;

    private GasDataManager gasDataManager;

    private MarketcapDataManager marketcapDataManager;

    /**
     * 确认转出dialog
     */
    private AlertDialog confirmDialog;

    /**
     * 输入密码dialog
     *
     * @param context
     * @param listener
     */
    private AlertDialog passwordDialog;

    @SuppressLint("HandlerLeak")
    Handler handlerCreate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND_SUCCESS:
                    hideProgress();
                    ToastUtils.toast("发送成功");
                    passwordDialog.dismiss();
                    break;
                case SEND_FAILED:
                    hideProgress();
                    ToastUtils.toast("转账失败，请重试" + errorMes);
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

    private AlertDialog dialog;

    private TextView dialogTitle;

    private TextView tvAmount;

    private TextView tvWalletInfo;

    private ImageView cancel;

    private TextView recommendGas;

    private IndicatorSeekBar gasSeekBar;

    private int value;

    private Double gasEthValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
        LyqbLogger.log((String) SPUtils.get(this, "filename", ""));
    }

    @Override
    protected void initPresenter() {
        balanceManager = BalanceDataManager.getInstance(this);
        tokenDataManager = TokenDataManager.getInstance(this);
        marketcapDataManager = MarketcapDataManager.getInstance(this);
        gasDataManager = GasDataManager.getInstance(this);
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
        address = (String) SPUtils.get(this, "address", "");
        sendChoose = (String) SPUtils.get(this, "send_choose", "LRC");
        gasDataManager.getGasObservable().subscribe(gasPrice -> {
            gasDataManager.setRecommendGasPrice(gasPrice);
            gasEthValue = Double.parseDouble(gasDataManager.getGasAmountInETH(gasDataManager.getGasLimitByType("token_transfer")
                    .toString(), gasDataManager.getGasPriceString()));
            transacitionFee.setText(new StringBuilder(gasEthValue.toString()).append(" ETH ≈ ")
                    .append(CurrencyUtil.format(this, gasEthValue * marketcapDataManager.getPriceBySymbol("ETH"))));
        });
        List<BalanceResult.Asset> listAsset = BalanceDataManager.getInstance(this).getAssets();
        String valueShow = "";
        for (BalanceResult.Asset asset : listAsset) {
            if (asset.getSymbol().equalsIgnoreCase(sendChoose)) {
                setWalletImage(asset.getSymbol());
                amountTotal = asset.getValue();
                valueShow = asset.getValueShown();
            }
        }
        sendWalletName.setText(sendChoose);
        walletName2.setText(sendChoose);
        sendWalletCount.setText(String.valueOf(valueShow + " " + sendChoose));
    }

    @Override
    public void onResume() {
        super.onResume();
        initSeekbar();
        initMoneyAmount();
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

    public void showConfirmDialog(Context context) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_send_confirm, null);
        builder.setView(view);
        TextView payAmount = view.findViewById(R.id.pay_amount);
        TextView toAddress = view.findViewById(R.id.to_address);
        TextView formAddress = view.findViewById(R.id.form_address);
        TextView tvGassFee = view.findViewById(R.id.gass_fee);
        Button confirm = view.findViewById(R.id.btn_confirm);
        payAmount.setText(moneyAmount.getText().toString());
        toAddress.setText(walletAddress.getText().toString());
        formAddress.setText(address);
        BigInteger gas = gasPrice.multiply(new BigInteger("25200"));
        BigDecimal bigDecimal = UnitConverter.weiToEth(gas.toString());
        tvGassFee.setText(new StringBuilder(bigDecimal.toPlainString().substring(0, 8)).append(" ETH = ")
                .append(NumberUtils.formatTwo(Double.toString(gasFee), Integer.toString(1))));
        confirm.setOnClickListener(v -> {
            confirmDialog.dismiss();
            showPasswordDialog();
        });
        builder.setCancelable(true);
        confirmDialog = null;
        confirmDialog = builder.create();
        confirmDialog.setCancelable(true);
        confirmDialog.setCanceledOnTouchOutside(true);
        confirmDialog.show();
    }

    public void showPasswordDialog() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.DialogTheme);//
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_put_password, null);
        builder.setView(view);
        final EditText passwordInput = view.findViewById(R.id.password_input);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        cancel.setOnClickListener(v -> passwordDialog.dismiss());
        confirm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(passwordInput.getText().toString())) {
                ToastUtils.toast("请输入密码");
            } else {
                send(passwordInput.getText().toString());
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
        new Thread(() -> {
            try {
                Account account;
                String keystore = FileUtils.getKeystoreFromSD(SendActivity.this);
                account = WalletHelper.unlockWallet(password, keystore); //获取account信息，里面有privatekey
                Integer chanid = 1;  //chanid
                BigInteger value = UnitConverter.ethToWei(moneyAmount.getText().toString()); //转账金额
                String nonceStr = loopringService.getNonce(address).toBlocking().single();
                long nonceLong = Long.valueOf(nonceStr, 16);   //d=255
                nonce = BigInteger.valueOf(nonceLong);//获得nonce
                LyqbLogger.log(nonce + "");
                //调用transaction方法
                TransactionObject transaction = TransactionHelper.createTransaction(chanid.byteValue(), address, walletAddress
                        .getText()
                        .toString(), nonce, gasPrice, BigInteger.valueOf(25200), value, "");
                String txhash = TransactionHelper.sendTransaction(transaction, account.getPrivateKey());
                LyqbLogger.log(txhash);
                handlerCreate.sendEmptyMessage(SEND_SUCCESS);
            } catch (TransactionException e) {
                errorMes = e.getMessage();
                handlerCreate.sendEmptyMessage(SEND_FAILED);
                e.printStackTrace();
            } catch (InvalidKeystoreException | IllegalCredentialException e) {
                handlerCreate.sendEmptyMessage(ERROR_THREE);
                e.printStackTrace();
            } catch (JSONException | IOException e) {
                handlerCreate.sendEmptyMessage(ERROR_FOUR);
                e.printStackTrace();
            }
        }).start();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //            处理扫描结果（在界面上显示）
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
                BalanceResult.Asset asset = balanceManager.getAssetBySymbol(symbol);
                setWalletImage(asset.getSymbol());
                sendWalletName.setText(symbol);
                walletName2.setText(symbol);
                sendWalletCount.setText(asset.getValueShown() + " " + symbol);
                sendChoose = asset.getSymbol();
            }
        }
    }

    /**
     * @param context
     */
    public void showFeeDialog(Context context) {
        if (dialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_fee, null);
            builder.setView(view);
            dialogTitle = view.findViewById(R.id.title);
            tvAmount = view.findViewById(R.id.tv_amount);
            tvWalletInfo = view.findViewById(R.id.tv_wallet_info);
            cancel = view.findViewById(R.id.cancel);
            recommendGas = view.findViewById(R.id.recommend_gas);
            gasSeekBar = view.findViewById(R.id.gasSeekBar);
            gasSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
                @Override
                public void onSeeking(SeekParams seekParams) {
                    gasDataManager.setCustomizeGasPriceInGWei((double) seekParams.progressFloat);
                    gasEthValue = Double.parseDouble(gasDataManager.getGasAmountInETH(String.valueOf(gasDataManager.getGasLimitByType("token_transfer")), String
                            .valueOf(gasDataManager.getCustomizeGasPriceInWei())));
                    tvAmount.setText(new StringBuilder(gasEthValue.toString()).append(" ETH ≈ ")
                            .append(CurrencyUtil.format(view.getContext(), gasEthValue * marketcapDataManager.getPriceBySymbol("ETH"))));
                    tvWalletInfo.setText(new StringBuilder("Gas limit(").append(gasDataManager.getGasLimitByType("token_transfer"))
                            .append(") * Gas Price(")
                            .append((int) gasDataManager.getGasPriceInGwei())
                            .append(" Gwei)"));
                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                }
            });
            recommendGas.setOnClickListener(view1 -> gasSeekBar.post(() -> gasSeekBar.setProgress(gasDataManager.getRecommendGasPriceInGWei()
                    .intValue())));
            builder.setCancelable(true);
            dialog = builder.create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.BOTTOM);
        }
        cancel.setOnClickListener(v -> dialog.dismiss());
        gasSeekBar.setMin(Float.parseFloat(NumberUtils.format1(gasDataManager.getRecommendGasPriceInGWei()
                .divide(new BigDecimal(2))
                .doubleValue(), 1)));
        gasSeekBar.setMax(Float.parseFloat(NumberUtils.format1(gasDataManager.getRecommendGasPriceInGWei()
                .multiply(new BigDecimal(2))
                .doubleValue(), 1)));
        gasSeekBar.setProgress((float) gasDataManager.getGasPriceInGwei());
        tvAmount.setText(new StringBuilder(gasEthValue.toString()).append(" ETH ≈ ")
                .append(CurrencyUtil.format(context, gasEthValue * marketcapDataManager.getPriceBySymbol("ETH"))));
        tvWalletInfo.setText(new StringBuilder("Gas limit(").append(gasDataManager.getGasLimitByType("token_transfer"))
                .append(") * Gas Price(")
                .append((int) gasDataManager.getGasPriceInGwei())
                .append(" Gwei)"));
        dialog.show();
    }

    private void setWalletImage(String symbol) {
        Token token = tokenDataManager.getTokenBySymbol(symbol);
        if (token.getImageResId() != 0) {
            walletSymbol.setVisibility(View.GONE);
            walletImage.setImageResource(token.getImageResId());
            walletImage.setVisibility(View.VISIBLE);
        } else {
            walletImage.setVisibility(View.GONE);
            walletSymbol.setText(symbol);
            walletSymbol.setVisibility(View.VISIBLE);
        }
    }

    private void initSeekbar() {
        moneyAmount.post(() -> moneyAmount.setText(""));
        seekBar.setProgress(0);
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                double value = balanceManager.getAssetBySymbol(sendChoose)
                        .getValue() * seekParams.progressFloat / 100;
                moneyAmount.setText(NumberUtils.format1(value, balanceManager.getPrecisionBySymbol(sendChoose)));
                amountToast.setText(CurrencyUtil.format(SendActivity.this, value * balanceManager.getAssetBySymbol(sendChoose)
                        .getLegalValue()));
                Selection.setSelection(moneyAmount.getText(), moneyAmount.getText().length());
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
    }

    private void initMoneyAmount() {
        amountToast.setText(CurrencyUtil.format(SendActivity.this, 0));
        moneyAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null || editable.toString().isEmpty())
                    return;
                if (Double.parseDouble(editable.toString()) > amountTotal) {
                    amountToast.setText(getResources().getText(R.string.input_valid_amount));
                    amountToast.setTextColor(getResources().getColor(R.color.colorRed));
                    Animation shakeAnimation = AnimationUtils.loadAnimation(SendActivity.this, R.anim.shake_x);
                    amountToast.startAnimation(shakeAnimation);
                } else {
                    amountToast.setText(balanceManager.getAssetBySymbol(sendChoose).getLegalShown());
                    amountToast.setTextColor(getResources().getColor(R.color.colorNineText));
                }
            }
        });
    }
}
