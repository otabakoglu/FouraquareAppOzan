package com.ozan.foursquareapp.Models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Venue {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("location")
    private Location location;

    @SerializedName("bestPhoto")
    private BestPhoto bestPhoto;

    @SerializedName("tips")
    private Tips tips;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public BestPhoto getBestPhoto() {
        return bestPhoto;
    }

    public Tips getTips() {
        return tips;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", bestPhoto=" + bestPhoto +
                ", tips=" + tips +
                '}';
    }
}
