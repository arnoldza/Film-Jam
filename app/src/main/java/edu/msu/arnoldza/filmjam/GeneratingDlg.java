package edu.msu.arnoldza.filmjam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

/**
 * Dialogue box appears as trivia is generated
 */
public class GeneratingDlg extends DialogFragment {

    final private TriviaActivity activity;

    /**
     * Represent the category (and possible subcategory) of the trivia game
     */
    final private String category;
    final private String subCategory;

    /**
     * Constructor
     */
    public GeneratingDlg(TriviaActivity activity, String category, String subCategory) {
        this.activity = activity;
        this.category = category;
        this.subCategory = subCategory;
    }

    /**
     * Set true if we want to cancel
     */
    private volatile boolean cancel = false;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {

        // Don't allow user to tap out of dialogue box
        this.setCancelable(false);

        cancel = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle(R.string.generating);

        builder.setNegativeButton(android.R.string.cancel,
                (dialog, id) -> {
                    cancel = true;
                    activity.finish();
                });


        // Create the dialog box
        final AlertDialog dlg = builder.create();

        // Get reference to activity view
        final View view = this.activity.findViewById(android.R.id.content).getRootView();

        // Create a thread to generate question set
        new Thread(() -> {

            // Get question set
            QuestionSetGenerator questionSetGenerator = new QuestionSetGenerator();
            ArrayList<Question> questionSet = questionSetGenerator.generateQuestionSet(category, subCategory);

            if(cancel) {
                return;
            }

            if(questionSet != null) {
                // if question set generates, create game
                view.post(() -> {
                    activity.createGame(questionSet);
                    dlg.dismiss();
                });
            } else {
                view.post(() -> {
                    // if question set fails to generate send user back to homepage
                    dlg.dismiss();
                    Toast.makeText(view.getContext(),
                            R.string.loading_fail,
                            Toast.LENGTH_SHORT).show();
                    activity.finish();
                });
            }
        }).start();
        return dlg;
    }

    /**
     * Called when the view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancel = true;
    }
}
