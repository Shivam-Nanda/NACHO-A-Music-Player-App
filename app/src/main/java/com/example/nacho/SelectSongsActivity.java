package com.example.nacho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nacho.MyDatabase.MyDb;
import com.example.nacho.ui.yourlibrary.YourLibraryFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static com.example.nacho.CreatePlaylistActivity.deletedName;
//import static com.example.nacho.TemporaryAdapter.selectedSongsArrayList;
import static com.example.nacho.TemporaryAdapter.temporaryList;
import static com.example.nacho.ui.home.HomeFragment.songsFiles;
import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.libraryArrayList;

public class SelectSongsActivity extends AppCompatActivity {

    RecyclerView selectSongsRecyclerView;
    String name;
    String description;
    int a;
    YourLibraryItems item;
    public static MyDb myDb;
    private int sizeOfSongsList;
    int playlistsImages[];
    Random random;
    int randomNumberForImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_songs);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B71C1C")));


        playlistsImages= new int[]{R.drawable.musiclibrary1,R.drawable.musiclibrary2,R.drawable.musiclibrary3,
                R.drawable.musiclibrary4,R.drawable.musiclibrary5,
                R.drawable.musiclibrary6,R.drawable.musiclibrary7,
                R.drawable.musiclibrary8,R.drawable.musiclibrary9,
                R.drawable.musiclibrary10,R.drawable.libraryimage};

        random=new Random();
        name=getIntent().getStringExtra("name");
        description=getIntent().getStringExtra("description");
        initializeRecyclerView();
        myDb=new MyDb(this);
        a=-1;
        Log.i("size", String.valueOf(temporaryList.size()));
    }

    private void initializeRecyclerView() {
        selectSongsRecyclerView=findViewById(R.id.selectSongsRecyclerView);
        selectSongsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        selectSongsRecyclerView.setAdapter(new TemporaryAdapter(getApplicationContext(),songsFiles));
    }

    public void saveLibraryData(){
        randomNumberForImages=random.nextInt(11);
//        temporaryList=setPositionsInSongs(temporaryList);
//        setPositionsInSongs();
        setPosInPlaylists();
        sizeOfSongsList=temporaryList.size();
        int s;
        if(libraryArrayList != null){
            s=libraryArrayList.size();
        }else {
            s=0;
        }
        item=new YourLibraryItems(name,description,playlistsImages[randomNumberForImages],temporaryList,s,sizeOfSongsList);
        myDb.addItem(item);
    }

    public void setPosInPlaylists(){
//        int length = temporaryList.size() -1;
//        int nextSong = 1;

//        for(int i=0;i<temporaryList.size();i++){
//            temporaryList.get(i).setPosition(i);
//        }

        int length = temporaryList.size() -1;
        int nextSong = 1;
        for(int i=0;i<temporaryList.size();i++){

            int nextPos=temporaryList.get(nextSong).getPosition();
            int prevPos=temporaryList.get(length).getPosition();
            temporaryList.get(i).setNextPos(nextPos);
            temporaryList.get(i).setPrevPos(prevPos);
            length = i;
            if(nextSong == temporaryList.size() - 1) {
                nextSong = 0;
            }
            else {
                nextSong = nextSong + 1;
            }

        }
    }

//    public void setPositionsInSongs(){
////        ArrayList<SongFiles> files=new ArrayList<>();
//        SongFiles file;
////        int len=songFiles.size() - 1;
////        int i=1,p=-1,pos=0;
////
////        for (SongFiles songMp3: songFiles){
//////            songMp3.setPosition(pos);
////            songMp3.setNextPos(i);
////            songMp3.setPrevPos(len);
////            if((songFiles.size() - 1) == i ){
////                i=0;
////            }else {
////                i++;
////            }
////            p++;
////            len=p;
////            pos++;
////            files.add(songMp3);
////        }
//        int length = temporaryList.size() -1;
//        int nextSong = 1;
//        for(int i=0;i<temporaryList.size();i++){
//
//            int nextPos=temporaryList.get(nextSong).getPosition();
//            int prevPos=temporaryList.get(length).getPosition();
//            temporaryList.get(i).setNextPos(nextPos);
//            temporaryList.get(i).setPrevPos(prevPos);
//            length = i;
//            if(nextSong == temporaryList.size() - 1) {
//                nextSong = 0;
//            }
//            else {
//                nextSong = nextSong + 1;
//            }
//
//        }
////        return files;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tick_menu_file,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.doneOption:

//                FragmentManager fm=getSupportFragmentManager();
//                YourLibraryFragment fragment=new YourLibraryFragment();
//                fm.beginTransaction().add(R.id.fragmentLayout,fragment).commit();
//                fm.popBackStackImmediate();
//                Log.i("Select","Songs");
//                Bundle bundle=new Bundle();
//                bundle.putString("Name",name);
//                bundle.putString("Description",description);
//                fragment.setArguments(bundle);


                if(temporaryList.isEmpty()){
                    Toast.makeText(this, " Your Playlist is Empty.Please Select Some Songs ", Toast.LENGTH_LONG).show();
                }
                else {
                    deletedName=null;
                    saveLibraryData();
//                Log.i("size", String.valueOf(temporaryList.size()));
                    Toast.makeText(this, "Playlist Created", Toast.LENGTH_SHORT).show();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,fragment).addToBackStack(null).commit();
                    Intent intent=new Intent(getApplicationContext(),SongsActivity.class);
                    finish();
                    startActivity(intent);
                }
                break;


//                Bundle args=new Bundle();
//                intent.putExtra("Name",name);
//                intent.putExtra("Description",description);
//                intent.putExtra("a",a);
//                args.putSerializable("TemporaryList",(Serializable)temporaryList);
//                intent.putExtra("BUNDLE",args);

//                intent.putParcelableArrayListExtra("temporaryList",temporaryList);
//                intent.putParcelableArrayListExtra("selectedSongsArrayList",selectedSongsArrayList);
//                if(temporaryList!=null)
//                temporaryList.clear();
//                intent.putParcelableArrayListExtra("LibrarySongsList", (ArrayList<? extends Parcelable>) temporaryList);

//                if(temporaryList!=null)
//                temporaryList.clear();

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Cannot go back.Please select songs", Toast.LENGTH_SHORT).show();
    }
}