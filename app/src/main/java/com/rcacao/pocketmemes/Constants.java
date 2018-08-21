package com.rcacao.pocketmemes;

import android.os.Environment;

import com.rcacao.pocketmemes.data.models.ColorItem;

import static android.os.Environment.DIRECTORY_PICTURES;

public class Constants {


    public static final ColorItem[] COLORS = new ColorItem[]{
            new ColorItem(R.drawable.color_white, R.color.font_creator_white),
            new ColorItem(R.drawable.color_gray, R.color.font_creator_gray),
            new ColorItem(R.drawable.color_black, R.color.font_creator_black),
            new ColorItem(R.drawable.color_blue, R.color.font_creator_blue),
            new ColorItem(R.drawable.color_green, R.color.font_creator_green),
            new ColorItem(R.drawable.color_orange, R.color.font_creator_orange),
            new ColorItem(R.drawable.color_purple, R.color.font_creator_purple),
            new ColorItem(R.drawable.color_red, R.color.font_creator_red),
            new ColorItem(R.drawable.color_yellow, R.color.font_creator_yellow)
    };

    public static final int[] FONT_SIZE = new int[]{25, 50, 75, 100, 125, 150};

    public static final String IMAGE_PATH
            = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).getPath() + "/pocketMemes/";
}
