// DatabaseHelper.java

package com.s22009961.toursyncer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TourSyncer.db";
    private static final int DATABASE_VERSION = 2;

    // User Table
    private static final String USER_TABLE_NAME = "user_table";
    private static final String USER_COL_ID = "ID";
    private static final String USER_COL_FIRST_NAME = "FIRST_NAME";
    private static final String USER_COL_LAST_NAME = "LAST_NAME";
    private static final String USER_COL_EMAIL = "EMAIL";
    private static final String USER_COL_PASSWORD = "PASSWORD";

    // Trips Table
    public static final String TRIP_TABLE_NAME = "trips";
    public static final String TRIP_COLUMN_ID = "_id";
    public static final String TRIP_COLUMN_TRIP_NAME = "trip_name";
    public static final String TRIP_COLUMN_DESTINATION = "destination";
    public static final String TRIP_COLUMN_START_DAY = "start_day";
    public static final String TRIP_COLUMN_START_MONTH = "start_month";
    public static final String TRIP_COLUMN_START_HOUR = "start_hour";
    public static final String TRIP_COLUMN_START_MINUTE = "start_minute";
    public static final String TRIP_COLUMN_END_DAY = "end_day";
    public static final String TRIP_COLUMN_END_MONTH = "end_month";
    public static final String TRIP_COLUMN_END_HOUR = "end_hour";
    public static final String TRIP_COLUMN_END_MINUTE = "end_minute";
    public static final String USER_EMAIL_ID = "user_email_id";

    // Journal Entries Table
    private static final String JOURNAL_TABLE_NAME = "journal_entries";
    private static final String JOURNAL_COLUMN_ID = "id";
    private static final String JOURNAL_COLUMN_ENTRY = "entry";
    private static final String JOURNAL_USER_EMAIL_ID = "user_email_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create User Table
        String createUserTable = "CREATE TABLE " + USER_TABLE_NAME + " (" +
                USER_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COL_FIRST_NAME + " TEXT, " +
                USER_COL_LAST_NAME + " TEXT, " +
                USER_COL_EMAIL + " TEXT UNIQUE, " +
                USER_COL_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        // Create Trips Table
        String createTripTable = "CREATE TABLE " + TRIP_TABLE_NAME + " (" +
                TRIP_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TRIP_COLUMN_TRIP_NAME + " TEXT, " +
                TRIP_COLUMN_DESTINATION + " TEXT, " +
                TRIP_COLUMN_START_DAY + " INTEGER, " +
                TRIP_COLUMN_START_MONTH + " INTEGER, " +
                TRIP_COLUMN_START_HOUR + " INTEGER, " +
                TRIP_COLUMN_START_MINUTE + " INTEGER, " +
                TRIP_COLUMN_END_DAY + " INTEGER, " +
                TRIP_COLUMN_END_MONTH + " INTEGER, " +
                TRIP_COLUMN_END_HOUR + " INTEGER, " +
                TRIP_COLUMN_END_MINUTE + " INTEGER, " +
                USER_EMAIL_ID + " TEXT, " +
                "FOREIGN KEY(" + USER_EMAIL_ID + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COL_EMAIL + "))";
        db.execSQL(createTripTable);

        // Create Journal Entries Table
        String createJournalTable = "CREATE TABLE " + JOURNAL_TABLE_NAME + " (" +
                JOURNAL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                JOURNAL_COLUMN_ENTRY + " TEXT, " +
                JOURNAL_USER_EMAIL_ID + " TEXT, " +
                "FOREIGN KEY(" + JOURNAL_USER_EMAIL_ID + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COL_EMAIL + "))";
        db.execSQL(createJournalTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRIP_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + JOURNAL_TABLE_NAME);
        onCreate(db);
    }

    // User Table Methods
    public boolean insertUser(String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_FIRST_NAME, firstName);
        contentValues.put(USER_COL_LAST_NAME, lastName);
        contentValues.put(USER_COL_EMAIL, email);
        contentValues.put(USER_COL_PASSWORD, password);
        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + USER_COL_EMAIL + "=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Trips Table Methods
    public boolean insertTrip(String tripName, String destination, int startDay, int startMonth,
                              int startHour, int startMinute, int endDay, int endMonth,
                              int endHour, int endMinute, int hour, int minute, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRIP_COLUMN_TRIP_NAME, tripName);
        contentValues.put(TRIP_COLUMN_DESTINATION, destination);
        contentValues.put(TRIP_COLUMN_START_DAY, startDay);
        contentValues.put(TRIP_COLUMN_START_MONTH, startMonth);
        contentValues.put(TRIP_COLUMN_START_HOUR, startHour);
        contentValues.put(TRIP_COLUMN_START_MINUTE, startMinute);
        contentValues.put(TRIP_COLUMN_END_DAY, endDay);
        contentValues.put(TRIP_COLUMN_END_MONTH, endMonth);
        contentValues.put(TRIP_COLUMN_END_HOUR, endHour);
        contentValues.put(TRIP_COLUMN_END_MINUTE, endMinute);
        contentValues.put(USER_EMAIL_ID, email);

        long result = db.insert(TRIP_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllTrips(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = USER_EMAIL_ID + " = ?";
        String[] selectionArgs = { userEmail };
        return db.query(TRIP_TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    public boolean deleteTripByName(String tripName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = TRIP_COLUMN_TRIP_NAME + " = ?";
        String[] selectionArgs = {tripName};
        int deletedRows = db.delete(TRIP_TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
    }

    // Journal Entries Table Methods
    public boolean insertJournalEntry(String entry, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JOURNAL_COLUMN_ENTRY, entry);
        contentValues.put(JOURNAL_USER_EMAIL_ID, email);
        long result = db.insert(JOURNAL_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getJournalEntries(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = JOURNAL_USER_EMAIL_ID + " = ?";
        String[] selectionArgs = { userEmail };
        return db.query(JOURNAL_TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }
    // DatabaseHelper.java

    public boolean deleteJournalEntry(String entry, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = JOURNAL_COLUMN_ENTRY + " = ? AND " + JOURNAL_USER_EMAIL_ID + " = ?";
        String[] selectionArgs = {entry, email};
        int deletedRows = db.delete(JOURNAL_TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
    }

}
