package com.example.nacho;

public class MusicLibrary {

    private int image;
    private String name;
    private int tracks;

    public MusicLibrary() {

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public MusicLibrary(int image, String name, int tracks) {
        this.image = image;
        this.name = name;
        this.tracks = tracks;
    }
}
