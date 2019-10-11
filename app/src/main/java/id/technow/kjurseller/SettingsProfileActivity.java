package id.technow.kjurseller;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.DetailUserResponse;
import id.technow.kjurseller.model.EditUserResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsProfileActivity extends AppCompatActivity {
    private EditText etSEmail, etSPhone;
    private TextView tvSBirth;
    private int pYear, pMonth, pDay;
    private Button btnConfirm;
    Context mContext;
    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;

        etSEmail = findViewById(R.id.edtSEmail);
        etSPhone = findViewById(R.id.edtSPhone);
        tvSBirth = findViewById(R.id.edtSBirth);

        tvSBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                pYear = c.get(Calendar.YEAR);
                pMonth = c.get(Calendar.MONTH);
                pDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        pYear = year;
                        pMonth = monthOfYear+1;
                        pDay = dayOfMonth;

                        String fm = "" + pMonth;
                        String fd = "" + pDay;
                        if(pMonth < 10){
                            fm = "0" + pMonth;
                        }
                        if (pDay < 10){
                            fd = "0" + pDay;
                        }

                        tvSBirth.setText(pYear+"-"+fm+"-"+fd);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(mContext, pDateSetListener, pYear, pMonth, pDay);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                editUser();
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
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String token = user.getToken();
        Call<DetailUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .detailUser("Bearer " + token, accept);

        call.enqueue(new Callback<DetailUserResponse>() {

            @Override
            public void onResponse(Call<DetailUserResponse> call, Response<DetailUserResponse> response) {
                loading.dismiss();
                btnConfirm.setEnabled(true);
                if (response.isSuccessful()) {
                    DetailUserResponse detailUserResponse = response.body();
                    if (detailUserResponse.getStatus().equals("success")) {
                        etSEmail.setText(detailUserResponse.getDetailUser().getEmail());
                        etSPhone.setText(detailUserResponse.getDetailUser().getPhone());
                        if (detailUserResponse.getDetailUser().getBirthDate() != null) {
                            tvSBirth.setText(sdf.format(detailUserResponse.getDetailUser().getBirthDate()));
                        } else {
                            tvSBirth.setText("null");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailUserResponse> call, Throwable t) {
                loading.dismiss();
                btnConfirm.setEnabled(false);
                Toast.makeText(mContext, "Can't get user information. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void editUser() {
        String email = etSEmail.getText().toString().trim();
        String phone = etSPhone.getText().toString().trim();
        String birth = tvSBirth.getText().toString().trim();

        if (email.isEmpty()) {
            loading.dismiss();
            etSEmail.setError("Email is required");
            etSEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.dismiss();
            etSEmail.setError("Enter a valid Email");
            etSEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            loading.dismiss();
            etSPhone.setError("Phone Number is required");
            etSPhone.requestFocus();
            return;
        }

        if (phone.length() < 10 || phone.length() > 13) {
            loading.dismiss();
            etSPhone.setError("Phone Number should be at least 10-13 characters long");
            etSPhone.requestFocus();
            return;
        }

        if (birth.equals("null")) {
            loading.dismiss();
            tvSBirth.setError("Birth Date is required");
            tvSBirth.requestFocus();
            return;
        }

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<EditUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .editUser("Bearer " + token, email, phone, birth);

        call.enqueue(new Callback<EditUserResponse>() {

            @Override
            public void onResponse(Call<EditUserResponse> call, Response<EditUserResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    EditUserResponse editUserResponse = response.body();
                    if (editUserResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Profiles updated", Toast.LENGTH_LONG).show();
                        if (editUserResponse.getDetailUser().getEmailVerifyAt() == null) {
                            String emailSend = editUserResponse.getDetailUser().getEmail();
                           /* Intent intent = new Intent(SettingsProfileActivity.this, VerifyEmailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("email" , emailSend);
                            startActivity(intent);*/
                        } else {
                            SettingsProfileActivity.this.finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EditUserResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}