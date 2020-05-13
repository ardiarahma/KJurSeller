package id.technow.kjurseller.adapter;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.technow.kjurseller.MapsActivity;
import id.technow.kjurseller.ProductOpenStoreActivity;
import id.technow.kjurseller.R;
import id.technow.kjurseller.model.Location;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class LocationOSAdapter extends RecyclerView.Adapter<LocationOSAdapter.CustomViewHolder> {

    List<Location> locations;

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
        holder.txtLocDesc.setText(location.getDesc());
        holder.id = locations.get(position).getId();
        holder.longitude = locations.get(position).getLongitude();
        holder.latitude = locations.get(position).getLatitude();
        int radius = 14;
        if (location.getPic() != null && !location.getPic().isEmpty()) {
            Picasso.get()
                    .load(location.getPic())
                    //.placeholder(R.drawable.ic_snack)
                    .error(R.drawable.ic_close)
                    .resize(700, 260)
                    .centerInside()
                    .noFade()
                    .transform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.TOP))
                    .into(holder.imgLoc);
        }
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtLocName, txtLocDesc;
        private ImageView imgLoc;
        private int id;
        private String longitude, latitude;

        public CustomViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            txtLocName = itemView.findViewById(R.id.txtLocName);
            txtLocDesc = itemView.findViewById(R.id.txtLocDesc);
            imgLoc = itemView.findViewById(R.id.imgLoc);
            imgLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), MapsActivity.class);
                    intent.putExtra("locId", id);
                    intent.putExtra("locName", txtLocName.getText().toString());
                    intent.putExtra("locDesc", txtLocDesc.getText().toString());
                    intent.putExtra("locLong", longitude);
                    intent.putExtra("locLat", latitude);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext(), ProductOpenStoreActivity.class);
            intent.putExtra("locAllId", id);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<Location> locations) {
        this.locations.clear();
        this.locations.addAll(locations);
        notifyDataSetChanged();
    }
}