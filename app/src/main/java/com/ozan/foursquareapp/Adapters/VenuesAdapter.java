package com.ozan.foursquareapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ozan.foursquareapp.Interfaces.OnClickVenueItem;
import com.ozan.foursquareapp.Models.Venue;
import com.ozan.foursquareapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VenuesAdapter extends RecyclerView.Adapter<VenuesAdapter.ViewHolder> {

    List<Venue> venuesItemList;
    OnClickVenueItem onClickVenueItem;

    public VenuesAdapter(Context context, List<Venue> venuesItemList) {
        this.venuesItemList = venuesItemList;
        onClickVenueItem = (OnClickVenueItem) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venue,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Venue venue = venuesItemList.get(position);

        viewHolder.tvTitle.setText(venue.getName());
        viewHolder.tvAddress.setText(venue.getLocation().getAddress());
        viewHolder.tvCountry.setText(venue.getLocation().getCountry());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickVenueItem.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return venuesItemList.size();
    }



    //View Holder
    public static class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.item_place_tv_title)
        TextView tvTitle;

        @BindView(R.id.item_place_tv_address)
        TextView tvAddress;

        @BindView(R.id.item_place_tv_country)
        TextView tvCountry;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }






}
