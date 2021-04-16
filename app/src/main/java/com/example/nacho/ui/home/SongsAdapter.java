package com.example.nacho.ui.home;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nacho.MusicLibrary;
import com.example.nacho.PlayerActivity;
import com.example.nacho.R;
import com.example.nacho.SongFiles;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.example.nacho.ApplicationClass.ACTION_NEXT;
import static com.example.nacho.ApplicationClass.ACTION_PLAY;
import static com.example.nacho.ApplicationClass.ACTION_PREV;
import static com.example.nacho.ApplicationClass.CHANNEL_ID_2;
import static com.example.nacho.R.drawable.nosongs;
import static com.example.nacho.SongsActivity.bottomBar;
import static com.example.nacho.SongsActivity.bottomBarSongArtist;
import static com.example.nacho.SongsActivity.bottomBarSongImage;
import static com.example.nacho.SongsActivity.bottomBarSongTitle;
import static com.example.nacho.YourInnerLibraryActivity.minimum;
import static com.example.nacho.ui.home.HomeFragment.min;


public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> {

      private Context context;
      private int prevPos = -1;
      private static int p;
      public static boolean updateSongs;
      SongFiles sf;
    //    private ArrayList<String> songsNames;
//    private ArrayList<String> songsArtists;
//    private ArrayList<String> songsImgs;
    public static ArrayList<SongFiles> songsFiles;
    public static ArrayList<SongFiles> mySongsmp3;
//    private int source;

    public SongsAdapter(Context context, ArrayList<SongFiles> songsFiles,boolean updateSongs) {
        this.context = context;

        this.songsFiles = songsFiles;
        this.updateSongs=updateSongs;
//
//        if(updateSongs){
//            mySongsmp3 = songsFiles;
//        }
//        else {
            mySongsmp3=HomeFragment.songsFiles;
//        }



//        this.songsNames=songsNames;
//        this.songsArtists=songsArtists;
//        this.songsImgs=songsImgs;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.song_item_layout, parent, false);

        return new SongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, final int position) {




        String title = songsFiles.get(position).getTitle();
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
        holder.songTitle.setText(title);
        String songArtist = songsFiles.get(position).getArtist();

        String songArtistsArr[]=songArtist.split(",");
        holder.artist.setText(songArtistsArr[0]);
        String songDuration=songsFiles.get(position).getDuration();
        songDuration = String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(songDuration)),
                TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(songDuration)) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(songDuration))));

        int colonIndex=songDuration.indexOf(":");
        if(colonIndex != -1){
            String min=songDuration.substring(0,colonIndex);

            String sec=songDuration.substring(colonIndex+1);
            if (sec.length() == 1){
                songDuration=min+":" + "0" + sec;
            }
        }

        holder.duration.setText(songDuration);
        byte[] image;

        image = getAlbumArt(songsFiles.get(position).getPath());



        if (image != null) {
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.songImage);


        } else {
            Glide.with(context)
                    .load(nosongs)
                    .into(holder.songImage);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomBar.setVisibility(View.VISIBLE);
                Intent intent=new Intent(context, PlayerActivity.class);

                updateBottomBarResources(context,songsFiles.get(position).getPosition());


//                String searchName=mySongsmp3.get(position).getTitle();
//                for (int i=0;i<mySongsmp3.size();i++){
//                    if(searchName.equals(mySongsmp3.get(i).getTitle())){
//                      p=i;
//                    }
//                }


//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                if(updateSongs){
//                    intent.putExtra("position",position);
//                }
//                else {
                intent.putExtra("position",songsFiles.get(position).getPosition());
//                intent.putExtra("mainPos",HomeFragment.songsFiles.get(position).getPosition());
//                }

                intent.putExtra("isUpdate",updateSongs);

                intent.putExtra("mainPos",position);
                intent.putExtra("prevPos",prevPos);

                if(prevPos != position) {
                    intent.putExtra("newSong", true);
                    prevPos = position;
                }

                context.startActivity(intent);
            }
        });

        if(min == Integer.MIN_VALUE){
            holder.menuMore.setVisibility(View.INVISIBLE);
            holder.menuMore.setEnabled(false);
            holder.menuMore.setClickable(false);
        }else{
            holder.menuMore.setVisibility(View.VISIBLE);
            holder.menuMore.setEnabled(true);
            holder.menuMore.setClickable(true);

            holder.menuMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    PopupMenu menu=new PopupMenu(context,view);
                    menu.getMenuInflater().inflate(R.menu.pop_up,menu.getMenu());
                    menu.show();
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.delete:

                                    AlertDialog.Builder builder=new AlertDialog.Builder(context);

                                    builder.setMessage("Are you sure you want to delete?This file will be deleted permanently from your device storage.")
                                            .setPositiveButton("Delete",new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog,int which){
                                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                                    deleteFile(mySongsmp3.get(position).getPosition(),view);
                                                }
                                            }).setNegativeButton("Cancel",null)
                                            .setCancelable(true);

                                    AlertDialog alertDialog=builder.create();
                                    alertDialog.show();
                                    break;
                            }
                            return true;
                        }
                    });
                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return songsFiles.size();
    }

    public class SongsViewHolder extends RecyclerView.ViewHolder {
        ImageView songImage,menuMore;
        TextView songTitle;
        TextView artist;
        TextView duration;

        public SongsViewHolder(@NonNull View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.songImage);
            songTitle = itemView.findViewById(R.id.songTitle);
            artist = itemView.findViewById(R.id.artist);
            duration=itemView.findViewById(R.id.duration);
            menuMore = itemView.findViewById(R.id.menuMore);
        }
    }


    private byte[] getAlbumArt(String uri){
//        Bitmap bArt = null;
//        BitmapFactory.Options bfo=new BitmapFactory.Options();
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
//        if(uri!=null) {

            retriever.setDataSource(uri);
//        }
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    private void deleteFile(int position,View view){
        Uri uri=ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mySongsmp3.get(position).getId()));// content ://

        File file=new File(mySongsmp3.get(position).getPath());
        boolean deleted=file.delete(); // delete your music file
        if (deleted){
            context.getContentResolver().delete(uri,null,null);
            mySongsmp3.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,mySongsmp3.size());

            Snackbar.make(view,"File Deleted : ",Snackbar.LENGTH_LONG).show();
        }else {
            // file may be in sd card or api level is 19 or above //
            Snackbar.make(view,"Can delete the file : ",Snackbar.LENGTH_LONG).show();
        }

    }

    private void updateBottomBarResources(final Context context, final int pos){


        Bitmap image;
        byte[] art=getAlbumArt(mySongsmp3.get(pos).getPath());
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


        SongFiles file=new SongFiles(getBottomBarTitle(pos),encodedImage,getBottomBarArtist(pos),pos);
        saveInSharedPreferences(file,context);


//        saveInSharedPreferences(file,context);


//        getFromSharedPreference(context);



        bottomBarSongTitle.setText(getBottomBarTitle(pos));

        bottomBarSongArtist.setText(getBottomBarArtist(pos));



        Glide.with(context).asBitmap().load(image).into(bottomBarSongImage);




    }

    private String getBottomBarTitle(int pos){
        String title = mySongsmp3.get(pos).getTitle();
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
        String songArtist = mySongsmp3.get(pos).getArtist();

        String songArtistsArr[]=songArtist.split(",");

        return songArtistsArr[0];
    }

    public static void saveInSharedPreferences(SongFiles file,Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("songFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(file);
        editor.clear();
//        editor.apply();
        editor.putString("file",json);
        editor.apply();
    }

    public static SongFiles getFromSharedPreference(Context context){

        SharedPreferences sharedPreferences=context.getSharedPreferences("songFile", Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString("file",null);
//        Log.i("json",json);
        Type type=new TypeToken<SongFiles>() {}.getType();

        SongFiles fromJsonSong=gson.fromJson(json,type);

        return fromJsonSong;

    }



//    public void updateFilteredList(ArrayList<SongFiles> mySongs){
//        songsFiles=new ArrayList<>();
//        songsFiles.addAll(mySongs);
//        notifyDataSetChanged();
//    }

//    private Bitmap getAlbumArtUri(long albumId, String path) {
//        Bitmap albumArtBitMap;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//
//        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
//
//        albumArtBitMap = BitmapFactory.decodeFile(path);
//        return albumArtBitMap;
//
//    }
}