package com.ozan.foursquareapp.Models;

import com.google.gson.annotations.SerializedName;

public class BestPhoto {

    @SerializedName("prefix")
    private String prefix;

    @SerializedName("suffix")
    private String suffix;


    public String getPhotoUrl(){
        return prefix + "400x300" + suffix;
    }


}
