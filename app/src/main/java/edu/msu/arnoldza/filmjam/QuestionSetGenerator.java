package edu.msu.arnoldza.filmjam;

import static edu.msu.arnoldza.filmjam.MainActivity.DECADES;
import static edu.msu.arnoldza.filmjam.MainActivity.GENRES;
import static edu.msu.arnoldza.filmjam.MainActivity.NOW_PLAYING;
import static edu.msu.arnoldza.filmjam.MainActivity.POPULAR;
import static edu.msu.arnoldza.filmjam.MainActivity.TOP_RATED;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.msu.arnoldza.filmjam.cloud.MovieAPI;
import edu.msu.arnoldza.filmjam.cloud.models.Cast;
import edu.msu.arnoldza.filmjam.cloud.models.Movie;

/**
 * Helper class generates question set for game
 */
public class QuestionSetGenerator {

    /**
     * Margin for incorrect year answers to differ from correct answer
     */
    private static final int RANDOM_YEAR_MARGIN = 4;

    /**
     * Total number of incorrect answers
     */
    private static final int NUM_INCORRECT_ANSWERS = 3;

    /**
     * Base path for building poster URL
     */
    private static final String POSTER_BASE_PATH = "https://image.tmdb.org/t/p/original";

    /**
     * Generate question set for game
     */
    public ArrayList<Question> generateQuestionSet(String category, String subcategory) {

        switch(category) {
            case POPULAR:
                return generatePopularSet();
            case TOP_RATED:
                return generateTopRatedSet();
            case NOW_PLAYING:
                return generateNowPlayingSet();
            case GENRES:
                return generateGenreSet(MainActivity.genreIds.get(subcategory));
            case DECADES:
                return generateDecadeSet(MainActivity.decadeValues.get(subcategory));
        }
        return null;
    }

