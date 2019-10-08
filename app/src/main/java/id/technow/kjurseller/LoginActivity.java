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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.LoginResponse;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout ilEmail, ilPassword;
    TextInputEditText etEmail, etPassword;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ilEmail = findViewById(R.id.layoutEdtEmail);
        ilPassword = findViewById(R.id.layoutEdtPassword);

        etEmail = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.edtPassword);

        mContext = this;

       /* etEmail.addTextChangedListener(new TextWatcher() {
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
*/
       /* etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = etPassword.getText().toString();

                if (password.isEmpty()) {
                    ilPassword.setError("Password is required");
                } else if (password.length() < 8) {
                    ilPassword.setError("Password should be at least 8 characters long");
                } else {
                    ilPassword.setError(null);
                }
            }
        });
*/
        TextView tvForgotPass = findViewById(R.id.btnForgotPass);
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        //        startActivity(intent);
            }
        });

        Button btnLogin = this.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                loginUser();
            }
        });
    }

    public void onStart(){
        super.onStart();
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            //loading.dismiss();
            ilEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            //loading.dismiss();
            ilPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .loginUser(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (response.isSuccessful()) {
                    if (loginResponse.getStatus().equals("success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        //loading.dismiss();
                        SharedPrefManager.getInstance(LoginActivity.this).saveUser(loginResponse.getUser());
                        Toast.makeText(mContext, "Login successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Log.i("debug", "onResponse: FAILED");
                        //loading.dismiss();
                        Toast.makeText(mContext, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //loading.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(mContext, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                //loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }
}

