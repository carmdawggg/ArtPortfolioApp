package com.carmenward.artportfolio.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {

    public static final String AUTHORITY = "com.carmenward.artportfolio.data";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Artwork implements BaseColumns {
        public static final String TABLE_NAME = "artwork";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE_CREATED = "date";
        public static final String COLUMN_MEDIUM = "medium";
        public static final String COLUMN_DIMENSIONS = "dimensions";
        public static final String COLUMN_IMAGE = "image";

        public static Uri buildArtworkUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }

}
