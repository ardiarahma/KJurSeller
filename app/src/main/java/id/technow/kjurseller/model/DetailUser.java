package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DetailUser {
    @SerializedName("id")
    @Expose
    private int userId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("email_verified_at")
    @Expose
    private String emailVerifyAt;

    @SerializedName("no_telepon")
    @Expose
    private String phone;

    @SerializedName("tanggal_lahir")
    @Expose
    private Date birthDate;

    @SerializedName("foto")
    @Expose
    private String pic;

    @SerializedName("saldo")
    @Expose
    private int balance;

    @SerializedName("status_id")
    @Expose
    private int statusId;

    @SerializedName("penjual")
    @Expose
    private DetailStore detailStore;

    public DetailUser(int userId, String name, String email, String emailVerifyAt, String phone, Date birthDate, String pic, int balance, int statusId, DetailStore detailStore) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.emailVerifyAt = emailVerifyAt;
        this.phone = phone;
        this.birthDate = birthDate;
        this.pic = pic;
        this.balance = balance;
        this.statusId = statusId;
        this.detailStore = detailStore;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailVerifyAt() {
        return emailVerifyAt;
    }

    public String getPhone() {
        return phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getPic() {
        return pic;
    }

    public int getBalance() {
        return balance;
    }

    public int getStatusId() {
        return statusId;
    }

    public DetailStore getDetailStore() {
        return detailStore;
    }
}
