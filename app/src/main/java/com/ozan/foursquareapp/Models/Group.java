package com.ozan.foursquareapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {

    @SerializedName("name")
    private String name;

    @SerializedName("items")
    List<GroupItem> items;

    public String getName() {
        return name;
    }

    public List<GroupItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", items=" + items +
                '}';
    }
}
