package edu.msu.arnoldza.filmjam.cloud;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import edu.msu.arnoldza.filmjam.cloud.models.Genre;
import edu.msu.arnoldza.filmjam.cloud.models.GenresResult;
import edu.msu.arnoldza.filmjam.cloud.models.Movie;
import edu.msu.arnoldza.filmjam.cloud.models.MoviesResult;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * MovieAPI class handles communication with Movie DB API
 */
@SuppressWarnings("deprecation")
public class MovieAPI {

    /**
     * HTTP Get call base url
     */
    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/";

    /**
     * Movie API DB API key
     */
    private static final String MOVIE_API_KEY = "19d6425ce3a789f262fd0716f55753a1";

    /**
     * Extended paths for HTTP GET calls
     */
    public static final String MOVIE_DISCOVER_PATH = "discover/movie";
    public static final String GET_GENRES_PATH = "genre/movie/list";
    public static final String GET_DETAILS_PATH = "movie/{movie_id}";
    public static final String GET_CREDITS_PATH = "movie/{movie_id}/credits";
    public static final String GET_NOW_PLAYING_PATH = "movie/now_playing";
    public static final String GET_POPULAR_PATH = "movie/popular";
    public static final String GET_TOP_RATED_PATH = "movie/top_rated";

    /**
     * Retrofit
     */
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MOVIE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /**
     * Get list of available genres from Movie DB API
     */
    public ArrayList<Genre> getGenres() {

        MovieService api = retrofit.create(MovieService.class);

        try {
            Response response = api.getGenres(MOVIE_API_KEY).execute();
            if (response.isSuccessful()) {
                GenresResult result = (GenresResult) response.body();
                if (!result.getGenres().isEmpty()) {
                    Log.i("GetGenres", "Successful retrieval of genres");
                    return result.getGenres();
                } else {
                    Log.e("GetGenres", "No genres retrieved");
                }
            }

        } catch (IOException e) {
            Log.e("GetGenres", "Exception occurred while trying to get genres!", e);
        } catch (RuntimeException e) {
            Log.e("GetGenres", "Runtime exception: " + e);
        }
        return new ArrayList<>();
    }

    /**
     * Get list of now playing movies from Movie DB API
     */
    public ArrayList<Movie> getNowPlayingMovies() {

        MovieService api = retrofit.create(MovieService.class);

        try {
            Response response = api.getNowPlaying(MOVIE_API_KEY).execute();
            if (response.isSuccessful()) {
                MoviesResult result = (MoviesResult) response.body();
                if (!result.getMovies().isEmpty()) {
                    Log.i("GetNowPlayingMovies", "Successful retrieval of movies");
                    return result.getMovies();
                } else {
                    Log.e("GetNowPlayingMovies", "No movies retrieved");
                }
            }

        } catch (IOException e) {
            Log.e("GetNowPlayingMovies", "Exception occurred while trying to get movies!", e);
        } catch (RuntimeException e) {
            Log.e("GetNowPlayingMovies", "Runtime exception: " + e);
        }
        return new ArrayList<>();
    }
}
