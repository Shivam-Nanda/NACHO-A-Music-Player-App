package com.example.nacho;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationManagerCompat;

public class ApplicationClass extends Application {
    public static final String CHANNEL_ID_1="CHANNEL_1";
    public static final String CHANNEL_ID_2="CHANNEL_2";
    public static final String ACTION_NEXT="NEXT";
    public static final String ACTION_PREV="PREV";
    public static final String ACTION_PLAY="PLAY";

//    public static Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel1=new NotificationChannel(CHANNEL_ID_1,"Channel(1)", NotificationManager.IMPORTANCE_LOW);
            notificationChannel1.setDescription("Channel 1 Description");

            NotificationChannel notificationChannel2=new NotificationChannel(CHANNEL_ID_2,"Channel(2)", NotificationManager.IMPORTANCE_LOW);
            notificationChannel2.setDescription("Channel 2 Description");

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel1);
            notificationManager.createNotificationChannel(notificationChannel2);

        }
    }
//    public static void createNotification(Context context,SongFiles songFile,int playBtn,int pos){
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//
//            NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
//            MediaSessionCompat mediaSessionCompat=new MediaSessionCompat(context,"tag");
//
//            Bitmap icon= BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(songFile.getPath()));
//
//
//        }
//    }
}
