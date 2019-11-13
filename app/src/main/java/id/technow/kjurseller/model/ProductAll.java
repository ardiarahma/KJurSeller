package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductAll {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("penjual_id")
    @Expose
    private String penjualId;

    @SerializedName("barang_id")
    @Expose
    private int barangId;

    @SerializedName("lokasi_id")
    @Expose
    private int lokasiId;

    @SerializedName("harga")
    @Expose
    private int productPrice;

    @SerializedName("barang")
    @Expose
    private String productName;

    @SerializedName("lokasi")
    @Expose
    private String productLoc;

    @SerializedName("foto")
    @Expose
    private String productPic;

    @SerializedName("status")
    @Expose
    private String status;

    public ProductAll(String id, String penjualId, int barangId, int lokasiId, int productPrice, String productName, String productLoc, String productPic, String status) {
        this.id = id;
        this.penjualId = penjualId;
        this.barangId = barangId;
        this.lokasiId = lokasiId;
        this.productPrice = productPrice;
        this.productName = productName;
        this.productLoc = productLoc;
        this.productPic = productPic;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPenjualId() {
        return penjualId;
    }

    public void setPenjualId(String penjualId) {
        this.penjualId = penjualId;
    }

    public int getBarangId() {
        return barangId;
    }

    public void setBarangId(int barangId) {
        this.barangId = barangId;
    }

    public int getLokasiId() {
        return lokasiId;
    }

    public void setLokasiId(int lokasiId) {
        this.lokasiId = lokasiId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
