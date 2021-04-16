package com.example.nacho.ui.home;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nacho.CreatePlaylistActivity;
import com.example.nacho.MusicService;
import com.example.nacho.R;
import com.example.nacho.SongFiles;
import com.example.nacho.TrackFiles;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.nacho.ApplicationClass.ACTION_NEXT;
import static com.example.nacho.ApplicationClass.ACTION_PLAY;
import static com.example.nacho.ApplicationClass.ACTION_PREV;
import static com.example.nacho.ApplicationClass.CHANNEL_ID_2;
import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.libraryArrayList;

public class HomeFragment extends Fragment  {

    public static ArrayList<SongFiles> songsFiles;
//    ArrayList<TrackFiles> trackFilesArrayList=new ArrayList<>();
//    MediaSessionCompat mediaSession;
    public static final int REQUEST_CODE=1;
    public static boolean shuffle=false,repeat=false;
    RecyclerView songsList;
    public static int min;

    public HomeFragment(){
        //REQUIRED EMPTY PUBLIC CONSTRUCTOR
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
//        != PackageManager.PERMISSION_GRANTED){
//            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
//                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
//            }else {
//                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
//            }
//        }else {
//
//        }

//        mediaSession=new MediaSessionCompat(getContext(),"PlayerAudio");
        View view= inflater.inflate(R.layout.fragment_home,container,false);
        setHasOptionsMenu(true);
        songsList=view.findViewById(R.id.songsList);
//        songsList.setHasFixedSize(true);
        doStuff();
        min=-1;
//        populateFiles();
        return view;
    }



//    private void populateFiles() {
//        TrackFiles trackFiles=new TrackFiles("Faded","Alan Walker",R.drawable.nosongs);
//        trackFilesArrayList.add(trackFiles);
//        TrackFiles trackFiles1=new TrackFiles("Faded","Alan Walker",R.drawable.nosongs);
//        trackFilesArrayList.add(trackFiles);
//        TrackFiles trackFiles2=new TrackFiles("Faded","Alan Walker",R.drawable.nosongs);
//        trackFilesArrayList.add(trackFiles);
//        TrackFiles trackFiles3=new TrackFiles("Faded","Alan Walker",R.drawable.nosongs);
//        trackFilesArrayList.add(trackFiles);
//    }

    public void doStuff(){
//
//        songNames=new ArrayList<>();
//        songArtists=new ArrayList<>();
//        readSongs();
        songsFiles=new ArrayList<>();
        songsFiles=getAllAudio(getActivity());
        songsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        songsList.setAdapter(new SongsAdapter(getActivity(),songsFiles,false));

    }

//    public void readSongs(){
//        Uri songUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor songCursor=getActivity().getApplicationContext().getContentResolver().query(songUri,null,null,null,null);
//
//        if(songCursor!=null && songCursor.moveToFirst()){
//            int songTitle=songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//            int songArtist=songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//            int songAlbumArt=songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
////            int songImg=songCursor.getColumnIndex(MediaStore.Audio.Media.)
//            do {
//                String currentTitle=songCursor.getString(songTitle);
//                String currentArtist=songCursor.getString(songArtist);
//                String currentImg=songCursor.getString(songAlbumArt);
//                songNames.add(currentTitle);
//                songArtists.add(currentArtist);
//                songImages.add(currentImg);
//            }while (songCursor.moveToNext());
//        }
////        songCursor.close();
//    }

    public static ArrayList<SongFiles> getAllAudio(Context context){
        ArrayList<SongFiles> tempAudioList=new ArrayList<>();
        Uri songUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID
        };

        Cursor songCursor=context.getContentResolver().query(songUri,projection,null,null,null);
//        Cursor imgCursor;
//        Long albumId=Long.valueOf(songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));


        if (songCursor != null ){
//            if (imgCursor != null) {
              //            }
            int ipos = 0;
            while (songCursor.moveToNext()){
                String album=songCursor.getString(0);
                String title=songCursor.getString(1);
                String duration=songCursor.getString(2);
                String path=songCursor.getString(3);
                String artist=songCursor.getString(4);
//                String path = imgCursor.getString(imgCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//                Long albumId1= Long.valueOf(songCursor.getString(5));
                String id=songCursor.getString(5);
//                Long albumId=songCursor.getLong(6);
//                imgCursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//                        new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
//                        MediaStore.Audio.Albums._ID + "=" + albumId, null, null);
//                String albumCoverPath = null;
//                if(imgCursor != null && imgCursor.moveToNext()){
//                    albumCoverPath = imgCursor.getString(imgCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//                }
//                Uri sArtworkUri = Uri
//                        .parse("content://media/external/audio/albumart");
//                String albumArtUri = ContentUris.withAppendedId(sArtworkUri,
//                        albumId).toString();
                if(duration != null) {
                    SongFiles songFiles = new SongFiles(path, title, artist, album, duration, id, ipos);
                    tempAudioList.add(songFiles);
                    ipos++;
                }
            }
            songCursor.close();
//            imgCursor.close();
        }
        return tempAudioList;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.create_playlist,menu);
//        MenuItem menuItem=menu.findItem(R.id.createPlaylist);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.createPlaylist:
                Intent intent=new Intent(getActivity(), CreatePlaylistActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
////    public void onDestroy() {
////        super.onDestroy();
////        if(libraryArrayList != null) {
////            libraryArrayList.clear();
////        }
////    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case REQUEST_CODE:
//                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED){
//                        Toast.makeText(getActivity(), "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
////                        doStuff();
//                    }else{
//                        Toast.makeText(getActivity(),"no PERMISSION GRANTED",Toast.LENGTH_SHORT).show();
//                    }
//                }
//        }
//        return;
//    }




//    public void showNotification(int playpausebtn){
////        Intent intent=new Intent(getContext(),HomeFragment.class);
////        PendingIntent pendingIntent=PendingIntent.getActivity(getContext(),0,intent,0);
////
////        Intent prevIntent=new Intent(getContext(),NotificationReceiver.class).setAction(ACTION_PREV);
////        PendingIntent prevPendingIntent=PendingIntent.getBroadcast(getContext(),0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);
////
////        Intent playIntent=new Intent(getContext(),NotificationReceiver.class).setAction(ACTION_PLAY);
////        PendingIntent playPendingIntent=PendingIntent.getBroadcast(getContext(),0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
////
////        Intent nextIntent=new Intent(getContext(),NotificationReceiver.class).setAction(ACTION_NEXT);
////        PendingIntent nextPendingIntent=PendingIntent.getBroadcast(getContext(),0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
////
////        Bitmap picture= BitmapFactory.decodeResource(getResources(),songsFiles.get(position).getPath());
////
////        Notification notification=new NotificationCompat.Builder(getContext(),CHANNEL_ID_2)
////                .setSmallIcon(songsFiles.get(position).getPath())
////                .setLargeIcon(picture)
////                .setContentTitle(songsFiles.get(position).getTitle())
////                .setContentText(songsFiles.get(position).getArtist())
////                .addAction(R.drawable.ic_skip_previous,"Previous",prevPendingIntent)
////                .addAction(playpausebtn,"Play",playPendingIntent)
////                .addAction(R.drawable.ic_skip_next,"Next",nextPendingIntent)
////                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
////                .setMediaSession(mediaSession.getSessionToken()))
////                .setPriority(NotificationCompat.PRIORITY_LOW)
////                .setContentIntent(pendingIntent)
////                .setOnlyAlertOnce(true)
////                .build();
////
////        NotificationManager notificationManager= (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
////
////        notificationManager.notify(0,notification);
////
////    }
}








