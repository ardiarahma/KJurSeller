package id.technow.kjurseller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView btnSettings;
    private LinearLayout btnBalance, btnOpenStore, btnLiveSales, btnCloseStore, btnReport;
    private TextView txtUsername, txtEmail, txtBalance;

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
        /*
        btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        */
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
        btnOpenStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReportActivity.class));
            }
        });
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtBalance = findViewById(R.id.txtBalance);
    }
}
