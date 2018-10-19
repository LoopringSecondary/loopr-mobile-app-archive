/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-10-18 6:51 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import leaf.prod.app.utils.SPUtils;
import leaf.prod.walletsdk.listener.PendingTxListener;
import leaf.prod.walletsdk.model.response.data.PendingTxResult;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransactionDataManager {

    private Context context;

    private Observable<PendingTxResult[]> txObservable;

    private PendingTxListener listener;

    // stores pending tx hashes to nofity
    private List<String> txHashes;

    private static TransactionDataManager instance = null;

    private TransactionDataManager(Context context) {
        this.context = context;
        this.listener = new PendingTxListener();
        this.initTxObservable();
    }

    public static TransactionDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new TransactionDataManager(context);
        }
        return instance;
    }

    private void initTxObservable() {
        txObservable = listener.start()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        this.txObservable.subscribe(new Observer<PendingTxResult[]>() {

            @Override
            public void onCompleted() {
                System.out.println(1111);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(1111);
            }

            @Override
            public void onNext(PendingTxResult[] pendingTxResult) {
                if (pendingTxResult != null) {
                    for (PendingTxResult tx : pendingTxResult) {
                        String hash = tx.getTxHash();
                        // txhashes stores lowercased hash string
                        if (txHashes.contains(hash.toLowerCase())) {
                            txHashes.remove(hash);
                        }
                        if (txHashes.isEmpty()) {
                            listener.stop();
                        }
                    }
                }
            }
        });
    }


    public void queryByHash(String txHash) {
        txHashes = SPUtils.getDataList(context, "pending_tx", String.class);
        if (txHashes == null) {
            txHashes = new ArrayList<>();
        }
        // always add lower case hash string
        txHashes.add(txHash.toLowerCase());
        String owner = (String) SPUtils.get(context, "address", "");
        listener.queryByOwner(owner);
    }
}
