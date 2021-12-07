package edu.msu.arnoldza.filmjam.cloud;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import edu.msu.arnoldza.filmjam.R;
import edu.msu.arnoldza.filmjam.cloud.models.AddResult;
import edu.msu.arnoldza.filmjam.cloud.models.Entry;
import edu.msu.arnoldza.filmjam.cloud.models.Leaderboard;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Cloud class handles HTTPS Calls to personal DB
 */
@SuppressWarnings("deprecation")
public class Cloud {

    /**
     * HTTP Call Base url
     */
    private static final String BASE_URL = "https://webdev.cse.msu.edu/~arnoldza/cse476/filmjam/";

    /**
     * HTTP Call Extended url
     */
    public static final String SHOW_PATH = "leaderboard-show.php";
    public static final String ADD_PATH = "leaderboard-add.php";

    /**
     * Text fields for dialogue box view
     */
    private static final String RANK = "RANK";
    private static final String NAME = "NAME";
    private static final String SCORE = "SCORE";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    /**
     * Leaderboard Adapter static nested class
     */
    public static class LeaderboardAdapter extends BaseAdapter {

        /**
         * The items we display in the list box. Initially this is
         * null until we get items from the server.
         */
        private Leaderboard leaderboard = new Leaderboard("", new ArrayList<>(), "");

        /**
         * Constructor
         */
        public LeaderboardAdapter(final View view) {
            // Create a thread to load the catalog
            new Thread(() -> {
                try {
                    leaderboard = getLeaderboard();

                    if (leaderboard.getStatus().equals("no")) {
                        String msg = "Loading catalog returned status 'no'! Message is = '" + leaderboard.getMessage() + "'";
                        throw new Exception(msg);
                    }
                    if (leaderboard.getEntries().isEmpty()) {
                        String msg = "Leaderboard does not contain any entries.";
                        throw new Exception(msg);
                    }

                    // Tell the adapter the data set has been changed
                    view.post(this::notifyDataSetChanged);

                } catch (Exception e) {
                    // Error condition! Something went wrong
                    Log.e("LeaderboardAdapter", "Something went wrong when loading the leaderboard" + e);
                    view.post(() -> {
                        String string;
                        // make sure that there is a message in the catalog
                        // if there isn't use the message from the exception
                        if (leaderboard.getMessage() == null) {
                            string = e.getMessage();
                        } else {
                            string = leaderboard.getMessage();
                        }
                        Toast.makeText(view.getContext(), string, Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        }

        /**
         * Get leaderboard from cloud
         */
        public Leaderboard getLeaderboard() throws IOException, RuntimeException {
            CloudService service = retrofit.create(CloudService.class);

            Response<Leaderboard> response = service.getEntries().execute();
            // check if request failed
            if (!response.isSuccessful()) {
                Log.e("getLeaderboard", "Failed to get catalog, response code is = " + response.code());
                return new Leaderboard("no", new ArrayList<>(), "Server error " + response.code());
            }
            Leaderboard leaderboard = response.body();
            if (leaderboard.getStatus().equals("no")) {
                String string = "Failed to get catalog, msg is = " + leaderboard.getMessage();
                Log.e("getLeaderboard", string);
                return new Leaderboard("no", new ArrayList<>(), string);
            }
            if (leaderboard.getEntries() == null) {
                leaderboard.setEntries(new ArrayList<>());
            }

            return leaderboard;
        }

        @Override
        public int getCount() {
            return leaderboard.getEntries().size() + 1;
        }

        @Override
        public Entry getItem(int position) {
            return leaderboard.getEntries().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_entry, parent, false);
            }

            TextView rankText = view.findViewById(R.id.rankText);
            TextView nameText = view.findViewById(R.id.nameText);
            TextView scoreText = view.findViewById(R.id.scoreText);

            // Set first row as titles, other rows as entries
            if (position == 0) {
                rankText.setText(RANK);
                nameText.setText(NAME);
                scoreText.setText(SCORE);
            } else {
                rankText.setText(toOrdinal(position));
                nameText.setText(leaderboard.getEntries().get(position - 1).getName());
                scoreText.setText(leaderboard.getEntries().get(position - 1).getScore());
            }

            return view;
        }

        /**
         * Convert integer to ordinal value
         */
        public String toOrdinal(int rank) {
            switch(rank) {
                case 1:
                    return "1st";
                case 2:
                    return "2nd";
                case 3:
                    return "3rd";
                default:
                    return rank + "th";
            }
        }
    }

    /**
     * Add entry to Leaderboard DB
     */
    public boolean addEntryToLeaderboard(final String name, final String score) throws IOException {

        CloudService service = retrofit.create(CloudService.class);

        try {
            Response response = service.addEntry(name, score).execute();
            if (response.isSuccessful()) {
                AddResult result = (AddResult) response.body();
                if (result.getStatus() != null && result.getStatus().equals("yes")) {
                    Log.i("SaveToCloud", "Successful save to cloud!");
                    return true;
                }
                Log.e("SaveToCloud", "Failed to save, message = '" + result.getMessage() + "'");
                return false;
            }
            Log.e("SaveToCloud", "Failed to save, message = '" + response.code() + "'");
            return false;
        } catch (IOException e) {
            Log.e("SaveToCloud", "Exception occurred while trying to save entry!", e);
            return false;
        } catch (RuntimeException e) {
            Log.e("SaveToCloud", "Runtime exception: " + e);
            return false;
        }

    }
}
