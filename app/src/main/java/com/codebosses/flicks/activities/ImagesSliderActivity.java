package com.codebosses.flicks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.image_slider.ImageSliderPagerAdapter;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.ZoomViewPager;

import java.util.ArrayList;
import java.util.List;

public class ImagesSliderActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.viewPagerImagesSlider)
    ZoomViewPager viewPagerImageSlider;
    @BindView(R.id.toolbarImageSlider)
    Toolbar toolbar;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
