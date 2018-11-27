package com.rcacao.pocketmemes.data.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.rcacao.pocketmemes.data.database.DataBaseContract.MemeEntry;
import com.rcacao.pocketmemes.data.models.Meme;

import java.util.ArrayList;
import java.util.List;

import static com.rcacao.pocketmemes.data.util.DataUtils.getMemeFromCursor;

public class MemesAsyncLoader extends AsyncTaskLoader<List<Meme>> {

    public static final String ARG_SEARCH = "search";
    public static final String ARG_ORDER = "order";
    public static final String ARG_GROUP = "group";

    public static final String ORDER_NAME = MemeEntry.COLUMN_NAME;
    public static final String ORDER_NEWEST = MemeEntry.COLUMN_CREATION + " DESC" ;
    public static final String ORDER_OLDEST = MemeEntry.COLUMN_CREATION + " ASC" ;

    private final Bundle args;
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

        search = "%" + getParam(search, ARG_SEARCH) + "%";
        group = getParam(group, ARG_GROUP);
        order = getParam(order, ARG_ORDER);

        if (!group.isEmpty()) {
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

                    Meme meme = getMemeFromCursor(result, getContext());
                    memes.add(meme);


                }
                while (result.moveToNext());
            }
            result.close();
        }

        return memes;

    }

    private String getParam(String value, String arg) {
        if (args != null && args.containsKey(arg)) {
            return args.getString(arg);
        }
        return value;
    }
}
