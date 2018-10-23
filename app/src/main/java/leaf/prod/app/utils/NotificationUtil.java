package leaf.prod.app.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import leaf.prod.app.R;
import leaf.prod.app.activity.MainActivity;
import leaf.prod.walletsdk.model.response.data.Transaction;

public class NotificationUtil {

    /**
     * show normal notification
     *
     * @param context      context
     * @param tx           transaction to notify.
     */
    public static void normal(Context context, Transaction tx) {
        String title = context.getString(R.string.transaction_success);
        String text = tx.getTxHash();
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_share_logo);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        String image = String.format("icon_tx_%s", tx.getType().getDescription().toLowerCase());
        int identifier = context.getResources().getIdentifier(image, "mipmap", context.getPackageName());

        builder.setLargeIcon(largeIcon)
                .setSmallIcon(identifier)
                .setTicker(context.getString(R.string.app_name))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }
}
