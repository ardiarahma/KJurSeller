package id.technow.kjurseller.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.technow.kjurseller.ProductLogActivity;
import id.technow.kjurseller.R;
import id.technow.kjurseller.model.ProductToday;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import java.util.List;

public class ProductLRAdapter extends RecyclerView.Adapter<ProductLRAdapter.CustomViewHolder>{

    List<ProductToday> products;
    Context mContext;

    public ProductLRAdapter(List<ProductToday> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_lr, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ProductToday product = products.get(position);
        holder.txtProductName.setText(product.getProductName());
        holder.id = products.get(position).getId();

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
        private TextView txtProductName;
        private ImageView btnNext, imgProduct;
        private String id;

        public CustomViewHolder(View view) {
            super(view);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            btnNext = itemView.findViewById(R.id.btnNext);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnNext.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , ProductLogActivity.class);
            intent.putExtra("productId" , id);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<ProductToday> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }
}