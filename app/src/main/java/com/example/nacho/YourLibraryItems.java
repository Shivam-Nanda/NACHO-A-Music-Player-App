package com.example.nacho;

import java.util.ArrayList;

public class YourLibraryItems {
    String name;
    String description;
    int image;
    ArrayList<SongFiles> myfiles;
    int position;
    int id;
    int tracks;

    public YourLibraryItems(String name, String description, int image, ArrayList<SongFiles> myfiles,int position) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.myfiles = myfiles;
        this.position=position;
    }

    public YourLibraryItems(String name,int image,int tracks,int position) {
        this.name = name;
        this.image=image;
        this.tracks=tracks;
        this.position=position;
    }

    public YourLibraryItems(String name, String description, int image, ArrayList<SongFiles> myfiles, int position,  int tracks) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.myfiles = myfiles;
        this.position = position;
        this.tracks = tracks;
    }

    public YourLibraryItems(String name, String description, int image,int tracks, ArrayList<SongFiles> myfiles) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.tracks=tracks;
        this.myfiles = myfiles;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public YourLibraryItems(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public ArrayList<SongFiles> getMyfiles() {
        return myfiles;
    }

    public void setMyfiles(ArrayList<SongFiles> myfiles) {
        this.myfiles = myfiles;
    }
}
