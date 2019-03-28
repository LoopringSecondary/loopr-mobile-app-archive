/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-26 下午5:00
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter.wallet;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;

import org.web3j.crypto.WalletUtils;
import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.R;
import leaf.prod.app.activity.wallet.AddCustomTokenActivity;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.wallet.WalletEntity;
import leaf.prod.walletsdk.model.token.Token;
import leaf.prod.walletsdk.service.RelayService;
import leaf.prod.walletsdk.util.NumberUtils;
import leaf.prod.walletsdk.util.WalletUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCustomTokenPresenter extends BasePresenter<AddCustomTokenActivity> {

    private String address;

    private String symbol;

    private String decimals;

    private TokenDataManager tokenManager;

    private RelayService loopringService;

    public AddCustomTokenPresenter(AddCustomTokenActivity view, Context context) {
        super(view, context);
        loopringService = new RelayService();
        tokenManager = TokenDataManager.getInstance(context);
    }

    private boolean validateAddress(String address) {
        boolean result = WalletUtils.isValidAddress(address);
        if (!result) {
            RxToast.warning(context.getResources().getString(R.string.input_valid_address));
        }
        return result;
    }

    private boolean validateSymbol(String symbol) {
        boolean result = !symbol.isEmpty();
        if (!result) {
            RxToast.warning(context.getResources().getString(R.string.input_valid_symbol));
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
            RxToast.warning(context.getResources().getString(R.string.input_valid_decimal));
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
                            RxToast.error(context.getResources().getString(R.string.token_add));
                        }

                        @Override
                        public void onNext(String s) {
                            RxToast.error(context.getResources().getString(R.string.token_add_error));
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
