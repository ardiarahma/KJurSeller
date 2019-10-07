package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewStockResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("result")
    @Expose
    private ArrayList<UpdateStock> updateStock = null;

    public NewStockResponse(String status, String message, ArrayList<UpdateStock> updateStock) {
        this.status = status;
        this.message = message;
        this.updateStock = updateStock;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<UpdateStock> getUpdateStock() {
        return updateStock;
    }
}
