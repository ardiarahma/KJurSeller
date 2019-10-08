package id.technow.kjurseller.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import id.technow.kjurseller.R;
import id.technow.kjurseller.model.ProductLogHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductLogAdapter extends RecyclerView.Adapter<ProductLogAdapter.CustomViewHolder> {

    List<ProductLogHistory> productLogHistories;
    List<String> colors;

    public ProductLogAdapter(List<ProductLogHistory> productLogHistories) {
        this.productLogHistories = productLogHistories;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_log, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ProductLogHistory productLogHistory = productLogHistories.get(position);
        holder.txtRef.setText(productLogHistory.getType());
        holder.txtDateTime.setText(productLogHistory.getDateCreated());
        holder.txtRef.setText(String.valueOf(productLogHistory.getId()));
        holder.txtOldStock.setText(String.valueOf(productLogHistory.getStockOld()));
        holder.txtChangeStock.setText(String.valueOf(productLogHistory.getStockChange()));
        holder.txtNewStock.setText(String.valueOf(productLogHistory.getStockNew()));

        colors = new ArrayList<String>();

        colors.add("#e51c23");
        colors.add("#e91e63");
        colors.add("#9c27b0");
        colors.add("#673ab7");
        colors.add("#3f51b5");
        colors.add("#5677fc");
        colors.add("#03a9f4");
        colors.add("#00bcd4");
        colors.add("#009688");
        colors.add("#259b24");
        colors.add("#8bc34a");
        colors.add("#cddc39");
        colors.add("#ffeb3b");
        colors.add("#ff9800");
        colors.add("#ff5722");
        colors.add("#795548");
        colors.add("#9e9e9e");
        colors.add("#607d8b");

        Random r = new Random();
        int i1 = r.nextInt(17 - 0) + 0;

        holder.imgLiveReport.setColorFilter(Color.parseColor(colors.get(i1)), PorterDuff.Mode.SRC_IN);
        holder.txtType.setTextColor(Color.parseColor(colors.get(i1)));
        holder.txtDateTime.setTextColor(Color.parseColor(colors.get(i1)));
    }

    @Override
    public int getItemCount() {
        return productLogHistories.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtType, txtDateTime, txtRef, txtOldStock, txtChangeStock, txtNewStock;
        private ImageView imgLiveReport;

        public CustomViewHolder(View view) {
            super(view);
            txtType = itemView.findViewById(R.id.txtType);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
            txtRef = itemView.findViewById(R.id.txtRef);
            txtOldStock = itemView.findViewById(R.id.txtOldStock);
            txtChangeStock = itemView.findViewById(R.id.txtChangeStock);
            txtNewStock = itemView.findViewById(R.id.txtNewStock);
            imgLiveReport = itemView.findViewById(R.id.imgLiveReport);
        }
    }
}