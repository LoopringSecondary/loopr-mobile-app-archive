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

import leaf.prod.app.utils.NotificationUtil;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.walletsdk.listener.TransactionStatusListener;
import leaf.prod.walletsdk.model.TxStatus;
import leaf.prod.walletsdk.model.response.data.Transaction;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransactionDataManager {

    private static TransactionDataManager instance = null;

    private Context context;

    private TransactionStatusListener listener;

    // stores pending tx hashes to nofity
    private List<String> txHashes;

    private TransactionDataManager(Context context) {
        this.context = context;
        this.listener = new TransactionStatusListener();
        this.initTxObservable();
    }

    public static TransactionDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new TransactionDataManager(context);
        }
        return instance;
    }

    private void initTxObservable() {
        Observable<Transaction[]> txObservable = listener.start()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        txObservable.subscribe(transactions -> {
            if (transactions != null) {
                for (Transaction tx : transactions) {
                    String hash = tx.getTxHash();
                    txHashes = SPUtils.getDataList(context, "pending_tx", String.class);
                    // txHashes stores lowercase pending hash string
                    if (txHashes.contains(hash.toLowerCase()) && tx.getStatus() == TxStatus.SUCCESS) {
                        txHashes.remove(hash);
                        SPUtils.setDataList(context, "pending_tx", txHashes);
                        NotificationUtil.normal(context, tx);
                    }
                    if (txHashes.isEmpty()) {
                        SPUtils.remove(context, "pending_tx");
                        listener.stop();
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
        // always store lower case hash string
        if (!txHashes.contains(txHash.toLowerCase())) {
            txHashes.add(txHash.toLowerCase());
            SPUtils.setDataList(context, "pending_tx", txHashes);
        }
        String owner = (String) SPUtils.get(context, "address", "");
        String[] hashArray = txHashes.toArray(new String[txHashes.size()]);
        listener.queryByHashes(owner, hashArray);
    }
}
