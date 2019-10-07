package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductListAllResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private ArrayList<ProductAll> product = null;

    public ProductListAllResponse(String status, ArrayList<ProductAll> product) {
        this.status = status;
        this.product = product;
    }

    public ArrayList<ProductAll> getProduct() {
        return product;
    }

    public String getStatus() {
        return status;
    }
}
