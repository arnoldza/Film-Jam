package edu.msu.arnoldza.filmjam;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import edu.msu.arnoldza.filmjam.cloud.Cloud;

/**
 * Leaderboard Dialogue box
 */
public class LeaderboardDlg extends DialogFragment {

    /**
     * Create the dialog box
     * @param savedInstanceState The saved instance bundle
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle(R.string.leaderboard);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Pass null as the parent view because its going in the dialog layout
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.leaderboard_dlg, null);

        // Find the list view
        ListView list = view.findViewById(R.id.showLeaderboard);
        final Cloud.LeaderboardAdapter adapter = new Cloud.LeaderboardAdapter(list);

        builder.setView(view)
            // Add a cancel button
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // Cancel just closes the dialog box
                }
            });


        final AlertDialog dlg = builder.create();

        // Create an adapter
        list.setAdapter(adapter);

        return dlg;
    }
}
