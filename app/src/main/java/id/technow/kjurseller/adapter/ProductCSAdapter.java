package id.technow.kjurseller.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.technow.kjurseller.ProductCloseStoreActivity;
import id.technow.kjurseller.R;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.CloseStoreResponse;
import id.technow.kjurseller.model.ProductToday;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtProductName;
        private ImageView btnNext;
        private View viewColor;
        private String id;

        public CustomViewHolder(View view) {
            super(view);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            btnNext = itemView.findViewById(R.id.btnNext);
            btnNext.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.confirmation);
            builder.setMessage(mContext.getString(R.string.are_you_sure_close_item) + "?");
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    closeItem();
                }
            });

            builder.show();
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
                            Intent intent = new Intent(itemView.getContext(), ProductCloseStoreActivity.class);
                            itemView.getContext().startActivity(intent);
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