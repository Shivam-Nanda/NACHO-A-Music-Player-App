package com.example.nacho.ui.yourlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.nacho.MusicLibrary;
import com.example.nacho.R;
import com.example.nacho.YourInnerLibraryActivity;
import com.example.nacho.YourLibraryItems;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.nacho.SelectSongsActivity.myDb;
//import static com.example.nacho.YourInnerLibraryActivity.database;
import static com.example.nacho.YourInnerLibraryActivity.yourLibraryItemsArrayList;
//import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.db;
//import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.len;
//import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.myDb;
import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.myLibraryRecyclerView;
import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.myLibrarySongs;
//import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.name;
//import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.description;
import static com.example.nacho.CreatePlaylistActivity.deletedName;
//import static com.example.nacho.ui.yourlibrary.YourLibraryFragment.prevLen;
import static com.example.nacho.ui.home.HomeFragment.min;

public class MyLibraryAdapter extends RecyclerView.Adapter<MyLibraryAdapter.LibraryViewHolder> {

    private Context context;
    private ArrayList<YourLibraryItems> myLibrary;


    public MyLibraryAdapter(Context context, ArrayList<YourLibraryItems> myLibrary) {
        this.context = context;
        this.myLibrary = myLibrary;
        min=Integer.MIN_VALUE;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.your_library_names, parent, false);
        return new LibraryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, final int position) {

//        for (int i=0;i<myLibrary.size();i++) {
            String libraryName = myLibrary.get(position).getName();
            holder.libraryName.setText(libraryName);

            holder.libraryImage.setImageResource(myLibrary.get(position).getImage());

            holder.numberOfTracks.setText((myLibrary.get(position).getTracks()) + " tracks ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, YourInnerLibraryActivity.class);
//                intent.putExtra("plName",name);
//                intent.putExtra("plDescription",description);
//                Log.i("opisiton", String.valueOf(position));
//                intent.putParcelableArrayListExtra("InnerLibrarySongs",myLibrarySongs);
//                intent.putParcelableArrayListExtra("songsArrayList",songsArrayList);
                intent.putExtra("position",position);
//                intent.putExtra("temp",a);
                intent.putExtra("min",min);
//                intent.putExtra("len",len);
//                intent.putExtra("prevLen",prevLen);
                context.startActivity(intent);
            }
        });


        holder.menuDelete.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
//                                removeAt(position);
                                removeSharedPreferences(position);
                                break;
                        }
                        return true;
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return myLibrary.size() ;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class LibraryViewHolder extends RecyclerView.ViewHolder{

        ImageView libraryImage,menuDelete;
        TextView libraryName;
        TextView numberOfTracks;
        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            libraryImage=itemView.findViewById(R.id.libraryImage);
            menuDelete=itemView.findViewById(R.id.menuDelete);
            libraryName=itemView.findViewById(R.id.libraryName);
            numberOfTracks=itemView.findViewById(R.id.numberOfTracks);
        }
    }
//    public void removeAt(int position) {
//
//
//    }

    public void removeSharedPreferences(int position){
//        myLibrary.remove(position);

//        ArrayList<MusicLibrary> lib=new ArrayList<>();
//        myLibrary.clear();
//        SharedPreferences sharedPreferences=context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.clear().commit();

        if(myLibrary != null) {
            deletedName = myLibrary.get(position).getName();
            Log.i("deletednameee", deletedName);
            myLibrary.remove(position);
//            yourLibraryItemsArrayList.remove(position);
            myDb.deleteItemByPosition(position);

            myDb.updateItem(position);
//            SharedPreferences sharedPreferences=context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor=sharedPreferences.edit();


//            Gson gson=new Gson();
//            editor.clear();
//            String json=gson.toJson(myLibrary);
//            editor.putString("library names",json);
//            Type type=new TypeToken<ArrayList<MusicLibrary>>() {}.getType();
//            lib=gson.fromJson(json,type);
//            lib.remove(position);


//            editor.commit();
//            myDb.deleteLibraryByName(myLibrary.get(position).getName());
        }


//        if(yourLibraryItemsArrayList != null) {
//
//            yourLibraryItemsArrayList.remove(position);
//
//            Log.i("1", String.valueOf(yourLibraryItemsArrayList.size()));
////            database.deleteItemById(yourLibraryItemsArrayList.get(position).getId());
//
//            Log.i("2", String.valueOf(yourLibraryItemsArrayList.size()));
//            Log.i("pos in sgpredd", String.valueOf(position));
//
//            Log.i("3", String.valueOf(yourLibraryItemsArrayList.size()));
//        }

//        yourLibraryItemsArrayList.remove(position);
//        yourLibraryItemsArrayList.clear();
//        database.deleteItemByPosition(position);
//

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, myLibrary.size());
//        myLibraryRecyclerView.removeViewAt(position);
//        notifyDataSetChanged();

//        Gson gson=new Gson();
//        SharedPreferences sharedPreference=context.getSharedPreferences(String.valueOf(position),Context.MODE_PRIVATE);
//        SharedPreferences.Editor ed=sharedPreference.edit();
//        ed.clear();



//        String json=gson.toJson(myLibrary);
//        editor.putString("library names",json);
//        editor.apply();
//        editor.remove("library names");
//        editor.apply();
//        editor.commit();

    }
}
