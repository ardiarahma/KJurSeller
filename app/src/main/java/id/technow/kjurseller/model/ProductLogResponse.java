package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductLogResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("stok")
    @Expose
    private ProductLogStock productLogStock;

    @SerializedName("history")
    @Expose
    private ArrayList<ProductLogHistory> productLogHistory = null;

    public ProductLogResponse(String status, ProductLogStock productLogStock, ArrayList<ProductLogHistory> productLogHistory) {
        this.status = status;
        this.productLogStock = productLogStock;
        this.productLogHistory = productLogHistory;
    }

    public String getStatus() {
        return status;
    }

    public ProductLogStock getProductLogStock() {
        return productLogStock;
    }

    public ArrayList<ProductLogHistory> getProductLogHistory() {
        return productLogHistory;
    }
}
