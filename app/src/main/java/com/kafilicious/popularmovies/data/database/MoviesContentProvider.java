package com.kafilicious.popularmovies.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Objects;

import static com.kafilicious.popularmovies.data.database.MovieContract.MovieEntry.CONTENT_URI;
import static com.kafilicious.popularmovies.data.database.MovieContract.MovieEntry.TABLE_ONE_NAME;

/**
 * Created by Abdulkarim on 5/13/2017.
 */

public class MoviesContentProvider extends ContentProvider {

    public static final int FAVORITES = 10;
    public static final int FAVORITES_WITH_ID = 11;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TASKS, FAVORITES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TASKS + "/#",
                FAVORITES_WITH_ID);

        return uriMatcher;
    }

    private MovieDbHelper mMoviesDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase database = mMoviesDbHelper.getReadableDatabase();
        int match = mUriMatcher.match(uri);
        Cursor mCursor;
        switch (match){
            case FAVORITES:
                mCursor = database.query(TABLE_ONE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        mCursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return mCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = mMoviesDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);

        Uri mUri;

        switch (match){

            case FAVORITES:
                long id = database.insert(TABLE_ONE_NAME, null, values);
                Log.v("MoviesContentProvider", id + "");

                if (id > 0){
                    mUri = ContentUris.withAppendedId(CONTENT_URI, id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        //Notify the resolver of the changed uri, and return the newly inserted item URI
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return mUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase database = mMoviesDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);
        //The variable to be used to track number of favorite video items deleted
        int favorite_deleted;
        if(selection == null)
            selection = "1";

        switch (match){

            //Handle single Item delete case, recognized by the ID added in the URI path
            case FAVORITES:
                //Get the ID of the favorite movie to be deleted from the URI path
                //Use the selection/selectionArgs to filter for this ID
                favorite_deleted = database.delete(TABLE_ONE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //Notify the resolver of the change made and return the number
        // of favorite video items deleted
        if (favorite_deleted != 0){

            // An item delete task was done, set notification of data change
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri,
                    null);
        }

        //Returns the number of items deleted int the task
        return favorite_deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase database = mMoviesDbHelper.getWritableDatabase();

        final int match = mUriMatcher.match(uri);
         int rows_changed;

        switch (match){
            case FAVORITES:
                rows_changed = database.update(TABLE_ONE_NAME, values, selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Insertion not supported for " + uri);
        }

        if (rows_changed != 0){
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri,
                    null);
        }

        return rows_changed;
    }
}
