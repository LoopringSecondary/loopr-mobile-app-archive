package leaf.prod.walletsdk;

import leaf.prod.walletsdk.api.EthereumApi;
import leaf.prod.walletsdk.service.LoopringService;

public class EventAdvisor {

    private static LoopringService loopringApi = new LoopringService();

    private static EthereumApi ethereumApi = new EthereumApi();

    public static void notifyTransaction(String txHash) {

        /*ethereumApi.getTransactionByHashObservable(txHash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(transaction -> {
                    Log.d("333333", new Gson().toJson(transaction));
                    return loopringApi.notifyTransactionSubmitted(txHash, transaction);
                }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
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
        });*/
    }

    public static void notifyCreation(String address) {
        loopringApi.notifyCreateWallet(address).toSingle()
                .doOnError(Throwable::printStackTrace);
    }
}
