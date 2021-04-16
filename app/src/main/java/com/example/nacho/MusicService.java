package com.example.nacho;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

//import static com.example.nacho.PlayerActivity.broadcastReceiver;

public class MusicService extends Service {

    private IBinder mBinder=new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//
////            if(!fromNotification) {
//            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
////            startService(new Intent(getBaseContext(), MusicService.class));
////                fromNotification=true;
////            }
//        }
        return START_NOT_STICKY; /*START_NOT_STICKY*/
    }

    @Override
    public void onDestroy() {
//        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        stopSelf();
//    }
}

