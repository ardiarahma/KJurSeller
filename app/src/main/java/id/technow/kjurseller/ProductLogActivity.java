package id.technow.kjurseller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import id.technow.kjurseller.adapter.ProductLogAdapter;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.ProductLogHistory;
import id.technow.kjurseller.model.ProductLogResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductLogActivity extends AppCompatActivity {
    private ArrayList<ProductLogHistory> productLogHistoryList;
    private RecyclerView recyclerView;
    private ProductLogAdapter plAdapter;
    private TextView tvProductName, tvProductStockNow;
    private SwipeRefreshLayout swipeRefresh;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_log);

        mContext = this;

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvProductName = findViewById(R.id.txtProductName);
        tvProductStockNow = findViewById(R.id.txtProductStockNow);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkConnection();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading = ProgressDialog.show(ProductLogActivity.this, null, getString(R.string.please_wait), true, false);
        checkConnection();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkConnection() {
        if (isNetworkAvailable()) {
            productLog();
        } else {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_no_internet);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            Button btnRetry = dialog.findViewById(R.id.btnRetry);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    checkConnection();
                }
            });
            dialog.show();
        }
    }

    private void productLog() {
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ProductLogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .productLog("Bearer " + token, productId);

        call.enqueue(new Callback<ProductLogResponse>() {

            @Override
            public void onResponse(Call<ProductLogResponse> call, Response<ProductLogResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    ProductLogResponse productLogResponse = response.body();
                    if (productLogResponse.getStatus().equals("success")) {
                        tvProductName.setText(productLogResponse.getProductLogStock().getProductName());
                        tvProductStockNow.setText(String.valueOf(productLogResponse.getProductLogStock().getProductStock()));
                        productLogHistoryList = productLogResponse.getProductLogHistory();
                        recyclerView = (RecyclerView) findViewById(R.id.listProductLog);
                        plAdapter = new ProductLogAdapter(productLogHistoryList, mContext);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(plAdapter);
                        if (eLayoutManager.getItemCount() == 0) {
                            //Do something
                        }
                    }
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProductLogResponse> call, Throwable t) {
                loading.dismiss();
                swipeRefresh.setRefreshing(false);
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
