package com.ozan.foursquareapp.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ozan.foursquareapp.Adapters.VenuesAdapter;
import com.ozan.foursquareapp.EventBus.VenuesListEvent;
import com.ozan.foursquareapp.Interfaces.OnClickVenueItem;
import com.ozan.foursquareapp.Models.FoursquareVenueDetailJSON;
import com.ozan.foursquareapp.Models.GroupItem;
import com.ozan.foursquareapp.Models.ResponseVenueDetail;
import com.ozan.foursquareapp.Models.Venue;
import com.ozan.foursquareapp.R;
import com.ozan.foursquareapp.Utils.StringOperations;
import com.ozan.foursquareapp.WebService.ApiClient;
import com.ozan.foursquareapp.WebService.FoursquareRestInterface;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesActivity extends AppCompatActivity implements OnClickVenueItem, OnMapReadyCallback {

    //Venues list
    private Venue selectedVenue;
    private List<Venue> venuesItemList = new ArrayList<>();

    //Selected Venue Tips
    private List<String> tips = new ArrayList<>();


    //Foursquare
    private FoursquareRestInterface restInterface;

    private String foursquareClientID;
    private String foursquareClientSecret;


    //RecyclerView
    @BindView(R.id.act_places_rv_place_list)
    RecyclerView rvVenues;

    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        setTitle("Places");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Butter knife
        ButterKnife.bind(this);

        //RecyclerView Layout settings
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        rvVenues.setLayoutManager(mLayoutManager);

        //Get Foursquare Api Keys from resources
        foursquareClientID = getString(R.string.foursquare_client_id);
        foursquareClientSecret = getString(R.string.foursquare_client_secret);


        //create and initialize Venue Detail Dialog
        createVenueDetailDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get Venues List from Main Activity with Event Bus
     * @param result
     */

    //region Event Bus
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventVenuesList(VenuesListEvent result){

        venuesItemList = result.getVenuesItemList();

        fillRecyclerView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //endregion


    private void fillRecyclerView(){

        mAdapter = new VenuesAdapter(this, venuesItemList);
        rvVenues.setAdapter(mAdapter);

        rvVenues.scrollToPosition(0);
    }

    /**
     * Venue item on click
     *
     * @see OnClickVenueItem interface
     * @param position
     */
    @Override
    public void onClick(int position) {

       getVenueDetailsFromRestService(position);

    }

    private ProgressDialog loadingDialog;
    private void showLoadingDailog(){

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("Yükleniyor");
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.show();
    }

    /**
     * get Venue detail with retrofit.
     *
     * @param pos
     */
    private void getVenueDetailsFromRestService(int pos){

        showLoadingDailog();

        restInterface = ApiClient.getClient().create(FoursquareRestInterface.class);

        /**
         * call venude details with Venue Id.
         * versionDate sould be.
         */
        Call<FoursquareVenueDetailJSON> call = restInterface.getVenueDetail(
                venuesItemList.get(pos).getId(),
                foursquareClientID,
                foursquareClientSecret,
                "20181108");

        call.enqueue(new Callback<FoursquareVenueDetailJSON>() {
            @Override
            public void onResponse(Call<FoursquareVenueDetailJSON> call, Response<FoursquareVenueDetailJSON> response) {

               //Json data venue detail
                FoursquareVenueDetailJSON foursquareResponse = response.body();

                //Venue Detail response model
                ResponseVenueDetail responseVenueDetail = foursquareResponse.getResponse();

                selectedVenue = responseVenueDetail.getVenue();

                //fill tips list
                tips.clear();
                for(GroupItem groupItem : selectedVenue.getTips().getGroups().get(0).getItems()){
                    tips.add(groupItem.getText());
                }

                //Fill Venue detail dialog and show
                fillAndShowVenueDetailDialog();

                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<FoursquareVenueDetailJSON> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Ups:Callback Failure" +  t.getMessage(), Toast.LENGTH_LONG).show();

                loadingDialog.dismiss();
            }
        });

    }

    //region Venue Detail Dialog

    private Dialog dialog = null;
    private View venueDetailDialogView;
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    private TextView tvTitle;
    private TextView tvTips;
    private ImageView ivPhoto;

    private void createVenueDetailDialog(){

        dialog = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        venueDetailDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_venue_detail,null);


        tvTitle = venueDetailDialogView.findViewById(R.id.dialog_venue_tv_title);
        tvTips = venueDetailDialogView.findViewById(R.id.dialog_venue_tv_tips);
        ivPhoto = venueDetailDialogView.findViewById(R.id.dialog_venue_iv_photo);


        //MapView initialize
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.dialog_venue_map_mapView);
        mapFragment.getMapAsync(this);

        //dialog settings
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(venueDetailDialogView);
        dialog.setCanceledOnTouchOutside(true);

    }

    private void fillAndShowVenueDetailDialog(){

        tvTitle.setText(selectedVenue.getName());
        tvTips.setText(StringOperations.stringListToString(tips));

        //load photo with picasso
        Picasso.get()
                .load(selectedVenue.getBestPhoto().getPhotoUrl())
                .into(ivPhoto);

        //add Market on Map and move camera
        map.clear();

        LatLng latLng = selectedVenue.getLocation().getLatLng();
        map.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(selectedVenue.getName()));

        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));


        //show Venue Detail Dailog
        dialog.show();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    //endregion
}
