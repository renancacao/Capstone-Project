package com.rcacao.pocketmemes.data.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBaseContract {

    public static final String AUTHORITY = "com.rcacao.pocketmemes";

    static final String PATH_MEMES = "memes";
    static final String PATH_LAST_MEMES = "lastmemes";
    static final String PATH_GROUP = "groups";
    static final String PATH_GROUP_MEMES = "groupmemes";
    static final String PATH_TAGS = "tags";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class MemeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMES).build();

        public static final Uri CONTENT_URI_LAST =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LAST_MEMES).build();

        public static final String TABLE_NAME = "meme";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CREATION = "creationDate";

    }

    public static final class GroupEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GROUP).build();

        public static final String TABLE_NAME = "groups";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";

    }

    public static final class GroupMemeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GROUP_MEMES).build();

        public static final String TABLE_NAME = "groupmemes";
        public static final String COLUMN_ID_MEME = "id_meme";
        public static final String COLUMN_ID_GROUP = "id_group";

    }

    public static final class TagsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAGS).build();

        public static final String TABLE_NAME = "tags";
        public static final String COLUMN_ID_MEME = "id_meme";
        public static final String COLUMN_TAG = "tag";

    }

}