package com.example.moviesapp;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditMovieActivity extends AppCompatActivity {

    private EditText etTitleEdit, etCommentsEdit;
    private Spinner spinnerCategoryEdit;
    private TextView tvSelectedDateEdit;
    private Button btnPickDateEdit, btnUpdate, btnDelete;

    private String selectedDate = "";
    private int movieId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        etTitleEdit = findViewById(R.id.etTitleEdit);
        etCommentsEdit = findViewById(R.id.etCommentsEdit);
        spinnerCategoryEdit = findViewById(R.id.spinnerCategoryEdit);
        tvSelectedDateEdit = findViewById(R.id.tvSelectedDateEdit);
        btnPickDateEdit = findViewById(R.id.btnPickDateEdit);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new DatabaseHelper(this);

        // Φορτώνουμε τις κατηγορίες στο Spinner
        String[] categories = {"Δράσης", "Κωμωδία", "Θρίλερ", "Κοινωνική", "Αισθηματική", "Φαντασίας"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategoryEdit.setAdapter(adapter);

        // Παίρνουμε το ID της ταινίας
        movieId = getIntent().getIntExtra("MOVIE_ID", -1);

        if (movieId != -1) {
            loadMovieData(movieId);
        }

        // Date Picker
        btnPickDateEdit.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditMovieActivity.this,
                    (DatePicker view, int year1, int month1, int dayOfMonth) -> {
                        selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        tvSelectedDateEdit.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Update
        btnUpdate.setOnClickListener(v -> {
            String title = etTitleEdit.getText().toString().trim();
            String category = spinnerCategoryEdit.getSelectedItem().toString();
            String comments = etCommentsEdit.getText().toString().trim();

            if (dbHelper.updateMovie(movieId, title, category, selectedDate, comments)) {
                Toast.makeText(this, "Η ταινία ενημερώθηκε!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Σφάλμα στην ενημέρωση!", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete
        btnDelete.setOnClickListener(v -> {
            if (dbHelper.deleteMovie(movieId)) {
                Toast.makeText(this, "Η ταινία διαγράφηκε!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Σφάλμα στη διαγραφή!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMovieData(int id) {
        Cursor cursor = dbHelper.getAllMovies();
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndexOrThrow("id")) == id) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String comments = cursor.getString(cursor.getColumnIndexOrThrow("comments"));

                etTitleEdit.setText(title);
                etCommentsEdit.setText(comments);
                tvSelectedDateEdit.setText(date);
                selectedDate = date;

                // Επιλογή κατηγορίας στο Spinner
                ArrayAdapter adapter = (ArrayAdapter) spinnerCategoryEdit.getAdapter();
                int pos = adapter.getPosition(category);
                spinnerCategoryEdit.setSelection(pos);
            }
        }
        cursor.close();
    }
}
