package com.carmenward.artportfolio.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.carmenward.artportfolio.R;

public class ArtPortfolioContentProvider extends ContentProvider {

    private ArtPortfolioDatabaseHelper dbHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ONE_ARTWORK = 1;
    private static final int ARTWORKS = 2;

    static {
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                DatabaseDescription.Artwork.TABLE_NAME + "/#", ONE_ARTWORK);
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                DatabaseDescription.Artwork.TABLE_NAME, ARTWORKS);

    }

    public ArtPortfolioContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int numberOfRowsDeleted;

        switch(uriMatcher.match(uri)){
            case ONE_ARTWORK:
                String id = uri.getLastPathSegment();

                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(DatabaseDescription.Artwork.TABLE_NAME, DatabaseDescription.Artwork._ID + "=" +id,
                        selectionArgs);
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.invalid_delete_uri)+uri);
        }

        if(numberOfRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        Uri newArtworkUri = null;

        switch (uriMatcher.match(uri)){
            case ARTWORKS:
                long rowId = dbHelper.getWritableDatabase().insert(DatabaseDescription.Artwork.TABLE_NAME, null, values);

                if(rowId > 0){
                    newArtworkUri = DatabaseDescription.Artwork.buildArtworkUri(rowId);

                    getContext().getContentResolver().notifyChange(uri, null);
                }
                else {
                    throw new SQLException(
                            getContext().getString(R.string.insert_failed) + uri
                    );
                }
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.invalid_insert_uri) + uri);

        }
        return newArtworkUri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.

        dbHelper = new ArtPortfolioDatabaseHelper(getContext());
        return true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseDescription.Artwork.TABLE_NAME);

        switch(uriMatcher.match(uri)){
            case ONE_ARTWORK:
                queryBuilder.appendWhere(DatabaseDescription.Artwork._ID + "=" + uri.getLastPathSegment());
                break;
            case  ARTWORKS:
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri)+ uri);
        }

        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int numberOfRowsUpdated;

        switch(uriMatcher.match(uri)){
            case ONE_ARTWORK:
                String id = uri.getLastPathSegment();

                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(DatabaseDescription.Artwork.TABLE_NAME, values,
                        DatabaseDescription.Artwork._ID + "=" + id, selectionArgs);
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.invalid_update_uri) + uri);
        }
        return numberOfRowsUpdated;
    }
}
