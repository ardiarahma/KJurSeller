package id.technow.kjurseller.adapter;

import android.content.Context;
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
    Context mContext;

    public ProductLogAdapter(List<ProductLogHistory> productLogHistories, Context mContext) {
        this.productLogHistories = productLogHistories;
        this.mContext = mContext;
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
        holder.txtType.setText(productLogHistory.getType());
        holder.txtRef.setText(productLogHistory.getReferenceId());
        holder.txtDateTime.setText(productLogHistory.getDateCreated());
        holder.txtRef.setText(String.valueOf(productLogHistory.getId()));
        holder.txtOldStock.setText(String.valueOf(productLogHistory.getStockOld()));
        holder.txtChangeStock.setText(String.valueOf(productLogHistory.getStockChange()));
        holder.txtNewStock.setText(String.valueOf(productLogHistory.getStockNew()));

        String type = productLogHistory.getType();
        if (type.equals("Open Stock")){
            holder.imgLiveReport.setColorFilter(mContext.getResources().getColor(R.color.colorPrimaryDark));
            holder.txtType.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            holder.txtDateTime.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        } else if (type.equals("Transaction")){
            holder.imgLiveReport.setColorFilter(mContext.getResources().getColor(R.color.dark_slate_gray));
            holder.txtType.setTextColor(mContext.getResources().getColor(R.color.dark_slate_gray));
            holder.txtDateTime.setTextColor(mContext.getResources().getColor(R.color.dark_slate_gray));
        } else {
            holder.imgLiveReport.setColorFilter(mContext.getResources().getColor(R.color.red));
            holder.txtType.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.txtDateTime.setTextColor(mContext.getResources().getColor(R.color.red));
        }
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