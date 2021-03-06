package id.technow.kjurseller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import id.technow.kjurseller.adapter.ProductCSAdapter;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.CloseStoreResponse;
import id.technow.kjurseller.model.ProductListTodayResponse;
import id.technow.kjurseller.model.ProductToday;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCloseStoreActivity extends AppCompatActivity {
    private ArrayList<ProductToday> productCloseStoreList;
    private RecyclerView recyclerView;
    private ProductCSAdapter pAdapter;
    private SwipeRefreshLayout swipeRefresh;
    ProgressDialog loading;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_close_store);

        mContext = this;

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView btnCloseAll = findViewById(R.id.btnCloseAll);
        btnCloseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeStore();
            }
        });

        recyclerView = findViewById(R.id.listProduct);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (pAdapter != null) {
                    pAdapter.refreshEvents(productCloseStoreList);
                }
                checkConnection();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading = ProgressDialog.show(ProductCloseStoreActivity.this, null, getString(R.string.please_wait), true, false);
        checkConnection();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void checkConnection() {
        if (isNetworkAvailable()) {
            productList();
        } else {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_no_internet);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            dialog.getWindow().setLayout((9 * width) / 10, height);

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

    private void productList() {
        Intent intent = getIntent();
        int locTodayId = intent.getIntExtra("locTodayId", 0);

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ProductListTodayResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .productListToday("Bearer " + token, locTodayId);

        call.enqueue(new Callback<ProductListTodayResponse>() {

            @Override
            public void onResponse(Call<ProductListTodayResponse> call, Response<ProductListTodayResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    ProductListTodayResponse productListTodayResponse = response.body();
                    if (productListTodayResponse.getStatus().equals("success")) {
                        productCloseStoreList = productListTodayResponse.getProduct();
                        pAdapter = new ProductCSAdapter(productCloseStoreList, mContext);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(pAdapter);
                        if (eLayoutManager.getItemCount() == 0) {
                            //Do something
                        }
                    }
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProductListTodayResponse> call, Throwable t) {
                loading.dismiss();
                swipeRefresh.setRefreshing(false);
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void closeStore() {
        Intent intent = getIntent();
        int locTodayId = intent.getIntExtra("locTodayId", 0);

        User user = SharedPrefManager.getInstance(mContext).getUser();
        String token = user.getToken();
        Call<CloseStoreResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .closeStore("Bearer " + token, locTodayId);

        call.enqueue(new Callback<CloseStoreResponse>() {

            @Override
            public void onResponse(Call<CloseStoreResponse> call, Response<CloseStoreResponse> response) {
                if (response.isSuccessful()) {
                    CloseStoreResponse closeStoreResponse = response.body();
                    if (closeStoreResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, closeStoreResponse.getMessage(), Toast.LENGTH_LONG).show();
                        onBackPressed();
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
