package com.example.tasksapp.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;


public final class NotificationUtils {
    public final void setNotification(long timeInMilliSeconds, @NotNull Activity activity) {
        if (timeInMilliSeconds > 0L) {
            AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Activity.ALARM_SERVICE);
            if (alarmManager == null) {
                throw new NullPointerException("null cannot be cast to non-null type android.app.AlarmManager");
            }
            Intent alarmIntent = new Intent(activity.getApplicationContext(), MyAlarmService.class);
            alarmIntent.putExtra("reason", "notification");
            alarmIntent.putExtra("timestamp", timeInMilliSeconds);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeInMilliSeconds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast((Context) activity, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliSeconds, pendingIntent);
        }
    }
}

