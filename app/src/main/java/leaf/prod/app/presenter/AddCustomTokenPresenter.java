/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-26 下午5:00
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;

import org.web3j.crypto.WalletUtils;

import leaf.prod.app.activity.AddCustomTokenActivity;
import leaf.prod.app.manager.TokenDataManager;
import leaf.prod.app.model.WalletEntity;
import leaf.prod.app.utils.NumberUtils;
import leaf.prod.app.utils.ToastUtils;
import leaf.prod.app.utils.WalletUtil;
import leaf.prod.walletsdk.model.response.data.Token;
import leaf.prod.walletsdk.service.LoopringService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCustomTokenPresenter extends BasePresenter<AddCustomTokenActivity> {

    private String address;

    private String symbol;

    private String decimals;

    private TokenDataManager tokenManager;

    private LoopringService loopringService;

    public AddCustomTokenPresenter(AddCustomTokenActivity view, Context context) {
        super(view, context);
        loopringService = new LoopringService();
        tokenManager = TokenDataManager.getInstance(context);
    }

    private boolean validateAddress(String address) {
        boolean result = WalletUtils.isValidAddress(address);
        if (!result) {
            ToastUtils.toast("please input a valid address");
        }
        return result;
    }

    private boolean validateSymbol(String symbol) {
        boolean result = !symbol.isEmpty();
        if (!result) {
            ToastUtils.toast("please input a valid symbol");
        }
        return result;
    }

    private boolean validateDecimal(String decimal) {
        boolean result = false;
        int target = Integer.parseInt(decimal);
        if (target >= 0 && target <= 20) {
            result = true;
        }
        if (!result) {
            ToastUtils.toast("please input a valid decimal");
        }
        return result;
    }

    public void doAddCustomToken() {
        String address = view.etTokenAddress.getText().toString();
        String symbol = view.etTokenSymbol.getText().toString();
        String decimals = view.etTokenDecimal.getText().toString();
        if (validateAddress(address) && validateSymbol(symbol) && validateDecimal(decimals)) {
            String owner = WalletUtil.getCurrentAddress(context);
            if (!address.startsWith("0x") && !address.startsWith("0X")) {
                this.address = "0x" + address;
            } else {
                this.address = address;
            }
            this.symbol = symbol.toUpperCase();
            this.decimals = NumberUtils.toBigDecimal(Integer.parseInt(decimals));
            loopringService.addCustomToken(owner, this.address, this.symbol, this.decimals)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.toast("添加代币失败");
                        }

                        @Override
                        public void onNext(String s) {
                            ToastUtils.toast("成功添加代币");
                            addTokenToChosen();
                        }
                    });
        }
    }

    private void addTokenToChosen() {
        Token token = Token.builder()
                .symbol(this.symbol)
                .protocol(this.address)
                .decimals(new BigDecimal(this.decimals))
                .source(this.symbol.toLowerCase())
                .build();
        tokenManager.addToken(token);
        WalletEntity wallet = WalletUtil.getCurrentWallet(context);
        List<String> tokenChosen = wallet.getChooseTokenList();
        if (tokenChosen != null && !tokenChosen.contains(this.symbol)) {
            tokenChosen.add(this.symbol);
        }
        wallet.setChooseTokenList(tokenChosen);
        WalletUtil.updateWallet(context, wallet);
    }
}
