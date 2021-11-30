package edu.msu.arnoldza.filmjam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
     * Nested helper class downloads poster image for use in poster image view
     */
    private static class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }


        /**
         * Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();

                // Decode an input stream into a bitmap.
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /**
         * Runs on the UI thread after doInBackground(Params...)
         */
        protected void onPostExecute(Bitmap result){
            // Set Question along with image load
            this.imageView.setImageBitmap(result);
        }
    }

    /**
     * Time in milliseconds for response message to be visible
     */
    private static final int MESSAGE_INTERVAL_MILLISECONDS = 1000;

    /**
     * Question set generator class
     */
    private final QuestionSetGenerator questionSetGenerator = new QuestionSetGenerator();

    /**
     * Game currently being played
     */
    private Game game;

    /**
     * Represent the category (and possible subcategory) of the trivia game
     */
    private String category;
    private String subCategory;

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

        // Generate question set for game
        ArrayList<Question> questionSet = this.questionSetGenerator.generateQuestionSet(this.category, this.subCategory);

        // Create game
        this.game = new Game(this, questionSet);

        // Set on click listeners for choice buttons
        getChoiceAButton().setOnClickListener(view -> game.chooseAnswer(getChoiceAButton().getText().toString()));
        getChoiceBButton().setOnClickListener(view -> game.chooseAnswer(getChoiceBButton().getText().toString()));
        getChoiceCButton().setOnClickListener(view -> game.chooseAnswer(getChoiceCButton().getText().toString()));
        getChoiceDButton().setOnClickListener(view -> game.chooseAnswer(getChoiceDButton().getText().toString()));

    }

    private TextView getQuestionTextView() {
        return findViewById(R.id.questionTextView);
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
    public void updateGameDataView(Game.State gameState, int livesLeft, int score, String question,
                                   String posterPath, String a, String b, String c, String d) {

        ImageView heartOne = findViewById(R.id.heartOneImageView);
        ImageView heartTwo = findViewById(R.id.heartTwoImageView);
        ImageView heartThree = findViewById(R.id.heartThreeImageView);

        // If heart images to null depending on whether or not the heart exists
        switch(livesLeft) {
            case 0:
                heartOne.setImageDrawable(null);
            case 1:
                heartTwo.setImageDrawable(null);
            case 2:
                heartThree.setImageDrawable(null);
        }

        // Set score text view
        String scoreText = getResources().getString(R.string.score, score);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(scoreText);

        // Set poster image view
        new DownLoadImageTask(findViewById(R.id.posterImageView)).execute(posterPath);

        // Set answer button text
        getChoiceAButton().setText(a);
        getChoiceBButton().setText(b);
        getChoiceCButton().setText(c);
        getChoiceDButton().setText(d);

        if (gameState == Game.State.INIT) {
            getQuestionTextView().setText(question);
            getQuestionTextView().setTextColor(getResources().getColor(R.color.black));
            getQuestionTextView().setTypeface(null, Typeface.ITALIC);
        } else {
            // Set response message
            getQuestionTextView().setText(gameState == Game.State.CORRECT ? R.string.correct : R.string.wrong);
            getQuestionTextView().setTextColor(gameState == Game.State.CORRECT ? getResources().getColor(R.color.correct)
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
