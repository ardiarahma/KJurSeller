package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BankListResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private ArrayList<BankList> bankList = null;

    public BankListResponse(String status, ArrayList<BankList> bankList) {
        this.status = status;
        this.bankList = bankList;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<BankList> getBankList() {
        return bankList;
    }
}
