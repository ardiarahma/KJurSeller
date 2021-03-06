package id.technow.kjurseller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.UpdateStockResponse;
import id.technow.kjurseller.model.User;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUpdateStock extends BottomSheetDialogFragment {
    Integer i = 1;
    private Button btnConfirm;
    private EditText edtStock;
    Context mContext;
    ProgressDialog loading;
    int locTodayId;
    private BottomSheetBehavior mBottomSheetBehavior;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_update_stock, null);
        dialog.setContentView(view);

        mContext = getActivity();
        Intent intent = getActivity().getIntent();
        locTodayId = intent.getIntExtra("locTodayId", 0);

        String productName = getArguments().getString("productName");
        String productPic = getArguments().getString("productPic");
        int productStock = getArguments().getInt("productStock", 0);

        TextView txtProductName = view.findViewById(R.id.txtProductName);
        txtProductName.setText(productName);
        TextView txtCurrentStock = view.findViewById(R.id.txtCurrentStock);
        txtCurrentStock.setText(String.valueOf(productStock));
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
                updateStock();
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

    private void updateStock() {
        String stockTxt = edtStock.getText().toString().trim();

        if (stockTxt.isEmpty()) {
            loading.dismiss();
            edtStock.setError("Stock is required");
            edtStock.requestFocus();
            return;
        }

        int stock = Integer.parseInt(stockTxt);
        String productId = getArguments().getString("productId");

        User user = SharedPrefManager.getInstance(mContext).getUser();
        String token = user.getToken();
        Call<UpdateStockResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateStock("Bearer " + token, productId, stock);

        call.enqueue(new Callback<UpdateStockResponse>() {

            @Override
            public void onResponse(Call<UpdateStockResponse> call, Response<UpdateStockResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    UpdateStockResponse updateStockResponse = response.body();
                    if (updateStockResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Stock added.", Toast.LENGTH_LONG).show();
                        if (mContext instanceof ProductUpdateStockActivity) {
                            ((ProductUpdateStockActivity) mContext).checkConnection();
                        }
                        mBottomSheetBehavior.setHideable(true);
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    } else {
                        Toast.makeText(mContext, updateStockResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateStockResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
