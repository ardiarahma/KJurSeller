package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportList {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("nama")
    @Expose
    private String name;

    @SerializedName("harga")
    @Expose
    private int price;

    @SerializedName("lokasi")
    @Expose
    private String location;

    @SerializedName("jumlah")
    @Expose
    private int total;

    @SerializedName("sold")
    @Expose
    private String sold;

    @SerializedName("remain")
    @Expose
    private int remain;

    @SerializedName("income")
    @Expose
    private int income;

    @SerializedName("created_at")
    @Expose
    private String dateTime;

    public ReportList(String id, String name, int price, String location, int total, String sold, int remain, int income, String dateTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.location = location;
        this.total = total;
        this.sold = sold;
        this.remain = remain;
        this.income = income;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public int getTotal() {
        return total;
    }

    public String getSold() {
        return sold;
    }

    public int getRemain() {
        return remain;
    }

    public int getIncome() {
        return income;
    }

    public String getDateTime() {
        return dateTime;
    }
}
