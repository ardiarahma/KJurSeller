package id.technow.kjurseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONObject;

import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.ForgotPasswordResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassActivity extends AppCompatActivity {
    TextInputLayout ilEmail;
    TextInputEditText etEmail;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        ilEmail = findViewById(R.id.layoutEdtEmail);
        etEmail = findViewById(R.id.edtEmail);

        mContext = this;

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = etEmail.getText().toString();

                if (email.isEmpty()) {
                    ilEmail.setError("Email is required");
                } else if (!isValidEmail(email)) {
                    ilEmail.setError("Enter a valid address");
                } else {
                    ilEmail.setError(null);
                }
            }
        });

        Button btnResetPass = this.findViewById(R.id.btnResetPass);
        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fpUser();
            }
        });
    }

    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void fpUser() {
        loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
        String accept = "application/json";
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            loading.dismiss();
            ilEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.dismiss();
            ilEmail.setError("Enter a valid Email");
            etEmail.requestFocus();
            return;
        }

        Call<ForgotPasswordResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .fpUser(accept, email);

        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                ForgotPasswordResponse forgotPasswordResponse = response.body();
                if (response.isSuccessful()){
                    if (forgotPasswordResponse.getStatus().equals("Success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        Toast.makeText(mContext, forgotPasswordResponse.getMessage(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(mContext, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
