package com.example.moviesapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddMovieActivity extends AppCompatActivity {

    private EditText etTitle, etComments;
    private Spinner spinnerCategory;
    private TextView tvSelectedDate;
    private Button btnPickDate, btnSave;

    private String selectedDate = ""; // αποθηκεύει την ημερομηνία
    private DatabaseHelper dbHelper;  // SQLite helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        // Βρίσκουμε τα views
        etTitle = findViewById(R.id.etTitle);
        etComments = findViewById(R.id.etComments);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnSave = findViewById(R.id.btnSave);

        // Αρχικοποίηση της SQLite βάσης
        dbHelper = new DatabaseHelper(this);

        // 1. Γεμίζουμε το Spinner με τις 6 κατηγορίες
        String[] categories = {"Δράσης", "Κωμωδία", "Θρίλερ", "Κοινωνική", "Αισθηματική", "Φαντασίας"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        // 2. Επιλογή ημερομηνίας με DatePicker
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddMovieActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                tvSelectedDate.setText(selectedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // 3. Αποθήκευση στη βάση
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();
                String comments = etComments.getText().toString().trim();

                // Έλεγχος ότι έχουν συμπληρωθεί όλα
                if (title.isEmpty() || selectedDate.isEmpty()) {
                    Toast.makeText(AddMovieActivity.this,
                            "Συμπλήρωσε όλα τα πεδία!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Αποθήκευση με SQLite
                boolean inserted = dbHelper.insertMovie(title, category, selectedDate, comments);

                if (inserted) {
                    Toast.makeText(AddMovieActivity.this, "Η ταινία αποθηκεύτηκε!", Toast.LENGTH_SHORT).show();
                    finish(); // κλείνει το Activity και επιστρέφει στο MainActivity
                } else {
                    Toast.makeText(AddMovieActivity.this, "Σφάλμα στην αποθήκευση!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
