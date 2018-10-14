package com.rcacao.pocketmemes.data.models;

import android.net.Uri;

import com.rcacao.pocketmemes.FileUtils;

import java.io.File;
import java.util.List;

public class Meme {

    private int id;
    private String name;
    private String creationDate;
    private List<String> tags;
    private List<Group> groups;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Uri getImageUri() {
        File file = new File(FileUtils.getFileNameWithPath(image));
        if (file.exists()){
            return Uri.fromFile(file);
        }
        return null;
    }

}
