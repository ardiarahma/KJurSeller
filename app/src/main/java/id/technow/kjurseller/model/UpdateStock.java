package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateStock {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("barang_jual_id")
    @Expose
    private String barangJualId;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("jumlah")
    @Expose
    private int stockAll;

    @SerializedName("stok")
    @Expose
    private int stockNow;

    public UpdateStock(String id, String barangJualId, String status, int stockAll, int stockNow) {
        this.id = id;
        this.barangJualId = barangJualId;
        this.status = status;
        this.stockAll = stockAll;
        this.stockNow = stockNow;
    }

    public String getId() {
        return id;
    }

    public String getBarangJualId() {
        return barangJualId;
    }

    public String getStatus() {
        return status;
    }

    public int getStockAll() {
        return stockAll;
    }

    public int getStockNow() {
        return stockNow;
    }
}
