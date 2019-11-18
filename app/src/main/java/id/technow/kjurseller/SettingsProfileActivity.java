package id.technow.kjurseller;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;
import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.DetailUserResponse;
import id.technow.kjurseller.model.EditUserResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsProfileActivity extends AppCompatActivity {
    private EditText edtSEmail, edtSPhone;
    private TextView edtSBirth, btnEditPic;
    private CircleImageView imgProfile;
    ;
    private int pYear, pMonth, pDay;
    private static final int REQUEST_CHOOSE_IMAGE = 3;
    private Button btnConfirm;
    Context mContext;
    ProgressDialog loading;
    File imgValue;
    String avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;

        imgProfile = findViewById(R.id.imgProfile);
        btnEditPic = findViewById(R.id.btnEditPic);
        edtSEmail = findViewById(R.id.edtSEmail);
        edtSPhone = findViewById(R.id.edtSPhone);
        edtSBirth = findViewById(R.id.edtSBirth);

        detailUser();

        btnEditPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(SettingsProfileActivity.this, "Choose Picture",
                        REQUEST_CHOOSE_IMAGE);
            }
        });

        edtSBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                pYear = c.get(Calendar.YEAR);
                pMonth = c.get(Calendar.MONTH);
                pDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        pYear = year;
                        pMonth = monthOfYear + 1;
                        pDay = dayOfMonth;

                        String fm = "" + pMonth;
                        String fd = "" + pDay;
                        if (pMonth < 10) {
                            fm = "0" + pMonth;
                        }
                        if (pDay < 10) {
                            fd = "0" + pDay;
                        }

                        edtSBirth.setText(pYear + "-" + fm + "-" + fd);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(mContext, pDateSetListener, pYear, pMonth, pDay);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                editUser();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
        detailUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                CropImage.activity(Uri.fromFile(imageFile))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setFixAspectRatio(true)
                        .start(SettingsProfileActivity.this);
            }

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                super.onImagePickerError(e, source, type);
                Toast.makeText(SettingsProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                super.onCanceled(source, type);
            }
        });

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imgValue = new File(resultUri.getPath());

                Picasso.get()
                        .load(new File(resultUri.getPath()))
                        .error(R.drawable.ic_close)
                        .resize(500, 500)
                        .centerInside()
                        .noFade()
                        .into(imgProfile);
                //avatar = 0;
                uploadFoto();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String imgToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String filePath = imgValue.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        //    bt_uploadFoto.setEnabled(true);
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    public void uploadFoto() {
        //  loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
        String accept = "application/json";
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();

        String image = imgToString();
        retrofit2.Call<EditUserResponse> call = RetrofitClient.getInstance().getApi().uploadAva("Bearer " + token, image);
        call.enqueue(new Callback<EditUserResponse>() {
            @Override
            public void onResponse(retrofit2.Call<EditUserResponse> call, Response<EditUserResponse> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    Toast.makeText(SettingsProfileActivity.this, "Succes Uploading Profile Picture", Toast.LENGTH_LONG).show();
                } else {
                    loading.dismiss();
                    Toast.makeText(SettingsProfileActivity.this, "Failed Uploading Profile Picture", Toast.LENGTH_LONG).show();
                    // Toast.makeText(SettingsProfileActivity.this, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    Picasso.get()
                            .load(avatar)
                            .error(R.drawable.ic_user)
                            .resize(500, 500)
                            .centerInside()
                            .noFade()
                            .into(imgProfile);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<EditUserResponse> call, Throwable t) {
                loading.dismiss();
                // Toast.makeText(SettingsProfileActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(SettingsProfileActivity.this, "Failed Uploading Profile Picture", Toast.LENGTH_LONG).show();
                Picasso.get()
                        .load(avatar)
                        .error(R.drawable.ic_user)
                        .resize(500, 500)
                        .centerInside()
                        .noFade()
                        .into(imgProfile);
            }
        });
    }

    private void detailUser() {
        String accept = "application/json";

        User user = SharedPrefManager.getInstance(this).getUser();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String token = user.getToken();
        Call<DetailUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .detailUser("Bearer " + token, accept);

        call.enqueue(new Callback<DetailUserResponse>() {

            @Override
            public void onResponse(Call<DetailUserResponse> call, Response<DetailUserResponse> response) {
                loading.dismiss();
                btnConfirm.setEnabled(true);
                if (response.isSuccessful()) {
                    DetailUserResponse detailUserResponse = response.body();
                    if (detailUserResponse.getStatus().equals("success")) {
                        edtSEmail.setText(detailUserResponse.getDetailUser().getEmail());
                        edtSPhone.setText(detailUserResponse.getDetailUser().getPhone());
                        if (detailUserResponse.getDetailUser().getBirthDate() != null) {
                            edtSBirth.setText(sdf.format(detailUserResponse.getDetailUser().getBirthDate()));
                        } else {
                            edtSBirth.setText("YYYY-MM-DD");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailUserResponse> call, Throwable t) {
                loading.dismiss();
                btnConfirm.setEnabled(false);
                Toast.makeText(mContext, "Can't get user information. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void editUser() {
        String email = edtSEmail.getText().toString().trim();
        String phone = edtSPhone.getText().toString().trim();
        String birth = edtSBirth.getText().toString().trim();

        if (email.isEmpty()) {
            loading.dismiss();
            edtSEmail.setError("Email is required");
            edtSEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.dismiss();
            edtSEmail.setError("Enter a valid Email");
            edtSEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            loading.dismiss();
            edtSPhone.setError("Phone Number is required");
            edtSPhone.requestFocus();
            return;
        }

        if (phone.length() < 10 || phone.length() > 13) {
            loading.dismiss();
            edtSPhone.setError("Phone Number should be at least 10-13 characters long");
            edtSPhone.requestFocus();
            return;
        }

        if (birth.equals("null")) {
            loading.dismiss();
            edtSBirth.setError("Birth Date is required");
            edtSBirth.requestFocus();
            return;
        }

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<EditUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .editUser("Bearer " + token, email, phone, birth);

        call.enqueue(new Callback<EditUserResponse>() {

            @Override
            public void onResponse(Call<EditUserResponse> call, Response<EditUserResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    EditUserResponse editUserResponse = response.body();
                    if (editUserResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Profiles updated", Toast.LENGTH_LONG).show();
                        if (editUserResponse.getDetailUser().getEmailVerifyAt() == null) {
                            String emailSend = editUserResponse.getDetailUser().getEmail();
                            Intent intent = new Intent(SettingsProfileActivity.this, VerifyEmailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("email", emailSend);
                            intent.putExtra("fromSettings", 1);
                            startActivity(intent);
                        } else {
                            SettingsProfileActivity.this.finish();
                        }
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody.trim());
                        jsonObject = jsonObject.getJSONObject("errors");
                        Iterator<String> keys = jsonObject.keys();
                        StringBuilder errors = new StringBuilder();
                        String separator = "";
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONArray arr = jsonObject.getJSONArray(key);
                            for (int i = 0; i < arr.length(); i++) {
                                errors.append(separator).append(key).append(" : ").append(arr.getString(i));
                                separator = "\n";
                            }
                        }
                        Toast.makeText(mContext, errors.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EditUserResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}