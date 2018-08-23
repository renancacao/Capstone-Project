package com.rcacao.pocketmemes.loaders;

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

public class MemesAsyncLoader extends AsyncTaskLoader<List<Meme>> {

    public static final String ARG_SEARCH = "search";
    public static final String ARG_ORDER = "order";

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
        if (args.containsKey(ARG_SEARCH)) {
            search = args.getString(ARG_SEARCH) + "%";
        }
        if (args.containsKey(ARG_ORDER)) {
            order = args.getString(ARG_ORDER);
        }

        Cursor result = getContext().getContentResolver().query(MemeEntry.CONTENT_URI,
                null, null, new String[]{search}, order);

        if (result != null) {
            if (result.moveToFirst()) {
                do {

                }
                while (result.moveToNext());
            }
            result.close();
        }

        return memes;

    }


}
