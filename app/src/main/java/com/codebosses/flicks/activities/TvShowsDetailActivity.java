package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.tvshowsdetail.TvShowsDetailPagerAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowDetailId;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.codebosses.flicks.utils.FontUtils;
import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class TvShowsDetailActivity extends AppCompatActivity {

    @BindView(R.id.youtubePlayerViewTvShowsDetail)
    YouTubePlayerView youTubePlayerView;
    @BindView(R.id.textViewTitleTvShowsDetail)
    TextView textViewTitle;
    @BindView(R.id.ratingBarTvShowsDetail)
    MaterialRatingBar ratingBar;
    @BindView(R.id.tabLayoutTvShowsDetail)
    TabLayout tabLayout;
    @BindView(R.id.viewPagerTvShowsDetail)
    ViewPager viewPageTvShowsDetail;

    //    Retrofit calls....
    private Call<MoviesTrailerMainObject> moviesTrailerMainObjectCall;

    //    Instance fields....
    private List<MoviesTrailerResult> moviesTrailerResultList = new ArrayList<>();
    private String tvShowId, tvShowTitle;
    private double rating;
    private TvShowsDetailPagerAdapter tvShowsDetailPagerAdapter;

//    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_shows_detail);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            tvShowId = String.valueOf(getIntent().getIntExtra(EndpointKeys.TV_ID, -1));
            tvShowTitle = getIntent().getStringExtra(EndpointKeys.TV_NAME);
            rating = getIntent().getDoubleExtra(EndpointKeys.RATING, 0.0);
            textViewTitle.setText(tvShowTitle);
            ratingBar.setRating((float) rating / 2);
            getTvTrailers("en-US", tvShowId);
        }
        FontUtils.getFontUtils(this).setTextViewRegularFont(textViewTitle);

//        Setting tab layout adapter....
        tvShowsDetailPagerAdapter = new TvShowsDetailPagerAdapter(getSupportFragmentManager(), this);
        viewPageTvShowsDetail.setAdapter(tvShowsDetailPagerAdapter);
        viewPageTvShowsDetail.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPageTvShowsDetail);

        if (tvShowId != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new EventBusTvShowDetailId(Integer.parseInt(tvShowId)));
                }
            }, 500);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (moviesTrailerMainObjectCall != null && moviesTrailerMainObjectCall.isExecuted()) {
            moviesTrailerMainObjectCall.cancel();
        }
        youTubePlayerView.release();
    }

    private void getTvTrailers(String language, String tvId) {
        moviesTrailerMainObjectCall = Api.WEB_SERVICE.getTvTrailer(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        moviesTrailerMainObjectCall.enqueue(new Callback<MoviesTrailerMainObject>() {
            @Override
            public void onResponse(Call<MoviesTrailerMainObject> call, retrofit2.Response<MoviesTrailerMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesTrailerMainObject moviesTrailerMainObject = response.body();
                    if (moviesTrailerMainObject != null) {
                        moviesTrailerResultList = moviesTrailerMainObject.getResults();
                        if (moviesTrailerResultList.size() > 0) {
                            youTubePlayerView.initialize(new YouTubePlayerInitListener() {
                                @Override
                                public void onInitSuccess(@NonNull YouTubePlayer youTubePlayer) {
                                    youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                                        @Override
                                        public void onReady() {
                                            super.onReady();
                                            youTubePlayer.loadVideo(moviesTrailerResultList.get(0).getKey(), 0);
                                        }
                                    });
                                }
                            }, true);
                        } else {
                            Toast.makeText(TvShowsDetailActivity.this, "Could not found trailer of this movie.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesTrailerMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
//                        textViewError.setText(internetProblem);
                    } else {
//                        textViewError.setText(error.getMessage());
                    }
                } else {
//                    textViewError.setText(couldNotGetCelebrities);
                }
            }
        });
    }

}