package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.image_slider.ImageSliderPagerAdapter;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.ZoomViewPager;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImagesSliderActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.viewPagerImagesSlider)
    ZoomViewPager viewPagerImageSlider;
    @BindView(R.id.toolbarImageSlider)
    Toolbar toolbar;
    @BindView(R.id.imageViewDownload)
    AppCompatImageView imageViewDownload;
    @BindView(R.id.adView)
    AdView adView;

    //    Adapter fields....
    ImageSliderPagerAdapter imageSliderPagerAdapter;

    //    Instance fields....
    private ArrayList<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_slider);
        ImagesSliderActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ValidUtils.changeToolbarFont(toolbar, this);
        }

        if (getIntent() != null) {
            images = getIntent().getStringArrayListExtra("images");
            String name = getIntent().getStringExtra(EndpointKeys.CELEB_NAME);
            int position = getIntent().getIntExtra(EndpointKeys.IMAGE_POSITION, 0);
            imageSliderPagerAdapter = new ImageSliderPagerAdapter(this, images, this);
            viewPagerImageSlider.setAdapter(imageSliderPagerAdapter);
            viewPagerImageSlider.setOffscreenPageLimit(images.size() - 1);
            viewPagerImageSlider.setCurrentItem(position);
            getSupportActionBar().setTitle(name);
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_image_slider, menu);
//        return true;
//    }

    @OnClick(R.id.imageViewDownload)
    public void onDownloadClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                downloadImage(images.get(viewPagerImageSlider.getCurrentItem()));
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            downloadImage(images.get(viewPagerImageSlider.getCurrentItem()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int counter = 0;
            for (int permission : grantResults) {
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    counter++;
                }
            }
            if (counter == grantResults.length) {
                downloadImage(images.get(viewPagerImageSlider.getCurrentItem()));
            }
        }
    }

    private void downloadImage(String path) {
        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String fileName = path.substring(path.lastIndexOf("/"));
        PRDownloader.download(path, file.getAbsolutePath(), fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {

                    @Override
                    public void onDownloadComplete() {
                        scanFile(file.getAbsolutePath());
                        Toast.makeText(ImagesSliderActivity.this, "Image downloaded successfully.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(ImagesSliderActivity.this, "Error downloading image.", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void scanFile(String path) {

        MediaScannerConnection.scanFile(ImagesSliderActivity.this,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }
}
