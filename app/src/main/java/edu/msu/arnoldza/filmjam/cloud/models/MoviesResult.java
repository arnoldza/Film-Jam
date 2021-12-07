package edu.msu.arnoldza.filmjam.cloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Results from getting movies
 */
public class MoviesResult {

    @Expose
    @SerializedName("page")
    private int page;

    @Expose
    @SerializedName("results")
    private ArrayList<Movie> movies;

    @Expose
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public MoviesResult(int page, ArrayList<Movie> movies, int totalPages) {
        this.page = page;
        this.movies = movies;
        this.totalPages = totalPages;
    }

    public MoviesResult() {}
}
