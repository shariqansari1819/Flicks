package com.codebosses.flicks.adapters.moviesdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebosses.flicks.R;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class YoutubePlayerPagerAdapter extends PagerAdapter {

    private List<MoviesTrailerResult> moviesTrailerResultList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public YoutubePlayerPagerAdapter(Context context, List<MoviesTrailerResult> moviesTrailerResultList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.moviesTrailerResultList = moviesTrailerResultList;
    }

    @Override
    public int getCount() {
        return moviesTrailerResultList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.pager_item_movies_detail, container, false);
        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtubePlayerMoviesDetailPagerItem);
        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = moviesTrailerResultList.get(position).getId();
                youTubePlayer.loadVideo(videoId, 0);
            }
        }, true);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
