package com.example.nacho.MyDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nacho.MusicLibrary;
import com.example.nacho.SongFiles;
import com.example.nacho.YourLibraryItems;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyDBForLibraryFragment extends SQLiteOpenHelper {


    public MyDBForLibraryFragment(Context context){
        super(context, LibraryParams.DATABASE_NAME,null, LibraryParams.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //CREATE TABLE
        String createTable="CREATE TABLE " + LibraryParams.TABLE_NAME +
                " ( " + LibraryParams.KEY_ID + " INTEGER PRIMARY KEY, " +
                LibraryParams.KEY_NAME + " TEXT , " +
                LibraryParams.KEY_TRACKS + " INTEGER , " +
                LibraryParams.KEY_IMAGE + " INTEGER  " +
                 " ) ";

//        LibraryParams.KEY_POS + " TEXT  "
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addLibrary(MusicLibrary item){
        SQLiteDatabase db=this.getWritableDatabase();


        ContentValues values=new ContentValues();
//        values.put(Params.KEY_ID,i);
//        i++;
        values.put(LibraryParams.KEY_NAME,item.getName());
        values.put(LibraryParams.KEY_TRACKS,item.getTracks());
        values.put(LibraryParams.KEY_IMAGE,item.getImage());
//        values.put(LibraryParams.KEY_POS,item.getPosition());
//        Gson gson=new Gson();
//        String json=gson.toJson(item.getMyfiles());
//        values.put(Params.KEY_FILES,json);

        db.insert(LibraryParams.TABLE_NAME,null,values);

        db.close();
    }

    public ArrayList<MusicLibrary> getLibraries(){
        ArrayList<MusicLibrary> itemsArrayList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        //GENERATE THE QUERY TO READ FROM DATABASE

        String select="SELECT * FROM " + LibraryParams.TABLE_NAME;
        Cursor cursor=db.rawQuery(select,null);


        //EXTRACT DATA FROM TABLE

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name;
                    int tracks, img;
//                    id = cursor.getInt(0);
//                    pos = cursor.getInt(0);
                    name = cursor.getString(1);
                    tracks = cursor.getInt(2);
                    img = cursor.getInt(3);




//                    Gson gson = new Gson();
//                    Type type = new TypeToken<ArrayList<SongFiles>>() {
//                    }.getType();
//                    ArrayList<SongFiles> mySongs = gson.fromJson(files, type);
                    MusicLibrary tempItem = new MusicLibrary(img,name,tracks);

                    itemsArrayList.add(tempItem);
                    Log.i("itemsaralist", String.valueOf(itemsArrayList.size()));
//                tempItem.setPosition(());
//                tempItem.setName();
//                tempItem.setDescription(cursor.getString(2));
//                tempItem.setImage(cursor.getInt(3));
//                Gson gson=new Gson();
//                String json=cursor.getString(4);
////                Type type = new TypeToken<ArrayList<YourLibraryItems>>() {
////                }.getType();
////                ArrayList<SongFiles> mySongs=gson.fromJson(json,type);
//                tempItem.setMyfiles(json);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return itemsArrayList;
    }

    public void deleteLibraryByName(String name){
        SQLiteDatabase db=this.getWritableDatabase();
//        Log.i("pos in delItem", String.valueOf(position));
//        db.execSQL("DELETE FROM " + Params.TABLE_NAME);
        db.delete(LibraryParams.TABLE_NAME,LibraryParams.KEY_NAME + "=?",new String[]{name});
        db.close();
    }

//    public void updateLibrary(int position){
////        int temp=position;
//        SQLiteDatabase db=this.getWritableDatabase();
//
//        String strSQL = "UPDATE " + LibraryParams.TABLE_NAME + " SET " + LibraryParams.KEY_POS + "="
//                + LibraryParams.KEY_POS + " - " + 1 + " WHERE "
//                + LibraryParams.KEY_POS + " > " + position;
//
//        db.execSQL(strSQL);
//
//        db.close();
//
//    }

}
