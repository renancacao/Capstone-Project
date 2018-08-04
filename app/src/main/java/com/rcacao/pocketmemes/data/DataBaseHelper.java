package com.rcacao.pocketmemes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaMetadata;

import com.rcacao.pocketmemes.data.DataBaseContract.MemeEntry;
import com.rcacao.pocketmemes.data.DataBaseContract.GroupEntry;
import com.rcacao.pocketmemes.data.DataBaseContract.GroupMemeEntry;
import com.rcacao.pocketmemes.data.DataBaseContract.TagsEntry;

class DataBaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String NAME = "db_memes";

    DataBaseHelper(Context context){
        super(context, NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + MemeEntry.TABLE_NAME + "("  +
                MemeEntry._ID + " INTEGER PRIMARY KEY," +
                MemeEntry.COLUMN_NAME + " TEXT," +
                MemeEntry.COLUMN_PATH + " TEXT," +
                MemeEntry.COLUMN_CREATION + " TEXT," +
                MemeEntry.COLUMN_ALTERATION + " TEXT)");

        db.execSQL("CREATE TABLE " + GroupEntry.TABLE_NAME + "("  +
                GroupEntry._ID + " INTEGER PRIMARY KEY," +
                GroupEntry.COLUMN_NAME + " TEXT," +
                GroupEntry.COLUMN_IMAGE + " TEXT)");

        db.execSQL("CREATE TABLE " + GroupMemeEntry.TABLE_NAME + "("  +
                GroupMemeEntry.COLUMN_ID_MEME + " INTEGER," +
                GroupMemeEntry.COLUMN_ID_GROUP + " INTEGER," +
                "PRIMARY KEY("+ GroupMemeEntry.COLUMN_ID_MEME + "," +
                GroupMemeEntry.COLUMN_ID_GROUP +"))");

        db.execSQL("CREATE TABLE " + TagsEntry.TABLE_NAME + "("  +
                TagsEntry.COLUMN_ID_MEME + " INTEGER," +
                TagsEntry.COLUMN_TAG + " TEXT," +
                "PRIMARY KEY ("+ TagsEntry.COLUMN_ID_MEME + "," +
                TagsEntry.COLUMN_TAG + "))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " +  MemeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +  GroupEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +  GroupMemeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +  TagsEntry.TABLE_NAME);

        onCreate(db);

    }
}
