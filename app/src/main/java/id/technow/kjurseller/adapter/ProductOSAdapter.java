package id.technow.kjurseller.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.technow.kjurseller.FragmentNewStock;
import id.technow.kjurseller.R;
import id.technow.kjurseller.model.ProductAll;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductOSAdapter extends RecyclerView.Adapter<ProductOSAdapter.CustomViewHolder>{

    List<ProductAll> products;
    Context mContext;
    public static final String TAG = "bottom_sheet";

    public ProductOSAdapter(List<ProductAll> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_os, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ProductAll product = products.get(position);

        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();

        fmts.setGroupingSeparator('.');
        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);
        fmt.setDecimalFormatSymbols(fmts);

        holder.txtProductName.setText(product.getProductName());
        holder.id = products.get(position).getId();
        holder.productName = products.get(position).getProductName();
        holder.productPrice = products.get(position).getProductPrice();
        holder.productPic = products.get(position).getProductPic();

        if(product.getProductPic() != null && !product.getProductPic().isEmpty()){
            Picasso.get()
                    .load(product.getProductPic())
                    //.placeholder(R.drawable.ic_snack)
                    .error(R.drawable.ic_close)
                   // .fit()
                    .resize(500, 500)
                    .centerInside()
                    // To prevent fade animation
                    .noFade()
                    .into(holder.imgProduct);
        } else{
            //holder.imgProduct.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_snack));
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtProductName;
        private LinearLayout btnNewStock;
        private ImageView imgProduct;
        private int productPrice;
        private String id, productName, productPic;

        public CustomViewHolder(View view) {
            super(view);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            btnNewStock = itemView.findViewById(R.id.btnNewStock);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnNewStock.setOnClickListener(this);
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("productId", id);
            bundle.putString("productName", productName);
            bundle.putInt("productPrice", productPrice);
            bundle.putString("productPic", productPic);
            FragmentNewStock fragment = new FragmentNewStock();
            fragment.setArguments(bundle);
            fragment.show(((FragmentActivity) mContext).getSupportFragmentManager(), TAG);
        }

    }

    public void refreshEvents(List<ProductAll> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }
}