    private ArrayList<Question> generatePopularSet() {

        // Get movies
        MovieAPI movieAPI = new MovieAPI();

        // Set of questions
        final ArrayList<Question> questionSet = new ArrayList<>();

        Thread t = new Thread(() -> {
            ArrayList<Movie> movies = movieAPI.getPopularMovies();

            addReleaseYearQuestions(questionSet, movies);
            addActorQuestions(questionSet, movies);
            addOverviewQuestions(questionSet, movies);
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
        Log.i("QuestionSet", "Successfully generated random question set of length" + questionSet.size());
        return questionSet;
    }

    private ArrayList<Question> generateTopRatedSet() {
        // Get movies
        MovieAPI movieAPI = new MovieAPI();

        // Set of questions
        final ArrayList<Question> questionSet = new ArrayList<>();

        Thread t = new Thread(() -> {
            ArrayList<Movie> movies = movieAPI.getTopRatedMovies();

            addReleaseYearQuestions(questionSet, movies);
            addActorQuestions(questionSet, movies);
            addOverviewQuestions(questionSet, movies);
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
        Log.i("QuestionSet", "Successfully generated random question set of length" + questionSet.size());
        return questionSet;
    }

    private ArrayList<Question> generateNowPlayingSet() {

        // Get movies
        MovieAPI movieAPI = new MovieAPI();

        // Set of questions
        final ArrayList<Question> questionSet = new ArrayList<>();

        Thread t = new Thread(() -> {
            ArrayList<Movie> movies = movieAPI.getNowPlayingMovies();

            addActorQuestions(questionSet, movies);
            addOverviewQuestions(questionSet, movies);
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
        Log.i("QuestionSet", "Successfully generated random question set of length" + questionSet.size());
        return questionSet;
    }

    /**
     * Generate question set based off specific genre
     */
    private ArrayList<Question> generateGenreSet(int genreId) {

        // Get movies
        MovieAPI movieAPI = new MovieAPI();

        // Set of questions
        final ArrayList<Question> questionSet = new ArrayList<>();

        Thread t = new Thread(() -> {
            ArrayList<Movie> movies = movieAPI.getMoviesByGenre(genreId);

            addReleaseYearQuestions(questionSet, movies);
            addActorQuestions(questionSet, movies);
            addOverviewQuestions(questionSet, movies);
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
        Log.i("QuestionSet", "Successfully generated random question set of length" + questionSet.size());
        return questionSet;
    }

    /**
     * Generate question set based off specific decade
     */
    private ArrayList<Question> generateDecadeSet(int decade) {

        // Get movies
        MovieAPI movieAPI = new MovieAPI();

        // Set of questions
        final ArrayList<Question> questionSet = new ArrayList<>();

        Thread t = new Thread(() -> {
            ArrayList<Movie> movies = movieAPI.getMoviesByDecade(decade);

            addReleaseYearQuestions(questionSet, movies);
            addActorQuestions(questionSet, movies);
            addOverviewQuestions(questionSet, movies);
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
        Log.i("QuestionSet", "Successfully generated random question set of length " + questionSet.size());
        return questionSet;
    }

    /**
     * Add randomly generated release year questions to question set
     */
    private void addReleaseYearQuestions(ArrayList<Question> questionSet, ArrayList<Movie> movies) {
        // For each movie generate date question
        for (Movie movie : movies) {

            // Get release year and generate question
            int year = Integer.parseInt(movie.getReleaseDate().substring(0, 4));

            // Question parameters
            String posterPath = POSTER_BASE_PATH + movie.getPosterPath();
            String question = "What year did " + movie.getTitle() + " release?";
            String answer = String.valueOf(year);
            ArrayList<String> incorrectAnswers = new ArrayList<>();

            // Get current year so answers can't be future years
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            // Get list of close years to answer
            ArrayList<Integer> randomYearDifferences = new ArrayList<>();
            for(int i = 1; i <= RANDOM_YEAR_MARGIN; i++) {
                // Add year + i and year - i
                if (year + i <= currentYear) {
                    // Don't add future years
                    randomYearDifferences.add(year + i);
                }
                randomYearDifferences.add(year - i);
            }

            // Shuffle possible years
            Collections.shuffle(randomYearDifferences);

            for (int i = 0; i < NUM_INCORRECT_ANSWERS; i++) {
                incorrectAnswers.add(String.valueOf(randomYearDifferences.get(i)));
            }

            questionSet.add(new Question(question, answer, incorrectAnswers, posterPath));
        }
    }

    /**
     * Adds randomly generated actor / character pairing questions to question set
     */
    private void addActorQuestions(ArrayList<Question> questionSet, ArrayList<Movie> movies) {

        MovieAPI movieAPI = new MovieAPI();
        Random random = new Random();

        for (Movie movie : movies) {

            // Create list of pairs of actors and characters they play
            ArrayList<Cast> movieCast = movieAPI.getMovieCast(movie.getId());

            // Only generate questions for movies with at least 4 entries
            if (movieCast.size() >= 4) {

                List<Cast> randomCast = movieCast;
                for (Cast member : movieCast) {

                    Collections.shuffle(randomCast);
                    String posterPath = POSTER_BASE_PATH + movie.getPosterPath();

                    // Randomly decide which version of the actor / character question to ask for this pairing
                    if (random.nextBoolean()) {

                        String question = "In " + movie.getTitle() + ", which actor played " + member.getCharacter() + "?";
                        String answer = member.getName();
                        ArrayList<String> incorrectAnswers = new ArrayList<>();

                        // Add three random actors
                        int i = 0;
                        while (incorrectAnswers.size() < NUM_INCORRECT_ANSWERS) {
                            String incorrectActor = randomCast.get(i).getName();
                            if (!incorrectActor.equals(answer)) {
                                incorrectAnswers.add(incorrectActor);
                            }
                            i++;
                        }
                        questionSet.add(new Question(question, answer, incorrectAnswers, posterPath));

                    } else {

                        String question = "In " + movie.getTitle() + ", which character is played by " + member.getName() + "?";
                        String answer = member.getCharacter();
                        ArrayList<String> incorrectAnswers = new ArrayList<>();

                        // Add three random characters
                        int i = 0;
                        while (incorrectAnswers.size() < NUM_INCORRECT_ANSWERS) {
                            String incorrectCharacter = randomCast.get(i).getCharacter();
                            if (!incorrectCharacter.equals(answer)) {
                                incorrectAnswers.add(incorrectCharacter);
                            }
                            i++;
                        }
                        questionSet.add(new Question(question, answer, incorrectAnswers, posterPath));
                    }
                }
            }
        }
    }

    /**
     * Adds randomly generated overview matching questions to question set
     */
    private void addOverviewQuestions(ArrayList<Question> questionSet, ArrayList<Movie> movies) {

        ArrayList<Movie> randomMovies = movies;
        for (Movie movie : movies) {
            String overview = movie.getOverview();

            if(!overview.isEmpty()) {
                Collections.shuffle(randomMovies);

                // Question parameters
                String question = overview;
                String answer = movie.getTitle();
                ArrayList<String> incorrectAnswers = new ArrayList<>();

                int i = 0;
                while (incorrectAnswers.size() < NUM_INCORRECT_ANSWERS) {
                    String incorrectTitle = randomMovies.get(i).getTitle();
                    if(!incorrectTitle.equals(answer)) {
                        incorrectAnswers.add(incorrectTitle);
                    }
                    i++;
                }

                questionSet.add(new Question(question, answer, incorrectAnswers, null));

            }

        }
    }


}
