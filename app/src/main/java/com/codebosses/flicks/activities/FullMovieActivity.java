package com.codebosses.flicks.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;

public class FullMovieActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.webViewFullMovie)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_movie);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            String url = getIntent().getStringExtra(EndpointKeys.MOVIE_URL);
            if (!url.isEmpty()) {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
//                        if (!mPb.isShown()) {
//                            mPb.setVisibility(View.VISIBLE);
//                        }
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
//                        if (mPb.isShown()) {
//                            mPb.setVisibility(View.GONE);
//                        }
                    }
                });
                webView.loadUrl(url);
            }
        }

    }
}
