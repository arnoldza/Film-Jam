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
     * Result strings for UI
     */
    public final static String CORRECT = "Correct!";
    public final static String WRONG = "Wrong! It's ";

    /**
     * Score of game currently
     */
    private int score = 0;

    /**
     * Number of lives left in game
     */
    private int livesLeft = 5;

    /**
     * Question set being used in game
     */
    private final ArrayList<Question> questionSet;

    /**
     * Index position of current question in question set
     */
    private int questionIndex = 0;

    /**
     * Activity of trivia game
     */
    private final TriviaActivity activity;

    public Game(TriviaActivity activity, ArrayList<Question> questionSet) {
        this.activity = activity;
        this.questionSet = questionSet;

        updateUI("");
    }

    /**
     * Get current question
     */
    public Question getCurrentQuestion() {
        return this.questionSet.get(this.questionIndex);
    }

    /**
     * Upon selecting choice in trivia
     */
    public void chooseAnswer(String answer) {

        // If answer is correct
        if (getCurrentQuestion().getAnswer().equals(answer)) {
            // Increase score
            int exponent = questionIndex / 5;
            this.score += 20 * Math.pow(2, (exponent));
            this.questionIndex++;
            this.updateUI(CORRECT);
        } else {
            // Remove life
            this.livesLeft--;
            if (this.livesLeft == 0) {
                this.activity.moveToLostActivity(this.score);
            }
            String correctAnswer = getCurrentQuestion().getAnswer();
            this.questionIndex++;
            this.updateUI(WRONG + correctAnswer);
        }
    }

    /**
     * Update User Interface with current question details
     */
    private void updateUI(String result) {

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
        activity.updateGameDataView(result, this.livesLeft, this.score,
                question, posterPath, choiceA, choiceB, choiceC, choiceD);
    }
}
