/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-17 6:48 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter.wallet;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.utils.Numeric;
import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.R;
import leaf.prod.app.activity.wallet.AirdropActivity;
import leaf.prod.app.activity.wallet.DefaultWebViewActivity;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.relay.ClaimBindAmount;
import leaf.prod.walletsdk.service.Erc20Service;
import leaf.prod.walletsdk.service.NeoService;
import leaf.prod.walletsdk.util.DateUtil;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.walletsdk.util.StringUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AirdropPresenter extends BasePresenter<AirdropActivity> {

    private NeoService neoService;

    private Erc20Service erc20Service;

    private String owner;

    private String bindAddress;

    private double bindAmount = 0;

    private Animation shakeAnimation;

    public AirdropPresenter(AirdropActivity view, Context context) {
        super(view, context);
        this.neoService = new NeoService(context);
        this.erc20Service = new Erc20Service();
        this.owner = WalletUtil.getCurrentAddress(context);
        this.setupBindAddress();
        shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake_x);
    }

    private void setupBindAddress() {
        view.clLoading.setVisibility(View.VISIBLE);
        erc20Service.getBindAddress(owner, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap((Func1<EthCall, Observable<String>>) ethCall -> {
                    List<TypeReference<Type>> typeReferences = erc20Service
                            .getBindFunction(owner, 1).getOutputParameters();
                    List<Type> values = FunctionReturnDecoder.decode(ethCall.getValue(), typeReferences);
                    bindAddress = values.get(0).toString();
                    if (StringUtils.isEmpty(bindAddress)) {
                        return Observable.just("failed");
                    } else {
                        return neoService.getAirdropAmount(bindAddress);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        setClaimButton(true);
                        view.clLoading.setVisibility(View.GONE);
                        RxToast.error(context.getString(R.string.airdrop_neo_error));
                    }

                    @Override
                    public void onNext(String result) {
                        if (!result.equals("failed")) {
                            BigInteger bigInteger = new BigInteger(result, 10);
                            BigInteger divider = BigInteger.valueOf(100000000L);
                            bindAmount = bigInteger.doubleValue() / divider.doubleValue();
                            String value = NumberUtils.format1(bindAmount, 4);
                            view.airdropAmount.setText(value);
                        }
                        view.airdropAddress.setText(bindAddress);
                        setClaimButton(false);
                        view.clLoading.setVisibility(View.GONE);
                    }
                });
    }

    public void handleClaim() {
        if (!StringUtils.isEmpty(bindAddress)) {
            view.clLoading.setVisibility(View.VISIBLE);
            neoService.claimAirdrop(bindAddress)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RelayResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            setClaimButton(true);
                            view.clLoading.setVisibility(View.GONE);
                            RxToast.error(context.getString(R.string.airdrop_neo_error));
                        }

                        @Override
                        public void onNext(RelayResponseWrapper wrapper) {
                            if (wrapper.getError() == null) {
                                RxToast.success(context.getString(R.string.airdrop_success));
                                SPUtils.put(context, "claim_date_" + bindAddress, new Date());
                                String txHash = Numeric.cleanHexPrefix(((ClaimBindAmount) wrapper.getResult()).getTxid());
                                SPUtils.put(context, "airdrop_txhash_" + bindAddress, txHash);
                            } else {
                                RxToast.error(context.getString(R.string.airdrop_time_invalid));
                            }
                            setClaimButton(false);
                            view.clLoading.setVisibility(View.GONE);
                        }
                    });
        } else {
            RxToast.error(context.getString(R.string.airdrop_failed));
        }
    }

    public boolean isClaimTimeValid() {
        boolean result = true;
        Date claimDate = SPUtils.getBean(context, "claim_date_" + bindAddress, Date.class);
        if (claimDate != null) {
            Date dateTarget = DateUtil.addDateTime(claimDate, 24);
            result = DateUtil.compareDate(dateTarget, new Date());
            if (result) {
                view.llAirdropDate.setVisibility(View.GONE);
                view.dateTip.setVisibility(View.GONE);
            } else {
                view.airdropDate.setText(DateUtil.formatDateTime(dateTarget, "yyyy-MM-dd HH:mm:ss"));
                view.llAirdropDate.setVisibility(View.VISIBLE);
                view.dateTip.setVisibility(View.VISIBLE);
            }
        } else {
            view.llAirdropDate.setVisibility(View.GONE);
            view.dateTip.setVisibility(View.GONE);
        }
        return result;
    }

    public void setClaimButton(Boolean netError) {
        if (netError) {
            RxToast.error(context.getString(R.string.airdrop_neo_error));
            view.claimButton.setOnClickListener(v -> RxToast.error(context.getString(R.string.airdrop_neo_error)));
            return;
        }
        String txHash = (String) SPUtils.get(context, "airdrop_txhash_" + bindAddress, "");
        if (!isClaimTimeValid() && !txHash.isEmpty()) {
            view.claimButton.setText(context.getString(R.string.airdrop_forward));
            view.claimButton.setOnClickListener(v -> {
                view.getOperation().addParameter("url", "https://neotracker.io/tx/" + txHash);
                view.getOperation().forward(DefaultWebViewActivity.class);
            });
        } else if (StringUtils.isEmpty(bindAddress)) {
            view.addressTip.setText(view.getString(R.string.airdrop_no_bind));
            view.addressTip.setTextColor(view.getResources().getColor(R.color.colorRed));
            view.claimButton.setOnClickListener(v -> view.addressTip.startAnimation(shakeAnimation));
        } else if (bindAmount == 0) {
            view.amountTip.setText(view.getString(R.string.airdrop_empty));
            view.amountTip.setTextColor(view.getResources().getColor(R.color.colorRed));
            view.claimButton.setOnClickListener(v -> view.amountTip.startAnimation(shakeAnimation));
        } else {
            view.claimButton.setText(context.getString(R.string.airdrop_button));
            view.claimButton.setOnClickListener(v -> {
                if (!(ButtonClickUtil.isFastDoubleClick(1))) {
                    handleClaim();
                }
            });
        }
    }
}
