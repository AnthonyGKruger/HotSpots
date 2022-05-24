package com.learning.hotspots;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//db helper class that will create db and save information into the db.
public class DBHelper extends SQLiteOpenHelper {

    //declaring column names
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hotspots.db";
    public static final String TABLE_NAME = "ratings";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOCATION_NAME = "location_name";
    public static final String COLUMN_ADDRESS = "location_address";
    public static final String COLUMN_BEER_RATING = "beer_rating";
    public static final String COLUMN_WINE_RATING = "wine_rating";
    public static final String COLUMN_MUSIC_DEP = "music_rating";
    public static final String COLUMN_AVERAGE_RATING = "average_rating";


    //constructor, creates the db
    public DBHelper(@Nullable Context context, @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override//on load setup the db table columns and expected data types
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_LOCATION_NAME + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " + COLUMN_BEER_RATING + " REAL, " +
                COLUMN_WINE_RATING + " REAL, " + COLUMN_MUSIC_DEP + " REAL, " +
                COLUMN_AVERAGE_RATING + " REAL)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //helper function to add data to the dbb
    public void addToDB( String locationName, String address, double beerRating,
                         double wineRating, double musicRating, double average){

        //setting up object that will store the values of the edit fields in a container with
        // key value pairs for us to put into the db table
        ContentValues values = new ContentValues();

        //inserting data in container
        values.put(COLUMN_LOCATION_NAME, locationName);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_BEER_RATING, beerRating);
        values.put(COLUMN_WINE_RATING, wineRating);
        values.put(COLUMN_MUSIC_DEP, musicRating);
        values.put(COLUMN_AVERAGE_RATING, average);

        SQLiteDatabase db = this.getWritableDatabase();

        //inserting into the table and closing the db
        db.insert(TABLE_NAME, null, values);
        db.close();
    }



    public void updateHandler(String locationName, String address, double beerRating,
                              double wineRating, double musicRating, double average) {

        //setting up object that will store the values of the edit fields in a container
        // with key value pairs for us to put into the db table
        ContentValues values = new ContentValues();

        values.put(COLUMN_LOCATION_NAME, locationName);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_BEER_RATING, beerRating);
        values.put(COLUMN_WINE_RATING, wineRating);
        values.put(COLUMN_MUSIC_DEP, musicRating);
        values.put(COLUMN_AVERAGE_RATING, average);


        SQLiteDatabase db = this.getWritableDatabase();

        //updating the db
        db.update(TABLE_NAME, values, COLUMN_LOCATION_NAME + " = '" + locationName +
                "' AND " + COLUMN_ADDRESS + " = '" + address + "'", null);

        //closing the db
        db.close();
    }

    //function to check if details exist in db.
    public boolean findHandler(String locationName, String address) {

        //building query string.
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOCATION_NAME + " = " + "'"
                + locationName + "' AND " + COLUMN_ADDRESS + " = '" + address + "'" ;

        SQLiteDatabase db = this.getWritableDatabase();

        //cursor object to go through items in the db
        Cursor cursor = db.rawQuery(query, null);

        //if the cursor can move, if result was returned it means there is data matching the input
        //arguments.
        if (cursor.moveToFirst()) {
            //close the cursor
            cursor.close();
            //close the db and return true
            db.close();
            return true;
        }

        //return false, meaning there is no current store information in the db matching input
        return false;
    }
}

