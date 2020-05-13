package id.technow.kjurseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;

import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.BankList;
import id.technow.kjurseller.model.BankListResponse;
import id.technow.kjurseller.model.DetailUserResponse;
import id.technow.kjurseller.model.EditBankResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsBankInfoActivity extends AppCompatActivity {

    private EditText etBankAccNumber, etBankHolderName;
    private Spinner spBank;
    private Button btnConfirm;
    private TextView bankIdSelected;
    ArrayList<BankList> bankLists;
    ArrayAdapter<BankList> adapterBank;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_bank_info);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;

        etBankAccNumber = findViewById(R.id.edtSBankNumber);
        etBankHolderName = findViewById(R.id.edtSBankHolder);
        bankIdSelected = findViewById(R.id.bankIdSelected);

        spBank = findViewById(R.id.spBank);
        spBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedBankName = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#8c8c8c"));
                ((TextView) parent.getChildAt(0)).setTextSize(15);
                ((TextView) parent.getChildAt(0)).setGravity(Gravity.END);
                BankList bankList = (BankList) parent.getSelectedItem();
                //spBank.setSelection(position);
                bankIdSelected.setText(String.valueOf(bankList.getId()));
                //Toast.makeText(mContext, "Bank ID: "+ String.valueOf(bankList.getId())+",  Bank Name : "+bankList.getBankName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                editBank();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
        bankList();
        detailUser();
    }

    private void detailUser() {
        String accept = "application/json";

        User user = SharedPrefManager.getInstance(this).getUser();
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
                        BigInteger bankAccNumber = detailUserResponse.getDetailUser().getDetailStore().getDetailBankAccount().getBankAccNumber();
                        if (bankAccNumber != null) {
                            etBankAccNumber.setText(String.valueOf(bankAccNumber));
                        }
                        String bankHolderName = detailUserResponse.getDetailUser().getDetailStore().getDetailBankAccount().getBankHolderName();
                        if (bankHolderName != null) {
                            etBankHolderName.setText(bankHolderName);
                        }
                        /*BankList bankUser = detailUserResponse.getDetailUser().getDetailStore().getDetailBankAccount().getDetailBankInfo();
                        if (bankUser != null) {
                            int spinnerPosition = 0;
                            spinnerPosition = adapterBank.getPosition(bankUser);
                            spBank.setSelection(spinnerPosition);
                        }*/
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailUserResponse> call, Throwable t) {
                loading.dismiss();
                btnConfirm.setEnabled(false);
                Toast.makeText(mContext, "Can't get bank information. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void bankList() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<BankListResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .bankList("Bearer " + token);

        call.enqueue(new Callback<BankListResponse>() {

            @Override
            public void onResponse(Call<BankListResponse> call, Response<BankListResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    BankListResponse bankListResponse = response.body();
                    if (bankListResponse.getStatus().equals("success")) {
                        loading.dismiss();
                        //bankLists = new ArrayList<>();
                        bankLists = bankListResponse.getBankList();
                        /*for (int i = 0; i < bankLists.size(); i++){
                            bankLists.add(new BankList(bankLists.get(i).getId(), bankLists.get(i).getBankName(), bankLists.get(i).getBankDesc()));
                        }*/
                        adapterBank = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, bankLists);
                        adapterBank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spBank.setAdapter(adapterBank);
                    }
                }
            }

            @Override
            public void onFailure(Call<BankListResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void editBank() {
        int bankName = Integer.parseInt(bankIdSelected.getText().toString());
        String bankAccNumber = etBankAccNumber.getText().toString().trim();
        String bankHolder = etBankHolderName.getText().toString().trim();

        if (bankHolder.isEmpty()) {
            loading.dismiss();
            etBankHolderName.setError("Bank Holder Name is required");
            etBankHolderName.requestFocus();
            return;
        }

        if (bankAccNumber.isEmpty()) {
            loading.dismiss();
            etBankAccNumber.setError("Bank Account Number is required");
            etBankAccNumber.requestFocus();
            return;
        }

        if (bankAccNumber.length() < 6) {
            loading.dismiss();
            etBankAccNumber.setError("Bank Account Number should be at least 6 characters long");
            etBankAccNumber.requestFocus();
            return;
        }

        BigInteger bankAccNum = new BigInteger(bankAccNumber);

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<EditBankResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .editBank("Bearer " + token, bankName, bankHolder, bankAccNum);

        call.enqueue(new Callback<EditBankResponse>() {

            @Override
            public void onResponse(Call<EditBankResponse> call, Response<EditBankResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    EditBankResponse editBankResponse = response.body();
                    if (editBankResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Bank Information updated", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(Call<EditBankResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
