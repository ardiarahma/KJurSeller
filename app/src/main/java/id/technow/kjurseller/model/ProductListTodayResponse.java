package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductListTodayResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private ArrayList<ProductToday> product = null;

    public ProductListTodayResponse(String status, ArrayList<ProductToday> product) {
        this.status = status;
        this.product = product;
    }

    public ArrayList<ProductToday> getProduct() {
        return product;
    }

    public String getStatus() {
        return status;
    }
}
