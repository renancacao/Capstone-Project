package com.rcacao.pocketmemes.data.models;

public class ColorItem {

    private int drawable;
    private int color;

    public ColorItem(int drawable, int color) {
        this.drawable = drawable;
        this.color = color;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getColor() {
        return color;
    }

}
