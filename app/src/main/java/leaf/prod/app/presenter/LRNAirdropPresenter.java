/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-17 6:48 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter;

import java.math.BigInteger;

import android.content.Context;

import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.utils.Numeric;

import leaf.prod.app.activity.MainActivity;
import leaf.prod.walletsdk.service.Erc20Service;
import leaf.prod.walletsdk.service.NeoService;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LRNAirdropPresenter extends BasePresenter<MainActivity> {

    private NeoService neoService;

    private Erc20Service erc20Service;

    private String owner;

    private String bindAddress;

    public LRNAirdropPresenter(MainActivity view, Context context) {
        super(view, context);
        this.neoService = new NeoService();
        this.erc20Service = new Erc20Service();
        this.owner = WalletUtil.getCurrentAddress(context);
        this.setupBindAddress();
    }

    public void setupBindAddress() {
        erc20Service.getBindAddress(owner, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap((Func1<EthCall, Observable<String>>) ethCall -> {
                    bindAddress = ethCall.getValue();
                    // todo: for view
                    return neoService.getAirdropAmount(bindAddress);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    BigInteger bigInteger = Numeric.toBigInt(Numeric.cleanHexPrefix(s));
                    BigInteger divider = BigInteger.valueOf(100000000L);
                    String value = NumberUtils.format1(bigInteger.divide(divider).doubleValue(), 4);
                    // todo: for view
                });

    }

    public void a() {
        if (bindAddress != null) {
            neoService.claimAirdrop(bindAddress)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        // todo: for view
                    });
        }
    }
}
