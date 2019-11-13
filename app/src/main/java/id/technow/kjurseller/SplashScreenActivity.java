package id.technow.kjurseller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import id.technow.kjurseller.api.RetrofitClient;
import id.technow.kjurseller.model.VersionAppResponse;
import id.technow.kjurseller.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    Context mContext;
    ImageView imgSplashOne;
    ImageView imgSplashTwo;
    ImageView imgSplashThree;
    ImageView imgSplashFour;
    ImageView imgSplashFive;
    int loadPosition = 0;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = this;

        imgSplashOne = findViewById(R.id.imgSplashOne);
        imgSplashTwo = findViewById(R.id.imgSplashTwo);
        imgSplashThree = findViewById(R.id.imgSplashThree);
        imgSplashFour = findViewById(R.id.imgSplashFour);
        imgSplashFive = findViewById(R.id.imgSplashFive);

        mHandler = new Handler();
        mStatusChecker.run();

        checkConnection();
    }

    private void checkLogin() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (SharedPrefManager.getInstance(SplashScreenActivity.this).isLoggedIn()) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                finish();
            }
        }, 2500L);
    }

    private void checkVersion() {
        Call<VersionAppResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .version();

        call.enqueue(new Callback<VersionAppResponse>() {
            @Override
            public void onResponse(Call<VersionAppResponse> call, Response<VersionAppResponse> response) {
                VersionAppResponse versionAppResponse = response.body();
                if (response.isSuccessful()) {
                    if (versionAppResponse.getStatus().equals("success")) {
                        String ver = String.valueOf(versionAppResponse.getVersionApp().getVersion());
                        String subVer = String.valueOf(versionAppResponse.getVersionApp().getVersionSub());
                        String subSubVer = String.valueOf(versionAppResponse.getVersionApp().getVersionSubSub());
                        String versionApi = ver + "." + subVer + "." + subSubVer;
                        PackageInfo pInfo = null;
                        try {
                            pInfo = mContext.getPackageManager().getPackageInfo(getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        String version = pInfo.versionName;
                        if (version.equals(versionApi)) {
                            checkLogin();
                        } else {
                            final Dialog dialog = new Dialog(mContext);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setContentView(R.layout.dialog_update_app);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
                            btnUpdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    final String appPackageName = getPackageName();
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            });

                            Button btnUpdateLater = dialog.findViewById(R.id.btnUpdateLater);
                            btnUpdateLater.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    SplashScreenActivity.this.finish();
                                }
                            });
                            dialog.show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<VersionAppResponse> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                Toast.makeText(mContext, "Something wrong. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkConnection() {
        if (isNetworkAvailable()) {
            checkLogin();
        } else {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_no_internet);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            Button btnRetry = dialog.findViewById(R.id.btnRetry);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    checkConnection();
                }
            });
            dialog.show();
        }
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            displayLoadingPosition(loadPosition);

            loadPosition++;

            mHandler.postDelayed(mStatusChecker, 250);
        }
    };

    private void displayLoadingPosition(int loadPosition) {
        int emphasizedViewPos = loadPosition % 5;

        imgSplashOne.setImageResource(R.drawable.ic_splash_one);
        imgSplashTwo.setImageResource(R.drawable.ic_splash_two);
        imgSplashThree.setImageResource(R.drawable.ic_splash_three);
        imgSplashFour.setImageResource(R.drawable.ic_splash_four);
        imgSplashFive.setImageResource(R.drawable.ic_splash_five);

        switch (emphasizedViewPos) {
            case 0:
                imgSplashOne.setImageResource(R.drawable.ic_splash_one_w);
                break;
            case 1:
                imgSplashTwo.setImageResource(R.drawable.ic_splash_two_w);
                break;
            case 2:
                imgSplashThree.setImageResource(R.drawable.ic_splash_three_w);
                break;
            case 3:
                imgSplashFour.setImageResource(R.drawable.ic_splash_four_w);
                break;
            case 4:
                imgSplashFive.setImageResource(R.drawable.ic_splash_five_w);
                break;
        }
    }
}
