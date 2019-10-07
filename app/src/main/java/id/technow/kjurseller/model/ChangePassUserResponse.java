package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassUserResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("user")
    @Expose
    private DetailUser detailUser;

    public ChangePassUserResponse(String status, DetailUser detailUser) {
        this.status = status;
        this.detailUser = detailUser;
    }

    public String getStatus() {
        return status;
    }

    public DetailUser getDetailUser() {
        return detailUser;
    }
}
