package com.example.nacho;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.palette.graphics.Palette;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nacho.Notifications.NotificationGenerator;
import com.example.nacho.ui.home.HomeFragment;
import com.example.nacho.ui.home.SongsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

import static com.example.nacho.ApplicationClass.ACTION_NEXT;
import static com.example.nacho.ApplicationClass.ACTION_PLAY;
import static com.example.nacho.ApplicationClass.ACTION_PREV;
import static com.example.nacho.ApplicationClass.CHANNEL_ID_2;
import static com.example.nacho.R.drawable.nosongs;
import static com.example.nacho.SongsActivity.bottomBar;
import static com.example.nacho.SongsActivity.bottomBarPlayButton;
import static com.example.nacho.SongsActivity.bottomBarSongArtist;
import static com.example.nacho.SongsActivity.bottomBarSongImage;
import static com.example.nacho.SongsActivity.bottomBarSongTitle;
import static com.example.nacho.ui.home.HomeFragment.repeat;
import static com.example.nacho.ui.home.HomeFragment.shuffle;
//import static com.example.nacho.ui.home.HomeFragment.songsFiles;
import static com.example.nacho.ui.home.SongsAdapter.saveInSharedPreferences;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, ServiceConnection {

    static TextView songName,songArtist,durationPlayed,durationTotal,nowPLaying;
    static ImageView coverArt, nextButton,prevButton,backButton,shuffleButton,repeatButton, gradient;
    static RelativeLayout musicContainer, topButtons;
    static FloatingActionButton playPauseButton;
    SeekBar seekBar;
    static Uri uri;
    TaskStackBuilder stackBuilder;
    public static MediaPlayer mediaPlayer;
    static String titlesearch;
    static int position=-1,z=-1;
    static ArrayList<SongFiles> songsFiles,tempSongs;
    private Handler handler=new Handler();
    private Thread playThread,prevThread,nextThread;
    static Window window;
    private  View decorView;
    MediaSessionCompat mediaSession;
    boolean isPlaying;
    public static NotificationManager notificationManager;
    public static Notification notification;
    MusicService musicService;
    PendingIntent pendingIntent;
    Intent notificationIntent;
    private long i=0;
    private long x;
    private boolean isTouched=false;
    ObjectAnimator objectAnimator;
    static boolean fromNotification=false;
    private int previousPos=-1;
    private static boolean isReceiver = false;
    private int searchPos;
    private static boolean onBottom;
    private static int mainPos;
    private static boolean isUpdate;
//    public static BroadcastReceiver broadcastReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        //BELOW LINE OF CODE KEEPS THE PLAYER ACTIVITY SCREEN ON //
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();

//        fromNotification=getIntent().getBooleanExtra("fromNotification",false);


        getIntentMethod();

        setBottomBar(getApplicationContext());

        onTouchBottomBar(getApplicationContext(),position);
//        onTouchBottomBar(getApplicationContext(),position);

        songName.setText("\t\t\t\t\t\t\t\t\t" + songsFiles.get(position).getTitle() + "\t\t\t\t\t\t\t\t\t");
        songArtist.setText(songsFiles.get(position).getArtist());
//        songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        songName.setSelected(true);
        mediaPlayer.setOnCompletionListener(this);
        isPlaying=false;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if(!fromNotification){
//                stopService(new Intent(getBaseContext(), MusicService.class));
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//
            if(!isReceiver) {
                BroadcastReceiver receiver = getReceiver();
                registerReceiver(receiver, new IntentFilter("TRACKS_TRACKS"));
                isReceiver=true;
            }
            startService(new Intent(getBaseContext(), MusicService.class));
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
////
//            if(fromNotification == false) {
////                registerReceiver(receiver, new IntentFilter("TRACKS_TRACKS"));
//                stopService(new Intent(getBaseContext(), MusicService.class));
////                fromNotification=false;
//            }
//        }
//        broadcastReceiver=receiver;
//        NotificationGenerator.showSongNotification(getApplicationContext(),position,);
        showNotification(R.drawable.ic_pause, position, getApplicationContext());
//






//        onClickNotification();
//        notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null & b){
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int currentPosition=mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                    durationPlayed.setText(formattedTime(currentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shuffle){
                    shuffle=false;
                    shuffleButton.setImageResource(R.drawable.ic_shuffle_off);
                    repeatButton.setEnabled(true);
                    repeatButton.setClickable(true);

                }else {
                    shuffle=true;
                    shuffleButton.setImageResource(R.drawable.ic_shuffle_on);
                    repeatButton.setEnabled(false);
                    repeatButton.setClickable(false);
                }
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(repeat){
                    repeat=false;
                    repeatButton.setImageResource(R.drawable.ic_repeat_off);
                    shuffleButton.setEnabled(true);
                    shuffleButton.setClickable(true);
                }else{
                    repeat=true;
                    repeatButton.setImageResource(R.drawable.ic_repeat_on);
                    shuffleButton.setEnabled(false);
                    shuffleButton.setClickable(false);
                }
            }
        });


        //for fading seekbar and other buttons on screen clicked//

        musicContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
                Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
                if(!isTouched){
                   isTouched=true;

                    seekBar.startAnimation(animFadeOut);
                    durationPlayed.startAnimation(animFadeOut);
                    durationTotal.startAnimation(animFadeOut);
                    shuffleButton.startAnimation(animFadeOut);
                    repeatButton.startAnimation(animFadeOut);
                    playPauseButton.startAnimation(animFadeOut);
                    nextButton.startAnimation(animFadeOut);
                    prevButton.startAnimation(animFadeOut);
                    backButton.startAnimation(animFadeOut);
                    animFadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            seekBar.setVisibility(View.GONE);
                            durationPlayed.setVisibility(View.GONE);
                            durationTotal.setVisibility(View.GONE);
                            shuffleButton.setVisibility(View.GONE);
                            repeatButton.setVisibility(View.GONE);
                            playPauseButton.setVisibility(View.GONE);
                            nextButton.setVisibility(View.GONE);
                            prevButton.setVisibility(View.GONE);
                            backButton.setVisibility(View.GONE);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });



                }
                else {
                    isTouched=false;


                    seekBar.startAnimation(animFadeIn);
                    durationPlayed.startAnimation(animFadeIn);
                    durationTotal.startAnimation(animFadeIn);
                    shuffleButton.startAnimation(animFadeIn);
                    repeatButton.startAnimation(animFadeIn);
                    playPauseButton.startAnimation(animFadeIn);
                    nextButton.startAnimation(animFadeIn);
                    prevButton.startAnimation(animFadeIn);
                    backButton.startAnimation(animFadeIn);
                    animFadeIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            seekBar.setVisibility(View.VISIBLE);
                            durationPlayed.setVisibility(View.VISIBLE);
                            durationTotal.setVisibility(View.VISIBLE);
                            shuffleButton.setVisibility(View.VISIBLE);
                            repeatButton.setVisibility(View.VISIBLE);
                            playPauseButton.setVisibility(View.VISIBLE);
                            nextButton.setVisibility(View.VISIBLE);
                            prevButton.setVisibility(View.VISIBLE);
                            backButton.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        //BELOW CODE FOR ROTATING COVER ART IMAGE
        objectAnimator = ObjectAnimator.ofFloat(coverArt, "rotation", 0, 360);
        objectAnimator.setDuration(17000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.start();

    }

    private BroadcastReceiver getReceiver() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getExtras().getString("actionname");

                switch (action) {
                    case ACTION_PREV:
                        prevButtonClicked();
                        break;
                    case ACTION_PLAY:

//                    if (mediaPlayer.isPlaying()){
                        playPauseButtonClicked();

//                        showNotification(R.drawable.ic_play_arrow,position);
//                        playPauseButton.setBackgroundResource(R.drawable.ic_pause);

//                    }else {
//                        playPauseButtonClicked();
//                        showNotification(R.drawable.ic_pause,position);

//                        playPauseButton.setBackgroundResource(R.drawable.ic_play_arrow);
//                    }
                        break;
                    case ACTION_NEXT:
                        nextButtonClicked();
                        break;
                }
            }
        };
        return receiver;
    }

    @Override
    protected void onResume() {
        playThreadButton();
        prevThreadButton();
        nextThreadButton();
        super.onResume();
        Intent intent=new Intent(getApplicationContext(),MusicService.class);
        getApplicationContext().bindService(intent,this,BIND_AUTO_CREATE);

    }


    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onStop() {
        super.onStop();



    }

    @Override
    public void onPause() {
        super.onPause();
//        Intent intent=new Intent(getApplicationContext(), PlayerActivity.class);
//        stackBuilder.addNextIntentWithParentStack(intent);
        getApplicationContext().unbindService(this);
//        this.unregisterReceiver(receiver);
    }


    private void playThreadButton() {
        playThread=new Thread(){
            @Override
            public void run() {
                super.run();
                playPauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseButtonClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void prevThreadButton() {
        prevThread=new Thread(){
            @Override
            public void run() {
                super.run();
                prevButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prevButtonClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void nextThreadButton() {
        nextThread=new Thread(){
            @Override
            public void run() {
                super.run();
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextButtonClicked();
                    }
                });
            }
        };
        nextThread.start();
    }



    private void playPauseButtonClicked() {
        if (mediaPlayer.isPlaying()){

            isPlaying=true;
            playPauseButton.setImageResource(R.drawable.ic_play_arrow);
            bottomBarPlayButton.setImageResource(R.drawable.ic_play_arrow);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition=mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
//                    resumeNotification();
                }
            });
            objectAnimator.pause();
            showNotification(R.drawable.ic_play_arrow,position,getApplicationContext());
//         resumeNotification();
        }else {
            isPlaying=false;
            playPauseButton.setImageResource(R.drawable.ic_pause);
            bottomBarPlayButton.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition=mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            objectAnimator.resume();
            showNotification(R.drawable.ic_pause,position,getApplicationContext());
//            cancelNotification();
//            showNotification(R.drawable.ic_pause,position);
        }
    }

    private void prevButtonClicked() {
        if(mediaPlayer.isPlaying()){
            isPlaying=true;
            mediaPlayer.stop();
            mediaPlayer.release();

            if (shuffle && !repeat){
                position=getRandom(songsFiles.size() - 1);
//                repeatButton.setClickable(false);
//                repeatButton.setEnabled(false);
            }else if (!shuffle && !repeat) {
                if(isUpdate){
                    position=tempSongs.get(mainPos).getPrevPos();
                    mainPos=(mainPos - 1) < 0 ? (tempSongs.size() - 1) : (mainPos - 1);
                }else {
                    position=((position - 1) < 0 ? (songsFiles.size() - 1) : (position - 1));
                }

            }
//            else if(repeat && !shuffle){
//                shuffleButton.setClickable(false);
//                shuffleButton.setEnabled(false);
//            }
//            else position will be position

            uri=Uri.parse(songsFiles.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);

            songName.setText("\t\t\t\t\t\t\t\t\t" + songsFiles.get(position).getTitle() + "\t\t\t\t\t\t\t\t\t");
            songArtist.setText(songsFiles.get(position).getArtist());
            songName.setSelected(true);


            SongFiles file=new SongFiles(songsFiles.get(position).getTitle(),getImageforSharedPreferences(position,getApplicationContext()),songsFiles.get(position).getArtist(),position);

            saveInSharedPreferences(file,getApplicationContext());

            bottomBarSongTitle.setText(songsFiles.get(position).getTitle());
            bottomBarSongArtist.setText(songsFiles.get(position).getArtist());
            onTouchBottomBar(getApplicationContext(),position);

            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            objectAnimator.resume();
            playPauseButton.setBackgroundResource(R.drawable.ic_pause);
            bottomBarPlayButton.setImageResource(R.drawable.ic_pause);
            showNotification(R.drawable.ic_pause,position,getApplicationContext());
            mediaPlayer.start();
        }else{
            isPlaying=false;
            mediaPlayer.stop();
            mediaPlayer.release();

            if (shuffle && !repeat){
                position=getRandom(songsFiles.size() - 1);
//                repeatButton.setClickable(false);
//                repeatButton.setEnabled(false);
            }
            else if (!shuffle && !repeat) {
                if(isUpdate){
                    position=tempSongs.get(mainPos).getPrevPos();
                    mainPos=(mainPos - 1) < 0 ? (tempSongs.size() - 1) : (mainPos - 1);
                }else {
                    position=((position - 1) < 0 ? (songsFiles.size() - 1) : (position - 1));
                }

            }
//            else if(repeat && !shuffle){
//                shuffleButton.setClickable(false);
//                shuffleButton.setEnabled(false);
//            }
//            else position will be position

            uri=Uri.parse(songsFiles.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText("\t\t\t\t\t\t\t\t\t" + songsFiles.get(position).getTitle() + "\t\t\t\t\t\t\t\t\t");
            songArtist.setText(songsFiles.get(position).getArtist());
            songName.setSelected(true);


            SongFiles file=new SongFiles(songsFiles.get(position).getTitle(),getImageforSharedPreferences(position,getApplicationContext()),songsFiles.get(position).getArtist(),position);

            saveInSharedPreferences(file,getApplicationContext());

            bottomBarSongTitle.setText(songsFiles.get(position).getTitle());
            bottomBarSongArtist.setText(songsFiles.get(position).getArtist());
            onTouchBottomBar(getApplicationContext(),position);

            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            objectAnimator.resume();
            playPauseButton.setImageResource(R.drawable.ic_pause);
            bottomBarPlayButton.setImageResource(R.drawable.ic_pause);
            showNotification(R.drawable.ic_pause,position,getApplicationContext());
            mediaPlayer.start();
        }
//        playPauseButton.setBackgroundResource(R.drawable.ic_pause);
    }


    private void nextButtonClicked() {
        if (mediaPlayer.isPlaying()){
            isPlaying=true;
            mediaPlayer.stop();
            mediaPlayer.release();

            if (shuffle && !repeat){
                position=getRandom(songsFiles.size() - 1);
//                repeatButton.setClickable(false);
//                repeatButton.setEnabled(false);
            }else if (!shuffle && !repeat) {
                if (isUpdate){
                    position=tempSongs.get(mainPos).getNextPos();
                    mainPos=((mainPos+1) > (tempSongs.size() - 1) ? (0) : (mainPos + 1));
                }else {
                    position = ((position + 1) > (songsFiles.size() - 1) ? (0) : (position + 1));
                }

            }
//            else if(repeat && !shuffle){
//                shuffleButton.setClickable(false);
//                shuffleButton.setEnabled(false);
//            }
//            else position will be position
            uri= Uri.parse(songsFiles.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText("\t\t\t\t\t\t\t\t\t" + songsFiles.get(position).getTitle() + "\t\t\t\t\t\t\t\t\t");
            songArtist.setText(songsFiles.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            songName.setSelected(true);


            SongFiles file=new SongFiles(songsFiles.get(position).getTitle(),getImageforSharedPreferences(position,getApplicationContext()),songsFiles.get(position).getArtist(),position);

            saveInSharedPreferences(file,getApplicationContext());

            bottomBarSongTitle.setText(songsFiles.get(position).getTitle());
            bottomBarSongArtist.setText(songsFiles.get(position).getArtist());
            onTouchBottomBar(getApplicationContext(),position);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition=mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            objectAnimator.resume();
            mediaPlayer.start();
            playPauseButton.setBackgroundResource(R.drawable.ic_pause);
            bottomBarPlayButton.setImageResource(R.drawable.ic_pause);
            showNotification(R.drawable.ic_pause,position,getApplicationContext());
//            Log.i("if","if");
        }
        else {
            isPlaying=false;
            mediaPlayer.stop();
            mediaPlayer.release();

            if (shuffle && !repeat){
                position=getRandom(songsFiles.size() - 1);
//                repeatButton.setClickable(false);
//                repeatButton.setEnabled(false);
            }else if (!shuffle && !repeat) {
                if (isUpdate){
                    position=tempSongs.get(mainPos).getNextPos();
                    mainPos++;
                }else {
                    position = ((position + 1) > (songsFiles.size() - 1) ? (0) : (position + 1));
                }

            }
//            else if(repeat && !shuffle){
//                shuffleButton.setClickable(false);
//                shuffleButton.setEnabled(false);
//            }
//            else position will be position
            uri= Uri.parse(songsFiles.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText("\t\t\t\t\t\t\t\t\t" + songsFiles.get(position).getTitle() + "\t\t\t\t\t\t\t\t\t");
            songArtist.setText(songsFiles.get(position).getArtist());
            songName.setSelected(true);


            SongFiles file=new SongFiles(songsFiles.get(position).getTitle(),getImageforSharedPreferences(position,getApplicationContext()),songsFiles.get(position).getArtist(),position);

            saveInSharedPreferences(file,getApplicationContext());

            bottomBarSongTitle.setText(songsFiles.get(position).getTitle());
            bottomBarSongArtist.setText(songsFiles.get(position).getArtist());
            onTouchBottomBar(getApplicationContext(),position);

            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition=mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            objectAnimator.resume();
            playPauseButton.setImageResource(R.drawable.ic_pause);
            bottomBarPlayButton.setImageResource(R.drawable.ic_pause);
//            if(x != -1){
//                cancelNotification();
//            }
            showNotification(R.drawable.ic_pause,position,getApplicationContext());
            mediaPlayer.start();
//            Log.i("else","else");
        }
//        playPauseButton.setBackgroundResource(R.drawable.ic_pause);
    }



    private int getRandom(int i) {
        Random random=new Random();

        return random.nextInt(i+1);
    }

    private String formattedTime(int currentPosition) {
        String totalOut="";
        String totalNew="";
        String seconds=String.valueOf(currentPosition % 60);
        String minutes=String.valueOf(currentPosition / 60);
        totalOut=minutes + ":" + seconds;
        totalNew=minutes + ":" + "0" + seconds;
        if (seconds.length() == 1){
            return totalNew;
        }
        else {
            return totalOut;
        }
    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int totalDuration=Integer.parseInt(songsFiles.get(position).getDuration() ) / 1000;
        durationTotal.setText(formattedTime(totalDuration));

        byte[] art=retriever.getEmbeddedPicture();

        Bitmap bitmap;
        if (art != null){

            bitmap= BitmapFactory.decodeByteArray(art,0,art.length);

            ImageAnimation(this,coverArt,bitmap);

            ImageAnimation(this,bottomBarSongImage,bitmap);


            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch=palette.getDominantSwatch();
//                    RelativeLayout topButtons=findViewById(R.id.layoutTopBtn);
//                    ImageView gradient=findViewById(R.id.imageViewGradient);
//                    musicContainer=findViewById(R.id.musicContainer);
                    gradient.setBackgroundResource(R.drawable.top_bg);
                    musicContainer.setBackgroundResource(R.drawable.player_background);
                    GradientDrawable gradientDrawable;

                    if(swatch != null){
                        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), 0x00000000});
                        gradient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), swatch.getRgb() });
                        musicContainer.setBackground(gradientDrawableBg);
                        topButtons.setBackground(gradientDrawable);
                        songName.setTextColor(swatch.getTitleTextColor());
                        songArtist.setTextColor(swatch.getBodyTextColor());
                        nowPLaying.setTextColor(swatch.getTitleTextColor());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(getApplicationContext().getResources().getColor(android.R.color.transparent));
                            window.setNavigationBarColor(getApplicationContext().getResources().getColor(android.R.color.transparent));
                            window.setBackgroundDrawable(gradientDrawableBg);
                        }
