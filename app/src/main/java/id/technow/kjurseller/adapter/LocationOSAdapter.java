package id.technow.kjurseller.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import id.technow.kjurseller.ProductOpenStoreActivity;
import id.technow.kjurseller.R;
import id.technow.kjurseller.model.Location;

public class LocationOSAdapter extends RecyclerView.Adapter<LocationOSAdapter.CustomViewHolder>{

    List<Location> locations;
    List<String> colors;

    public LocationOSAdapter(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_location, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.txtLocName.setText(location.getNama());
        //holder.txtLocDesc.setText();
        holder.id = locations.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtLocName, txtLocDesc;
        private int id;

        public CustomViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            txtLocName = itemView.findViewById(R.id.txtLocName);
            txtLocDesc = itemView.findViewById(R.id.txtLocDesc);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , ProductOpenStoreActivity.class);
            intent.putExtra("locAllId" , id);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<Location> locations) {
        this.locations.clear();
        this.locations.addAll(locations);
        notifyDataSetChanged();
    }
}