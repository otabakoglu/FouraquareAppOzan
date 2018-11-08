package com.ozan.foursquareapp.Models;

import com.google.gson.annotations.SerializedName;

public class ResponseVenueDetail {

    @SerializedName("venue")
    private Venue venue;

    public Venue getVenue() {
        return venue;
    }

    @Override
    public String toString() {
        return "ResponseVenueDetail{" +
                "venue=" + venue +
                '}';
    }
}
