package com.example.nacho.ui.yourlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nacho.MusicLibrary;
import com.example.nacho.MyDatabase.MyDb;
import com.example.nacho.R;
import com.example.nacho.SongFiles;
import com.example.nacho.YourLibraryItems;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.nacho.CreatePlaylistActivity.deletedName;
import static com.example.nacho.SelectSongsActivity.myDb;

public class YourLibraryFragment extends Fragment {

    public static RecyclerView myLibraryRecyclerView;
//    ArrayList<String> myLibraryNames;
//    public static String description;
//    public static String name,tp;
//    MusicLibrary library;
    public static ArrayList<YourLibraryItems> libraryArrayList;
//    ArrayList<SongFiles> files;
//    int sizeOfList,k,m;
//    public static int a,len,prevLen,temporaryVar;
//    private int p;
//    public static MyDBForLibraryFragment myDb;
    public static ArrayList<SongFiles> myLibrarySongs;
//    public static ArrayList<SelectedSongs> songsArrayList;
    public YourLibraryFragment(){
        //REQUIRED EMPTY PUBLIC CONSTRUCTOR
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.fragment_your_library,container,false);

        View view1=inflater.inflate(R.layout.empty_your_library_activity,container,false);

        myLibraryRecyclerView=view.findViewById(R.id.myLibraryRecyclerview);


//        Log.i("After recyxler","yes");

        if(libraryArrayList == null){
            libraryArrayList=new ArrayList<>();
            myDb=new MyDb(getActivity());

//            db=new MyDb(getActivity());
//            myDb=new MyDBForLibraryFragment(getActivity());
//            Log.i("lib arraylist null","yes");
//            loadData();
//            libraryArrayList=myDb.getLibraries();

        }

        getTheLibraryData();


        showMyLibrary();

        if(libraryArrayList != null && !libraryArrayList.isEmpty()){
            view1.setVisibility(View.GONE);

            return view;
        }else {

            view.setVisibility(View.GONE);
            return view1;
        }
//        name=null;
//        string="";
//        tp=null;
//        Log.i("oncreate","on");
//        Log.i("Before recyxler","yes");





//        getIntentData();

//        Log.i("After getIntentdata","yes");
//        Log.i("lib arraylist not null","yes");

//        Log.i("name",name);
//        Bundle bundle = this.getArguments();
//        String myValue = bundle.getString("Name","name");
//        Log.i("myValue",myValue);
//        reset();
//        Log.i("myLibrarySongs", String.valueOf(myLibrarySongs.size()));
//        if(json != null){
//            loadData();
//        }

//        if(libraryArrayList != null) {
//            prevLen = libraryArrayList.size();
//        }
//        Log.i("prevlennnnnnnnn", String.valueOf(prevLen));
//        temporaryVar=prevLen + 1;
//        Log.i("After prev len","yes");

//        getLibraryData();


//        if(libraryArrayList != null) {
//            len = libraryArrayList.size();
//        }
//        Log.i("lennnnnnnnn", String.valueOf(len));

//        Log.i("After getlib","yes");
//        if(temporaryVar == len) {


//            Log.i("After save data","yes");
//        }

//        if (prevLen != len) {
        //below in the main save
//            saveData();
//        }
        // below is the main loadData
//            loadData();



//        libraryArrayList=myDb.getLibraries();

//        Log.i("After load data","yes");
//        showMyLibrary();
//        Log.i("After showlib","yes");
//
//        Log.i("After len","yes");
//        name=null;
//        Log.i("Serialize", String.valueOf(myLibrarySongs.size()));
//        reset();



//        if (name == null){
//            name="";
//        }

//        if (temporaryVar == len){
//            name="";
//        }

//        name="";
//        if(name.isEmpty()){
//            name="";
//        }

//        name="";
//        if(!name.isEmpty()){
//            name=null;
//        }
//        if (string == null){
//            name= "";
//        }


//        if (a != -1){
//            name = null;
//        }



//        Log.i("namemzzzzzzzz",name);

//        if (temporaryVar == len){
//            name=null;
//        }
//        m=0;
//        name=null;


    }

    public void showView(int R, ViewGroup conta){

        View view=getLayoutInflater().inflate(R,conta,false);
    }



    private void saveData() {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(libraryArrayList);
        editor.clear();
//        editor.apply();
        editor.putString("library names",json);
        editor.apply();
//        editor.commit();
//        editor.clear();
//        editor.commit();
    }

    private void loadData(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString("library names",null);
//        Log.i("json",json);
        Type type=new TypeToken<ArrayList<MusicLibrary>>() {}.getType();
//        if(libraryArrayList != null)
        libraryArrayList=gson.fromJson(json,type);

//        if (libraryArrayList == null){
//            libraryArrayList=new ArrayList<>();
//        }
    }


