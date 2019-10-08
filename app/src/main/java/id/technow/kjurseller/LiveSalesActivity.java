package id.technow.kjurseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import id.technow.kjurseller.adapter.ProductListLLLiveSalesAdapter;
import id.technow.kjurseller.model.ProductToday;

public class LiveSalesActivity extends AppCompatActivity {
    private ArrayList<ProductToday> productLiveReportList;
    private RecyclerView recyclerView;
    private ProductListLLLiveSalesAdapter plAdapter;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_sales);

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
}
