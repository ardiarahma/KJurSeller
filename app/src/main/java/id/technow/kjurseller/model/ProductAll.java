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

    public ProductAll(String id, String penjualId, int barangId, int lokasiId, int productPrice, String productName, String productLoc, String productPic) {
        this.id = id;
        this.penjualId = penjualId;
        this.barangId = barangId;
        this.lokasiId = lokasiId;
        this.productPrice = productPrice;
        this.productName = productName;
        this.productLoc = productLoc;
        this.productPic = productPic;
    }

    public String getId() {
        return id;
    }

    public String getPenjualId() {
        return penjualId;
    }

    public int getBarangId() {
        return barangId;
    }

    public int getLokasiId() {
        return lokasiId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductLoc() {
        return productLoc;
    }

    public String getProductPic() {
        return productPic;
    }
}
