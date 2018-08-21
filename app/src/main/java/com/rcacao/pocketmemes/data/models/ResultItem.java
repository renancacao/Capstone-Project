package com.rcacao.pocketmemes.data.models;

import com.google.gson.annotations.SerializedName;

public class ResultItem {

    @SerializedName("link")
    private String link;

    @SerializedName("image")
    private ItemImage image;

    public ItemImage getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

}
