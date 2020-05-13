package id.technow.kjurseller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {

    TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        toolbarTitle = findViewById(R.id.toolbarTitle);
        WebView mWebView = (WebView) findViewById(R.id.webView2);
        String bannerLink = getIntent().getStringExtra("BannerLink");
        mWebView.loadUrl("http://"+bannerLink);
        mWebView.setWebViewClient(new BannerWebViewClient());

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

    }

    private class BannerWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(view.getTitle().length() > 20){
                String title = view.getTitle().substring(0, 20) + "....";
                toolbarTitle.setText(title);
            }else {
                toolbarTitle.setText(view.getTitle());
            }

        }
    }
}
