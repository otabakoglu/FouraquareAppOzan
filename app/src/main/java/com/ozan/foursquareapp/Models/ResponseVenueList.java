package com.ozan.foursquareapp.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseVenueList {

	@SerializedName("venues")
	private List<Venue> venues;

	public List<Venue> getVenues(){
		return venues;
	}

}