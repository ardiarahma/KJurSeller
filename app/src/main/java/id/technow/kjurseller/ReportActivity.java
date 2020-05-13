package id.technow.kjurseller;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import id.technow.kjurseller.adapter.ReportAdapter;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.ReportList;
import id.technow.kjurseller.model.ReportListResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    private TextView dateFrom, dateUntil, txtRStock, txtRSold, txtRRemain, txtRLocation, txtRItem, txtRIncome;
    private LinearLayout layoutResult;
    private int pYear, pMonth, pDay;
    private ArrayList<ReportList> reportList;
    private RecyclerView recyclerView;
    private ReportAdapter rAdapter;
    Context mContext;
    private SwipeRefreshLayout swipeRefresh;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;

        dateFrom = findViewById(R.id.dateFrom);
        dateUntil = findViewById(R.id.dateUntil);
        txtRStock = findViewById(R.id.txtRStock);
        txtRSold = findViewById(R.id.txtRSold);
        txtRRemain = findViewById(R.id.txtRRemain);
        txtRLocation = findViewById(R.id.txtRLocation);
        txtRItem = findViewById(R.id.txtRItem);
        txtRIncome = findViewById(R.id.txtRIncome);

        layoutResult = findViewById(R.id.layoutResult);

        recyclerView = findViewById(R.id.listReport);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date().getTime());

        dateFrom.setText(currentDate);
        dateUntil.setText(currentDate);

        dateFrom.setOnClickListener(new View.OnClickListener() {
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

                        dateFrom.setText(pYear+"-"+fm+"-"+fd);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(mContext, pDateSetListener, pYear, pMonth, pDay);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        dateUntil.setOnClickListener(new View.OnClickListener() {
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

                        dateUntil.setText(pYear+"-"+fm+"-"+fd);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(mContext, pDateSetListener, pYear, pMonth, pDay);
                String strDate = dateFrom.getText().toString();
                SimpleDateFormat dateFromFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFromFormat.parse(strDate);
                    long mills = date.getTime();
                    dialog.getDatePicker().setMinDate(mills);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                /*
                int dateUntilChangedYear = Integer.parseInt(dateUntil.getText().toString().substring(0,4));
                int dateUntilChangedMonth = Integer.parseInt(dateUntil.getText().toString().substring(5,7));
                int dateUntilChangedDay = Integer.parseInt(dateUntil.getText().toString().substring(8,10));
                dialog.getDatePicker().updateDate(dateUntilChangedYear, dateUntilChangedMonth, dateUntilChangedDay);
                */
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                reportList();
            }
        });

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (rAdapter != null) {
                    rAdapter.refreshEvents(reportList);
                }
                reportList();
            }
        });
    }

    private void reportList() {
        String fromDate = dateFrom.getText().toString();
        String untilDate = dateUntil.getText().toString();

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ReportListResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .reportList("Bearer " + token, fromDate, untilDate);

        call.enqueue(new Callback<ReportListResponse>() {

            @Override
            public void onResponse(Call<ReportListResponse> call, Response<ReportListResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    ReportListResponse reportListResponse = response.body();
                    if (reportListResponse.getStatus().equals("success")) {
                        layoutResult.setVisibility(View.VISIBLE);
                        if (reportListResponse.getStock() == null) {
                            txtRStock.setText("0");
                        } else {
                            txtRStock.setText(reportListResponse.getStock());
                        }
                        txtRSold.setText(String.valueOf(reportListResponse.getSold()));
                        txtRRemain.setText(String.valueOf(reportListResponse.getRemain()));
                        txtRLocation.setText(String.valueOf(reportListResponse.getLocation()));
                        txtRItem.setText(String.valueOf(reportListResponse.getItem()));
                        txtRIncome.setText(String.valueOf(reportListResponse.getIncome()));
                        reportList = reportListResponse.getReportList();
                        rAdapter = new ReportAdapter(reportList);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(rAdapter);
                        if (eLayoutManager.getItemCount() == 0) {
                            //Do something
                        }
                    }
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ReportListResponse> call, Throwable t) {
                loading.dismiss();
                swipeRefresh.setRefreshing(false);
                layoutResult.setVisibility(View.GONE);
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
