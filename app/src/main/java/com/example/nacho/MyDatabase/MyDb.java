package com.example.nacho.MyDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nacho.SongFiles;
import com.example.nacho.YourLibraryItems;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyDb extends SQLiteOpenHelper {

    public static int id;
    int i;

    public MyDb(Context context){
        super(context,Params.DATABASE_NAME,null,Params.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //CREATE TABLE
        String createTable="CREATE TABLE " + Params.TABLE_NAME +
                " ( " + Params.KEY_ID + " INTEGER PRIMARY KEY, "
                + Params.KEY_POS + " INTEGER , " +
                Params.KEY_NAME + " TEXT , " +
                Params.KEY_DESCRIPTION + " Text , " +
                Params.KEY_IMAGE + " INTEGER , " +
                Params.KEY_FILES + " Text , " +
                Params.KEY_TRACKS + " INTEGER " + " ) ";
        sqLiteDatabase.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void addItem(YourLibraryItems item){
        SQLiteDatabase db=this.getWritableDatabase();


        ContentValues values=new ContentValues();
//        values.put(Params.KEY_ID,i);
//        i++;
        values.put(Params.KEY_POS,item.getPosition());
        values.put(Params.KEY_NAME,item.getName());
        values.put(Params.KEY_DESCRIPTION,item.getDescription());
        values.put(Params.KEY_IMAGE,item.getImage());
        Gson gson=new Gson();
        String json=gson.toJson(item.getMyfiles());
        values.put(Params.KEY_FILES,json);
        values.put(Params.KEY_TRACKS,item.getTracks());

        db.insert(Params.TABLE_NAME,null,values);

        db.close();
    }

    public ArrayList<YourLibraryItems> getItems(){
        ArrayList<YourLibraryItems> itemsArrayList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        //GENERATE THE QUERY TO READ FROM DATABASE

        String select="SELECT * FROM " +Params.TABLE_NAME;
        Cursor cursor=db.rawQuery(select,null);


        //EXTRACT DATA FROM TABLE

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name, desc, files;
                    int pos, img,tracks;
//                    id = cursor.getInt(0);
                    pos = cursor.getInt(1);
                    name = cursor.getString(2);
                    desc = cursor.getString(3);
                    img = cursor.getInt(4);
                    files = cursor.getString(5);
                    tracks=cursor.getInt(6);

                    Log.i("pos in getItem", String.valueOf(pos));

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<SongFiles>>() {
                    }.getType();
                    ArrayList<SongFiles> mySongs = gson.fromJson(files, type);
                    YourLibraryItems tempItem = new YourLibraryItems(name, desc, img, mySongs, pos,tracks);

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


    public void deleteItemByPosition(int position){
        SQLiteDatabase db=this.getWritableDatabase();
        Log.i("pos in delItem", String.valueOf(position));
//        db.execSQL("DELETE FROM " + Params.TABLE_NAME);
//        db.execSQL("DROP TABLE " + Params.TABLE_NAME);
        db.delete(Params.TABLE_NAME,Params.KEY_POS + "=?",new String[]{String.valueOf(position)});
        db.close();
    }

    public void deleteItemByName(String n){
        SQLiteDatabase db=this.getWritableDatabase();
//
        db.delete(Params.TABLE_NAME,Params.KEY_NAME + "=?",new String[]{String.valueOf(n)});
        db.close();
    }

    public void deleteItemById(int id){
        SQLiteDatabase db=this.getWritableDatabase();
//        db.execSQL("DELETE FROM " + Params.TABLE_NAME);
        db.delete(Params.TABLE_NAME,Params.KEY_ID + "=?",new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteLastRow(){
        SQLiteDatabase db=this.getWritableDatabase();

        db.execSQL("DELETE FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_ID + " = " + " ( SELECT MAX  ( "  + Params.KEY_ID + " ) FROM " + Params.TABLE_NAME + " );");

        db.close();
    }

    public void updateItem(int position){
//        int temp=position;
        SQLiteDatabase db=this.getWritableDatabase();

        String strSQL = "UPDATE " + Params.TABLE_NAME + " SET " + Params.KEY_POS + "="
                + Params.KEY_POS + " - " + 1 + " WHERE "
                + Params.KEY_POS + " > " + position;

        db.execSQL(strSQL);

        db.close();

    }

    public ArrayList<YourLibraryItems> getMusicLibrary(){
        ArrayList<YourLibraryItems> itemsArrayList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        //GENERATE THE QUERY TO READ FROM DATABASE

        String select="SELECT * FROM " +Params.TABLE_NAME;
        Cursor cursor=db.rawQuery(select,null);


        //EXTRACT DATA FROM TABLE

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name;
                    int img,tracks,pos;
//                    id = cursor.getInt(0);
                    pos = cursor.getInt(1);
                    name = cursor.getString(2);
//                    desc = cursor.getString(3);
                    img = cursor.getInt(4);
//                    files = cursor.getString(5);
                    tracks=cursor.getInt(6);

//                    Log.i("pos in getItem", String.valueOf(pos));

//                    Gson gson = new Gson();
//                    Type type = new TypeToken<ArrayList<SongFiles>>() {
//                    }.getType();
//                    ArrayList<SongFiles> mySongs = gson.fromJson(files, type);
                    YourLibraryItems tempItem = new YourLibraryItems(name,img,tracks,pos);

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

}
