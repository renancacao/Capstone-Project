package com.rcacao.pocketmemes.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MemeContentProvider extends ContentProvider {

    private static final int MEMES = 100;
    private static final int MEME_BY_ID = 101;

    private DataBaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context context =  getContext();
        dbHelper = new DataBaseHelper(context);
        return true;
    }

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DataBaseContract.AUTHORITY, DataBaseContract.PATH_MEMES, MEMES);
        uriMatcher.addURI(DataBaseContract.AUTHORITY, DataBaseContract.PATH_MEMES + "/#", MEME_BY_ID);

        return uriMatcher;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
