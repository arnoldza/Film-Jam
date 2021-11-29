package edu.msu.arnoldza.filmjam.cloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Results from getting available genres
 */
public class GenresResult {

    @Expose
    @SerializedName("genres")
    private ArrayList<Genre> genres;

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public GenresResult(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public GenresResult() {}
}
