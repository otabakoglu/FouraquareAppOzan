package com.ozan.foursquareapp.WebService;

import com.ozan.foursquareapp.Models.FoursquareVenueDetailJSON;
import com.ozan.foursquareapp.Models.FoursquareVenueListJSON;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FoursquareRestInterface {

    /**
     * GET Venues
     *
     * @param clientID
     * @param clientSecret
     * @param ll
     * @param placeType
     * @param areaCity
     * @param versionDate
     * @return
     */
    @GET("v2/venues/search")
    Call<FoursquareVenueListJSON> getVenues(
            @Query("client_id") String clientID,
            @Query("client_secret") String clientSecret,
            @Query("ll") String ll,
            @Query("query") String placeType,
            @Query("near") String areaCity,
            @Query("v") String versionDate
    );

    /**
     * GET Venues
     *
     * @param clientID
     * @param clientSecret
     * @param ll
     * @param placeType
     * @param versionDate
     * @return
     */
    @GET("v2/venues/search")
    Call<FoursquareVenueListJSON> getVenues(
            @Query("client_id") String clientID,
            @Query("client_secret") String clientSecret,
            @Query("ll") String ll,
            @Query("query") String placeType,
            @Query("v") String versionDate
    );

    /**
     * GET Venue Detail
     *
     * @param venueId
     * @param clientID
     * @param clientSecret
     * @param versionDate
     * @return
     */

    @GET("v2/venues/{VENUE_ID}")
    Call<FoursquareVenueDetailJSON> getVenueDetail(
            @Path(value = "VENUE_ID", encoded = true) String venueId,
            @Query("client_id") String clientID,
            @Query("client_secret") String clientSecret,
            @Query("v") String versionDate
    );

}
