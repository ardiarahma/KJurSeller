package id.technow.kjurseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import id.technow.kjurseller.adapter.WalletHistoryAdapter;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.model.WalletLog;
import id.technow.kjurseller.model.WalletResponse;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {
    private ArrayList<WalletLog> walletLogList;
    private RecyclerView recyclerView;
    private WalletHistoryAdapter wAdapter;
    private TextView txtBalanceAll, txtAccNumber;
    private SwipeRefreshLayout swipeRefresh;
    private CardView btnWithdraw;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtBalanceAll = findViewById(R.id.txtBalanceAll);
        txtAccNumber = findViewById(R.id.txtAccNumber);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (WalletActivity.this, WithdrawActivity.class);
                startActivity(i);
            }
        });

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                walletDetail();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(WalletActivity.this, null, getString(R.string.please_wait), true, false);
        walletDetail();
    }

    private void walletDetail() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<WalletResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .gamapay("Bearer " + token);

        call.enqueue(new Callback<WalletResponse>() {

            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    WalletResponse walletResponse = response.body();
                    if (walletResponse.getStatus().equals("success")) {
                        txtBalanceAll.setText(String.valueOf(walletResponse.getWalletAccount().getBalance()));
                        txtAccNumber.setText(walletResponse.getWalletAccount().getPhoneNumber());
                        walletLogList = walletResponse.getWalletLog();
                        recyclerView = (RecyclerView) findViewById(R.id.listWalletHistory);
                        wAdapter = new WalletHistoryAdapter(walletLogList);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(wAdapter);
                        if (eLayoutManager.getItemCount() == 0) {
                            //Do something
                        }
                    }
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                loading.dismiss();
                swipeRefresh.setRefreshing(false);
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
