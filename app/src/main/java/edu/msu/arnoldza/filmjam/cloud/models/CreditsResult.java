package edu.msu.arnoldza.filmjam.cloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Result from getting movie's credits
 */
public class CreditsResult {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("cast")
    private ArrayList<Cast> cast;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Cast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Cast> cast) {
        this.cast = cast;
    }

    public CreditsResult(int id, ArrayList<Cast> cast) {
        this.id = id;
        this.cast = cast;
    }

    public CreditsResult() {}
}
