package id.technow.kjurseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.DetailUserResponse;
import id.technow.kjurseller.model.ResendVerifyEmailResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.model.VerifyEmailResponse;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailActivity extends AppCompatActivity {
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    TextView txtEmail, btnResend, txtTime;
    LinearLayout layoutTimer;
    Context mContext;
    ProgressDialog loading;
    CountDownTimer cTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        txtEmail = findViewById(R.id.txtEmail);
        btnResend = findViewById(R.id.btnResend);
        txtTime = findViewById(R.id.txtTime);
        layoutTimer = findViewById(R.id.layoutTimer);
        mContext = this;

        Intent intent = getIntent();
        String emailSend = intent.getStringExtra("email");
        String emailCensoredFirst = emailSend.substring(0, 3);
        String emailCensoredLast = emailSend.substring(emailSend.length() - 4);
        txtEmail.setText(emailCensoredFirst + "***@***" + emailCensoredLast);
        layoutTimer.setVisibility(View.GONE);

        Bundle extras = intent.getExtras();
        if(extras.containsKey("fromSettings")) {
            resendVerifyUser();
        }

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerifyUser();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                detailUser();
            }
        }, 30000);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        cancelTimer();
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailUser();
    }

    private void startTimer() {
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                layoutTimer.setVisibility(View.VISIBLE);
                txtTime.setText("" + millisUntilFinished / 1000);
                btnResend.setTextColor(getResources().getColor(R.color.light_grey));
                btnResend.setClickable(false);
            }

            public void onFinish() {
                layoutTimer.setVisibility(View.GONE);
                btnResend.setTextColor(getResources().getColor(R.color.white));
                btnResend.setClickable(true);
            }
        };
        cTimer.start();
    }

    private void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    public void resendVerifyUser() {
        loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ResendVerifyEmailResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .resendVerifyUser("Bearer " + token);

        call.enqueue(new Callback<ResendVerifyEmailResponse>() {

            @Override
            public void onResponse(Call<ResendVerifyEmailResponse> call, Response<ResendVerifyEmailResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    ResendVerifyEmailResponse resendVerifyEmailResponse = response.body();
                    if (resendVerifyEmailResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Please check your email.", Toast.LENGTH_LONG).show();
                        startTimer();
                    } else {
                        Toast.makeText(mContext, resendVerifyEmailResponse.getMessage(), Toast.LENGTH_LONG).show();
                        layoutTimer.setVisibility(View.GONE);
                        btnResend.setTextColor(getResources().getColor(R.color.white));
                        btnResend.setClickable(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResendVerifyEmailResponse> call, Throwable t) {
                loading.dismiss();
                layoutTimer.setVisibility(View.GONE);
                btnResend.setTextColor(getResources().getColor(R.color.white));
                btnResend.setClickable(true);
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void detailUser() {
        String accept = "application/json";

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<DetailUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .detailUser(accept, "Bearer " + token);

        call.enqueue(new Callback<DetailUserResponse>() {

            @Override
            public void onResponse(Call<DetailUserResponse> call, Response<DetailUserResponse> response) {
                DetailUserResponse detailUserResponse = response.body();
                if (response.isSuccessful()) {
                    if (detailUserResponse.getStatus().equals("success")) {
                        Intent intent = new Intent(VerifyEmailActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<DetailUserResponse> call, Throwable t) {
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
