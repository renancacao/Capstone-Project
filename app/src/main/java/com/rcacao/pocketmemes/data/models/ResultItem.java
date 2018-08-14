package com.rcacao.pocketmemes.data.models;

import com.google.gson.annotations.SerializedName;

public class ResultItem {

    @SerializedName("fileFormat")
    String fileFormat;

    @SerializedName("link")
    String link;

    @SerializedName("image")
    ItemImage image;

    public ItemImage getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

    public String getImageUrl() {

        if (link != null && link.length() > 4) {

            String endUrl = link.substring(link.length() - 4, link.length());
            switch (endUrl.toLowerCase()) {
                case ".jpg":
                case "jpeg":
                case ".png":
                case ".gif":
                case ".bmp":
                    return link;
            }
        }
        return image.getThumbnailLink();
    }
}
