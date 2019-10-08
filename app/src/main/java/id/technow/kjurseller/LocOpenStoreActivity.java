package id.technow.kjurseller;

import android.app.ProgressDialog;
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

import id.technow.kjurseller.adapter.LocationOSAdapter;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.Location;
import id.technow.kjurseller.model.LocationResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocOpenStoreActivity extends AppCompatActivity {
    private ArrayList<Location> locationList;
    private RecyclerView recyclerView;
    private LocationOSAdapter lAdapter;
    private SwipeRefreshLayout swipeRefresh;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_open_store);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

      /*  swipeRefresh = findViewById(R.id.swipeContainer);
        //swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (lAdapter != null) {
                    lAdapter.refreshEvents(locationList);
                }
                locAll();
            }
        });*/
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(LocOpenStoreActivity.this, null, getString(R.string.please_wait), true, false);
        locAll();
    }

    private void locAll() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<LocationResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .locAll("Bearer " + token);

        call.enqueue(new Callback<LocationResponse>() {

            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                loading.dismiss();
                LocationResponse locationResponse = response.body();
                if (response.isSuccessful()) {
                    if (locationResponse.getStatus().equals("success")) {
                        locationList = locationResponse.getLocation();
                        recyclerView = (RecyclerView) findViewById(R.id.listLoc);
                        lAdapter = new LocationOSAdapter(locationList);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(lAdapter);
                    }
                }
                //swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                loading.dismiss();
                //swipeRefresh.setRefreshing(false);
                Toast.makeText(LocOpenStoreActivity.this, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }
}
