package leaf.prod.app.utils;

public class NotificationUtil {

    public final static int ID_FOR_NORMAL = 1;

    /**
     * show normal notification
     *
     * @param context      context
     * @param isSound      Set the sound to play.  if no, it will play on the default stream.
     * @param isShowLock   show when mobile locks screen
     * @param isHeads      heads up dialog
     * @param isAutoCancel cancel notification while click
     * @param isOnly       only show one notification
     */
//    public static void normal(Context context, boolean isSound, boolean isShowLock, boolean isHeads, boolean isAutoCancel, boolean isOnly) {
//        String title = "This is normal title";
//        String text = "This is normal message";
//        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_share_logo);
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setClass(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setLargeIcon(largeIcon)
//                .setSmallIcon(R.drawable.cry)
//                .setTicker(context.getString(R.string.app_name))
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle(title)
//                .setContentText(text)
//                .setAutoCancel(isAutoCancel)
//                .setContentIntent(pendingIntent);
//        if (isSound) {
//            builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.message));
//        } else {
//            builder.setDefaults(Notification.DEFAULT_ALL);
//        }
//        if (isShowLock) {
//            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//        }
//        builder.setPriority(isHeads ? NotificationCompat.PRIORITY_MAX : NotificationCompat.PRIORITY_DEFAULT);
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (notificationManager != null) {
//            notificationManager.notify(isOnly ? ID_FOR_NORMAL : (int) System.currentTimeMillis(), builder.build());
//        }
//    }
}
