package com.ozan.foursquareapp.Models;

import com.google.gson.annotations.SerializedName;

public class FoursquareVenueDetailJSON {

	@SerializedName("response")
	private ResponseVenueDetail response;

	public ResponseVenueDetail getResponse() {
		return response;
	}

	@Override
	public String toString() {
		return "FoursquareVenueDetailJSON{" +
				"response=" + response +
				'}';
	}
}