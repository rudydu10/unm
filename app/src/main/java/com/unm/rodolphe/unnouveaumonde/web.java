package com.unm.rodolphe.unnouveaumonde;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class web extends Activity {

    public WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        webView = new WebView(this);

        //activer JavaScript

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(Constants.webserver_ADRESS);
        webView.setWebViewClient(new WebViewClient()
        {
          @Override
          public boolean shouldOverrideUrlLoading(WebView view, String url)
          {
              view.loadUrl(url);
              return true;
          }
        });

        this.setContentView(webView);
    }

}
