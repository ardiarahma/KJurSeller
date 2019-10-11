package id.technow.kjurseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import id.technow.kjurseller.adapter.ProductUSAdapter;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.ProductListTodayResponse;
import id.technow.kjurseller.model.ProductToday;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductUpdateStockActivity extends AppCompatActivity {
    private ArrayList<ProductToday> productLiveReportList;
    private RecyclerView recyclerView;
    private ProductUSAdapter pAdapter;
    private SwipeRefreshLayout swipeRefresh;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update_stock);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductUpdateStockActivity.this, LocUpdateStockActivity.class));
            }
        });

        recyclerView = findViewById(R.id.listProduct);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (pAdapter != null) {
                    pAdapter.refreshEvents(productLiveReportList);
                }
                productList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading = ProgressDialog.show(ProductUpdateStockActivity.this, null, getString(R.string.please_wait), true, false);
        productList();
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
                        productLiveReportList = productListTodayResponse.getProduct();
                        pAdapter = new ProductUSAdapter(productLiveReportList, ProductUpdateStockActivity.this);
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
                Toast.makeText(ProductUpdateStockActivity.this, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProductUpdateStockActivity.this, LocUpdateStockActivity.class));
    }
}
