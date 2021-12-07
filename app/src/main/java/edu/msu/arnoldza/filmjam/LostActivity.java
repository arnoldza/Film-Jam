package edu.msu.arnoldza.filmjam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import edu.msu.arnoldza.filmjam.cloud.Cloud;

/**
 * Activity for user after game is lost
 */
public class LostActivity extends AppCompatActivity {

    /**
     * Final score from game
     */
    int finalScore;

    /**
     * Represent the category (and possible subcategory) of the trivia game
     */
    private String category;
    private String subCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        // Grab score, category & subcategory values passed in from TriviaActivity
        Bundle categories = getIntent().getExtras();
        if (categories != null) {
            // Get category names
            this.category = categories.getString("category");
            this.subCategory = categories.getString("subcategory");
            this.finalScore = categories.getInt("score");
        }

        // Set final score text
        Resources res = getResources();
        String scoreText = res.getString(R.string.congrats, this.finalScore);
        TextView scoreTextView = findViewById(R.id.congratsTextView);
        scoreTextView.setText(scoreText);

        //Dialogue box shows when user has incomplete initials
        AlertDialog.Builder incompleteInitialsDlg = new AlertDialog.Builder(this)
            .setTitle("Incomplete Initials")
            .setMessage("Are you sure you don't want your initials on the leaderboard?")
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert);

        // Setup initials edit text keyboard hide params
        setKeyboardHideParams();

        // Setup replay button
        Button replayButton = findViewById(R.id.replayButton);
        replayButton.setOnClickListener(view -> {
            if (getInitialsEditText().getText().length() == 3) {
                addEntryToCloudLeaderboard(getInitialsEditText().getText().toString(),
                        String.valueOf(finalScore), true);
            } else {
                // Set positive button on click method to move to trivia activity
                incompleteInitialsDlg.setPositiveButton(R.string.sure, (dialog, which) -> moveToTriviaActivity());
                incompleteInitialsDlg.show();
            }
        });

        // Setup menu button
        Button menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(view -> {
            if (getInitialsEditText().getText().length() == 3) {
                addEntryToCloudLeaderboard(getInitialsEditText().getText().toString(),
                        String.valueOf(finalScore), false);
            } else {
                // Set positive button on click method to move to main activity
                incompleteInitialsDlg.setPositiveButton(R.string.sure, (dialog, which) -> finish());
                incompleteInitialsDlg.show();
            }
        });
    }

    /**
     * Initials edit text
     */
    public EditText getInitialsEditText() {
        return findViewById(R.id.initialsEditText);
    }

    /**
     * Set keyboard hide params for edit text
     */
    private void setKeyboardHideParams() {
        getInitialsEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0 || charSequence.length() == 3) {
                    // If text changed reached full length dismiss keyboard
                    hideSoftKeyboard();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getInitialsEditText().setOnKeyListener((view, keyCode, keyEvent) -> {
            if(keyCode == KeyEvent.KEYCODE_DEL && getInitialsEditText().getText().length() == 0) {
                // If backspace on empty edit text dismiss keyboard
                hideSoftKeyboard();
            }
            return false;
        });
    }

    /**
     * Add a score entry to cloud leaderboard, and move to new activity
     */
    private void addEntryToCloudLeaderboard(String name, String score, final boolean replay) {

        final View view = findViewById(android.R.id.content).getRootView();
        final LostActivity activity = this;

        new Thread(() -> {

            Cloud cloud = new Cloud();
            boolean success;
            try {
                success = cloud.addEntryToLeaderboard(name, score);
            } catch (IOException e) {
                e.printStackTrace();
                success = false;
            }
            if(success) {
                view.post(() -> {
                    if (replay) {
                        moveToTriviaActivity();
                    } else {
                        finish();
                    }
                });
            } else {
                view.post(() -> Toast.makeText(activity, R.string.add_fail, Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /**
     * Move back to trivia activity
     */
    private void moveToTriviaActivity() {
        // Move to trivia activity
        Intent intent = new Intent(this, TriviaActivity.class);
        intent.putExtra("category", this.category);
        intent.putExtra("subcategory", this.subCategory);
        startActivity(intent);
        finish();
    }

    /**
     * Hide soft keyboard
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(
                        this.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    this.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
}