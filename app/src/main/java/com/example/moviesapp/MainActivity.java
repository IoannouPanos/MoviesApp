package com.example.moviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewMovies;
    private Button btnAddMovie;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewMovies = findViewById(R.id.listViewMovies);
        btnAddMovie = findViewById(R.id.btnAddMovie);

        dbHelper = new DatabaseHelper(this);
        moviesList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, moviesList);
        listViewMovies.setAdapter(adapter);


        // Άνοιγμα AddMovieActivity


        /*btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbhelper = new DatabaseHelper (MainActivity.this);
            }
        });*/



        btnAddMovie.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
            startActivity(intent);
        });

        listViewMovies.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = dbHelper.getAllMovies();
            if (cursor.moveToPosition(position)) {
                int movieId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

                // Στέλνουμε το ID στο EditMovieActivity
                Intent intent = new Intent(MainActivity.this, EditMovieActivity.class);
                intent.putExtra("MOVIE_ID", movieId);
                startActivity(intent);
            }
            cursor.close();
        });

        // Φόρτωση ταινιών
        loadMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies(); // Ανανεώνει τη λίστα κάθε φορά που επιστρέφουμε
    }

    // Φόρτωση ταινιών από τη βάση
    private void loadMovies() {
        moviesList.clear();
        Cursor cursor = dbHelper.getAllMovies();

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                // Εμφανίζουμε τίτλο, κατηγορία και ημερομηνία
                moviesList.add(title + " (" + category + ") - " + date);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

}