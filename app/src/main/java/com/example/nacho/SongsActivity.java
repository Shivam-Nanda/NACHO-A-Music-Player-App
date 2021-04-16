package com.example.nacho;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nacho.ui.search.SearchFragment;
import com.example.nacho.ui.home.HomeFragment;
import com.example.nacho.ui.yourlibrary.YourLibraryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static com.example.nacho.PlayerActivity.mediaPlayer;
import static com.example.nacho.PlayerActivity.onTouchBottomBar;
import static com.example.nacho.PlayerActivity.playPauseButton;
import static com.example.nacho.ui.home.SongsAdapter.getFromSharedPreference;

public class SongsActivity extends AppCompatActivity {


    public static final int REQUEST_CODE=1;
    BottomNavigationView navigationView;
    public static ImageView bottomBarSongImage,bottomBarPlayButton;
    public static TextView bottomBarSongTitle,bottomBarSongArtist;

    public static CardView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        initBottomBar();
        getBottomBarData(getApplicationContext());
        bottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("position",getFromSharedPreference(getApplicationContext()).getPosition());


                getApplicationContext().startActivity(intent);
            }
        });

        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            bottomBarPlayButton.setImageResource(R.drawable.ic_pause);
            playPauseButton.setImageResource(R.drawable.ic_pause);
        }else if(mediaPlayer != null){
            bottomBarPlayButton.setImageResource(R.drawable.ic_play_arrow);
            playPauseButton.setImageResource(R.drawable.ic_play_arrow);
        }

        bottomBarPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    bottomBarPlayButton.setImageResource(R.drawable.ic_play_arrow);
                    playPauseButton.setImageResource(R.drawable.ic_play_arrow);
//                    showNotification(R.drawable.ic_play_arrow,position,getApplicationContext());
                }else if(mediaPlayer != null) {
                    mediaPlayer.start();
                    bottomBarPlayButton.setImageResource(R.drawable.ic_pause);
                    playPauseButton.setImageResource(R.drawable.ic_pause);
//                    showNotification(R.drawable.ic_pause,position,getApplicationContext());
                }else {
                    Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("position",getFromSharedPreference(getApplicationContext()).getPosition());

                    getApplicationContext().startActivity(intent);
                }
            }
        });


//        BottomNavigationView navView = findViewById(R.id.navigationView);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#B71C1C"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        grantPermissions();
        navigationView=findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(navListener);

        //SETTING HOME FRAGMENT AS MAIN FRAGMENT

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout,new HomeFragment()).commit();
    }

    private void grantPermissions() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SongsActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }else {
//            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //DO WHATEVER YOU WANT WHEN PERMISSION IS GRANTED
            }else {
                ActivityCompat.requestPermissions(SongsActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener=new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment=null;


                    switch (item.getItemId()){
                        case R.id.navigationHome:
                          selectedFragment=new HomeFragment();
                            break;
                        case R.id.navigationSearch:
                            selectedFragment=new SearchFragment();
                            break;
                        case R.id.navigationYourLibrary:
                            selectedFragment=new YourLibraryFragment();
                            break;
                    }
                    //Begin Transaction

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,selectedFragment).commit();
                    return true;
                }
            };


    public void initBottomBar(){
        bottomBarSongImage=findViewById(R.id.bottomBarSongImage);
        bottomBarSongTitle=findViewById(R.id.bottomBarSongTitle);
        bottomBarSongArtist=findViewById(R.id.bottomBarSongArtist);
        bottomBarPlayButton=findViewById(R.id.bottomBarPlayButton);
        bottomBar=findViewById(R.id.bottomBar);
    }

    public void getBottomBarData(Context context){

        SongFiles sf;
        sf = getFromSharedPreference(context);
        if(sf != null) {
//            Log.i("titlebttom",sf.getTitle());
            bottomBarSongTitle.setText(sf.getTitle());
            bottomBarSongArtist.setText(sf.getArtist());
            byte[] b = Base64.decode(sf.getImage(), Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);
            Glide.with(context).load(image).into(bottomBarSongImage);
        }
        else{
//            bottomBarSongTitle.setText("Dekh Lena");
//            bottomBarSongArtist.setText("Arijit Singh");
//            Glide.with(context).load(R.drawable.nosongs).into(bottomBarSongImage);
            bottomBar.setVisibility(View.GONE);
        }
    }
}