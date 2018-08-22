package com.rcacao.pocketmemes;

public class MyUtils {
    private static final String PNG_FILE = ".png";
    private static final String POCKET_MEMES_SUFIX = "pocketmemes_";

    public static String getFileName(int id) {
        return Constants.IMAGE_PATH + POCKET_MEMES_SUFIX + String.valueOf(id) + PNG_FILE;
    }
}
