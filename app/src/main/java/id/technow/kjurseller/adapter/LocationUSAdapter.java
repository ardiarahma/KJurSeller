package id.technow.kjurseller.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.technow.kjurseller.ProductLiveReportActivity;
import id.technow.kjurseller.ProductUpdateStockActivity;
import id.technow.kjurseller.R;
import id.technow.kjurseller.model.LocationToday;

public class LocationUSAdapter extends RecyclerView.Adapter<LocationUSAdapter.CustomViewHolder> {

    List<LocationToday> locationTodays;

    public LocationUSAdapter(List<LocationToday> locationTodays) {
        this.locationTodays = locationTodays;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_location, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        LocationToday locationToday = locationTodays.get(position);
        holder.txtLocName.setText(locationToday.getNama());
        holder.id = locationTodays.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return locationTodays.size();
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
            Intent intent = new Intent(itemView.getContext(), ProductUpdateStockActivity.class);
            intent.putExtra("locTodayId", id);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<LocationToday> locationTodays) {
        this.locationTodays.clear();
        this.locationTodays.addAll(locationTodays);
        notifyDataSetChanged();
    }
}