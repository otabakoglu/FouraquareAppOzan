package com.ozan.foursquareapp.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Location{


	@SerializedName("country")
	private String country;

	@SerializedName("address")
	private String address;

	@SerializedName("lat")
	private double lat;

	@SerializedName("lng")
	private double lng;


	public String getCountry() {
		return country;
	}

	public String getAddress() {
		return address;
	}

	public LatLng getLatLng(){
		return new LatLng(lat, lng);
	}

	@Override
	public String toString() {
		return "Location{" +
				"country='" + country + '\'' +
				", address='" + address + '\'' +
				", lat=" + lat +
				", lng=" + lng +
				'}';
	}
}