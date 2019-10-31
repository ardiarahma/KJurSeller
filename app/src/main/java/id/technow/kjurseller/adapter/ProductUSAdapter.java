package id.technow.kjurseller.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.technow.kjurseller.FragmentUpdateStock;
import id.technow.kjurseller.R;
import id.technow.kjurseller.model.ProductToday;

import java.util.List;

public class ProductUSAdapter extends RecyclerView.Adapter<ProductUSAdapter.CustomViewHolder> {

    List<ProductToday> products;
    Context mContext;
    public static final String TAG = "bottom_sheet";

    public ProductUSAdapter(List<ProductToday> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_us, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ProductToday product = products.get(position);
        holder.txtProductName.setText(product.getProductName());
        holder.txtCurrentStock.setText(String.valueOf(product.getProductStockAll()));
        holder.id = products.get(position).getId();
        holder.productName = products.get(position).getProductName();
        holder.productStock = products.get(position).getProductStockNow();
        holder.productPic = products.get(position).getProductPic();

        if(product.getProductPic() != null && !product.getProductPic().isEmpty()){
            Picasso.get()
                    .load(product.getProductPic())
                    //.placeholder(R.drawable.ic_snack)
                    .error(R.drawable.ic_close)
                    .resize(500, 500)
                    .centerInside()
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
        private TextView txtProductName, txtCurrentStock;
        private LinearLayout btnUpdateStock;
        private ImageView imgProduct;
        private int productStock;
        private String id, productName, productPic;

        public CustomViewHolder(View view) {
            super(view);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtCurrentStock = itemView.findViewById(R.id.txtCurrentStock);
            btnUpdateStock = itemView.findViewById(R.id.btnUpdateStock);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnUpdateStock.setOnClickListener(this);
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("productId", id);
            bundle.putString("productName", productName);
            bundle.putInt("productStock", productStock);
            bundle.putString("productPic", productPic);

            FragmentUpdateStock fragment = new FragmentUpdateStock();
            fragment.setArguments(bundle);
            fragment.show(((FragmentActivity) mContext).getSupportFragmentManager(), TAG);
        }
    }

    public void refreshEvents(List<ProductToday> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }
}