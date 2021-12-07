package edu.msu.arnoldza.filmjam;

import static edu.msu.arnoldza.filmjam.Game.CORRECT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Activity for trivia gameplay
 */
public class TriviaActivity extends AppCompatActivity {

    /**
     * Time in milliseconds for response message to be visible
     */
    private static final int MESSAGE_INTERVAL_MILLISECONDS = 2000;

    /**
     * Game currently being played
     */
    private Game game;

    /**
     * Represent the category (and possible subcategory) of the trivia game
     */
    private String category;
    private String subCategory;

    /**
     * String represents current posterPath
     */
    private String poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        // Grab category & subcategory values passed in from MainActivity
        Bundle categories = getIntent().getExtras();
        if (categories != null) {
            // Get category names
            this.category = categories.getString("category");
            this.subCategory = categories.getString("subcategory");
        }

        // Generate trivia question set
        GeneratingDlg generatingDlg = new GeneratingDlg(this, this.category, this.subCategory);
        generatingDlg.show(getSupportFragmentManager(), "generating");

        // Set on click listeners for choice buttons
        getChoiceAButton().setOnClickListener(view -> game.chooseAnswer(getChoiceAButton().getText().toString()));
        getChoiceBButton().setOnClickListener(view -> game.chooseAnswer(getChoiceBButton().getText().toString()));
        getChoiceCButton().setOnClickListener(view -> game.chooseAnswer(getChoiceCButton().getText().toString()));
        getChoiceDButton().setOnClickListener(view -> game.chooseAnswer(getChoiceDButton().getText().toString()));

    }

    public void createGame(ArrayList<Question> questionSet) {
        this.game = new Game(this, questionSet);
    }

    private TextView getQuestionTextView() {
        return findViewById(R.id.questionTextView);
    }

    private ImageView getPosterImageView() {
        return findViewById(R.id.posterImageView);
    }

    /**
     * Get choice A button
     */
    private Button getChoiceAButton() {
        return findViewById(R.id.choiceAButton);
    }

    /**
     * Get choice B button
     */
    private Button getChoiceBButton() {
        return findViewById(R.id.choiceBButton);
    }

    /**
     * Get choice C button
     */
    private Button getChoiceCButton() {
        return findViewById(R.id.choiceCButton);
    }

    /**
     * Get choice D button
     */
    private Button getChoiceDButton() {
        return findViewById(R.id.choiceDButton);
    }

    /**
     * Move into Lost Activity
     */
    public void moveToLostActivity(int score) {
        // Move to lost activity
        Intent intent = new Intent(this, LostActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("category", this.category);
        intent.putExtra("subcategory", this.subCategory);
        startActivity(intent);
        finish();
    }

    /**
     * Update Game Metadata in UI - score, lives left, load message into UI
     */
    public void updateGameDataView(String result, int livesLeft, int score, String question,
                                   String posterPath, String a, String b, String c, String d) {

        ImageView heartOne = findViewById(R.id.heartOneImageView);
        ImageView heartTwo = findViewById(R.id.heartTwoImageView);
        ImageView heartThree = findViewById(R.id.heartThreeImageView);
        ImageView heartFour = findViewById(R.id.heartFourImageView);
        ImageView heartFive = findViewById(R.id.heartFiveImageView);

        // If heart images to null depending on whether or not the heart exists
        switch(livesLeft) {
            case 0:
                heartOne.setImageResource(android.R.color.transparent);
            case 1:
                heartTwo.setImageResource(android.R.color.transparent);
            case 2:
                heartThree.setImageResource(android.R.color.transparent);
            case 3:
                heartFour.setImageResource(android.R.color.transparent);
            case 4:
                heartFive.setImageResource(android.R.color.transparent);
        }

        // Set score text view
        String scoreText = getResources().getString(R.string.score, score);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(scoreText);

        // Set poster image view
        this.poster = posterPath;
        if (posterPath != null) {
            Log.i("PosterPath", posterPath);
            View view = findViewById(android.R.id.content).getRootView();
            new Thread(() -> {
                try {
                    URL url = new URL(posterPath);
                    InputStream is = url.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    view.post(() -> {
                        if (posterPath.equals(this.poster)) {
                            getPosterImageView().setImageBitmap(bitmap);
                        }
                    });
                } catch (Exception e) {
                    Log.e("PosterPath", "Error setting poster: ", e);
                }
            }).start();
        } else {
            getPosterImageView().setImageResource(android.R.color.transparent);
        }

        // Set answer button text
        getChoiceAButton().setText(a);
        getChoiceBButton().setText(b);
        getChoiceCButton().setText(c);
        getChoiceDButton().setText(d);

        if (result.isEmpty()) {
            getQuestionTextView().setText(question);
            getQuestionTextView().setTextColor(getResources().getColor(R.color.black));
            getQuestionTextView().setTypeface(null, Typeface.ITALIC);
        } else {
            // Set response message
            getQuestionTextView().setText(result);
            getQuestionTextView().setTextColor(result.equals(CORRECT) ?
                    getResources().getColor(R.color.correct)
                    : getResources().getColor(R.color.wrong));
            getQuestionTextView().setTypeface(null, Typeface.BOLD);

            // Set new question title after message interval
            Handler handler = new Handler();
            Runnable runnable = () -> {
                getQuestionTextView().setText(question);
                getQuestionTextView().setTextColor(getResources().getColor(R.color.black));
                getQuestionTextView().setTypeface(null, Typeface.ITALIC);
            };

            handler.postAtTime(runnable, System.currentTimeMillis() + MESSAGE_INTERVAL_MILLISECONDS);
            handler.postDelayed(runnable, MESSAGE_INTERVAL_MILLISECONDS);
        }
    }

}
