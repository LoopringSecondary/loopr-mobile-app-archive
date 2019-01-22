package leaf.prod.app.receiver;

import java.util.Objects;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.UpgradeUtil;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-16 3:30 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class ApkInstallReceiver extends BroadcastReceiver {

    private DownloadManager downloadManager;

    private long enqueue;

    public ApkInstallReceiver() {
    }

    public ApkInstallReceiver(DownloadManager downloadManager, long enqueue) {
        this.downloadManager = downloadManager;
        this.enqueue = enqueue;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            UpgradeUtil.installApk(context, enqueue);
        } else if (Objects.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
            LyqbLogger.log(intent.getData().getSchemeSpecificPart() + "========================");
        }
    }
}
