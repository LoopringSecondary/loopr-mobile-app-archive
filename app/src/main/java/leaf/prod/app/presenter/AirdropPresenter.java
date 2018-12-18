/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-17 6:48 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import android.content.Context;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.utils.Numeric;
import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.R;
import leaf.prod.app.activity.AirdropActivity;
import leaf.prod.app.activity.DefaultWebViewActivity;
import leaf.prod.walletsdk.model.response.ClaimBindAmount;
import leaf.prod.walletsdk.service.Erc20Service;
import leaf.prod.walletsdk.service.NeoService;
import leaf.prod.walletsdk.util.DateUtil;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.walletsdk.util.StringUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AirdropPresenter extends BasePresenter<AirdropActivity> {

    private NeoService neoService;

    private Erc20Service erc20Service;

    private String owner;

    private String bindAddress;

    public AirdropPresenter(AirdropActivity view, Context context) {
        super(view, context);
        this.neoService = new NeoService();
        this.erc20Service = new Erc20Service();
        this.owner = WalletUtil.getCurrentAddress(context);
        this.setupBindAddress();
    }

    private void setupBindAddress() {
        erc20Service.getBindAddress(owner, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap((Func1<EthCall, Observable<String>>) ethCall -> {
                    List<TypeReference<Type>> typeReferences = erc20Service
                            .getBindFunction(owner, 1).getOutputParameters();
                    List<Type> values = FunctionReturnDecoder.decode(ethCall.getValue(), typeReferences);
                    bindAddress = values.get(0).toString();
                    return neoService.getAirdropAmount(bindAddress);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    BigInteger bigInteger = Numeric.toBigInt(Numeric.cleanHexPrefix(s));
                    BigInteger divider = BigInteger.valueOf(100000000L);
                    String value = NumberUtils.format1(bigInteger.divide(divider).doubleValue(), 4);
                    view.airdropAddress.setText(bindAddress);
                    view.airdropAmount.setText(value);
                });
    }

    public void handleClaim() {
        if (bindAddress != null) {
            neoService.claimAirdrop(bindAddress)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(wrapper -> {
                        if (wrapper.getError() == null) {
                            RxToast.success(context.getString(R.string.airdrop_success));
                            SPUtils.put(context, "claim_date", new Date());
                            String txHash = ((ClaimBindAmount) wrapper.getResult()).getTxid();
                            view.claimButton.setText(context.getString(R.string.airdrop_forward));
                            view.claimButton.setOnClickListener(v -> {
                                view.getOperation().addParameter("url", "https://neotracker.io/tx/" + txHash);
                                view.getOperation().forward(DefaultWebViewActivity.class);
                            });
                        } else {
                            RxToast.error(context.getString(R.string.airdrop_failed));
                        }
                    });
        }
    }

    public boolean isClaimTimeValid() {
        boolean result = true;
        Date claimDate = SPUtils.getBean(context, "claim_date", Date.class);
        if (claimDate != null) {
            Date dateTarget = DateUtil.addDateTime(claimDate, 24);
            result = DateUtil.compareDate(dateTarget, new Date());
        }
        return result;
    }

    public boolean isAddressBinded() {
        return !StringUtils.isEmpty(bindAddress);
    }
}
