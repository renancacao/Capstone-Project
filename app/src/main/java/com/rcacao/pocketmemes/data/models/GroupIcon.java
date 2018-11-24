package com.rcacao.pocketmemes.data.models;

public class GroupIcon {

    private int image;
    private boolean checked = false;
    private int id = -1;
    private String name;

    public GroupIcon(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == Group.class) {
            return ((Group) obj).getId() == id;
        } else {
            return this == obj;
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
