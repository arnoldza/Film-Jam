package edu.msu.arnoldza.filmjam.cloud.models;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents leaderboard from get call
 */
@Root(name = "leaderboard")
public final class Leaderboard {
    @Attribute
    private String status;

    @ElementList(name = "entry", inline = true, required = false, type = Entry.class)
    private List<Entry> entries;

    @Attribute(name = "msg", required = false)
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Leaderboard(String status, ArrayList<Entry> entries, @Nullable String msg) {
        this.status = status;
        this.entries = entries;
        this.message = msg;
    }

    public Leaderboard() {
    }
}
