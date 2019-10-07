package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailStore {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("toko")
    @Expose
    private String storeName;

    @SerializedName("rekening_id")
    @Expose
    private int bankAccId;

    @SerializedName("rekening")
    @Expose
    private DetailBankAccount detailBankAccount;

    public DetailStore(String id, String userId, String storeName, int bankAccId, DetailBankAccount detailBankAccount) {
        this.id = id;
        this.userId = userId;
        this.storeName = storeName;
        this.bankAccId = bankAccId;
        this.detailBankAccount = detailBankAccount;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public int getBankAccId() {
        return bankAccId;
    }

    public DetailBankAccount getDetailBankAccount() {
        return detailBankAccount;
    }
}
