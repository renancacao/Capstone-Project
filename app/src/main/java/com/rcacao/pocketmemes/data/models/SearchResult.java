package com.rcacao.pocketmemes.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {

    @SerializedName("items")
    private List<ResultItem> items;

    public List<ResultItem> getItems() {
        return items;
    }
}
