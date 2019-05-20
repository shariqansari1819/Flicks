package com.codebosses.flicks.adapters.celebrity_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

public class CelebrityImagesPagerAdapter extends PagerAdapter {

    private List<EpisodePhotosData> celebImagesList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public CelebrityImagesPagerAdapter(Context context, List<EpisodePhotosData> celebImagesList) {
        this.context = context;
        this.celebImagesList = celebImagesList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return celebImagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.pager_item_celeb_images, container, false);
        AppCompatImageView imageView = view.findViewById(R.id.imageViewCelebPagerItem);

        Glide.with(context)
                .load(EndpointUrl.PROFILE_BASE_URL + celebImagesList.get(position).getFile_path())
                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                .into(imageView);

        container.addView(view);
        return view;
    }

}
