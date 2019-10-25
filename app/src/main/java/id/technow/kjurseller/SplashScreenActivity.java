package id.technow.kjurseller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import id.technow.kjurseller.storage.SharedPrefManager;

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
