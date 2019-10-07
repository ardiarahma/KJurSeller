package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GamapayResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("user")
    @Expose
    private GamapayAccount gamapayAccount;

    @SerializedName("riwayat")
    @Expose
    private ArrayList<GamapayLog> gamapayLog = null;

    public GamapayResponse(String status, GamapayAccount gamapayAccount, ArrayList<GamapayLog> gamapayLog) {
        this.status = status;
        this.gamapayAccount = gamapayAccount;
        this.gamapayLog = gamapayLog;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GamapayAccount getGamapayAccount() {
        return gamapayAccount;
    }

    public void setGamapayAccount(GamapayAccount gamapayAccount) {
        this.gamapayAccount = gamapayAccount;
    }

    public ArrayList<GamapayLog> getGamapayLog() {
        return gamapayLog;
    }

    public void setGamapayLog(ArrayList<GamapayLog> gamapayLog) {
        this.gamapayLog = gamapayLog;
    }
}
