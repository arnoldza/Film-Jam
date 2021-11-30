package edu.msu.arnoldza.filmjam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.msu.arnoldza.filmjam.cloud.MovieAPI;
import edu.msu.arnoldza.filmjam.cloud.models.Genre;

/**
 * Main homepage activity
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Options for category spinner
     */
    public static final String POPULAR = "Popular";
    public static final String TOP_RATED = "Top Rated";
    public static final String NOW_PLAYING = "Now Playing";
    public static final String GENRES = "Genres";
    public static final String DECADES = "Decades";

    /**
     * Options for decades subcategory spinner
     */
    public static final String _1970s = "1970s";
    public static final String _1980s = "1980s";
    public static final String _1990s = "1990s";
    public static final String _2000s = "2000s";
    public static final String _2010s = "2010s";

    /**
     * Represent the category (and possible subcategory) of the trivia game
     */
    private String category;
    private String subCategory;

    /**
     * Subcategory Spinner Adapters
     */
    private ArrayAdapter<String> genreAdapter;
    private ArrayAdapter<String> decadesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup leaderboard button
        Button leaderboardButton = findViewById(R.id.leaderboardButton);
        leaderboardButton.setOnClickListener((View view) -> {
            LeaderboardDlg dlg = new LeaderboardDlg();
            Log.i("LeaderboardDlg", "creating new dlg box");
            dlg.show(getSupportFragmentManager(), "leaderboard");
        });

        // Setup spinners
        setupCategorySpinner();
        setupSubCategorySpinner();

        // Setup start game button
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener((View view) -> {
            // TODO: Remove this if statement - app currently crashes otherwise
            if (this.category.equals(NOW_PLAYING)) {
                // Move to trivia activity
                Intent intent = new Intent(this, TriviaActivity.class);
                intent.putExtra("category", this.category);
                intent.putExtra("subcategory", this.subCategory);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Setup category spinner
     */
    private void setupCategorySpinner() {
        // Populate spinner with decades
        ArrayList<String> spinnerCategories = new ArrayList<>();
        spinnerCategories.add(POPULAR);
        spinnerCategories.add(TOP_RATED);
        spinnerCategories.add(NOW_PLAYING);
        spinnerCategories.add(GENRES);
        spinnerCategories.add(DECADES);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter now, will never change
        getCategorySpinner().setAdapter(categoryAdapter);

        // Set up category spinner on item selected
        getCategorySpinner().setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id) {
                // Set category
                category = getCategorySpinner().getSelectedItem().toString();

                // Enable subcategory spinner if necessary
                getSubCategorySpinner().setEnabled(category.equals(GENRES) || category.equals(DECADES));

                // Set subcategory spinner adapter
                if (category.equals(GENRES)) {
                    getSubCategorySpinner().setAdapter(genreAdapter);
                } else if (category.equals(DECADES)) {
                    getSubCategorySpinner().setAdapter(decadesAdapter);
                } else {
                    getSubCategorySpinner().setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
    }

    /**
     * Setup subcategory spinner
     */
    private void setupSubCategorySpinner() {
        // Setup subcategory adapters
        setupGenreSpinnerAdapter();
        setupDecadesSpinnerAdapter();

        // set up subcategory spinner on item selected
        getSubCategorySpinner().setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id) {
                // Set subcategory
                subCategory = getSubCategorySpinner().getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
    }

    /**
     * Setup Subcategory Spinner's genre adapter
     */
    private void setupGenreSpinnerAdapter() {

        // Execute tasks in non-UI thread
        new Thread(() -> {

            // Get genres from API call
            MovieAPI movieAPI = new MovieAPI();
            ArrayList<Genre> genres = movieAPI.getGenres();

            // Populate spinner with genres
            ArrayList<String> spinnerGenres = new ArrayList<>();
            for (Genre genre : genres) {
                spinnerGenres.add(genre.getName());
            }

            // Set up adapter
            genreAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, spinnerGenres);
            genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }).start();
    }

    /**
     * Setup Subcategory Spinner's decades adapter
     */
    private void setupDecadesSpinnerAdapter() {
        // Populate spinner with decades
        ArrayList<String> spinnerDecades = new ArrayList<>();
        spinnerDecades.add(_1970s);
        spinnerDecades.add(_1980s);
        spinnerDecades.add(_1990s);
        spinnerDecades.add(_2000s);
        spinnerDecades.add(_2010s);

        decadesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerDecades);
        decadesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    /**
     * The category spinner
     */
    private Spinner getCategorySpinner() {
        return findViewById(R.id.categorySpinner);
    }

    /**
     * The sub-category spinner
     */
    private Spinner getSubCategorySpinner() {
        return findViewById(R.id.subCategorySpinner);
    }
}