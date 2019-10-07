package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afyad Kafa on 1/14/2019.
 */

public class DetailUserResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("product")
    @Expose
    private int product;

    @SerializedName("stock")
    @Expose
    private int stock;

    @SerializedName("sold")
    @Expose
    private int sold;

    @SerializedName("location")
    @Expose
    private int location;

    @SerializedName("income")
    @Expose
    private int income;

    @SerializedName("result")
    @Expose
    private DetailUser detailUser;

    public DetailUserResponse(String status, String message, String email, int product, int stock, int sold, int location, int income, DetailUser detailUser) {
        this.status = status;
        this.message = message;
        this.email = email;
        this.product = product;
        this.stock = stock;
        this.sold = sold;
        this.location = location;
        this.income = income;
        this.detailUser = detailUser;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public int getProduct() {
        return product;
    }

    public int getStock() {
        return stock;
    }

    public int getSold() {
        return sold;
    }

    public int getLocation() {
        return location;
    }

    public int getIncome() {
        return income;
    }

    public DetailUser getDetailUser() {
        return detailUser;
    }
}
