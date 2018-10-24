package leaf.prod.walletsdk;

import android.util.Log;

import org.web3j.protocol.core.methods.response.Transaction;
import com.google.gson.Gson;

import leaf.prod.walletsdk.api.EthereumApi;
import leaf.prod.walletsdk.service.LoopringService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventAdvisor {

    private static LoopringService loopringApi = new LoopringService();

    private static EthereumApi ethereumApi = new EthereumApi();

    public static void notifyTransaction(String txHash) {
        ethereumApi.getTransactionByHashObservable(txHash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Transaction>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("333333", e.getMessage());
                        unsubscribe();
                    }

                    @Override
                    public void onNext(Transaction transaction) {
                        Log.d("", new Gson().toJson(transaction));
                        loopringApi.notifyTransactionSubmitted(txHash, transaction).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<String>() {
                                    @Override
                                    public void onCompleted() {
                                        unsubscribe();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d("222222", e.getMessage());
                                        unsubscribe();
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        Log.d("111111", s);
                                        unsubscribe();
                                    }
                                });
                        unsubscribe();
                    }
                });
        //        ethereumApi.getTransactionByHashObservable(txHash).toSingle()
        //                .doOnSuccess(transaction ->
        //                {
        //                    loopringApi.notifyTransactionSubmitted(txHash, transaction)
        //                            .doOnError(error -> {
        //                                Log.e("", error.getMessage());
        //                            });
        //                })
        //                .doOnError(error -> {
        //                    Log.e("", error.getMessage());
        //                });
    }

    public static void notifyCreation(String address) {
        loopringApi.notifyCreateWallet(address).toSingle()
                .doOnError(Throwable::printStackTrace);
    }
}
