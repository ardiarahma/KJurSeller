package id.technow.kjurseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.DetailUserResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ImageView btnSettings;
    private LinearLayout btnBalance, btnOpenStore, btnLiveSales, btnCloseStore, btnReport;
    private TextView txtUsername, txtEmail, txtBalance, txtStoreName, txtProduct;
    private CircleImageView imgProfile;
    private SwipeRefreshLayout swipeRefresh;
    ProgressDialog loading;
    Context mContext;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        btnBalance = findViewById(R.id.btnBalance);
        btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WalletActivity.class));
            }
        });

        btnOpenStore = findViewById(R.id.btnOpenStore);
        btnOpenStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocOpenStoreActivity.class));
            }
        });
        btnLiveSales = findViewById(R.id.btnLiveSales);
        btnLiveSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LiveSalesActivity.class));
            }
        });
        btnCloseStore = findViewById(R.id.btnCloseStore);
        btnCloseStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocCloseStoreActivity.class));
            }
        });
        btnReport = findViewById(R.id.btnReport);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReportActivity.class));
            }
        });
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtStoreName = findViewById(R.id.txtStoreName);
        txtBalance = findViewById(R.id.txtBalance);
        txtProduct = findViewById(R.id.txtProduct);
        imgProfile = findViewById(R.id.imgProfile);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                detailUser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    public void onStart() {
        super.onStart();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
        detailUser();
    }

    private void detailUser() {
        String accept = "application/json";

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<DetailUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .detailUser("Bearer " + token, accept);

        call.enqueue(new Callback<DetailUserResponse>() {

            @Override
            public void onResponse(Call<DetailUserResponse> call, Response<DetailUserResponse> response) {
                //loading.dismiss();
                if (response.isSuccessful()) {
                    DetailUserResponse detailUserResponse = response.body();
                    if (detailUserResponse.getStatus().equals("success")) {
                        txtEmail.setText(detailUserResponse.getDetailUser().getEmail());
                        txtUsername.setText(detailUserResponse.getDetailUser().getName());
                        txtBalance.setText(String.valueOf(detailUserResponse.getDetailUser().getBalance()));
                        txtStoreName.setText(detailUserResponse.getDetailUser().getDetailStore().getStoreName());
                        txtProduct.setText(String.valueOf(detailUserResponse.getProduct()) + " Product");
                        Picasso.get().load(detailUserResponse.getDetailUser().getPic()).into(imgProfile);
                    } else {
                        String emailSend = detailUserResponse.getEmail();
                      /*  Intent intent = new Intent(MainActivity.this, VerifyEmailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("email", emailSend);
                        startActivity(intent);*/
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(mContext, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        if (jObjError.getString("message").equals("Unauthenticated.")) {
                            SharedPrefManager.getInstance(MainActivity.this).clear();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DetailUserResponse> call, Throwable t) {
                //loading.dismiss();
                swipeRefresh.setRefreshing(false);
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

}
