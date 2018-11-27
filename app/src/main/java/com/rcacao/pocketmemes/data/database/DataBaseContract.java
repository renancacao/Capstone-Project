package com.rcacao.pocketmemes.data.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBaseContract {

    static final String AUTHORITY = "com.rcacao.pocketmemes";

    static final String PATH_RANDOM = "random";
    static final String PATH_MEMES = "memes";
    static final String PATH_LAST_MEMES = "lastmemes";
    static final String PATH_GROUP = "groups";
    static final String PATH_GROUP_MEMES = "groupmemes";
    static final String PATH_TAGS = "tags";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private DataBaseContract() {
    }

    public static final class RandomEntry {
        private RandomEntry(){}
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RANDOM).build();
    }

    public static final class MemeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMES).build();

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CREATION = "creationDate";
        public static final String COLUMN_IMAGE = "imagefile";
        static final String TABLE_NAME = "meme";
        private MemeEntry() {
        }

    }

    public static final class GroupEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GROUP).build();
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
        static final String TABLE_NAME = "groups";
        private GroupEntry() {
        }

    }

    public static final class GroupMemeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GROUP_MEMES).build();
        public static final String COLUMN_ID_MEME = "id_meme";
        public static final String COLUMN_ID_GROUP = "id_group";
        static final String TABLE_NAME = "groupmemes";
        private GroupMemeEntry() {
        }

    }

    public static final class TagsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAGS).build();
        public static final String COLUMN_ID_MEME = "id_meme";
        public static final String COLUMN_TAG = "tag";
        static final String TABLE_NAME = "tags";
        private TagsEntry() {
        }

    }

}