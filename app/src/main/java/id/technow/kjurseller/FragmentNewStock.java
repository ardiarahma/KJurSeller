package id.technow.kjurseller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import id.technow.kjurseller.model.NewStockResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNewStock extends BottomSheetDialogFragment {
    Integer i = 1;
    private Button btnConfirm;
    private EditText edtStock, edtPrice;
    Context mContext;
    ProgressDialog loading;
    private BottomSheetBehavior mBottomSheetBehavior;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_new_stock, null);
        dialog.setContentView(view);

        mContext = getActivity();

        String productName = getArguments().getString("productName");
        String productPic = getArguments().getString("productPic");
        int productPrice = getArguments().getInt("productPrice", 0);

        TextView txtProductName = view.findViewById(R.id.txtProductName);
        txtProductName.setText(productName);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtPrice.setText(String.valueOf(productPrice));
        edtStock = view.findViewById(R.id.edtStock);

        ImageView imgProduct = view.findViewById(R.id.imgProduct);
        Picasso.get()
                .load(productPic)
                // .placeholder(R.drawable.ic_snack)
                .error(R.drawable.ic_close)
                .resize(500, 500)
                .centerInside()
                .noFade()
                .into(imgProduct);

        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                newStock();
            }
        });

        edtStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String stock = edtStock.getText().toString();

                if (stock.isEmpty()) {
                    edtStock.setError("Stock required");
                    btnConfirm.setEnabled(false);
                } else if (stock.length() > 0) {
                    int stockValue = Integer.parseInt(stock);
                    if (stockValue < 1) {
                        edtStock.setError("Stock should be at least 1 item");
                        btnConfirm.setEnabled(false);
                    } else {
                        edtStock.setError(null);
                        btnConfirm.setEnabled(true);
                    }
                } else {
                    edtStock.setError(null);
                    btnConfirm.setEnabled(true);
                }
            }
        });

        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String price = edtPrice.getText().toString();

                if (price.isEmpty()) {
                    edtPrice.setError("Price required");
                    btnConfirm.setEnabled(false);
                } else if (price.length() > 0) {
                    int priceValue = Integer.parseInt(price);
                    if (priceValue < 1) {
                        edtPrice.setError("Price can't be less than Rp. 100");
                        btnConfirm.setEnabled(false);
                    } else {
                        edtPrice.setError(null);
                        btnConfirm.setEnabled(true);
                    }
                } else {
                    edtPrice.setError(null);
                    btnConfirm.setEnabled(true);
                }
            }
        });

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            mBottomSheetBehavior = (BottomSheetBehavior) behavior;
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
    }

    private void newStock() {
        String stockTxt = edtStock.getText().toString().trim();
        String priceTxt = edtPrice.getText().toString().trim();

        if (stockTxt.isEmpty()) {
            loading.dismiss();
            edtStock.setError("Stock is required");
            edtStock.requestFocus();
            return;
        }

        if (priceTxt.isEmpty()) {
            loading.dismiss();
            edtStock.setError("Price is required");
            edtStock.requestFocus();
            return;
        }

        int stock = Integer.parseInt(stockTxt);
        int price = Integer.parseInt(priceTxt);
        String productId = getArguments().getString("productId");

        User user = SharedPrefManager.getInstance(mContext).getUser();
        String token = user.getToken();
        Call<NewStockResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .newStock("Bearer " + token, productId, price, stock);

        call.enqueue(new Callback<NewStockResponse>() {

            @Override
            public void onResponse(Call<NewStockResponse> call, Response<NewStockResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    NewStockResponse newStockResponse = response.body();
                    if (newStockResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                        if (mContext instanceof ProductOpenStoreActivity) {
                            ((ProductOpenStoreActivity) mContext).checkConnection();
                        }
                        mBottomSheetBehavior.setHideable(true);
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    } else {
                        Toast.makeText(mContext, "Can't open on the same day.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewStockResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
