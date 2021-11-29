package edu.msu.arnoldza.filmjam;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class represents current game state
 */
public class Game {

    /**
     * Values represent choice button positions
     */
    public final static int A = 0;
    public final static int B = 1;
    public final static int C = 2;
    public final static int D = 3;

    /**
     * Current state of game
     */
    enum State {

    }
    private State state;

    /**
     * Score of game currently
     */
    private int score = 0;

    /**
     * Number of lives left in game
     */
    private int livesLeft = 3;

    /**
     * Question set being used in game
     */
    private ArrayList<Question> questionSet;

    /**
     * Index position of current question in question set
     */
    private int questionIndex = 0;

    /**
     * Activity of trivia game
     */
    private TriviaActivity activity;

    public Game(TriviaActivity activity, ArrayList<Question> questionSet) {
        this.activity = activity;
        this.questionSet = questionSet;

        updateUI();
    }

    public Question getCurrentQuestion() {
        return this.questionSet.get(this.questionIndex);
    }

    /**
     * Move to next question
     */
    public void moveToNextQuestion() {
        this.questionIndex++;
        updateUI();
    }

    /**
     * Update User Interface with current question details
     */
    private void updateUI() {

        // Get current question and corresponding answer
        Question currentQuestion = this.getCurrentQuestion();
        String correctAnswer = this.getCurrentQuestion().getAnswer();

        // get question text and path to poster
        String question = currentQuestion.getQuestion();
        String posterPath = currentQuestion.getPosterPath();

        // Get button text values
        ArrayList<String> choicesArray = currentQuestion.getIncorrectAnswers();
        choicesArray.add(correctAnswer);
        Collections.shuffle(choicesArray);

        // Set choices
        String choiceA = choicesArray.get(A);
        String choiceB = choicesArray.get(B);
        String choiceC = choicesArray.get(C);
        String choiceD = choicesArray.get(D);

        // Activity updates UI
        activity.updateUI(this.livesLeft, this.score,
                question, posterPath, choiceA, choiceB, choiceC, choiceD);
    }
}
