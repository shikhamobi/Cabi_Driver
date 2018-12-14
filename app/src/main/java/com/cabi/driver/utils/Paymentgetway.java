package com.cabi.driver.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cabi.driver.FarecalcAct;
import com.cabi.driver.R;

import java.net.URI;
import java.net.URL;

/**
 * Created by developer on 7/6/18.
 */

public class Paymentgetway extends Activity {

    private WebView webview;
    private String url;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_webview);
        url = getIntent().getStringExtra("redirect_url");

        webview = (WebView) findViewById(R.id.webview_id);
        renderWebPage(url);
    }


    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onBackPressed();
            return;
        } else {
            doubleBackToExitPressedOnce = false;
            Toast.makeText(this, "Please Complete the Payment. Cannot Exit", Toast.LENGTH_SHORT).show();

        }

    }


    // Custom method to render a web page
    protected void renderWebPage(String urlToRender) {
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Do something on page loading started
                Toast.makeText(getApplicationContext(), "Page loading started", Toast.LENGTH_SHORT).show();

                String mUrl = view.getUrl();
                System.out.println("current url" + "......." + mUrl);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                // Do something when page loading finished
                Toast.makeText(getApplicationContext(), "Page loaded", Toast.LENGTH_SHORT).show();

                String mUrl = view.getUrl();
                System.out.println("current url" + "......." + mUrl);

                if (mUrl.contains("telr_payment_status")) {
                    FarecalcAct.Payment_status = 1;
                    finish();
                } else {
                    FarecalcAct.Payment_status = 0;
                }
            }

        });

        // Enable the javascript
        webview.getSettings().setJavaScriptEnabled(true);
        // Render the web page
        webview.loadUrl(urlToRender);
    }
}
