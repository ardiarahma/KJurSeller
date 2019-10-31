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

    @SerializedName("foto")
    @Expose
    private String productPic;

    public ProductToday(String id, String barangJualId, int productPrice, String productStatus, int productStockAll, int productStockNow, String productName, String productLoc, String productPic) {
        this.id = id;
        this.barangJualId = barangJualId;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productStockAll = productStockAll;
        this.productStockNow = productStockNow;
        this.productName = productName;
        this.productLoc = productLoc;
        this.productPic = productPic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarangJualId() {
        return barangJualId;
    }

    public void setBarangJualId(String barangJualId) {
        this.barangJualId = barangJualId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public int getProductStockAll() {
        return productStockAll;
    }

    public void setProductStockAll(int productStockAll) {
        this.productStockAll = productStockAll;
    }

    public int getProductStockNow() {
        return productStockNow;
    }

    public void setProductStockNow(int productStockNow) {
        this.productStockNow = productStockNow;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductLoc() {
        return productLoc;
    }

    public void setProductLoc(String productLoc) {
        this.productLoc = productLoc;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }
}
