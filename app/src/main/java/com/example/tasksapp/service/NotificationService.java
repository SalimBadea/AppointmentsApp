package com.example.tasksapp.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import com.example.tasksapp.R;
import com.example.tasksapp.ui.MainActivity;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;

public final class NotificationService extends IntentService {
    private Notification mNotification;
    private final int mNotificationId = 1000;

    public NotificationService() {
        super(null);
    }

    private final void createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            Context context = this.getApplicationContext();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager == null) {
                throw new NullPointerException("null cannot be cast to non-null type android.app.NotificationManager");
            }

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(this.getString(R.string.notification_channel_id), (CharSequence)this.getString(R.string.notification_channel_name), importance);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setDescription(this.getString(R.string.task_notification_channel_description));
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    protected void onHandleIntent(@Nullable Intent intent) {
        createChannel();
        long timestamp = 0L;
        if (intent != null && intent.getExtras() != null) {
            timestamp = intent.getExtras().getLong("timestamp");
        }

        if (timestamp > 0L) {
            Context context = this.getApplicationContext();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager == null) {
                throw new NullPointerException("null cannot be cast to non-null type android.app.NotificationManager");
            }

            Intent notifyIntent = new Intent((Context)this, MainActivity.class);
            String title = "Appointment Scheduler";
            String message = "There is a Scheduled Appointment , Check it Now";
            notifyIntent.putExtra("title", title);
            notifyIntent.putExtra("message", message);
            notifyIntent.putExtra("notification", true);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Resources res = this.getResources();
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification notification;
            if (Build.VERSION.SDK_INT >= 26) {
                mNotification = (new Notification.Builder((Context)this, this.getString(R.string.notification_channel_id)))
                        .setContentIntent(pendingIntent).setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setAutoCancel(true).setContentTitle((CharSequence)title)
                        .setStyle((Notification.Style)(new Notification.BigTextStyle())
                                .bigText((CharSequence)message))
                        .setContentText((CharSequence)message).build();
            } else {
                mNotification = (new Notification.Builder((Context)this)).setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(res, 1500001))
                        .setAutoCancel(false).setPriority(Notification.PRIORITY_HIGH)
                        .setContentTitle((CharSequence)title)
                        .setStyle((Notification.Style)(new Notification.BigTextStyle())
                                .bigText((CharSequence)message)).setSound(uri)
                        .setContentText((CharSequence)message).build();
            }

            notificationManager.notify(mNotificationId, mNotification);
        }

    }

}
