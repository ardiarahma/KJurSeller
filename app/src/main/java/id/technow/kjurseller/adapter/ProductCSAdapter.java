package id.technow.kjurseller.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import id.technow.kjurseller.ProductCloseStoreActivity;
import id.technow.kjurseller.R;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.CloseStoreResponse;
import id.technow.kjurseller.model.ProductToday;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCSAdapter extends RecyclerView.Adapter<ProductCSAdapter.CustomViewHolder> {

    List<ProductToday> products;
    Context mContext;

    public ProductCSAdapter(List<ProductToday> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_cs, parent, false);

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
        private ImageView btnNext;
        private ImageView imgProduct;
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
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_close_store_item);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            dialog.getWindow().setLayout((9 * width) / 10, (2 * height) / 5);

            Button btnNo = dialog.findViewById(R.id.btnNo);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Button btnYes = dialog.findViewById(R.id.btnYes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeItem();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }

        private void closeItem() {
            User user = SharedPrefManager.getInstance(mContext).getUser();
            String token = user.getToken();
            Call<CloseStoreResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .closeItem("Bearer " + token, id);

            call.enqueue(new Callback<CloseStoreResponse>() {

                @Override
                public void onResponse(Call<CloseStoreResponse> call, Response<CloseStoreResponse> response) {
                    if (response.isSuccessful()) {
                        CloseStoreResponse closeStoreResponse = response.body();
                        if (closeStoreResponse.getStatus().equals("success")) {
                            Toast.makeText(mContext, closeStoreResponse.getMessage(), Toast.LENGTH_LONG).show();
                            refreshEvents(products);
                            if (mContext instanceof ProductCloseStoreActivity) {
                                ((ProductCloseStoreActivity) mContext).checkConnection();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<CloseStoreResponse> call, Throwable t) {
                    Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                    Log.d("TAG", "Response = " + t.toString());
                }
            });
        }
    }

    public void refreshEvents(List<ProductToday> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }
}