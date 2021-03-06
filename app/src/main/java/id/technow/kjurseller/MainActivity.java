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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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

        mContext = this;

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
            detailUser();
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

                        DecimalFormat fmt = new DecimalFormat();
                        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
                        fmts.setGroupingSeparator('.');
                        fmt.setGroupingSize(3);
                        fmt.setGroupingUsed(true);
                        fmt.setDecimalFormatSymbols(fmts);

                        txtBalance.setText(String.valueOf(fmt.format(detailUserResponse.getDetailUser().getBalance())));
                        txtStoreName.setText(detailUserResponse.getDetailUser().getDetailStore().getStoreName());
                        txtProduct.setText(String.valueOf(detailUserResponse.getProduct()) );
                        Picasso.get().load(detailUserResponse.getDetailUser().getPic()).error(R.drawable.ic_person).into(imgProfile);
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
                        Log.e("TAG", "Response = " + e.toString());
                        Log.e("Error", "Response = " + e.getMessage());
                        //Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
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
