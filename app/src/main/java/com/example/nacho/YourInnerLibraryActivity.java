package com.example.nacho;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nacho.MyDatabase.MyDb;
import com.example.nacho.ui.home.SongsAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.nacho.CreatePlaylistActivity.deletedName;
import static com.example.nacho.TemporaryAdapter.temporaryList;
//import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.len;
//import static com.example.nacho.MyDatabase.MyDb.id;
//import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.prevLen;
import static com.example.nacho.SelectSongsActivity.myDb;


import static com.example.nacho.ui.home.HomeFragment.min;



public class YourInnerLibraryActivity extends AppCompatActivity {

    TextView playlistNameTextView,playlistDescriptionTextView;
    ImageView imageViewPlaylist;
    RecyclerView innerSongsRecyclerView;
//    private String plName;
//    private String plDescription;
//    public static ArrayList<SelectedSongs> mainArrayList;


//    String nameP,descriptionP;
//    SharedPreferences sharedPreferences;
    ArrayList<SongFiles> myLibrarySongs;
    YourLibraryItems myLibraryItem;
   public static ArrayList<YourLibraryItems> yourLibraryItemsArrayList;
//    int pos,i,j;
    int pos,l;
    int i;
    private int tempPosition;
//    public static MyDb database;
    public static int minimum;
//    private int length;
//    private int prevLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_inner_library);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B71C1C")));
        pos=-1;

        min=Integer.MIN_VALUE;


        Log.i("Oncreate","oncreate");
        Log.i("not null","not");
       if(yourLibraryItemsArrayList == null){
            Log.i("yourLibraryItemsArrayList","null");
            yourLibraryItemsArrayList=new ArrayList<>();
            Log.i("null","yes");
//            database=new MyDb(this);
//            yourLibraryItemsArrayList=database.getItems();
//            loadDataM();
        }

        initializeAllView();
        pos=getIntent().getIntExtra("position",-1);
        minimum=getIntent().getIntExtra("min",0);


        getInnerLibraryData();

        updateLayout();

//        if(yourLibraryItemsArrayList != null) {
//            Log.i("loadDataMMMMMMMMMMMMMMM", String.valueOf(yourLibraryItemsArrayList.size()));
//        }else{
//            Log.i("loadDataMMMMMMMMMMMMMMM","null");
//
//        }

//        if(myLibrarySongs == null){
//            myLibrarySongs=new ArrayList<>();
////            loadDataM();
//        }


//        getDataOfIntent();


//        Log.i("opisiton", String.valueOf(pos));

//        yourLibraryItemsArrayList=database.getItems();

//        Log.i("songListtttttttt",yourLibraryItemsArrayList.get(pos).getName());
//        if (yourLibraryItemsArrayList == null){
//            Log.i("yourLibraryItemsArrayList2","null");
//            yourLibraryItemsArrayList=new ArrayList<>();
//            loadDataM();
//        }
//        i=-1;
//        j=-1;
//        if(myLibrarySongs == null){
//            myLibrarySongs=new ArrayList<>();
//            loadDataM();
//        }
//        if (myLibrarySongs != null) {
//            Log.i("sssssssssssssssssssss", String.valueOf(myLibrarySongs.size()));
//        }
//        Log.i("yyyyyyyyyyyyyyyyyyyy", String.valueOf(myLibrarySongs.size()));
//       if(myLibrarySongs != null)
//        myLibrarySongs.clear();

//        i=pos;
//        j=pos;
//        loadDataPermanent();
//        if (length == prevLength + 1) {
//        if()
//        if(!(pos < l))
//        tempPosition=pos+1;

//        if(tempPosition == len) {
//            saveDataM();

//        }
//        Log.i("tempPos", String.valueOf(tempPosition));
//        Log.i("lenttttttttt", String.valueOf(len));
//        }
//        loadDataM();
//        Log.i("pos", String.valueOf(pos));
//        if(nameP != null)
//        Log.i("name",nameP);

//        Log.i("posssssssss", String.valueOf(pos));
//        Log.i("lengthhhhhhh", String.valueOf(yourLibraryItemsArrayList.size()));

//        Log.i("index", String.valueOf(pos));
//        Log.i("sizeYours", String.valueOf(yourLibraryItemsArrayList.size()));



//        Log.i("youurlibitemsssarrlist", String.valueOf(yourLibraryItemsArrayList.size()));

//        saveDate();
//        myLibrarySongs=temporaryList;

//        saveDatePermanent();


//            Log.i("songs","nulllllllll");
//            Log.i("mysongs", String.valueOf(myLibrarySongs.size()));
//            for (int i=0;i<myLibrarySongs.size();i++) {
//                Log.i("songs", myLibrarySongs.get(i).getTitle());
//            }

//        saveDate();
//        tempLibrarySongs=myLibrarySongs;
//        if(myLibrarySongs != null)
//        l=myLibrarySongs.size();

//        saveDate();
    }

    private void getInnerLibraryData() {
        yourLibraryItemsArrayList=myDb.getItems();
    }

    private void updateLayout() {



        imageViewPlaylist.setImageResource(yourLibraryItemsArrayList.get(pos).getImage());

        playlistDescriptionTextView.setText(yourLibraryItemsArrayList.get(pos).getDescription());
        playlistNameTextView.setText(yourLibraryItemsArrayList.get(pos).getName());

        innerSongsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(yourLibraryItemsArrayList != null && yourLibraryItemsArrayList.get(pos).getMyfiles() != null) {

            innerSongsRecyclerView.setAdapter(new SongsAdapter(this, yourLibraryItemsArrayList.get(pos).getMyfiles(),true));
//            innerSongsRecyclerView.setNestedScrollingEnabled(true);

        }

    }



