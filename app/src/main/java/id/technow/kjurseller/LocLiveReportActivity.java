package id.technow.kjurseller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;

import id.technow.kjurseller.adapter.LocationLRAdapter;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.LocationToday;
import id.technow.kjurseller.model.LocationTodayResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocLiveReportActivity extends AppCompatActivity {
    private ArrayList<LocationToday> locationTodayList;
    private RecyclerView recyclerView;
    private LocationLRAdapter lAdapter;
    private SwipeRefreshLayout swipeRefresh;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_live_report);

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
                if (lAdapter != null) {
                    lAdapter.refreshEvents(locationTodayList);
                }
                checkConnection();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(LocLiveReportActivity.this, null, getString(R.string.please_wait), true, false);
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
            locToday();
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
                        lAdapter = new LocationLRAdapter(locationTodayList);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(lAdapter);
                    }
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<LocationTodayResponse> call, Throwable t) {
                loading.dismiss();
                swipeRefresh.setRefreshing(false);
                Toast.makeText(LocLiveReportActivity.this, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }
}
