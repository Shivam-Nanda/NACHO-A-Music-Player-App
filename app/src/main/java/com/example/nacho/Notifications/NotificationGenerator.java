package com.example.nacho.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.nacho.NotificationReceiver;
import com.example.nacho.PlayerActivity;
import com.example.nacho.R;

import static com.example.nacho.ApplicationClass.ACTION_NEXT;
import static com.example.nacho.ApplicationClass.ACTION_PLAY;
import static com.example.nacho.ApplicationClass.ACTION_PREV;
import static com.example.nacho.ApplicationClass.CHANNEL_ID_2;
import static com.example.nacho.ui.home.SongsAdapter.songsFiles;

public class NotificationGenerator {

    private static final int NOTIFICATION_SONG_ID=0;
    private static RemoteViews remoteViews, bigRemoteView;
    private static NotificationCompat.Builder notificationBuilder;
    private static NotificationManager notificationManager;

    private int currentSongPosition;
    private boolean isPlaying = true;

    public static void showSongNotification(Context context, int currentSongPosition, Bitmap bitmap) {

//        this.currentSongPosition = currentSongPosition;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        bigRemoteView = new RemoteViews(context.getPackageName(), R.layout.custom_notification_expandable);

        remoteViews.setTextViewText(R.id.notificationSongTitle, songsFiles.get(currentSongPosition).getTitle());
        remoteViews.setTextViewText(R.id.notificationArtistName, songsFiles.get(currentSongPosition).getArtist());
        if (bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.notificationImage, bitmap);
        } else {
            remoteViews.setImageViewResource(R.id.notificationImage, R.drawable.nosongs);
        }

        bigRemoteView.setTextViewText(R.id.exNotificationSongTitle, songsFiles.get(currentSongPosition).getTitle());
        bigRemoteView.setTextViewText(R.id.exNotificationArtistName, songsFiles.get(currentSongPosition).getArtist());
        if (bitmap != null) {
            bigRemoteView.setImageViewBitmap(R.id.exNotificationImage, bitmap);
        } else {
            bigRemoteView.setImageViewResource(R.id.exNotificationImage, R.drawable.nosongs);
        }

        notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(),CHANNEL_ID_2);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, PlayerActivity.class);

//        notifyIntent.putExtra(Constants.FROM_NOTIFICATION, true);
//        notifyIntent.putExtra(Constants.PLAYLIST_MODEL_KEY, currentPlaylist);
        notifyIntent.putExtra("z", currentSongPosition);
//        notifyIntent.putExtra(Constants.IS_PLAYING, isPlaying);


        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.mylogo)
                .setContentTitle("Nacho")
                .setContentText("Song Notification")
                .setContent(remoteViews)
                .setCustomBigContentView(bigRemoteView)
                .setPriority(NotificationCompat.PRIORITY_MAX);

//        Intent next = new Intent();
//        Intent previous = new Intent(ACTION_PREV);
//        Intent playPause = new Intent(Constants.CLICK_PLAY_PAUSE);

        Intent prevIntent=new Intent(context.getApplicationContext(), NotificationReceiver.class).setAction(ACTION_PREV);

        Intent playIntent=new Intent(context.getApplicationContext(),NotificationReceiver.class).setAction(ACTION_PLAY);

        final Intent nextIntent=new Intent(context.getApplicationContext(),NotificationReceiver.class).setAction(ACTION_NEXT);


        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notificationNext, nextPendingIntent);
        bigRemoteView.setOnClickPendingIntent(R.id.exNotificationNext, nextPendingIntent);

        PendingIntent previousPendingIntent = PendingIntent.getBroadcast(context, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notificationPrevious, previousPendingIntent);
        bigRemoteView.setOnClickPendingIntent(R.id.exNotificationPrevious, previousPendingIntent);

        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notificationPlay, playPendingIntent);
        bigRemoteView.setOnClickPendingIntent(R.id.exNotificationPlay, playPendingIntent);
        notificationBuilder.setOngoing(true);
        notificationManager.notify(NOTIFICATION_SONG_ID, notificationBuilder.build());
    }

    public static void updateView(boolean isPlaying) {
//        this.isPlaying = isPlaying;
//        this.currentSongPosition = currentSongPosition;

        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.notificationPlay, R.drawable.ic_pause);
            bigRemoteView.setImageViewResource(R.id.exNotificationPlay, R.drawable.ic_pause);
            notificationBuilder.setOngoing(true);
        } else {
            notificationBuilder.setOngoing(false);
            remoteViews.setImageViewResource(R.id.notificationPlay, R.drawable.ic_play_arrow);
            bigRemoteView.setImageViewResource(R.id.exNotificationPlay, R.drawable.ic_play_arrow);
        }

        notificationBuilder.setContent(remoteViews);
        notificationBuilder.setCustomBigContentView(bigRemoteView);
        notificationManager.notify(NOTIFICATION_SONG_ID, notificationBuilder.build());
    }
}
