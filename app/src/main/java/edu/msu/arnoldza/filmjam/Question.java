package edu.msu.arnoldza.filmjam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Class represents question in trivia game
 */
public class Question {

    /**
     * Margin for incorrect year answers to differ from correct answer
     */
    private static final int RANDOM_YEAR_MARGIN = 4;

    /**
     * Total number of incorrect answers
     */
    private static final int NUM_INCORRECT_ANSWERS = 3;

    private String question;
    private String answer;
    private ArrayList<String> incorrectAnswers = new ArrayList<>();
    private String posterPath;

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public ArrayList<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Release year question constructor
     */
    public Question(String movieTitle, int year, String posterPath) {
        this.question = "What year did " + movieTitle + " release?";
        this.answer = String.valueOf(year);
        this.posterPath = posterPath;

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
            this.incorrectAnswers.add(String.valueOf(randomYearDifferences.get(i)));
        }
    }
}
