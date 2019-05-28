package com.hellokiki.autoupdate;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class NotificationUpdateManager {

    private static NotificationUpdateManager sNotificationUpdateManager = new NotificationUpdateManager();

    public static NotificationUpdateManager getInstance() {
        return sNotificationUpdateManager;
    }

    private NotificationUpdateManager() {

    }

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private int mNotifyId = 12345;

    public void createNotification(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("123", "update", NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId("123");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        builder.setProgress(100, 50, false);
        builder.setContentTitle("更新");
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), context.getApplicationInfo().icon));
        builder.setSmallIcon(context.getApplicationInfo().icon);
        notificationManager.notify(mNotifyId, builder.build());
    }

    public void updateProgress(int progress) {
        if (builder != null && notificationManager != null) {
            builder.setProgress(100, progress, false);
            builder.setContentText(progress + "%");
            notificationManager.notify(mNotifyId, builder.build());
        }
    }

    public void dismiss() {
        if (builder != null && notificationManager != null) {
            notificationManager.cancel(mNotifyId);
            builder = null;
            notificationManager = null;
        }
    }


}
