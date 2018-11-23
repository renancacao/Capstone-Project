package com.rcacao.pocketmemes.data.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.rcacao.pocketmemes.FileUtils;

import java.io.File;
import java.util.List;

public class Meme implements Parcelable {

    private int id;
    private String name;
    private String creationDate;
    private List<String> tags;
    private List<Group> groups;
    private String image;

    public Meme(){}

    private Meme(Parcel in) {
        id = in.readInt();
        name = in.readString();
        creationDate = in.readString();
        tags = in.createStringArrayList();
        groups = in.createTypedArrayList(Group.CREATOR);
        image = in.readString();
    }

    public static final Creator<Meme> CREATOR = new Creator<Meme>() {
        @Override
        public Meme createFromParcel(Parcel in) {
            return new Meme(in);
        }

        @Override
        public Meme[] newArray(int size) {
            return new Meme[size];
        }
    };

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
        return Uri.fromFile(file);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(creationDate);
        dest.writeStringList(tags);
        dest.writeTypedList(groups);
        dest.writeString(image);
    }
}
