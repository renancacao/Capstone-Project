package com.rcacao.pocketmemes.data;

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

public class MemeContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int MEMES = 100;
    private static final int MEME_BY_ID = 101;

    private static final int GROUPS = 200;
    private static final int GROUP_BY_ID = 201;

    private static final int GROUP_MEMES = 300;
    private static final int GROUP_MEME_BY_IDS = 301;

    private static final int TAGS = 400;
    private static final int TAGS_BY_ID = 401;


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

        uriMatcher.addURI(DataBaseContract.AUTHORITY, DataBaseContract.PATH_GROUP, GROUPS);
        uriMatcher.addURI(DataBaseContract.AUTHORITY, DataBaseContract.PATH_GROUP + "/#", GROUP_BY_ID);

        uriMatcher.addURI(DataBaseContract.AUTHORITY, DataBaseContract.PATH_GROUP_MEMES, GROUP_MEMES);
        uriMatcher.addURI(DataBaseContract.AUTHORITY, DataBaseContract.PATH_GROUP_MEMES + "/#/#", GROUP_MEME_BY_IDS);

        uriMatcher.addURI(DataBaseContract.AUTHORITY, DataBaseContract.PATH_TAGS, TAGS);
        uriMatcher.addURI(DataBaseContract.AUTHORITY, DataBaseContract.PATH_TAGS + "/#", TAGS_BY_ID);

        return uriMatcher;
    }



    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] args, @Nullable String sortOrder
    ) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        String idMeme;
        String idGroup;

        switch (match) {

            case MEMES:
                retCursor = db.query(DataBaseContract.MemeEntry.TABLE_NAME,projection,selection,
                        args,null,null,sortOrder);
                break;

            case GROUPS:
                retCursor = db.query(DataBaseContract.GroupEntry.TABLE_NAME,projection,selection,
                        args,null,null,sortOrder);
                break;

            case GROUP_MEMES:
                retCursor = db.query(DataBaseContract.GroupMemeEntry.TABLE_NAME,projection,selection,
                        args,null,null,sortOrder);
                break;


            case GROUP_MEME_BY_IDS:
                idMeme = uri.getPathSegments().get(1);
                idGroup = uri.getPathSegments().get(2);

                retCursor = db.query(DataBaseContract.GroupMemeEntry.TABLE_NAME,projection,
                        DataBaseContract.GroupMemeEntry.COLUMN_ID_MEME + "=? and " +
                                DataBaseContract.GroupMemeEntry.COLUMN_ID_GROUP + "=?",
                        new String[]{idMeme, idGroup},null,null,sortOrder);
                break;

            case TAGS:
                retCursor = db.query(DataBaseContract.TagsEntry.TABLE_NAME,projection,selection,
                        args,null,null,sortOrder);
                break;

            case TAGS_BY_ID:
                idMeme = uri.getPathSegments().get(1);
                retCursor = db.query(DataBaseContract.TagsEntry.TABLE_NAME,projection,
                        DataBaseContract.GroupMemeEntry.COLUMN_ID_MEME + "=?",
                        new String[]{idMeme},null,null,sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Falha: " + uri);

        }


        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        long id;

        switch (match){

            case MEMES:
                id = db.insert(DataBaseContract.MemeEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(DataBaseContract.MemeEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Falha: " + uri);
                }
            case GROUPS:
                id = db.insert(DataBaseContract.GroupEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(DataBaseContract.GroupEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Falha: " + uri);
                }
            case GROUP_MEMES:
                id = db.insert(DataBaseContract.GroupMemeEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(DataBaseContract.GroupMemeEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Falha: " + uri);
                }
            case TAGS:
                id = db.insert(DataBaseContract.TagsEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(DataBaseContract.TagsEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Falha: " + uri);
                }

                break;

            default:
                throw new UnsupportedOperationException("URI deconhecida: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int deleted;

        String idMeme;
        String idGroup;

        switch (match) {

            case MEMES:
                deleted = db.delete(DataBaseContract.MemeEntry.TABLE_NAME, null,null);
                break;

            case MEME_BY_ID:
                idMeme = uri.getPathSegments().get(1);
                deleted = db.delete(DataBaseContract.MemeEntry.TABLE_NAME, "_id=?", new String[]{idMeme});
                break;

            case GROUPS:
                deleted = db.delete(DataBaseContract.GroupEntry.TABLE_NAME, null,null);
                break;

            case GROUP_BY_ID:
                idGroup = uri.getPathSegments().get(1);
                deleted = db.delete(DataBaseContract.GroupEntry.TABLE_NAME, "_id=?", new String[]{idGroup});
                break;

            case GROUP_MEMES:
                deleted = db.delete(DataBaseContract.GroupMemeEntry.TABLE_NAME, null,null);
                break;

            case GROUP_MEME_BY_IDS:
                idMeme = uri.getPathSegments().get(1);
                idGroup = uri.getPathSegments().get(2);
                deleted = db.delete(DataBaseContract.GroupMemeEntry.TABLE_NAME,
                        DataBaseContract.GroupMemeEntry.COLUMN_ID_MEME + "=? and " +
                                DataBaseContract.GroupMemeEntry.COLUMN_ID_GROUP + "=?",
                        new String[]{idMeme, idGroup});
                break;

            case TAGS:
                deleted = db.delete(DataBaseContract.TagsEntry.TABLE_NAME, null,null);
                break;

            case TAGS_BY_ID:
                idMeme = uri.getPathSegments().get(1);
                deleted = db.delete(DataBaseContract.TagsEntry.TABLE_NAME, "_id=?", new String[]{idMeme});
                break;

            default:
                throw new UnsupportedOperationException("URI deconhecida: " + uri);

        }

        if (deleted>0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");

    }
}
