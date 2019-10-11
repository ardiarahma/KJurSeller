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

import java.util.ArrayList;

import id.technow.kjurseller.adapter.LocationUSAdapter;
import id.technow.kjurseller.adapter.ProductUSAdapter;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.LocationToday;
import id.technow.kjurseller.model.LocationTodayResponse;
import id.technow.kjurseller.model.ProductToday;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocUpdateStockActivity extends AppCompatActivity {
    private ArrayList<LocationToday> locationTodayList;
    private RecyclerView recyclerView;
    private LocationUSAdapter lAdapter;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_update_stock);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             startActivity(new Intent(LocUpdateStockActivity.this, LiveSalesActivity.class));
            }
        });

        swipeContainer = findViewById(R.id.swipeRefresh);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (lAdapter != null) {
                    lAdapter.refreshEvents(locationTodayList);
                }
                locToday();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(LocUpdateStockActivity.this, null, getString(R.string.please_wait), true, false);
        locToday();
    }

    private void locToday() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<LocationTodayResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .locToday("Bearer " + token);

        call.enqueue(new Callback<LocationTodayResponse>() {

            @Override
            public void onResponse(Call<LocationTodayResponse> call, Response<LocationTodayResponse> response) {
                loading.dismiss();
                LocationTodayResponse locationTodayResponse = response.body();
                if (response.isSuccessful()) {
                    if (locationTodayResponse.getStatus().equals("success")) {
                        locationTodayList = locationTodayResponse.getLocationToday();
                        recyclerView = (RecyclerView) findViewById(R.id.listLoc);
                        lAdapter = new LocationUSAdapter(locationTodayList);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(lAdapter);
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<LocationTodayResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LocUpdateStockActivity.this, LiveSalesActivity.class));
    }
}
