package com.ozan.foursquareapp.EventBus;

import com.ozan.foursquareapp.Models.Venue;

import java.util.List;

public class VenuesListEvent {

    private List<Venue> venuesItemList;

    public VenuesListEvent(List<Venue> venuesItemList) {
        this.venuesItemList = venuesItemList;
    }

    public List<Venue> getVenuesItemList() {
        return venuesItemList;
    }
}
