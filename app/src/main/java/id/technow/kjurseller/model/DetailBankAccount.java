package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class DetailBankAccount {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("bank_id")
    @Expose
    private int bankAccId;

    @SerializedName("pemilik")
    @Expose
    private String bankHolderName;

    @SerializedName("no_rekening")
    @Expose
    private BigInteger bankAccNumber;

    @SerializedName("bank")
    @Expose
    private BankList detailBankInfo;

    public DetailBankAccount(int id, int bankAccId, String bankHolderName, BigInteger bankAccNumber, BankList detailBankInfo) {
        this.id = id;
        this.bankAccId = bankAccId;
        this.bankHolderName = bankHolderName;
        this.bankAccNumber = bankAccNumber;
        this.detailBankInfo = detailBankInfo;
    }

    public int getId() {
        return id;
    }

    public int getBankAccId() {
        return bankAccId;
    }

    public String getBankHolderName() {
        return bankHolderName;
    }

    public BigInteger getBankAccNumber() {
        return bankAccNumber;
    }

    public BankList getDetailBankInfo() {
        return detailBankInfo;
    }
}