//
                    }
                    else {
                        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0x00000000});
                        gradient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0xff000000 });
                        musicContainer.setBackground(gradientDrawableBg);
                        topButtons.setBackground(gradientDrawable);
                        songName.setTextColor(Color.WHITE);
                        songArtist.setTextColor(Color.DKGRAY);
                        nowPLaying.setTextColor(Color.DKGRAY);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(getApplicationContext().getResources().getColor(android.R.color.transparent));
                            window.setNavigationBarColor(getApplicationContext().getResources().getColor(android.R.color.transparent));
                            window.setBackgroundDrawable(gradientDrawableBg);
                        }

                    }
                }
            });
        }else {
            Glide.with(this).asBitmap().load(R.drawable.nosongs).into(coverArt);
            Glide.with(this).asBitmap().load(R.drawable.nosongs).into(bottomBarSongImage);
//            ImageView gradient=findViewById(R.id.imageViewGradient);
//            RelativeLayout musicContainer=findViewById(R.id.musicContainer);
            gradient.setBackgroundResource(R.drawable.top_bg);
            musicContainer.setBackgroundResource(R.drawable.player_background);
            songName.setTextColor(Color.WHITE);
            songArtist.setTextColor(Color.DKGRAY);
            nowPLaying.setTextColor(Color.DKGRAY);

        }

    }


    public void goBack(View view){

        super.onBackPressed();

    }

