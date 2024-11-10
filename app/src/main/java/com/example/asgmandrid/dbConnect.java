package com.example.asgmandrid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class dbConnect extends SQLiteOpenHelper {

     Context context;
    // Database info
    private static String dbName = "Android";
    private static int dbVersion = 3;

    // Users table columns
    static final String dbTable = "users";
    private static final String USER_ID = "id";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    // Habits table columns
    private static final String HABITS_TABLE = "habits";
    private static final String HABIT_ID = "habit_id";
    private static final String HABIT_TITLE = "title";
    private static final String HABIT_DESCRIPTION = "description";
    private static final String STARTING_TIME = "starting_time";
    private static final String ENDING_TIME = "ending_time";

    dbConnect(Context context) {

        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + dbTable + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EMAIL + " TEXT, " +
                USERNAME + " TEXT, " +
                PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        String createHabitsTable = "CREATE TABLE " + HABITS_TABLE + " (" +
                HABIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HABIT_TITLE + " TEXT, " +
                HABIT_DESCRIPTION + " TEXT, " +
                STARTING_TIME + " TEXT, " +
                ENDING_TIME + " TEXT)";
        db.execSQL(createHabitsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + dbTable);
        db.execSQL("DROP TABLE IF EXISTS " + HABITS_TABLE);
        onCreate(db);
    }

    public void addUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL, user.getEmail());
        values.put(USERNAME, user.getUsername());
        values.put(PASSWORD, user.getPassword());
        db.insert(dbTable, null, values);
        db.close();

    }

    public long addHabit(String title, String description, String startingTime, String endingTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HABIT_TITLE, title);
        values.put(HABIT_DESCRIPTION, description);
        values.put(STARTING_TIME, startingTime);
        values.put(ENDING_TIME, endingTime);

        long result = db.insert(HABITS_TABLE, null, values);
        db.close();
        return result;
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + HABITS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db !=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void UpdateData(String habit_id, String title, String description, String startingTime, String endingTime){
        SQLiteDatabase db = this.getWritableDatabase();  // Use getWritableDatabase()
        ContentValues cv = new ContentValues();
        cv.put(HABIT_TITLE, title);
        cv.put(HABIT_DESCRIPTION, description);
        cv.put(STARTING_TIME, startingTime);
        cv.put(ENDING_TIME, endingTime);

        // Use HABIT_ID instead of "_id" if habit_id is your primary key column
        long result = db.update(HABITS_TABLE, cv, HABIT_ID + "=?", new String[]{habit_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRow(String habit_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(HABITS_TABLE, HABIT_ID+ "=?",new String[]{habit_id});
        if (result== -1){
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();

        }
    }


    // Method to check user credentials
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + dbTable + " WHERE " + USERNAME + " = ? AND " + PASSWORD + " = ?";
        String[] selectionArgs = { username, password };

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean hasUser = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return hasUser;
    }
}
