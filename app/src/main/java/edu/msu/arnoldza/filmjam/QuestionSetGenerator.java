package edu.msu.arnoldza.filmjam;

import static edu.msu.arnoldza.filmjam.MainActivity.NOW_PLAYING;

import java.util.ArrayList;
import java.util.Collections;

import edu.msu.arnoldza.filmjam.cloud.MovieAPI;
import edu.msu.arnoldza.filmjam.cloud.models.Movie;

/**
 * Helper class generates question set for game
 */
public class QuestionSetGenerator {

    /**
     * Base path for building poster URL
     */
    private static final String POSTER_BASE_PATH = "https://image.tmdb.org/t/p/original";

    public ArrayList<Question> generateQuestionSet(String category, String subcategory) {

        if (category.equals(NOW_PLAYING)) {
            return generateNowPlayingSet();
        }
        return null;
    }

    private ArrayList<Question> generateNowPlayingSet() {

        // Get movies
        MovieAPI movieAPI = new MovieAPI();

        // Set of questions
        final ArrayList<Question> questionSet = new ArrayList<>();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Movie> movies = movieAPI.getNowPlayingMovies();

                // TODO: Remove, release year questions make no sense for "Now Playing" movies
                addReleaseYearQuestions(questionSet, movies);
            }
        });

        // Start thread and wait for its completion
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return set of questions
        Collections.shuffle(questionSet);
        return questionSet;
    }

    /**
     * Add release year questions to question set
     */
    private void addReleaseYearQuestions(ArrayList<Question> questionSet, ArrayList<Movie> movies) {
        // For each movie generate date question
        for (Movie movie : movies) {

            // Get release year and generate question
            int year = Integer.parseInt(movie.getReleaseDate().substring(0, 4));
            String title = movie.getTitle();
            String posterPath = POSTER_BASE_PATH + movie.getPosterPath();
            questionSet.add(new Question(title, year, posterPath));
        }
    }
}
