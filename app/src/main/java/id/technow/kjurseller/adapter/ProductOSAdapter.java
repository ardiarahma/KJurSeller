package id.technow.kjurseller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.technow.kjurseller.FragmentNewStock;
import id.technow.kjurseller.R;
import id.technow.kjurseller.model.ProductAll;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class ProductOSAdapter extends RecyclerView.Adapter<ProductOSAdapter.CustomViewHolder> {

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

    @SuppressLint("ResourceType")
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
        holder.status = products.get(position).getStatus();
        if (holder.status.equals("Close")) {
            holder.btnNewStock.setClickable(true);
            holder.txtProductStatus.setVisibility(View.GONE);
            holder.btnNewStock.setBackgroundResource(R.drawable.btn_rounded_corner_primary_dark);
        } else {
            holder.btnNewStock.setClickable(false);
            holder.txtProductStatus.setVisibility(View.VISIBLE);
            holder.btnNewStock.setBackgroundResource(R.drawable.btn_rounded_corner_gray);
        }

        int radius = 10;
        if (product.getProductPic() != null && !product.getProductPic().isEmpty()) {
            Picasso.get()
                    .load(product.getProductPic())
                    .placeholder(R.drawable.ic_close)
                    .error(R.drawable.ic_close)
                    .resize(500, 500)
                    .centerInside()
                    .noFade()
                    .transform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_close));
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtProductName, txtProductStatus;
        private LinearLayout btnNewStock;
        private ImageView imgProduct;
        private int productPrice;
        private String id, productName, productPic, status;

        public CustomViewHolder(View view) {
            super(view);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            btnNewStock = itemView.findViewById(R.id.btnNewStock);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductStatus = itemView.findViewById(R.id.txtProductStatus);
            btnNewStock.setOnClickListener(this);
        }

        @Override
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