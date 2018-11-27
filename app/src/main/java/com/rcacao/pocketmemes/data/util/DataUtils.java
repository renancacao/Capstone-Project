package com.rcacao.pocketmemes.data.util;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.rcacao.pocketmemes.data.database.DataBaseContract;
import com.rcacao.pocketmemes.data.models.Group;
import com.rcacao.pocketmemes.data.models.Meme;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    private DataUtils(){}

    @NonNull
    public static Meme getMemeFromCursor(Cursor result, Context context) {
        Meme meme = new Meme();
        meme.setId(result.getInt(result.getColumnIndex(DataBaseContract.MemeEntry._ID)));
        meme.setName(result.getString(result.getColumnIndex(DataBaseContract.MemeEntry.COLUMN_NAME)));
        meme.setCreationDate(result.getString(result.getColumnIndex(DataBaseContract.MemeEntry.COLUMN_CREATION)));
        meme.setImage(result.getString(result.getColumnIndex(DataBaseContract.MemeEntry.COLUMN_IMAGE)));

        meme.setTags(getMemeTags(meme.getId(), context));
        meme.setGroups(getMemeGroups(meme.getId(), context));
        return meme;
    }

    private static List<String> getMemeTags(int id, Context context) {

        Cursor result = context.getContentResolver().query(DataBaseContract.TagsEntry.CONTENT_URI,
                null, null, new String[]{String.valueOf(id)}, DataBaseContract.TagsEntry.COLUMN_TAG);
        List<String> tags = new ArrayList<>();

        if (result != null) {
            if (result.moveToFirst()) {
                do {
                    tags.add(result.getString(result.getColumnIndex(DataBaseContract.TagsEntry.COLUMN_TAG)));
                }
                while (result.moveToNext());
            }
            result.close();
        }
        return tags;
    }

    private static List<Group> getMemeGroups(int id, Context context) {
        Cursor result = context.getContentResolver().query(DataBaseContract.GroupMemeEntry.CONTENT_URI,
                null, null, new String[]{String.valueOf(id)}, null);
        List<Group> groups = new ArrayList<>();

        if (result != null) {
            if (result.moveToFirst()) {
                do {
                    Group group = new Group();
                    group.setId(result.getInt(result.getColumnIndex(DataBaseContract.GroupEntry._ID)));
                    group.setName(result.getString(result.getColumnIndex(DataBaseContract.GroupEntry.COLUMN_NAME)));
                    group.setImage(result.getInt(result.getColumnIndex(DataBaseContract.GroupEntry.COLUMN_IMAGE)));
                    groups.add(group);
                }
                while (result.moveToNext());
            }
            result.close();
        }
        return groups;
    }

}
