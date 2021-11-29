package edu.msu.arnoldza.filmjam;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
            imageView.setImageBitmap(result);
        }
    }


    /**
     * Question set generator class
     */
    private final QuestionSetGenerator questionSetGenerator = new QuestionSetGenerator();

    /**
     * Game currently being played
     */
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        // Grab category & subcategory values passed in from MainActivity
        String category = "";
        String subcategory = "";
        Bundle categories = getIntent().getExtras();
        if (categories != null) {
            // Get category names
            category = categories.getString("category");
            subcategory = categories.getString("subcategory");
        }

        // Generate question set for game
        ArrayList<Question> questionSet = this.questionSetGenerator.generateQuestionSet(category, subcategory);

        // Create game
        this.game = new Game(this, questionSet);

        // Set on click listeners for choice buttons
        getChoiceAButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getChoiceAButton().getText().toString().equals(game.getCurrentQuestion().getAnswer())) {
                    game.moveToNextQuestion();
                }
            }
        });

        getChoiceBButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getChoiceBButton().getText().toString().equals(game.getCurrentQuestion().getAnswer())) {
                    game.moveToNextQuestion();
                }
            }
        });

        getChoiceCButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getChoiceCButton().getText().toString().equals(game.getCurrentQuestion().getAnswer())) {
                    game.moveToNextQuestion();
                }
            }
        });

        getChoiceDButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getChoiceDButton().getText().toString().equals(game.getCurrentQuestion().getAnswer())) {
                    game.moveToNextQuestion();
                }
            }
        });

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
     * Update UI of the game
     */
    public void updateUI(int livesLeft, int score, String question, String posterPath, String a, String b, String c, String d) {

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
        Resources res = getResources();
        String scoreText = res.getString(R.string.score, score);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(scoreText);

        // Set question text view
        TextView questionTextView = findViewById(R.id.questionTextView);
        questionTextView.setText(question);

        // Set poster image view
        ImageView posterImageView = findViewById(R.id.posterImageView);
        new DownLoadImageTask(posterImageView).execute(posterPath);

        // Set choice buttons text
        getChoiceAButton().setText(a);
        getChoiceBButton().setText(b);
        getChoiceCButton().setText(c);
        getChoiceDButton().setText(d);
    }

}