//    private void getIntentData() {
//
//        name=getActivity().getIntent().getStringExtra("Name");
//
//        description=getActivity().getIntent().getStringExtra("Description");
//
//        a=getActivity().getIntent().getIntExtra("a",0);
//
//        myLibrarySongs=getActivity().getIntent().getParcelableArrayListExtra("temporaryList");
//
////        songsArrayList=getActivity().getIntent().getParcelableArrayListExtra("selectedSongsArrayList");
//
//
//
////        m=-1;
////        Bundle args = getActivity().getIntent().getBundleExtra("BUNDLE");
////
////        myLibrarySongs = (ArrayList<SongFiles>) args.getSerializable("TemporaryList");
//
//
//
////        if(myLibrarySongs != null){
////            myLibrarySongs=getActivity().getIntent().getStringArrayListExtra("LibrarySongsList");
////        }
//
//    }



//    private void getLibraryData() {
//        if(libraryArrayList == null){
//            libraryArrayList=new ArrayList<>();
//        }
//        if((myLibrarySongs != null) && myLibrarySongs.size() > 0) {
//            sizeOfList = myLibrarySongs.size();
//            Log.i("size", String.valueOf(sizeOfList));
//        }else {
//            sizeOfList= 0;
////            Log.i("h", "getLibraryData: ");
//        }
//        int image=R.drawable.libraryimage;
////        libraryArrayList.clear();
//
//
//        if( name!=null ) {
////                Log.i("added", String.valueOf(libraryArrayList.size()));
////                if(libraryArrayList.get(libraryArrayList.size()-1).getName() != name) {
////                    try {
////                        Log.i("how","true");
////                        Log.i("library",libraryArrayList.get(libraryArrayList.size()-1).getName());
////                        Log.i("simplename",name);
////
////                    }catch (Exception e){
////                        e.printStackTrace();
////                    }
//
//
//                    library = new MusicLibrary(image, name, sizeOfList);
////                    myDb.addLibrary(library);
//                    libraryArrayList.add(library);
////                    name=null;
////                }
//
//        }
//        if(libraryArrayList != null && libraryArrayList.size() > 1){
//            int s=libraryArrayList.size();
//            if(libraryArrayList.get(s - 1).getName().equals(libraryArrayList.get(s-2).getName())){
//                libraryArrayList.remove(s-1);
//                Log.i("equal","equal");
//            }
//        }
//        if(libraryArrayList != null && deletedName != null) {
//
//            int s = libraryArrayList.size();
//            if (s > 0){
//                if (libraryArrayList.get(s - 1).getName().equals(deletedName)) {
//                    libraryArrayList.remove(s - 1);
//                }
//            }
//        }
//    }

    private void getTheLibraryData(){

        if(libraryArrayList == null) {
            libraryArrayList = new ArrayList<>();
            Log.i("nuuullll","nullll");
        }

        if(libraryArrayList != null && myDb != null) {
            libraryArrayList = myDb.getMusicLibrary();
            Log.i("nooooot nuuullll","nooooot nullll");
            Log.i("siiizzzzeeee", String.valueOf(libraryArrayList.size()));
        }
    }




    private void showMyLibrary() {
//        myLibraryNames=new ArrayList<>();
//        files=new ArrayList<>();

        myLibraryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(libraryArrayList != null) {
            myLibraryRecyclerView.setAdapter(new MyLibraryAdapter(getActivity(), libraryArrayList));
//            Log.i("not null","list");
        }
//        else if(temporaryList != null){
//            myLibraryRecyclerView.setAdapter(new MyLibraryAdapter(getActivity(), libraryArrayList));
////            Log.i("null","list");
//        }

//        new MyLibraryAdapter(getActivity(), libraryArrayList).notifyDataSetChanged();
    }

//    //for not taking name of playlist more times when come back to Your Library
//    public void reset(){
////        libraryArrayList.remove(libraryArrayList.size() - 1);
//        Log.i("reset","reset");
//        if(libraryArrayList!=null)
//        libraryArrayList.clear();
//        name=null;
//        description=null;
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        name=null;
//        Log.i("reset","reset");
//    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        p=-1;
//        name=null;
//
//        Log.i("resume","resume");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        name=null;
//        Log.i("pause","pause");
//    }
////
//    @Override
//    public void onStart() {
//        super.onStart();
//        getIntentData();
//        Log.i("start","start");
//    }


//    @Override
//    public void onStop() {
//        super.onStop();
//        name=null;
//    }


}