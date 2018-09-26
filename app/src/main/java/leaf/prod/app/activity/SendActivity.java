package leaf.prod.app.activity;

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
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.manager.BalanceDataManager;
import leaf.prod.app.manager.GasDataManager;
import leaf.prod.app.manager.MarketcapDataManager;
import leaf.prod.app.manager.TokenDataManager;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.CurrencyUtil;
import leaf.prod.app.utils.FileUtils;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.NumberUtils;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.utils.ToastUtils;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.Erc20TransactionManager;
import leaf.prod.walletsdk.EthTransactionManager;
import leaf.prod.walletsdk.Transfer;
import leaf.prod.walletsdk.exception.IllegalCredentialException;
import leaf.prod.walletsdk.exception.InvalidKeystoreException;
import leaf.prod.walletsdk.exception.TransactionException;
import leaf.prod.walletsdk.model.response.data.BalanceResult;
import leaf.prod.walletsdk.model.response.data.Token;
import leaf.prod.walletsdk.service.LoopringService;
import leaf.prod.walletsdk.util.KeystoreUtils;
import leaf.prod.walletsdk.util.UnitConverter;

public class SendActivity extends BaseActivity {

    public final static int SEND_SUCCESS = 3;

    public final static int SEND_FAILED = 4;

    public final static int ERROR_ONE = 5;

    public final static int ERROR_TWO = 6;

    public final static int ERROR_THREE = 7;

    public final static int ERROR_FOUR = 8;

    private static int REQUEST_CODE = 1;  //二维码扫一扫code

    private static int TOKEN_CODE = 2;  //选择币种code

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

    private String sendChoose;

    private LoopringService loopringService = new LoopringService();

    private BalanceDataManager balanceManager;

    private TokenDataManager tokenDataManager;

    private GasDataManager gasDataManager;

    private MarketcapDataManager marketcapDataManager;

    private Erc20TransactionManager erc20TransactionManager;

    private EthTransactionManager ethTransactionManager;

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

    /**
     * 邮费选择弹窗相关组件
     */
    private AlertDialog feeDialog;

    private TextView tvAmount;

    private TextView tvWalletInfo;

    private ImageView cancel;

    private TextView recommendGas;

    private IndicatorSeekBar gasSeekBar;

    /**
     * 确认转出弹窗相关组件
     */
    private AlertDialog confirmDialog;

    private TextView payAmount;

    private TextView toAddress;

    private TextView formAddress;

    private TextView tvGassFee;

    private Button confirm;

    private Double gasEthValue;

