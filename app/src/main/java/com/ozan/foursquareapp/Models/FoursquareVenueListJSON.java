package com.ozan.foursquareapp.Models;

import com.google.gson.annotations.SerializedName;

public class FoursquareVenueListJSON {

	@SerializedName("response")
	private ResponseVenueList response;

	public ResponseVenueList getResponse() {
		return response;
	}
}