package edu.msu.arnoldza.filmjam.cloud;

import static edu.msu.arnoldza.filmjam.MainActivity.DECADES;
import static edu.msu.arnoldza.filmjam.MainActivity.GENRES;
import static edu.msu.arnoldza.filmjam.MainActivity.NOW_PLAYING;
import static edu.msu.arnoldza.filmjam.MainActivity.POPULAR;
import static edu.msu.arnoldza.filmjam.MainActivity.TOP_RATED;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.msu.arnoldza.filmjam.MainActivity;
import edu.msu.arnoldza.filmjam.cloud.models.Cast;
import edu.msu.arnoldza.filmjam.cloud.models.CreditsResult;
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
    public static final String GET_CREDITS_PATH = "movie/{movie_id}/credits";
    public static final String GET_NOW_PLAYING_PATH = "movie/now_playing";
    public static final String GET_POPULAR_PATH = "movie/popular";
    public static final String GET_TOP_RATED_PATH = "movie/top_rated";

    /**
     * Total number of pages to retrieve via API calls when returning movies
     */
    private static final int TOTAL_NUM_PAGES = 5;

    /**
     * Number of cast members to retrieve from API call
     */
    private static final int TOTAL_SIZE_CAST = 5;

    /**
     * Integer representations of cast member's gender
     */
    private static final int MALE = 2;
    private static final int FEMALE = 1;

    /**
     * Retrofit
     */
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MOVIE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /**
     * Get list of available genres from Movie DB API
     */
    public ArrayList<Genre> getAvailableGenres() {

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
     * Get list of movies from Movie DB API
     */
    public ArrayList<Movie> getMovies(String category, String subcategory) {

        MovieService api = retrofit.create(MovieService.class);
        ArrayList<Movie> movies = new ArrayList<>();

        for (int pageNum = 1; pageNum <= TOTAL_NUM_PAGES; pageNum++) {
            try {
                Response response = makeMoviesAPICall(category, subcategory, api, pageNum);
                if (response.isSuccessful()) {
                    MoviesResult result = (MoviesResult) response.body();
                    if (result.getMovies().isEmpty()) {
                        break;
                    }
                    movies.addAll(result.getMovies());
                }

            } catch (IOException e) {
                Log.e("GetMovies", "Exception occurred while trying to get movies!", e);
                return new ArrayList<>();
            } catch (RuntimeException e) {
                Log.e("GetMovies", "Runtime exception: " + e);
                return new ArrayList<>();
            }
        }
        if (movies.isEmpty()) {
            Log.e("GetMovies", "No movies retrieved");
        } else {
            Log.i("GetMovies", "Successful retrieval of movies");
        }
        return movies;
    }

    /**
     * Make API call given category, helper function for get movies
     */
    private Response makeMoviesAPICall(String category, String subcategory, MovieService api, int pageNum) throws IOException {
        switch(category) {
            case POPULAR:
                return api.getPopular(MOVIE_API_KEY, String.valueOf(pageNum)).execute();
            case TOP_RATED:
                return api.getTopRated(MOVIE_API_KEY, String.valueOf(pageNum)).execute();
            case NOW_PLAYING:
                return api.getNowPlaying(MOVIE_API_KEY, String.valueOf(pageNum)).execute();
            case GENRES:
                // Get genre id
                int genreId = MainActivity.genreIds.get(subcategory);

                return api.getByGenre(MOVIE_API_KEY, String.valueOf(genreId), String.valueOf(pageNum)).execute();
            case DECADES:
                // Get decade value
                int decade = MainActivity.decadeValues.get(subcategory);

                // Get lower and upper bounds for movie release dates
                String releaseDateGTE = decade + "-01-01";
                String releaseDateLTE = (decade + 9) + "-12-31";

                return api.getByDecade(MOVIE_API_KEY, releaseDateGTE, releaseDateLTE, String.valueOf(pageNum)).execute();
        }
        return null;
    }

    /**
     * Get Cast of movie given Id
     */
    public ArrayList<Cast> getMovieCast(int movieId) {

        MovieService api = retrofit.create(MovieService.class);

        try {
            Response response = api.getCredits(String.valueOf(movieId), MOVIE_API_KEY).execute();
            if (response.isSuccessful()) {
                CreditsResult result = (CreditsResult) response.body();
                if (!result.getCast().isEmpty()) {
                    Log.i("GetMovieCast", "Successful retrieval of cast");
                    ArrayList<Cast> movieCastMale = new ArrayList<>();
                    ArrayList<Cast> movieCastFemale = new ArrayList<>();

                    Set<String> actors = new HashSet<>();
                    Set<String> characters = new HashSet<>();

                    // Get the first 5 entries with no blank entries, no duplicates
                    for(Cast member : result.getCast()) {
                        if (!member.getName().isEmpty() && !member.getCharacter().isEmpty()
                        && !actors.contains(member.getName()) && !characters.contains(member.getCharacter())) {

                            // Add results to sets to prevent duplicates
                            actors.add(member.getName());
                            characters.add(member.getCharacter());

                            // Add cast member
                            if(member.getGender() == MALE) {
                                movieCastMale.add(member);
                                if (movieCastMale.size() == TOTAL_SIZE_CAST) {
                                    return movieCastMale;
                                }
                            } else if(member.getGender() == FEMALE) {
                                movieCastFemale.add(member);
                                if (movieCastFemale.size() == TOTAL_SIZE_CAST) {
                                    return movieCastFemale;
                                }
                            }
                        }
                    }
                } else {
                    Log.e("GetMovieCast", "No cast returned");
                }
            }

        } catch (IOException e) {
            Log.e("GetMovieCase", "Exception occurred while trying to get cast!", e);
        } catch (RuntimeException e) {
            Log.e("GetMovieCast", "Runtime exception: " + e);
        }
        return new ArrayList<>();
    }
}
