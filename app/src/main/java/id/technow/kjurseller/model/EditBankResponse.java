package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditBankResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("user")
    @Expose
    private DetailUser detailUser;

    @SerializedName("rekening")
    @Expose
    private DetailBankAccount detailBankAccount;

    public EditBankResponse(String status, DetailUser detailUser, DetailBankAccount detailBankAccount) {
        this.status = status;
        this.detailUser = detailUser;
        this.detailBankAccount = detailBankAccount;
    }

    public String getStatus() {
        return status;
    }

    public DetailUser getDetailUser() {
        return detailUser;
    }

    public DetailBankAccount getDetailBankAccount() {
        return detailBankAccount;
    }
}
