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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.moviesdetail.MoviesDetailPagerAdapter;
import com.codebosses.flicks.adapters.moviesdetail.YoutubePlayerPagerAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesMainObject;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieDetailId;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.MovieDetailMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.codebosses.flicks.utils.FontUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoviesDetailActivity extends AppCompatActivity {

    //    Android fields....
//    @BindView(R.id.viewPagerYoutubePlayerMoviesDetail)
//    ViewPager viewPagerYoutubePlayer;
    @BindView(R.id.youtubePlayerViewMoviesDetail)
    YouTubePlayerView youTubePlayerView;
    @BindView(R.id.textViewTitleMoviesDetail)
    TextView textViewTitle;
    @BindView(R.id.ratingBarMovieDetail)
    MaterialRatingBar ratingBar;
    @BindView(R.id.tabLayoutMoviesDetail)
    TabLayout tabLayout;
    @BindView(R.id.viewPagerMoviesDetail)
    ViewPager viewPagerMoviesDetail;

    //    Retrofit calls....
    private Call<MoviesTrailerMainObject> moviesTrailerMainObjectCall;

    //    Instance fields....
    private List<MoviesTrailerResult> moviesTrailerResultList = new ArrayList<>();
    private YoutubePlayerPagerAdapter youtubePlayerPagerAdapter;
    private String movieId, movieTitle;
    private double rating;
    private MoviesDetailPagerAdapter moviesDetailPagerAdapter;

//    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            movieId = String.valueOf(getIntent().getIntExtra(EndpointKeys.MOVIE_ID, -1));
            movieTitle = getIntent().getStringExtra(EndpointKeys.MOVIE_TITLE);
            rating = getIntent().getDoubleExtra(EndpointKeys.RATING, 0.0);
            textViewTitle.setText(movieTitle);
            ratingBar.setRating((float) rating / 2);
            getMovieTrailers("en-US", movieId);
        }
        FontUtils.getFontUtils(this).setTextViewRegularFont(textViewTitle);

//        Setting tab layout adapter....
        moviesDetailPagerAdapter = new MoviesDetailPagerAdapter(getSupportFragmentManager(), this);
        viewPagerMoviesDetail.setAdapter(moviesDetailPagerAdapter);
        viewPagerMoviesDetail.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPagerMoviesDetail);

        if (movieId != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new EventBusMovieDetailId(Integer.parseInt(movieId)));
                }
            }, 500);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        showInterstitial();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (moviesTrailerMainObjectCall != null && moviesTrailerMainObjectCall.isExecuted()) {
            moviesTrailerMainObjectCall.cancel();
        }
        youTubePlayerView.release();
    }

    private void getMovieTrailers(String language, String movieId) {
        moviesTrailerMainObjectCall = Api.WEB_SERVICE.getMovieTrailer(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
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
//                            youtubePlayerPagerAdapter = new YoutubePlayerPagerAdapter(MoviesDetailActivity.this, moviesTrailerResultList);
//                            viewPagerYoutubePlayer.setAdapter(youtubePlayerPagerAdapter);
//                            viewPagerYoutubePlayer.setOffscreenPageLimit(moviesTrailerResultList.size() - 1);
                        } else {
                            Toast.makeText(MoviesDetailActivity.this, "Could not found trailer of this movie.", Toast.LENGTH_SHORT).show();
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
