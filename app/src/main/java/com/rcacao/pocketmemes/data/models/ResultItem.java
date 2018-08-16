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

    public String getImageUrl() {
        if (isImage(link)) {
            return link;
        }
        return image.getThumbnailLink();
    }

    public static boolean isImage(String url) {
        if (url != null && url.length() > 4) {

            String endUrl = url.substring(url.length() - 4, url.length());
            switch (endUrl.toLowerCase()) {
                case ".jpg":
                case "jpeg":
                case ".png":
                case ".gif":
                case ".bmp":
                    return true;
            }
        }
        return false;
    }
}
