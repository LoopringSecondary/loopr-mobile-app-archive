/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-14 3:30 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter;

import java.math.BigDecimal;
import java.util.Objects;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.ConvertActivity;
import leaf.prod.app.manager.BalanceDataManager;
import leaf.prod.app.manager.GasDataManager;
import leaf.prod.app.manager.MarketcapDataManager;
import leaf.prod.app.manager.TokenDataManager;
import leaf.prod.app.utils.CurrencyUtil;
import leaf.prod.app.utils.NumberUtils;
import leaf.prod.app.utils.ToastUtils;
import leaf.prod.walletsdk.model.response.data.BalanceResult;

public class ConvertPresenter extends BasePresenter<ConvertActivity> {

    @BindView(R.id.first_token_img)
    ImageView firstTokenImg;

    @BindView(R.id.second_token_img)
    ImageView secondTokenImg;

    @BindView(R.id.first_token_tx)
    TextView firstTokenTx;

    @BindView(R.id.second_token_tx)
    TextView secondTokenTx;

    @BindView(R.id.transacition_fee)
    TextView transacitionFee;

    @BindView(R.id.hint_text)
    TextView hint;

    @BindView(R.id.first_val)
    TextView firstVal;

    @BindView(R.id.second_val)
    TextView secondVal;

    private String firstToken = "ETH";

    private AlertDialog feeDialog;

    private AlertDialog passwordDialog;

    private TextView tvAmount;

    private TextView tvWalletInfo;

    private ImageView cancel;

    private TextView recommendGas;

    private BubbleSeekBar gasSeekBar;

    private Double gasEthValue;

    private GasDataManager gasDataManager;

    private MarketcapDataManager marketcapDataManager;

    private BalanceDataManager balanceDataManager;

    private TokenDataManager tokenDataManager;

    public ConvertPresenter(ConvertActivity view, Context context) {
        super(view, context);
        ButterKnife.bind(this, view);
        gasDataManager = GasDataManager.getInstance(context);
        marketcapDataManager = MarketcapDataManager.getInstance(context);
        balanceDataManager = BalanceDataManager.getInstance(context);
        tokenDataManager = TokenDataManager.getInstance(context);
        setHint();
    }

    /**
     * token切换
     */
    public void switchToken() {
        if (firstToken.equals("ETH")) {
            firstTokenImg.setImageDrawable(view.getResources().getDrawable(R.mipmap.icon_token_weth));
            firstTokenTx.setText("WETH");
            secondTokenImg.setImageDrawable(view.getResources().getDrawable(R.mipmap.icon_token_eth));
            secondTokenTx.setText("ETH");
            firstToken = "WETH";
        } else {
            firstTokenImg.setImageDrawable(view.getResources().getDrawable(R.mipmap.icon_token_eth));
            firstTokenTx.setText("ETH");
            secondTokenImg.setImageDrawable(view.getResources().getDrawable(R.mipmap.icon_token_weth));
            secondTokenTx.setText("WETH");
            firstToken = "ETH";
        }
        firstVal.setText("0");
        secondVal.setText("0");
        setHint();
    }

    /**
     * 油费选择弹窗
     */
    public void showFeeDialog() {
        if (feeDialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_fee, null);
            builder.setView(view);
            tvAmount = view.findViewById(R.id.tv_amount);
            tvWalletInfo = view.findViewById(R.id.tv_wallet_info);
            cancel = view.findViewById(R.id.cancel);
            recommendGas = view.findViewById(R.id.recommend_gas);
            gasSeekBar = view.findViewById(R.id.gasSeekBar);
            gasSeekBar.getConfigBuilder()
                    .min(1)
                    .max(Float.parseFloat(NumberUtils.format1(gasDataManager.getRecommendGasPriceInGWei()
                            .multiply(new BigDecimal(2))
                            .doubleValue(), 1))).build();
            gasSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                    gasDataManager.setCustomizeGasPriceInGWei((double) progressFloat);
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
                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                }

                @Override
                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
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
        gasSeekBar.setProgress((float) gasDataManager.getGasPriceInGwei());
        tvAmount.setText(transacitionFee.getText());
        tvWalletInfo.setText(new StringBuilder("Gas limit(").append(gasDataManager.getGasLimitByType("token_transfer"))
                .append(") * Gas Price(")
                .append((int) gasDataManager.getGasPriceInGwei())
                .append(" Gwei)"));
        feeDialog.show();
    }

    /**
     * 密码弹框
     */
    public void showPasswordDialog() {
        if (passwordDialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_put_password, null);
            builder.setView(view);
            final EditText passwordInput = view.findViewById(R.id.password_input);
            view.findViewById(R.id.cancel).setOnClickListener(v -> passwordDialog.dismiss());
            view.findViewById(R.id.confirm).setOnClickListener(v -> {
                if (TextUtils.isEmpty(passwordInput.getText().toString())) {
                    ToastUtils.toast(view.getResources().getString(R.string.put_password));
                } else {
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

    /**
     * 设置最大交换额度
     */
    public void setMax() {
        balanceDataManager.getAssetBySymbol(firstToken);
        BalanceResult.Asset firstAsset = balanceDataManager.getAssetBySymbol(firstToken);
        firstVal.setText(firstAsset.getValueShown());
        secondVal.setText(firstAsset.getValueShown());
    }

    /**
     * 交换
     */
    public void convert() {
    }

    private void setHint() {
        hint.setTextColor(view.getResources().getColor(R.color.colorNineText));
        hint.setText(view.getResources()
                .getString(R.string.available_balance, " " + balanceDataManager.getAssetBySymbol(firstToken)
                        .getValueShown() + " " + firstToken));
    }
}
