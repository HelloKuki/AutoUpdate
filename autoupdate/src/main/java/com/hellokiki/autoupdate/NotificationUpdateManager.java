package com.hellokiki.autoupdate;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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

    public NotificationManager createNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("123", "update", NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId("123");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_notification_progress);
        builder.setContent(views);


        return notificationManager;
    }
}
