package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class ProductLogStock {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("barang_jual_id")
    @Expose
    private String barangJualId;

    @SerializedName("barang")
    @Expose
    private String productName;

    @SerializedName("harga")
    @Expose
    private int productPrice;

    @SerializedName("deskripsi")
    @Expose
    private String productDesc;

    @SerializedName("foto")
    @Expose
    private String productPic;

    @SerializedName("stok")
    @Expose
    private int productStock;

    @SerializedName("lokasi")
    @Expose
    private String productLoc;

    public ProductLogStock(String id, String barangJualId, String productName, int productPrice, String productDesc, String productPic, int productStock, String productLoc) {
        this.id = id;
        this.barangJualId = barangJualId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDesc = productDesc;
        this.productPic = productPic;
        this.productStock = productStock;
        this.productLoc = productLoc;
    }

    public String getId() {
        return id;
    }

    public String getBarangJualId() {
        return barangJualId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getProductPic() {
        return productPic;
    }

    public int getProductStock() {
        return productStock;
    }

    public String getProductLoc() {
        return productLoc;
    }
}
