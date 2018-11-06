package com.rcacao.pocketmemes.data.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.rcacao.pocketmemes.data.database.DataBaseContract.MemeEntry;
import com.rcacao.pocketmemes.data.database.DataBaseContract.TagsEntry;
import com.rcacao.pocketmemes.data.models.Group;
import com.rcacao.pocketmemes.data.models.Meme;

import java.util.ArrayList;
import java.util.List;

import static com.rcacao.pocketmemes.data.database.DataBaseContract.GroupEntry;
import static com.rcacao.pocketmemes.data.database.DataBaseContract.GroupMemeEntry;

public class MemesAsyncLoader extends AsyncTaskLoader<List<Meme>> {

    public static final String ARG_SEARCH = "search";
    public static final String ARG_ORDER = "order";
    public static final String ARG_GROUP = "group";

    public static final String ORDER_NAME = MemeEntry.COLUMN_NAME;
    public static final String ORDER_DATE = MemeEntry.COLUMN_CREATION;

    private Bundle args;
    private List<Meme> memes;

    public MemesAsyncLoader(@NonNull Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        if (memes == null) {
            forceLoad();
        } else {
            deliverResult(memes);
        }
    }

    @Nullable
    @Override
    public List<Meme> loadInBackground() {

        String search = "%";
        String order = MemeEntry._ID;
        String group = "";

        String[] queryArgs;

        if (args != null) {
            if (args.containsKey(ARG_SEARCH)) {
                search = args.getString(ARG_SEARCH) + "%";
            }

            if (args.containsKey(ARG_GROUP)) {
                group = args.getString(ARG_GROUP);
            }

            if (args.containsKey(ARG_ORDER)) {
                order = args.getString(ARG_ORDER);
            }
        }

        if (group != null && !group.isEmpty()) {
            queryArgs = new String[]{search, search, group};
        } else {
            queryArgs = new String[]{search, search};
        }

        Cursor result = getContext().getContentResolver().query(MemeEntry.CONTENT_URI,
                null, null, queryArgs, order);

        memes = new ArrayList<>();

        if (result != null) {
            if (result.moveToFirst()) {
                do {

                    Meme meme = new Meme();
                    meme.setId(result.getInt(result.getColumnIndex(MemeEntry._ID)));
                    meme.setName(result.getString(result.getColumnIndex(MemeEntry.COLUMN_NAME)));
                    meme.setCreationDate(result.getString(result.getColumnIndex(MemeEntry.COLUMN_CREATION)));
                    meme.setImage(result.getString(result.getColumnIndex(MemeEntry.COLUMN_IMAGE)));

                    meme.setTags(getMemeTags(meme.getId()));
                    meme.setGroups(getMemeGroups(meme.getId()));

                    memes.add(meme);

                }
                while (result.moveToNext());
            }
            result.close();
        }

        return memes;

    }

    private List<String> getMemeTags(int id) {

        Cursor result = getContext().getContentResolver().query(TagsEntry.CONTENT_URI,
                null, null, new String[]{String.valueOf(id)}, TagsEntry.COLUMN_TAG);
        List<String> tags = new ArrayList<>();

        if (result != null) {
            if (result.moveToFirst()) {
                do {
                    tags.add(result.getString(result.getColumnIndex(TagsEntry.COLUMN_TAG)));
                }
                while (result.moveToNext());
            }
            result.close();
        }
        return tags;
    }

    private List<Group> getMemeGroups(int id) {
        Cursor result = getContext().getContentResolver().query(GroupMemeEntry.CONTENT_URI,
                null, null, new String[]{String.valueOf(id)}, null);
        List<Group> groups = new ArrayList<>();

        if (result != null) {
            if (result.moveToFirst()) {
                do {
                    Group group = new Group();
                    group.setId(result.getInt(result.getColumnIndex(GroupEntry._ID)));
                    group.setName(result.getString(result.getColumnIndex(GroupEntry.COLUMN_NAME)));
                    group.setImage(result.getInt(result.getColumnIndex(GroupEntry.COLUMN_IMAGE)));
                    groups.add(group);
                }
                while (result.moveToNext());
            }
            result.close();
        }
        return groups;
    }


}
