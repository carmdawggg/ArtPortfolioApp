package com.carmenward.artportfolio.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ArtPortfolioDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ArtPortfolio.db";
    private static final int DATABASE_VERSION = 1;

    public ArtPortfolioDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_ARTWORK_TABLE =
                "CREATE TABLE "+ DatabaseDescription.Artwork.TABLE_NAME +"("+
                        DatabaseDescription.Artwork._ID+" integer primary key, "+
                        DatabaseDescription.Artwork.COLUMN_TITLE + " TEXT, " +
                        DatabaseDescription.Artwork.COLUMN_DATE_CREATED + " TEXT, " +
                        DatabaseDescription.Artwork.COLUMN_MEDIUM + " TEXT, " +
                        DatabaseDescription.Artwork.COLUMN_DIMENSIONS + " TEXT, " +
                        DatabaseDescription.Artwork.COLUMN_IMAGE + " TEXT);";
        db.execSQL(CREATE_ARTWORK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
