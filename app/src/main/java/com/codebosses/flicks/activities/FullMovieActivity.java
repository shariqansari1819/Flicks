package com.codebosses.flicks.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FullMovieActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.webViewFullMovie)
    WebView webView;
    @BindView(R.id.imageViewCloseFullMovie)
    ImageView imageViewClose;
    @BindView(R.id.circularProgressBarFullMovie)
    CircularProgressBar circularProgressBar;

    private String videoUrl = "";

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_movie);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            String url = getIntent().getStringExtra(EndpointKeys.MOVIE_URL);
            String movieName = getIntent().getStringExtra(EndpointKeys.MOVIE_TITLE);
            String moviePosterPath = getIntent().getStringExtra(EndpointKeys.MOVIES_IMAGES);
            if (!url.isEmpty()) {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setBuiltInZoomControls(false);
                webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                    }
                });

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        if (circularProgressBar.getVisibility() == View.GONE)
                            circularProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (circularProgressBar.getVisibility() == View.VISIBLE)
                            circularProgressBar.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadResource(WebView view, String url) {
                        super.onLoadResource(view, url);
                        if (url.contains(".mp4") && videoUrl.isEmpty()) {
                            videoUrl = url;
                            Intent intent = new Intent(FullMovieActivity.this, FullMoviePlayerActivity.class);
                            intent.putExtra(EndpointKeys.MOVIE_URL, videoUrl);
                            intent.putExtra(EndpointKeys.MOVIE_TITLE, movieName);
                            intent.putExtra(EndpointKeys.MOVIES_IMAGES, moviePosterPath);
                            startActivity(intent);
                            finish();
//                            Log.d("FullMovie", url);
                        }
                    }

                    @SuppressWarnings("deprecation")
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                        return shouldOverrideUrlLoading(url);
                    }

                    @TargetApi(Build.VERSION_CODES.N)
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
                        Uri uri = request.getUrl();
                        return shouldOverrideUrlLoading(uri.toString());
                    }

                    private boolean shouldOverrideUrlLoading(final String myUrl) {
                        try {
//                            Log.i("WebView", "shouldOverrideUrlLoading() URL : " + myUrl + " Host: " + new URL(myUrl).getHost());
                            if (new URL(myUrl).getHost().equalsIgnoreCase("oload.party")) {
                                webView.loadUrl(myUrl);
                            }
                        } catch (Exception e) {

                        }
                        return true; // Returning True means that application wants to leave the current WebView and handle the url itself, otherwise return false.
                    }
                });
                webView.loadUrl(url);
            }
        }

    }

    @OnClick(R.id.imageViewCloseFullMovie)
    public void onCloseClick(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}