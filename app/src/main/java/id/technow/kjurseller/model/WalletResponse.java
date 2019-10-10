package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WalletResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("user")
    @Expose
    private WalletAccount walletAccount;

    @SerializedName("riwayat")
    @Expose
    private ArrayList<WalletLog> walletLog = null;

    public WalletResponse(String status, WalletAccount walletAccount, ArrayList<WalletLog> walletLog) {
        this.status = status;
        this.walletAccount = walletAccount;
        this.walletLog = walletLog;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public WalletAccount getWalletAccount() {
        return walletAccount;
    }

    public void setWalletAccount(WalletAccount walletAccount) {
        this.walletAccount = walletAccount;
    }

    public ArrayList<WalletLog> getWalletLog() {
        return walletLog;
    }

    public void setWalletLog(ArrayList<WalletLog> walletLog) {
        this.walletLog = walletLog;
    }
}
