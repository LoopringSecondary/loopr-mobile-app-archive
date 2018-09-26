package leaf.prod.walletsdk;

import leaf.prod.walletsdk.api.EthereumApi;
import leaf.prod.walletsdk.service.LoopringService;

import leaf.prod.walletsdk.service.LoopringService;

public class EventAdvisor {
    private static LoopringService loopringApi = new LoopringService();
    private static EthereumApi ethereumApi = new EthereumApi();

    public static void notifyTransaction(String txHash) {
        ethereumApi.getTransactionByHashObservable(txHash).toSingle()
                .doOnSuccess(transaction -> loopringApi.notifyTransactionSubmitted(txHash, transaction).doOnError(Throwable::printStackTrace))
                .doOnError(Throwable::printStackTrace);
    }

    public static void notifyCreation(String address) {
        loopringApi.notifyCreateWallet(address).toSingle()
                .doOnError(Throwable::printStackTrace);
    }
}
