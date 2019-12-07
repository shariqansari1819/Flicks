package com.codebosses.flicks.adapters.image_slider;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ImageSliderPagerAdapter extends PagerAdapter {

    private List<String> images = new ArrayList<>();
    private Context context;
    private Activity activity;
    private LayoutInflater layoutInflater;

    public ImageSliderPagerAdapter(Context context, List<String> images, Activity activity) {
        this.context = context;
        this.images = images;
        layoutInflater = LayoutInflater.from(context);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.pager_item_image_slider, container, false);
        ImageView imageView = view.findViewById(R.id.imageViewPagerItemImageSlider);

        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
        photoViewAttacher.update();

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//        imageView.setMinimumHeight(height);
//        imageView.setMinimumWidth(width);

        Glide.with(context)
                .load(images.get(position))
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .apply(new RequestOptions().fitCenter())
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}