package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletLog {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("reference_id")
    @Expose
    private String referenceId;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("saldo_awal")
    @Expose
    private int saldoAwal;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("total")
    @Expose
    private int total;

    @SerializedName("grand_total")
    @Expose
    private int grandTotal;

    @SerializedName("created_at")
    @Expose
    private String dateTime;

    public WalletLog(String id, int userId, String referenceId, String source, int saldoAwal, String type, int total, int grandTotal, String dateTime) {
        this.id = id;
        this.userId = userId;
        this.referenceId = referenceId;
        this.source = source;
        this.saldoAwal = saldoAwal;
        this.type = type;
        this.total = total;
        this.grandTotal = grandTotal;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSaldoAwal() {
        return saldoAwal;
    }

    public void setSaldoAwal(int saldoAwal) {
        this.saldoAwal = saldoAwal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
