package id.technow.kjurseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.DetailUserResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.model.WithdrawResponse;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawActivity extends AppCompatActivity {
    private TextView txtNoInfo, txtHolderName, txtHolderBankNumber, txtBalance;
    private EditText edtAmount;
    private Button btnWithdraw;
    private LinearLayout layoutBankInfo;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;

        layoutBankInfo = findViewById(R.id.layoutBankInfo);
        txtNoInfo = findViewById(R.id.txtNoInfo);
        txtHolderName = findViewById(R.id.txtHolderName);
        txtHolderBankNumber = findViewById(R.id.txtHolderBankNumber);
        txtBalance = findViewById(R.id.txtBalance);
        edtAmount = findViewById(R.id.edtAmount);
        btnWithdraw = findViewById(R.id.btnWithdraw);

        layoutBankInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WithdrawActivity.this, SettingsBankInfoActivity.class);
                startActivity(i);
            }
        });

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                withdraw();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
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
                loading.dismiss();
                btnWithdraw.setEnabled(true);
                if (response.isSuccessful()) {
                    DetailUserResponse detailUserResponse = response.body();
                    if (detailUserResponse.getStatus().equals("success")) {
                        txtBalance.setText(String.valueOf(detailUserResponse.getDetailUser().getBalance()));
                        BigInteger bankAccNumber = detailUserResponse.getDetailUser().getDetailStore().getDetailBankAccount().getBankAccNumber();
                        if (bankAccNumber != null) {
                            txtNoInfo.setVisibility(View.GONE);
                            txtHolderBankNumber.setVisibility(View.VISIBLE);
                            txtHolderBankNumber.setText(String.valueOf(bankAccNumber));
                        }
                        String bankHolderName = detailUserResponse.getDetailUser().getDetailStore().getDetailBankAccount().getBankHolderName();
                        if (bankHolderName != null) {
                            txtNoInfo.setVisibility(View.GONE);
                            txtHolderName.setVisibility(View.VISIBLE);
                            txtHolderName.setText(bankHolderName);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailUserResponse> call, Throwable t) {
                loading.dismiss();
                btnWithdraw.setEnabled(false);
                Toast.makeText(mContext, "Can't get bank information. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void withdraw() {
        String amountET = edtAmount.getText().toString();

        if (amountET.isEmpty()) {
            loading.dismiss();
            edtAmount.setError("Withdraw amount is required");
            edtAmount.requestFocus();
            return;
        }

        int amount = Integer.parseInt(amountET);

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<WithdrawResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .withdraw("Bearer " + token, amount);

        call.enqueue(new Callback<WithdrawResponse>() {

            @Override
            public void onResponse(Call<WithdrawResponse> call, Response<WithdrawResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    WithdrawResponse withdrawResponse = response.body();
                    if (withdrawResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                    else {
                        Toast.makeText(mContext, withdrawResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<WithdrawResponse> call, Throwable t) {
                loading.dismiss();
                btnWithdraw.setEnabled(false);
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
