package edu.msu.arnoldza.filmjam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import edu.msu.arnoldza.filmjam.cloud.Cloud;

/**
 * Activity for user after game is lost
 */
public class LostActivity extends AppCompatActivity {

    // TODO: Temporary final score, eventually needs to be passed from trivia activity
    int finalScore = 700;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        //Dialogue box shows when user has incomplete initials
        AlertDialog.Builder incompleteInitialsDlg = new AlertDialog.Builder(this)
            .setTitle("Incomplete Initials")
            .setMessage("Are you sure you don't want your initials on the leaderboard?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    moveToMainActivity();
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert);

        // Setup initials edit text keyboard hide params
        setKeyboardHideParams();

        // Setup replay button
        // TODO: Add logic to replay using same question set - currently sends back to menu
        Button replayButton = findViewById(R.id.replayButton);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getInitialsEditText().getText().length() == 3) {
                    addEntryToCloudLeaderboard(getInitialsEditText().getText().toString(),
                            String.valueOf(finalScore));
                    moveToMainActivity();
                } else {
                    incompleteInitialsDlg.show();
                }
            }
        });

        // Setup menu button
        Button menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getInitialsEditText().getText().length() == 3) {
                    addEntryToCloudLeaderboard(getInitialsEditText().getText().toString(),
                            String.valueOf(finalScore));
                    moveToMainActivity();
                } else {
                    incompleteInitialsDlg.show();
                }
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

        getInitialsEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_DEL && getInitialsEditText().getText().length() == 0) {
                    // If backspace on empty edit text dismiss keyboard
                    hideSoftKeyboard();
                }
                return false;
            }
        });
    }

    /**
     * Add a score entry to cloud leaderboard
     */
    private void addEntryToCloudLeaderboard(String name, String score) {

        final View view = findViewById(android.R.id.content).getRootView();
        final LostActivity activity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {

                Cloud cloud = new Cloud();
                boolean success;
                try {
                    success = cloud.addEntryToLeaderboard(name, score);
                } catch (IOException e) {
                    e.printStackTrace();
                    success = false;
                }
                if(!success) {
                    /*
                     * If we fail to save, display a toast
                     */
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, R.string.add_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * Moves back to main homepage activity
     */
    private void moveToMainActivity() {
        Intent intent = new Intent(LostActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}