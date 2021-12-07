package edu.msu.arnoldza.filmjam.cloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class represents cast member
 */
public class Cast {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("character")
    private String character;

    @Expose
    @SerializedName("gender")
    private int gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Cast(int id, String name, String character, int gender) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.gender = gender;
    }

    public Cast() {}
}
