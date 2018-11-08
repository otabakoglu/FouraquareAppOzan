package com.ozan.foursquareapp.Models;

import com.google.gson.annotations.SerializedName;

public class GroupItem {

    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "GroupItem{" +
                "text='" + text + '\'' +
                '}';
    }
}
