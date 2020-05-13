package id.technow.kjurseller.adapter;

import android.content.Intent;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import id.technow.kjurseller.ProductLogActivity;
import id.technow.kjurseller.R;
import id.technow.kjurseller.model.ReportList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.CustomViewHolder> {

    List<ReportList> reportLists;
    List<String> colors;

    public ReportAdapter(List<ReportList> reportLists) {
        this.reportLists = reportLists;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ReportList reportList = reportLists.get(position);
        holder.txtDate.setText(reportList.getDateTime());
        holder.txtName.setText(reportList.getName());
        holder.txtLocation.setText(reportList.getLocation());
        holder.txtPrice.setText(String.valueOf(reportList.getPrice()));
        holder.txtTotal.setText(String.valueOf(reportList.getTotal()));
        holder.txtSold.setText(reportList.getSold());
        holder.txtRemain.setText(String.valueOf(reportList.getRemain()));
        holder.txtIncome.setText(String.valueOf(reportList.getIncome()));
        holder.id = reportLists.get(position).getId();

        colors = new ArrayList<String>();

        colors.add("#e51c23");
        colors.add("#e91e63");
        colors.add("#9c27b0");
        colors.add("#673ab7");
        colors.add("#3f51b5");
        colors.add("#5677fc");
        colors.add("#03a9f4");
        colors.add("#00bcd4");
        colors.add("#009688");
        colors.add("#259b24");
        colors.add("#8bc34a");
        colors.add("#cddc39");
        colors.add("#ffeb3b");
        colors.add("#ff9800");
        colors.add("#ff5722");
        colors.add("#795548");
        colors.add("#9e9e9e");
        colors.add("#607d8b");

        Random r = new Random();
        int i1 = r.nextInt(17 - 0) + 0;

        holder.viewColor.setBackgroundColor(Color.parseColor(colors.get(i1)));
    }

    @Override
    public int getItemCount() {
        return reportLists.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDate, txtName, txtLocation, txtPrice, txtTotal, txtSold, txtRemain, txtIncome;
        private ImageView btnNext;
        private View viewColor;
        private String id;

        public CustomViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtName = itemView.findViewById(R.id.txtName);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtSold = itemView.findViewById(R.id.txtSold);
            txtRemain = itemView.findViewById(R.id.txtRemain);
            txtIncome = itemView.findViewById(R.id.txtIncome);
            viewColor = itemView.findViewById(R.id.viewColor);
            btnNext = itemView.findViewById(R.id.btnNext);
            btnNext.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext(), ProductLogActivity.class);
            intent.putExtra("productId", id);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<ReportList> reportLists) {
        this.reportLists.clear();
        this.reportLists.addAll(reportLists);
        notifyDataSetChanged();
    }
}