//        private void getDataOfIntent() {
//
//        plName=getIntent().getStringExtra("plName");
//        plDescription = getIntent().getStringExtra("plDescription");
//        myLibrarySongs=getIntent().getParcelableArrayListExtra("InnerLibrarySongs");
////        mainArrayList=getIntent().getParcelableArrayListExtra("songsArrayList");
//        pos=getIntent().getIntExtra("position",-1);
////        minimum=getIntent().getIntExtra("min",0);
//
//
//        if(plName == null){
//            Log.i("p1","null");
//        }else {
//            Log.i("p1","not null");
//
//        }
//
//
//        myLibraryItem=new YourLibraryItems(plName,plDescription,R.drawable.libraryimage,myLibrarySongs,pos);
////        i++;
//
////        if (yourLibraryItemsArrayList == null) {
////
////            yourLibraryItemsArrayList=new ArrayList<>();
////
////        }
//
//
//
//            if (yourLibraryItemsArrayList == null) {
//                yourLibraryItemsArrayList = new ArrayList<>();
//            }
//
//
//            if(plName != null ) {
//                Log.i("added","yes getting added");
//                yourLibraryItemsArrayList.add(myLibraryItem);
//                database.addItem(myLibraryItem);
//            }
//
//
//
//
//
//
//            if(yourLibraryItemsArrayList != null && yourLibraryItemsArrayList.size() > 1){
//                Log.i("yourLibraryItemsArrayList","not null");
//                int length=yourLibraryItemsArrayList.size();
////                Log.i("getname",yourLibraryItemsArrayList.get(length - 1).getName());
//                String tempName=yourLibraryItemsArrayList.get(length - 1).getName();
//                if(tempName != null) {
//                    if (tempName.equals(yourLibraryItemsArrayList.get(length - 2).getName())) {
//                        yourLibraryItemsArrayList.remove(length - 1);
////                        database.deleteItemByPosition(length - 1);
//                        database.deleteLastRow();
//                    }
//                }
//            }
//
//
//
//            if(yourLibraryItemsArrayList != null && deletedName != null) {
//
//                int length = yourLibraryItemsArrayList.size();
//                if (length > 0){
//                    if (yourLibraryItemsArrayList.get(length - 1).getName().equals(deletedName)) {
//                        yourLibraryItemsArrayList.remove(length - 1);
//                        database.deleteLastRow();
//                    }
//                }
//            }
//
//
//
//
//
//
//
////        length=getIntent().getIntExtra("len",-1);
////        temp=getIntent().getIntExtra("temp",0);
////        prevLength=getIntent().getIntExtra("prevLen",-1);
////        Log.i("sssssssssssssssssssss", String.valueOf(myLibrarySongs.size()));
//    }

    private void initializeAllView() {
        playlistNameTextView=findViewById(R.id.playlistNameTextView);
        playlistDescriptionTextView=findViewById(R.id.playlistDescriptionTextView);
        imageViewPlaylist=findViewById(R.id.imageViewPlaylist);
        innerSongsRecyclerView=findViewById(R.id.innerSongsRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadDataM();
        Log.i("onresume","onresume");
    }
}