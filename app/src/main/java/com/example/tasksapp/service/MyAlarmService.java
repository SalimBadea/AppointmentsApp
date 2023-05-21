package com.example.tasksapp.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public final class MyAlarmService extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        this.isAppsIsInBackground(context);
        Intent service = new Intent(context, NotificationService.class);
        service.putExtra("reason", intent.getStringExtra("reason"));
        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0L));
        context.startService(service);
        context.getApplicationContext().bindService(service, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e("MyAlarmService", "Service >>" + " Run in Background");

                //retrieve an instance of the service here from the IBinder returned
                //from the onBind method to communicate with
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        }, Context.BIND_AUTO_CREATE);
    }

    private final void isAppsIsInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List runningAppProcessInfo = am.getRunningAppProcesses();

        for (int i = 0; i < runningAppProcessInfo.size(); ++i) {
            Log.e("MyAlarmService", "package = <" + ((ActivityManager.RunningAppProcessInfo) runningAppProcessInfo.get(i)).processName + '>');
        }

    }
}
