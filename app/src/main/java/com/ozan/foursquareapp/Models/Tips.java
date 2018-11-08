package com.ozan.foursquareapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tips {

    @SerializedName("groups")
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "Tips{" +
                "groups=" + groups +
                '}';
    }
}
