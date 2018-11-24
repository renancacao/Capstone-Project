package com.rcacao.pocketmemes.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {

    private int id;
    private int image;
    private String name;

    public Group() {

    }

    public Group(int id, int image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    private Group(Parcel in) {
        id = in.readInt();
        image = in.readInt();
        name = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(image);
        dest.writeString(name);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == GroupIcon.class) {
            return ((GroupIcon) obj).getId() == id;
        } else {
            return this == obj;
        }
    }
}
