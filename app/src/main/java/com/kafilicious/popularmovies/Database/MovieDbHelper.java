package com.kafilicious.popularmovies.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kafilicious.popularmovies.Database.MovieContract.MovieEntry;

/*
 * Copyright (C) 2017 Popular Movies, Stage 2
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_ONE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_POSTR_PATH + " TEXT NOT NULL, "  +
                MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_VOTE_COUNT + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TOTAL_PAGES + " INTEGER, " +
                MovieEntry.COLUMN_MOVIE_TOTAL_RESULTS + " INTEGER, " +
                MovieEntry.COLUMN_FAVORITES + " INTEGER" +
                ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_ONE_NAME);
        onCreate(db);

    }
}
