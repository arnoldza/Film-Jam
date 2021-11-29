package edu.msu.arnoldza.filmjam.cloud;

import static edu.msu.arnoldza.filmjam.cloud.Cloud.ADD_PATH;
import static edu.msu.arnoldza.filmjam.cloud.Cloud.SHOW_PATH;

import edu.msu.arnoldza.filmjam.cloud.models.AddResult;
import edu.msu.arnoldza.filmjam.cloud.models.Leaderboard;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Service for making HTTP GET calls to Personal Cloud DB
 */
public interface CloudService {

    @GET(SHOW_PATH)
    Call<Leaderboard> getEntries();

    @GET(ADD_PATH)
    Call<AddResult> addEntry(
            @Query("name") String name,
            @Query("score") String score
    );

}
