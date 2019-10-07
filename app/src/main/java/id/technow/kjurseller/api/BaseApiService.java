package id.technow.kjurseller.api;

import java.math.BigInteger;

import id.technow.kjurseller.model.BankListResponse;
import id.technow.kjurseller.model.ChangePassUserResponse;
import id.technow.kjurseller.model.CloseStoreResponse;
import id.technow.kjurseller.model.DetailUserResponse;
import id.technow.kjurseller.model.EditBankResponse;
import id.technow.kjurseller.model.EditUserResponse;
import id.technow.kjurseller.model.ForgotPasswordResponse;
import id.technow.kjurseller.model.GamapayResponse;
import id.technow.kjurseller.model.LocationResponse;
import id.technow.kjurseller.model.LocationTodayResponse;
import id.technow.kjurseller.model.LoginResponse;
import id.technow.kjurseller.model.LogoutResponse;
import id.technow.kjurseller.model.NewStockResponse;
import id.technow.kjurseller.model.ProductListAllResponse;
import id.technow.kjurseller.model.ProductListTodayResponse;
import id.technow.kjurseller.model.ProductLogResponse;
import id.technow.kjurseller.model.ReportListResponse;
import id.technow.kjurseller.model.ResendVerifyEmailResponse;
import id.technow.kjurseller.model.UpdateStockResponse;
import id.technow.kjurseller.model.VerifyEmailResponse;
import id.technow.kjurseller.model.WithdrawResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("forgot-password")
    Call<ForgotPasswordResponse> fpUser(
            @Header("Accept") String accept,
            @Field("email") String email);

    @GET("detail")
    Call<DetailUserResponse> detailUser(
            @Header("Authorization") String authToken,
            @Header("Accept") String accept);

    @FormUrlEncoded
    @POST("email/verify")
    Call<VerifyEmailResponse> verifyUser(
            @Header("Authorization") String authToken,
            @Field("verification_code") String verifyCode);

    @GET("email/resend")
    Call<ResendVerifyEmailResponse> resendVerifyUser(
            @Header("Authorization") String authToken);

    @GET("logout")
    Call<LogoutResponse> logoutUser(
            @Header("Authorization") String authToken);

    @GET("lokasi")
    Call<LocationResponse> locAll(
            @Header("Authorization") String authToken);

    @GET("barang-jual/{id}")
    Call<ProductListAllResponse> productListAll(
            @Header("Authorization") String authToken,
            @Path("id") int id);

    @FormUrlEncoded
    @POST("stok-barang/create/{id}")
    Call<NewStockResponse> newStock(
            @Header("Authorization") String authToken,
            @Path("id") String id,
            @Field("harga") int price,
            @Field("jumlah") int stock);

    @GET("lokasi-today")
    Call<LocationTodayResponse> locToday(
            @Header("Authorization") String authToken);

    @GET("stok-barang/today/{id}")
    Call<ProductListTodayResponse> productListToday(
            @Header("Authorization") String authToken,
            @Path("id") int id);

    @GET("stok-barang/history/{id}")
    Call<ProductLogResponse> productLog(
            @Header("Authorization") String authToken,
            @Path("id") String id);

    @FormUrlEncoded
    @POST("stok-barang/add-stok/{id}")
    Call<UpdateStockResponse> updateStock(
            @Header("Authorization") String authToken,
            @Path("id") String id,
            @Field("add_stok") int stock);

    @PUT("stok-barang/close/{id}")
    Call<CloseStoreResponse> closeStore(
            @Header("Authorization") String authToken,
            @Path("id") int id);

    @PUT("stok-barang/close/{id}/stok")
    Call<CloseStoreResponse> closeItem(
            @Header("Authorization") String authToken,
            @Path("id") String id);

    @GET("saldo/riwayat")
    Call<GamapayResponse> gamapay(
            @Header("Authorization") String authToken);

    @FormUrlEncoded
    @POST("pencairan/create")
    Call<WithdrawResponse> withdraw(
            @Header("Authorization") String authToken,
            @Field("total") int total);

    @GET("report")
    Call<ReportListResponse> reportList(
            @Header("Authorization") String authToken,
            @Query("from") String dateFrom,
            @Query("until") String dateUntil);

    @GET("bank")
    Call<BankListResponse> bankList(
            @Header("Authorization") String authToken);

    @FormUrlEncoded
    @PUT("edit")
    Call<EditUserResponse> editUser(
            @Header("Authorization") String authToken,
            @Field("email") String email,
            @Field("no_telepon") String phone,
            @Field("tanggal_lahir") String birthDate);

    @FormUrlEncoded
    @PUT("edit-bank")
    Call<EditBankResponse> editBank(
            @Header("Authorization") String authToken,
            @Field("bank") int bankName,
            @Field("owner") String bankHolder,
            @Field("account_number") BigInteger bankAccNumber);

    @FormUrlEncoded
    @PUT("edit-password")
    Call<ChangePassUserResponse> changePassUser(
            @Header("Authorization") String authToken,
            @Field("password_current") String passwordCurrent,
            @Field("password") String passwordNew,
            @Field("password_confirmation") String passwordNewConfirm);
}
