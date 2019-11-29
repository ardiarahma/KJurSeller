package id.technow.kjurseller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class LiveSalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_sales);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LiveSalesActivity.this, MainActivity.class));
            }
        });

        LinearLayout btnLiveReport = findViewById(R.id.btnLiveReport);
        btnLiveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LiveSalesActivity.this, LocLiveReportActivity.class));
            }
        });

        LinearLayout btnUpdateStock = findViewById(R.id.btnUpdateStock);
        btnUpdateStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LiveSalesActivity.this, LocUpdateStockActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LiveSalesActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
