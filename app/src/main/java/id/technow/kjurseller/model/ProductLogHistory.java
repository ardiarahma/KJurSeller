package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductLogHistory {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("stok_barang_id")
    @Expose
    private String stokBarangId;

    @SerializedName("reference_id")
    @Expose
    private String referenceId;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("stok_awal")
    @Expose
    private int stockOld;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("total")
    @Expose
    private int stockChange;

    @SerializedName("stok_update")
    @Expose
    private int stockNew;

    @SerializedName("created_at")
    @Expose
    private String dateCreated;

    @SerializedName("updated_at")
    @Expose
    private String dateUpdated;

    public ProductLogHistory(String id, String stokBarangId, String referenceId, String source, int stockOld, String type, int stockChange, int stockNew, String dateCreated, String dateUpdated) {
        this.id = id;
        this.stokBarangId = stokBarangId;
        this.referenceId = referenceId;
        this.source = source;
        this.stockOld = stockOld;
        this.type = type;
        this.stockChange = stockChange;
        this.stockNew = stockNew;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public String getId() {
        return id;
    }

    public String getStokBarangId() {
        return stokBarangId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getSource() {
        return source;
    }

    public int getStockOld() {
        return stockOld;
    }

    public String getType() {
        return type;
    }

    public int getStockChange() {
        return stockChange;
    }

    public int getStockNew() {
        return stockNew;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }
}
