package edu.msu.arnoldza.filmjam.cloud;

import static edu.msu.arnoldza.filmjam.cloud.MovieAPI.MOVIE_DISCOVER_PATH;
import static edu.msu.arnoldza.filmjam.cloud.MovieAPI.GET_GENRES_PATH;
import static edu.msu.arnoldza.filmjam.cloud.MovieAPI.GET_CREDITS_PATH;
import static edu.msu.arnoldza.filmjam.cloud.MovieAPI.GET_NOW_PLAYING_PATH;
import static edu.msu.arnoldza.filmjam.cloud.MovieAPI.GET_POPULAR_PATH;
import static edu.msu.arnoldza.filmjam.cloud.MovieAPI.GET_TOP_RATED_PATH;

import edu.msu.arnoldza.filmjam.cloud.models.CreditsResult;
import edu.msu.arnoldza.filmjam.cloud.models.GenresResult;
import edu.msu.arnoldza.filmjam.cloud.models.MoviesResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Service for making HTTP GET calls to Movie DB API
 */
public interface MovieService {

    @GET(MOVIE_DISCOVER_PATH)
    Call<MoviesResult> getByGenre(
            @Query("api_key") String apiKey,
            @Query("with_genres") String withGenres,
            @Query("page") String page
    );

    @GET(MOVIE_DISCOVER_PATH)
    Call<MoviesResult> getByDecade(
            @Query("api_key") String apiKey,
            @Query("primary_release_date.gte") String releaseDateGTE,
            @Query("primary_release_date.lte") String releaseDateLTE,
            @Query("page") String page
    );

    @GET(GET_GENRES_PATH)
    Call<GenresResult> getGenres(
            @Query("api_key") String apiKey
    );

    @GET(GET_CREDITS_PATH)
    Call<CreditsResult> getCredits(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );

    @GET(GET_NOW_PLAYING_PATH)
    Call<MoviesResult> getNowPlaying(
            @Query("api_key") String apiKey,
            @Query("page") String page
    );

    @GET(GET_POPULAR_PATH)
    Call<MoviesResult> getPopular(
            @Query("api_key") String apiKey,
            @Query("page") String page
    );

    @GET(GET_TOP_RATED_PATH)
    Call<MoviesResult> getTopRated(
            @Query("api_key") String apiKey,
            @Query("page") String page
    );

}
