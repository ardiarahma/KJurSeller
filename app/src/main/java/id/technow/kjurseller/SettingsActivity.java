package id.technow.kjurseller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.LogoutResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;

        LinearLayout llProfiles = this.findViewById(R.id.btnProfile);
        llProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsProfileActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llBankInfo = this.findViewById(R.id.btnBankInfo);
        llBankInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsBankInfoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llChangePassword = this.findViewById(R.id.btnChangePass);
        llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsChangePassActivity.class);
                startActivity(intent);
            }
        });

        Button llLogOut = this.findViewById(R.id.btnLogout);
        llLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogOut();
            }
        });
    }

    private void logOut(){
        SharedPrefManager.getInstance(this).clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void confirmLogOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirmation);
        builder.setMessage(R.string.are_you_sure_logout);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutUser();
                logOut();
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void logoutUser() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<LogoutResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .logoutUser("Bearer " + token);

        call.enqueue(new Callback<LogoutResponse>() {

            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    LogoutResponse logoutResponse = response.body();
                    Toast.makeText(mContext, logoutResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                //Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
