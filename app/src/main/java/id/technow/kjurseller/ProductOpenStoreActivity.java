package id.technow.kjurseller;

import android.app.ProgressDialog;
import android.content.Context;
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

import id.technow.kjurseller.adapter.ProductOSAdapter;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.ProductAll;
import id.technow.kjurseller.model.ProductListAllResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductOpenStoreActivity extends AppCompatActivity {
    private ArrayList<ProductAll> productOpenStoreList;
    private RecyclerView recyclerView;
    private ProductOSAdapter pAdapter;
    private SwipeRefreshLayout swipeRefresh;
    ProgressDialog loading;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_open_store);

        mContext = this;

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (pAdapter != null) {
                    pAdapter.refreshEvents(productOpenStoreList);
                }
                productList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading = ProgressDialog.show(ProductOpenStoreActivity.this, null, getString(R.string.please_wait), true, false);
        productList();
    }

    private void productList() {
        Intent intent = getIntent();
        int locAllId = intent.getIntExtra("locAllId", 0);

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ProductListAllResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .productListAll("Bearer " + token, locAllId);

        call.enqueue(new Callback<ProductListAllResponse>() {

            @Override
            public void onResponse(Call<ProductListAllResponse> call, Response<ProductListAllResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    productOpenStoreList = response.body().getProduct();
                    recyclerView = (RecyclerView) findViewById(R.id.listProduct);
                    pAdapter = new ProductOSAdapter(productOpenStoreList, mContext);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(pAdapter);
                    if (eLayoutManager.getItemCount() == 0) {
                        //Do something
                    }
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProductListAllResponse> call, Throwable t) {
                loading.dismiss();
                swipeRefresh.setRefreshing(false);
                Toast.makeText(ProductOpenStoreActivity.this, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
