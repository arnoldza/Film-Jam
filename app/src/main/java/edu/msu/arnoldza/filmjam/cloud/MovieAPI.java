package edu.msu.arnoldza.filmjam.cloud;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

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
     * Retrofit
     */
    private static Retrofit retrofit = new Retrofit.Builder()
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
     * Get list of popular movies from Movie DB API
     */
    public ArrayList<Movie> getPopularMovies() {

        MovieService api = retrofit.create(MovieService.class);

        try {
            Response response = api.getPopular(MOVIE_API_KEY).execute();
            if (response.isSuccessful()) {
                MoviesResult result = (MoviesResult) response.body();
                if (!result.getMovies().isEmpty()) {
                    Log.i("GetPopularMovies", "Successful retrieval of movies");
                    return result.getMovies();
                } else {
                    Log.e("GetPopularMovies", "No movies retrieved");
                }
            }

        } catch (IOException e) {
            Log.e("GetPopularMovies", "Exception occurred while trying to get movies!", e);
        } catch (RuntimeException e) {
            Log.e("GetPopularMovies", "Runtime exception: " + e);
        }
        return new ArrayList<>();
    }

    /**
     * Get list of top rated movies from Movie DB API
     */
    public ArrayList<Movie> getTopRatedMovies() {

        MovieService api = retrofit.create(MovieService.class);

        try {
            Response response = api.getTopRated(MOVIE_API_KEY).execute();
            if (response.isSuccessful()) {
                MoviesResult result = (MoviesResult) response.body();
                if (!result.getMovies().isEmpty()) {
                    Log.i("GetTopRatedMovies", "Successful retrieval of movies");
                    return result.getMovies();
                } else {
                    Log.e("GetTopRatedMovies", "No movies retrieved");
                }
            }

        } catch (IOException e) {
            Log.e("GetTopRatedMovies", "Exception occurred while trying to get movies!", e);
        } catch (RuntimeException e) {
            Log.e("GetTopRatedMovies", "Runtime exception: " + e);
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

    /**
     * Get list of movies from Movie DB API by genre
     */
    public ArrayList<Movie> getMoviesByGenre(int genreId) {

        MovieService api = retrofit.create(MovieService.class);

        try {
            Response response = api.getByGenre(MOVIE_API_KEY, String.valueOf(genreId)).execute();
            if (response.isSuccessful()) {
                MoviesResult result = (MoviesResult) response.body();
                if (!result.getMovies().isEmpty()) {
                    Log.i("GetMoviesByGenre", "Successful retrieval of movies");
                    return result.getMovies();
                } else {
                    Log.e("GetMoviesByGenre", "No movies retrieved");
                }
            }

        } catch (IOException e) {
            Log.e("GetMoviesByGenre", "Exception occurred while trying to get movies!", e);
        } catch (RuntimeException e) {
            Log.e("GetMoviesByGenre", "Runtime exception: " + e);
        }
        return new ArrayList<>();
    }

    /**
     * Get list of movies from Movie DB API by decade
     */
    public ArrayList<Movie> getMoviesByDecade(int decade) {

        MovieService api = retrofit.create(MovieService.class);

        // Get lower and upper bounds for movie release dates
        String releaseDateGTE = decade + "-01-01";
        String releaseDateLTE = (decade + 9) + "-12-31";

        try {
            Response response = api.getByDecade(MOVIE_API_KEY, releaseDateGTE, releaseDateLTE).execute();
            if (response.isSuccessful()) {
                MoviesResult result = (MoviesResult) response.body();
                if (!result.getMovies().isEmpty()) {
                    Log.i("GetMoviesByDecade", "Successful retrieval of movies");
                    return result.getMovies();
                } else {
                    Log.e("GetMoviesByDecade", "No movies retrieved");
                }
            }

        } catch (IOException e) {
            Log.e("GetMoviesByDecade", "Exception occurred while trying to get movies!", e);
        } catch (RuntimeException e) {
            Log.e("GetMoviesByDecade", "Runtime exception: " + e);
        }
        return new ArrayList<>();
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
                    ArrayList<Cast> movieCast = new ArrayList<>();

                    // Get the first 5 entries with no blank entries
                    for(Cast member : result.getCast()) {
                        if (!member.getName().isEmpty() && !member.getCharacter().isEmpty()) {
                            movieCast.add(member);
                            if (movieCast.size() == 5) {
                                break;
                            }
                        }
                    }
                    return movieCast;
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
