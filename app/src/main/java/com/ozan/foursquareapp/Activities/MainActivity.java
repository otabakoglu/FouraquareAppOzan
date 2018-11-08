package com.ozan.foursquareapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ozan.foursquareapp.EventBus.VenuesListEvent;
import com.ozan.foursquareapp.Models.FoursquareVenueListJSON;
import com.ozan.foursquareapp.Models.Venue;
import com.ozan.foursquareapp.R;
import com.ozan.foursquareapp.WebService.ApiClient;
import com.ozan.foursquareapp.WebService.FoursquareRestInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity {

    private String placeType;
    private String areaCity;

    @BindView(R.id.act_main_et_place_type)
    EditText etPlaceType;

    @BindView(R.id.act_main_et_area_or_city)
    EditText etAreaCity;


    private String foursquareClientID;
    private String foursquareClientSecret;

    private FoursquareRestInterface restInterface;

    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Main Page");

        //Butter Knife
        ButterKnife.bind(this);


        //Get Foursquare Api Keys from resources
        foursquareClientID = getString(R.string.foursquare_client_id);
        foursquareClientSecret = getString(R.string.foursquare_client_secret);

        //for location service
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();




    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * On Click Search Button
     *
     *
     */
    @OnClick(R.id.act_main_btn_search)
    public void onClickSearch(View v){

        placeType = etPlaceType.getText().toString();

        if(placeType.length() < 3){
            Toast.makeText(this, "Mekan türü 3 karakterden az olamaz.", Toast.LENGTH_SHORT).show();
            return;
        }

        areaCity = etAreaCity.getText().toString();

        getVenuesFromRestService();

    }

    //region loading dialog

    private ProgressDialog loadingDialog;
    private void showLoadingDailog(){

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("Yükleniyor");
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.show();
    }

    //endregion

    private Location mLastLocation;
    private void getVenuesFromRestService(){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            mLastLocation = location;

                            //Show loading dialog
                            showLoadingDailog();

                            //default location istanbul
                            String userLL = "41.013840,28.949660";

                            if(mLastLocation != null){
                                userLL = mLastLocation.getLatitude() + "," +  mLastLocation.getLongitude();
                            }

                            restInterface = ApiClient.getClient().create(FoursquareRestInterface.class);

                            Call<FoursquareVenueListJSON> call = restInterface.getVenues(
                                    foursquareClientID,
                                    foursquareClientSecret,
                                    userLL,
                                    placeType,
                                    areaCity,
                                    "20181108");


                            call.enqueue(new Callback<FoursquareVenueListJSON>() {
                                @Override
                                public void onResponse(Call<FoursquareVenueListJSON> call, Response<FoursquareVenueListJSON> response) {

                                    //close loading dialog
                                    loadingDialog.dismiss();

                                    //Venues List
                                    FoursquareVenueListJSON foursquareResponse = response.body();
                                    List<Venue> venuesList = foursquareResponse.getResponse().getVenues();

                                    //Pass datas Places Activity with Event Bus
                                    Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
                                    EventBus.getDefault().postSticky(new VenuesListEvent(venuesList));
                                    startActivity(intent);

                                }

                                @Override
                                public void onFailure(Call<FoursquareVenueListJSON> call, Throwable t) {

                                    loadingDialog.dismiss();

                                    Toast.makeText(getApplicationContext(), "Ups:Callback Failure" +  t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });


           ;// = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            if(mLastLocation == null){


            }else{
                Toast.makeText(this, "Ups: mLastLocation", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Uygulamaya lokasyon izni verilmesi gerekli !", Toast.LENGTH_SHORT).show();
        }


    }

    private void showPlacesPage(){

    }


}

