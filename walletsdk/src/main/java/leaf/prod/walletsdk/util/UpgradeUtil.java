package leaf.prod.app.utils;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import com.google.gson.Gson;

import leaf.prod.app.R;
import leaf.prod.app.receiver.ApkInstallReceiver;
import leaf.prod.walletsdk.model.response.data.VersionResult;
import leaf.prod.walletsdk.service.VersionService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-16 3:10 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class UpgradeUtil {

    private static boolean updateHint = false;

    private static VersionService versionService = new VersionService();

    private static DownloadManager downloadManager;

    /**
     * 升级提示框
     */
    public static void showUpdateHint(Context context) {
        if (!updateHint) {
            versionService.getNewVersion().enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    VersionResult versionResult = null;
                    try {
                        versionResult = new Gson().fromJson(result, VersionResult.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (versionResult != null && !leaf.prod.app.utils.AndroidUtils.getVersionName(context)
                            .equals(versionResult.getAppVersions().get(0).getVersion())) {
                        AlertDialog.Builder updateDialog = new AlertDialog.Builder(context);
                        updateDialog.setPositiveButton(context.getResources()
                                .getString(R.string.upgrade_confirm), (dialogInterface, i0) -> {
                            updateHint = true;
                            downloadApk(context);
                            dialogInterface.dismiss();
                        });
                        updateDialog.setNegativeButton(context.getResources()
                                .getString(R.string.upgrade_cancel), (dialogInterface, i) -> {
                            updateHint = true;
                            dialogInterface.dismiss();
                        });
                        updateDialog.setMessage(context.getResources()
                                .getString(R.string.upgrade_tips, versionResult.getAppVersions()
                                        .get(0)
                                        .getVersion()));
                        updateDialog.setTitle(context.getResources().getString(R.string.upgrade_title));
                        Looper.prepare();
                        updateDialog.show();
                        Looper.loop();
                    }
                }
            });
        }
    }

    public static void downloadApk(Context context) {
        if (downloadManager == null) {
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        String url = "http://p.gdown.baidu.com/d4c01c5fb63e0b80db2cb5210e1a7038de8e87417b29f2bb36692d47aa1d04639c9c14f93f9021e25f89d17d8df244c9e601907de250520372fb7106abbc5517a2895b89bda9856a1538926fee90cd003bb2cb2e6a9c1e09bccbe9f52f58a57520ff8e7c413981c0dc8d729f0561b27f91ac060d21500be2c1889a5a6c8bcc873dbf9632292eee80c5da66846dc094575241188279336373";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Upwallet.apk")
                .setMimeType("application/vnd.android.package-archive");
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        ApkInstallReceiver apkInstallReceiver = new ApkInstallReceiver(downloadManager, downloadManager.enqueue(request));
        context.registerReceiver(apkInstallReceiver, filter);
    }

    public static void installApk(Context context, long downloadApkId) {
        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //根据id判断如果文件已经下载成功返回保存文件的路径
        Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
        if (downloadFileUri != null) {
            if ((Build.VERSION.SDK_INT >= 24)) {//判读版本是否在7.0以上
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            context.startActivity(install);
        }
    }
}
