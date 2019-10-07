package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class ProductToday {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("barang_jual_id")
    @Expose
    private String barangJualId;

    @SerializedName("harga")
    @Expose
    private int productPrice;

    @SerializedName("status")
    @Expose
    private String productStatus;

    @SerializedName("jumlah")
    @Expose
    private int productStockAll;

    @SerializedName("stok")
    @Expose
    private int productStockNow;

    @SerializedName("barang")
    @Expose
    private String productName;

    @SerializedName("lokasi")
    @Expose
    private String productLoc;

    public ProductToday(String id, String barangJualId, int productPrice, String productStatus, int productStockAll, int productStockNow, String productName, String productLoc) {
        this.id = id;
        this.barangJualId = barangJualId;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productStockAll = productStockAll;
        this.productStockNow = productStockNow;
        this.productName = productName;
        this.productLoc = productLoc;
    }

    public String getId() {
        return id;
    }

    public String getBarangJualId() {
        return barangJualId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public int getProductStockAll() {
        return productStockAll;
    }

    public int getProductStockNow() {
        return productStockNow;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductLoc() {
        return productLoc;
    }
}
