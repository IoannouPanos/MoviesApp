package com.example.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    // Όνομα πίνακα και στήλες
    private static final String TABLE_MOVIES = "movies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_COMMENTS = "comments";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Δημιουργία πίνακα
        String createTable = "CREATE TABLE " + TABLE_MOVIES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_COMMENTS + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Αν αλλάξουμε schema, σβήνουμε τον πίνακα και τον ξαναδημιουργούμε
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    // Εισαγωγή ταινίας
    public boolean insertMovie(String title, String category, String date, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_COMMENTS, comments);

        long result = db.insert(TABLE_MOVIES, null, values);
        db.close();
        return result != -1; // -1 σημαίνει αποτυχία
    }

    // Επιστροφή όλων των ταινιών (θα χρειαστεί για λίστα στο MainActivity)
    public Cursor getAllMovies() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MOVIES + " ORDER BY " + COLUMN_ID + " DESC", null);
    }

    // Ενημέρωση ταινίας
    public boolean updateMovie(int id, String title, String category, String date, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("category", category);
        values.put("date", date);
        values.put("comments", comments);

        int result = db.update("movies", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // Διαγραφή ταινίας
    public boolean deleteMovie(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("movies", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }
}
