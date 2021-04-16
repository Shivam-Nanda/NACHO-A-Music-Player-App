package com.example.nacho;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.example.nacho.PlayerActivity.notification;
import static com.example.nacho.PlayerActivity.notificationManager;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        context.sendBroadcast(new Intent("TRACKS_TRACKS").putExtra("actionname",intent.getAction()));
//        notificationManager.notify(0,notification);
    }
}
