package com.rcacao.pocketmemes.data.models;

import com.google.gson.annotations.SerializedName;

public class ItemImage {

    @SerializedName("contextLink")
    private String contextLink;

    @SerializedName("height")
    private int height;

    @SerializedName("width")
    private int width;

    @SerializedName("thumbnailLink")
    private String thumbnailLink;

    @SerializedName("thumbnailHeight")
    private int thumbnailHeight;

    @SerializedName("thumbnailWidth")
    private int thumbnailWidth;

    public String getThumbnailLink() {
        return thumbnailLink;
    }
}