    /**
     * 输入密码dialog
     */
    private AlertDialog passwordDialog;

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
            LyqbLogger.log("gas: " + gasPrice);
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
        initWalletAddress();
    }

    @Override
    public void onRestart() {
        super.onRestart();
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
        if (amountSend > amountTotal) {
            ToastUtils.toast("可用余额不足");
            return;
        }
        showConfirmDialog(this);
    }

    public void showConfirmDialog(Context context) {
        if (confirmDialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_send_confirm, null);
            builder.setView(view);
            payAmount = view.findViewById(R.id.pay_amount);
            toAddress = view.findViewById(R.id.to_address);
            formAddress = view.findViewById(R.id.form_address);
            tvGassFee = view.findViewById(R.id.gass_fee);
            confirm = view.findViewById(R.id.btn_confirm);
            confirm.setOnClickListener(v -> {
                confirmDialog.dismiss();
                showPasswordDialog();
            });
            builder.setCancelable(true);
            confirmDialog = null;
            confirmDialog = builder.create();
            confirmDialog.setCancelable(true);
            confirmDialog.setCanceledOnTouchOutside(true);
        }
        payAmount.setText(moneyAmount.getText().toString());
        toAddress.setText(walletAddress.getText().toString());
        formAddress.setText(address);
        tvGassFee.setText(transacitionFee.getText());
        confirmDialog.show();
    }

    public void showPasswordDialog() {
        if (passwordDialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.DialogTheme);//
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_put_password, null);
            builder.setView(view);
            final EditText passwordInput = view.findViewById(R.id.password_input);
            view.findViewById(R.id.cancel).setOnClickListener(v -> passwordDialog.dismiss());
            view.findViewById(R.id.confirm).setOnClickListener(v -> {
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
        }
        passwordDialog.show();
    }

    private void send(String password) {
        showProgress("加载中");
        new Thread(() -> {
            try {
                String keystore = FileUtils.getKeystoreFromSD(SendActivity.this);
                Credentials credentials = KeystoreUtils.unlock(password, keystore);
                BigInteger values = UnitConverter.ethToWei(moneyAmount.getText().toString()); //转账金额
                //调用transaction方法
                Transfer transfer = new Transfer(credentials);
                String txHash;
                if (sendChoose.equals("ETH")) {
                    txHash = transfer.eth()
                            .send(gasDataManager.getCustomizeGasPriceInWei().toBigInteger(), walletAddress.getText()
                                    .toString(), values);
                } else {
                    txHash = transfer.erc20(tokenDataManager.getTokenBySymbol(sendChoose).getProtocol())
                            .transfer(gasDataManager.getCustomizeGasPriceInWei().toBigInteger(), walletAddress.getText()
                                    .toString(), values);
                }
                LyqbLogger.log(txHash);
                handlerCreate.sendEmptyMessage(SEND_SUCCESS);
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
            } catch (Exception e) {
                errorMes = e.getMessage();
                handlerCreate.sendEmptyMessage(ERROR_TWO);
                e.printStackTrace();
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
                amountTotal = asset.getValue();
                sendWalletCount.setText(asset.getValueShown() + " " + symbol);
                sendChoose = asset.getSymbol();
            }
        }
    }

    /**
     * 邮费选择弹窗
     *
     * @param context
     */
    public void showFeeDialog(Context context) {
        if (feeDialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_fee, null);
            builder.setView(view);
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
            cancel.setOnClickListener(v -> feeDialog.dismiss());
            builder.setCancelable(true);
            feeDialog = builder.create();
            feeDialog.setCancelable(true);
            feeDialog.setCanceledOnTouchOutside(true);
            feeDialog.setOnDismissListener(dialogInterface -> transacitionFee.setText(tvAmount.getText()));
            Objects.requireNonNull(feeDialog.getWindow()).setGravity(Gravity.BOTTOM);
        }
        gasSeekBar.setMin(Float.parseFloat(NumberUtils.format1(gasDataManager.getRecommendGasPriceInGWei()
                .divide(new BigDecimal(2))
                .doubleValue(), 1)));
        gasSeekBar.setMax(Float.parseFloat(NumberUtils.format1(gasDataManager.getRecommendGasPriceInGWei()
                .multiply(new BigDecimal(2))
                .doubleValue(), 1)));
        gasSeekBar.setProgress((float) gasDataManager.getGasPriceInGwei());
        tvAmount.setText(transacitionFee.getText());
        tvWalletInfo.setText(new StringBuilder("Gas limit(").append(gasDataManager.getGasLimitByType("token_transfer"))
                .append(") * Gas Price(")
                .append((int) gasDataManager.getGasPriceInGwei())
                .append(" Gwei)"));
        feeDialog.show();
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

    /**
     * 金额拖动条
     */
    private void initSeekbar() {
        moneyAmount.post(() -> moneyAmount.setText(""));
        seekBar.setProgress(0);
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                int precision = balanceManager.getPrecisionBySymbol(sendChoose);
                moneyAmount.setText(NumberUtils.format1(balanceManager.getAssetBySymbol(sendChoose)
                        .getValue() * seekParams.progressFloat / 100, precision));
                amountToast.setText(CurrencyUtil.getCurrency(getBaseContext()).getSymbol() + NumberUtils.format1(balanceManager.getAssetBySymbol(sendChoose)
                        .getLegalValue() * seekParams.progressFloat / 100, precision));
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

    /**
     * 输入金额实时验证
     */
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
                double currentAmount = Double.parseDouble(editable.toString());
                if (currentAmount > amountTotal) {
                    amountToast.setText(getResources().getText(R.string.input_valid_amount));
                    amountToast.setTextColor(getResources().getColor(R.color.colorRed));
                    Animation shakeAnimation = AnimationUtils.loadAnimation(SendActivity.this, R.anim.shake_x);
                    amountToast.startAnimation(shakeAnimation);
                } else {
                    int precision = balanceManager.getPrecisionBySymbol(sendChoose);
                    amountToast.setText(NumberUtils.format1(currentAmount * marketcapDataManager.getPriceBySymbol(sendChoose), precision));
                    amountToast.setTextColor(getResources().getColor(R.color.colorNineText));
                }
                // TODO 拖动条随金额变化
            }
        });
    }

    /**
     * 钱包地址实时验证
     */
    private void initWalletAddress() {
        walletAddress.addTextChangedListener(new TextWatcher() {
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
                if (!WalletUtils.isValidAddress(editable.toString().trim())) {
                    addressToast.setText(getResources().getText(R.string.input_valid_address));
                    Animation shakeAnimation = AnimationUtils.loadAnimation(SendActivity.this, R.anim.shake_x);
                    addressToast.startAnimation(shakeAnimation);
                    addressToast.setVisibility(View.VISIBLE);
                } else {
                    addressToast.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
