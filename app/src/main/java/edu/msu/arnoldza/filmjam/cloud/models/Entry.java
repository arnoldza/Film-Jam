package edu.msu.arnoldza.filmjam.cloud.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Class represents entry in leaderboard
 */
@Root(name = "entry")
public final class Entry {

    @Attribute
    private String name;

    @Attribute
    private String score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Entry(String score, String name) {
        this.name = name;
        this.score = score;
    }

    public Entry() {}
}
