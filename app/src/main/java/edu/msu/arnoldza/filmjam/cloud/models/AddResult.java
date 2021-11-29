package edu.msu.arnoldza.filmjam.cloud.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Result from adding entry to leaderboard
 */
@Root(name = "leaderboard")
public class AddResult {
    @Attribute
    private String status;

    @Attribute(name = "msg", required = false)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AddResult() {}

    public AddResult(String status, String msg) {
        this.status = status;
        this.message = msg;
    }
}
