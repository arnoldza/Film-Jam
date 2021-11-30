package edu.msu.arnoldza.filmjam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Class represents question in trivia game
 */
public class Question {

    private String question;
    private String answer;
    private ArrayList<String> incorrectAnswers;
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
     * Constructor
     */
    public Question(String question, String answer,
                    ArrayList<String> incorrectAnswers, String posterPath) {
        this.question = question;
        this.answer = answer;
        this.incorrectAnswers = incorrectAnswers;
        this.posterPath = posterPath;
    }
}