//    public void onClickNotification(){
//        z=getIntent().getIntExtra("z",-1);
//        if(z!=-1) {
//            position = z;
//        }
//        Log.i("z", String.valueOf(z));
//    }

    private void getIntentMethod() {
//        x=getIntent().getIntExtra("i",-1);
        z=getIntent().getIntExtra("z",-1);
        boolean newSong = getIntent().getBooleanExtra("newSong",false);
//        Log.i("z", String.valueOf(z));
        position=getIntent().getIntExtra("position",z);//put default value = z
//        onBottom=getIntent().getBooleanExtra("onBottom",false);
        Log.i("position", String.valueOf(position));

        mainPos=getIntent().getIntExtra("mainPos",-1);

        isUpdate=getIntent().getBooleanExtra("isUpdate",false);

        songsFiles= SongsAdapter.mySongsmp3;

        tempSongs=SongsAdapter.songsFiles;
//        if(HomeFragment.songsFiles.size() == songsFiles.size()){
//            position=getIntent().getIntExtra("mainPos",-1);
//        }
//        tempSongs=songsFiles;
//        if(songsFiles.size() != HomeFragment.songsFiles.size()) {
//
//            titlesearch = songsFiles.get(position).getTitle();
//        }


//        searchPos=-1;
//        int j;


//        if(!newSong || onBottom) {
//            for (j = 0; j < HomeFragment.songsFiles.size(); j++) {
//                if (HomeFragment.songsFiles.get(j).getTitle().equals(titlesearch)) {
//                    searchPos = j;
//                    break;
//                }

//            }
//            if( j == HomeFragment.songsFiles.size()){
//                searchPos= -1;
//            }
//        }




//        if(songsFiles.size() == HomeFragment.songsFiles.size() && searchPos != -1) {
//            position = searchPos;
//            SongFiles myFile=new SongFiles(getBottomBarTitle(position),getImageforSharedPreferences(position,getApplicationContext()),getBottomBarArtist(position),position);
//
//            saveInSharedPreferences(myFile,getApplicationContext());
//        }

//        searchPos=getIntent().getIntExtra("p",-1);

//        position=searchPos;
        if(songsFiles != null){
            uri=Uri.parse(songsFiles.get(position).getPath());
        }


//        if(songsList != null){
//            playPauseButton.setImageResource(R.drawable.ic_pause);
//            uri=Uri.parse(songsList.get(position).getPath());
//        }
        if (mediaPlayer == null) {
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else if (newSong) {
//            titlesearch="";
//            onBottom=false;
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }

        if(mediaPlayer.isPlaying()){
            playPauseButton.setImageResource(R.drawable.ic_pause);
            bottomBarPlayButton.setImageResource(R.drawable.ic_pause);
        }else {
            playPauseButton.setImageResource(R.drawable.ic_play_arrow);
            bottomBarPlayButton.setImageResource(R.drawable.ic_play_arrow);
        }

        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
    }

    public void ImageAnimation(final Context context, final ImageView imageView, final Bitmap bitmap){
        Animation animOut= AnimationUtils.loadAnimation(context,android.R.anim.fade_out);
        final Animation animIn= AnimationUtils.loadAnimation(context,android.R.anim.fade_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(getBaseContext()).load(bitmap).into(imageView);

                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animIn);
//                rotateImageAnimation(context,imageView);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);

    }

    private void initView() {
        songName=findViewById(R.id.songName);
        songArtist=findViewById(R.id.songArtist);
        durationPlayed=findViewById(R.id.durationPlayed);
        durationTotal=findViewById(R.id.durationTotal);
        coverArt=findViewById(R.id.coverArt);
        nextButton=findViewById(R.id.skipNext);
        prevButton=findViewById(R.id.skipPrevious);
        backButton=findViewById(R.id.backBtn);
        shuffleButton=findViewById(R.id.shuffleButton);
        repeatButton=findViewById(R.id.repeatButton);
        playPauseButton=findViewById(R.id.playPause);
        seekBar=findViewById(R.id.seekBar);
        songsFiles=new ArrayList<>();
        window=this.getWindow();
        nowPLaying=findViewById(R.id.nowPlaying);
        mediaSession=new MediaSessionCompat(getApplicationContext(),"PlayerAudio");
        musicContainer=findViewById(R.id.musicContainer);
        gradient=findViewById(R.id.imageViewGradient);
        topButtons=findViewById(R.id.layoutTopBtn);


//        decorView=getWindow().getDecorView();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextButtonClicked();
        if (mediaPlayer == null){
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(this);
        }
    }
//
//    public boolean checkServiceRunning(Class<?> serviceClass){
//        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
//        {
//            if (serviceClass.getName().equals(service.service.getClassName()))
//            {
//                return true;
//            }
//        }
//        return false;
//    }

    public void showNotification(int playpausebtn,int position,Context context){
        isPlaying=true;
//        if(checkServiceRunning(PlayerActivity.class)){
//
//        }
        notificationIntent=new Intent(context, PlayerActivity.class);
        notificationIntent.putExtra("z",position);
        notificationIntent.putExtra("fromNotification",true);
        stackBuilder = TaskStackBuilder.create(this);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);




        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        stackBuilder.addNextIntentWithParentStack(notificationIntent);
        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        intent.putExtra("i",i);
//        i++;
        ;

//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


//        intent.setAction("android.intent.action.MAIN");
//
//        intent.addCategory("android.intent.category.LAUNCHER");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        Intent prevIntent=new Intent(context.getApplicationContext(), NotificationReceiver.class).setAction(ACTION_PREV);
        PendingIntent prevPendingIntent=PendingIntent.getBroadcast(context,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent=new Intent(context.getApplicationContext(),NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent playPendingIntent=PendingIntent.getBroadcast(context,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        final Intent nextIntent=new Intent(context.getApplicationContext(),NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent=PendingIntent.getBroadcast(context,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);



        byte[] art=getAlbumArt(songsFiles.get(position).getPath());
//        byte[] art1=getAlbumArt(String.valueOf(nosongs));

//        Bitmap picture= BitmapFactory.decodeResource(Resources.getSystem(), Integer.parseInt(songsFiles.get(position).getPath()));
        Bitmap image;
        if (art != null){
            image=BitmapFactory.decodeByteArray(art,0,art.length);
        }
        else {
            image = BitmapFactory.decodeResource(context.getResources(), nosongs);

        }

//        int k;
//        if (isPlaying){
//            k=R.drawable.ic_play_arrow;
//        }else{
//            k=R.drawable.ic_pause;
//        }
//        onClickNotification();

        notification=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID_2)
                .setSmallIcon(R.mipmap.mylogo)
                .setLargeIcon(image)
                .setContentTitle(songsFiles.get(position).getTitle())
                .setContentText(songsFiles.get(position).getArtist())
                .addAction(R.drawable.ic_skip_previous,"Previous",prevPendingIntent)
                .addAction(playpausebtn,"Play",playPendingIntent)
                .addAction(R.drawable.ic_skip_next,"Next",nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0,1,2)
                        .setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
                .build();

//        notification.flags = Notification.FLAG_ONGOING_EVENT;
       notificationManager= (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

       notificationManager.notify(0,notification);

    }

//    public void cancelNotification(){
//        notificationManager.cancelAll();
//
//    }

//    public void resumeNotification(){
//        notification.flags = Notification.FLAG_ONGOING_EVENT;
//    }
    private byte[] getAlbumArt(String uri){
//        Bitmap bArt = null;
//        BitmapFactory.Options bfo=new BitmapFactory.Options();
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    @Override
    public void onBackPressed() {
//        stackBuilder.addNextIntentWithParentStack(notificationIntent);
//        pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        super.onBackPressed();
    }

    public void setBottomBar(final Context context){


        bottomBarPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    bottomBarPlayButton.setImageResource(R.drawable.ic_play_arrow);
                    playPauseButton.setImageResource(R.drawable.ic_play_arrow);
                    showNotification(R.drawable.ic_play_arrow,position,getApplicationContext());
                }else {
                    mediaPlayer.start();
                    bottomBarPlayButton.setImageResource(R.drawable.ic_pause);
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    showNotification(R.drawable.ic_pause,position,getApplicationContext());
                }
            }
        });
    }

    public static void onTouchBottomBar(final Context context, final int pos){
        bottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBottom=true;
                Intent intent=new Intent(context,PlayerActivity.class);
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("onBottom",true);
                intent.putExtra("position",pos);
//                intent.putExtra("prevPos",prevPos);
//                if(prevPos != pos) {
//                    intent.putExtra("newSong", true);
//                    prevPos = pos;
//                }

                context.startActivity(intent);
            }
        });
    }

    public String getImageforSharedPreferences(int pos,Context context){
        byte[] art=getAlbumArt(songsFiles.get(pos).getPath());
        Bitmap image;
        if (art != null){
            image=BitmapFactory.decodeByteArray(art,0,art.length);
        }
        else {
            image = BitmapFactory.decodeResource(context.getResources(), nosongs);

        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private String getBottomBarTitle(int pos){
        String title = songsFiles.get(pos).getTitle();
//        if(title.length() >= 35){
//            title=title.substring(0,32);
//            title.concat("..");
//        }
        int bracketIndex=title.indexOf("(");
        if (bracketIndex != -1) {
            title = title.substring(0, bracketIndex);
        }
        else{
            int hyphenIndex=title.indexOf("-");
            if(hyphenIndex != -1){
                title=title.substring(0,hyphenIndex);
            }
        }
        return title;
    }

    private String getBottomBarArtist(int pos){
        String songArtist = songsFiles.get(pos).getArtist();

        String songArtistsArr[]=songArtist.split(",");

        return songArtistsArr[0];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            notificationManager.cancelAll();
//        }

//        unregisterReceiver(receiver);
//        showNotification(R.drawable.ic_play_arrow,position);


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        showNotification(R.drawable.ic_play_arrow,position);
//    }



    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder=(MusicService.MyBinder) iBinder;
        musicService=binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService=null;
    }





//    private void rotateImageAnimation(Context context,final ImageView imageView) {
////        RotateAnimation rotate = new RotateAnimation(
////                0, 360,
////                Animation.RELATIVE_TO_SELF, 0.5f,
////                Animation.RELATIVE_TO_SELF, 0.5f
////        );
////        rotate.setDuration(2000);
////        rotate.setRepeatCount(Animation.INFINITE);
////        imageView.startAnimation(rotate);
//        Animation rotate=AnimationUtils.loadAnimation(context,R.anim.rotate);
//        rotate.setInterpolator(new LinearInterpolator());
//        rotate.setFillAfter(true);
//
//        imageView.startAnimation(rotate);
//    }



    //    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if(hasFocus){
//            decorView.setSystemUiVisibility(hideSystemBars());
//        }
//    }
//
//    private int hideSystemBars(){
//        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ;
//    }
}
