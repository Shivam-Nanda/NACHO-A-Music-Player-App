package com.example.nacho;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class SongFiles implements Parcelable {
    private String path;
//    private String albumCoverPath;
    private String title;
    private String artist;
    private String album;
    private String duration;
    private String id;
    private String image;
    private int position;
    private int prevPos;
    private int nextPos;

//    public String getAlbumCoverPath() {
//        return albumCoverPath;
//    }

//    public void setAlbumCoverPath(String albumCoverPath) {
//        this.albumCoverPath = albumCoverPath;
//    }

    protected SongFiles(Parcel in) {
        path = in.readString();
        title = in.readString();
        artist = in.readString();
        album = in.readString();
        duration = in.readString();
        id = in.readString();
    }

    public static final Creator<SongFiles> CREATOR = new Creator<SongFiles>() {
        @Override
        public SongFiles createFromParcel(Parcel in) {
            return new SongFiles(in);
        }

        @Override
        public SongFiles[] newArray(int size) {
            return new SongFiles[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setAlbumId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPrevPos() {
        return prevPos;
    }

    public void setPrevPos(int prevPos) {
        this.prevPos = prevPos;
    }

    public int getNextPos() {
        return nextPos;
    }

    public void setNextPos(int nextPos) {
        this.nextPos = nextPos;
    }

    public SongFiles(String path, String title, String artist, String album, String duration, String id,int position) {
        this.path = path;
//        this.albumCoverPath=albumCoverPath;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.id=id;
        this.position=position;
//        this.prevPos=prevPos;
//        this.nextPos=nextPos;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SongFiles(String title, String image, String artist, int position){
        this.title=title;
        this.image=image;
        this.artist=artist;
        this.position=position;
    }
    public SongFiles(){

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
        parcel.writeString(title);
        parcel.writeString(artist);
        parcel.writeString(album);
        parcel.writeString(duration);
        parcel.writeString(id);
    }
}
