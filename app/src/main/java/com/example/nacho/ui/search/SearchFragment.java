package com.example.nacho.ui.search;

import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nacho.R;
import com.example.nacho.SongFiles;
import com.example.nacho.ui.home.HomeFragment;
import com.example.nacho.ui.home.SongsAdapter;

import java.util.ArrayList;

import static android.content.Context.SEARCH_SERVICE;
import static com.example.nacho.ui.home.HomeFragment.songsFiles;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    RecyclerView filteredSongsList;
    ArrayList<SongFiles> files;
    ArrayList<SongFiles> emptyList;
    public static boolean inSearch;
    public SearchFragment(){
        //REQUIRED EMPTY PUBLIC CONSTRUCTOR
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        setHasOptionsMenu(true);
        inSearch=true;
        filteredSongsList=view.findViewById(R.id.filteredSongsList);
        filteredSongsList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        if (files != null && (files.size() > 0)) {

            Log.i("if","not null");
//        }
        Log.i("else","null");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search,menu);
        MenuItem item=menu.findItem(R.id.search_option);
        SearchView searchView= (SearchView) item.getActionView();
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search...");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput=newText.toLowerCase();
        files=new ArrayList<>();
//        int ipos=0;
        for (SongFiles song:songsFiles) {
//            Log.i("song", String.valueOf(song));
            if (song.getTitle().toLowerCase().contains(userInput)){
                files.add(song);
            }
//            ipos++;
        }
//        new SongsAdapter(getActivity(),songsFiles).updateFilteredList(files);
        if(!userInput.isEmpty()) {
            filteredSongsList.setAdapter(new SongsAdapter(getActivity(), files,false));
        }
//        else {
////            Log.i("empty","empty");
//            emptyList=new ArrayList<>();
//            filteredSongsList.setAdapter(new SongsAdapter(getActivity(), emptyList));
//        }
        return true;
    }
}