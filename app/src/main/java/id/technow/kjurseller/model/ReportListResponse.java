package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReportListResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("stok")
    @Expose
    private String stock;

    @SerializedName("sold")
    @Expose
    private int sold;

    @SerializedName("remain")
    @Expose
    private int remain;

    @SerializedName("location")
    @Expose
    private int location;

    @SerializedName("item")
    @Expose
    private int item;

    @SerializedName("icome")
    @Expose
    private int income;

    @SerializedName("rekap")
    @Expose
    private ArrayList<ReportList> reportList = null;

    public ReportListResponse(String status, String stock, int sold, int remain, int location, int item, int income, ArrayList<ReportList> reportList) {
        this.status = status;
        this.stock = stock;
        this.sold = sold;
        this.remain = remain;
        this.location = location;
        this.item = item;
        this.income = income;
        this.reportList = reportList;
    }

    public String getStatus() {
        return status;
    }

    public String getStock() {
        return stock;
    }

    public int getSold() {
        return sold;
    }

    public int getRemain() {
        return remain;
    }

    public int getLocation() {
        return location;
    }

    public int getItem() {
        return item;
    }

    public int getIncome() {
        return income;
    }

    public ArrayList<ReportList> getReportList() {
        return reportList;
    }
}
