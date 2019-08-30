package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.adapters.tvshowsadapter.TvShowsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SimilarTvShowsActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageSimilarTvShows)
    TextView textViewError;
    @BindView(R.id.circularProgressBarSimilarTvShows)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewSimilarTvShows)
    RecyclerView recyclerViewSimilarMovies;
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.appBarSimilarTvShows)
    Toolbar toolbarSimilarMovies;
    @BindView(R.id.textViewAppBarMainTitle)
    TextView textViewAppBarTitle;
    @BindView(R.id.adView)
    AdView adView;

    //    Resource fields....
    @BindString(R.string.could_not_get_similar_tv_shows)
    String couldNotGetTvShows;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Retrofit fields....
    private Call<TvMainObject> similarTvShowsCall;

    //    Adapter fields....
    private List<TvResult> similarTvResultList = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int pageNumber = 1, totalPages = 0;
    private String tvShowsId;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_tv_shows);
        ButterKnife.bind(this);

//        Settinf custom action bar....
        setSupportActionBar(toolbarSimilarMovies);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            textViewAppBarTitle.setText(getResources().getString(R.string.similar_tv_shows));
        }

        //        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setTextViewRegularFont(textViewAppBarTitle);

        if (ValidUtils.isNetworkAvailable(this)) {

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_admob_id));
            AdRequest adRequestInterstitial = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequestInterstitial);
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    showInterstitial();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }
            });

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });

            tvShowsAdapter = new TvShowsAdapter(this, similarTvResultList, EndpointKeys.SIMILAR_TV_SHOWS);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerViewSimilarMovies.setLayoutManager(linearLayoutManager);
            recyclerViewSimilarMovies.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSimilarMovies.setAdapter(tvShowsAdapter);

            if (getIntent() != null) {
                tvShowsId = getIntent().getStringExtra(EndpointKeys.TV_ID);
                if (tvShowsId != null) {
                    circularProgressBar.setVisibility(View.VISIBLE);
                    getSimilarTvShows(tvShowsId, "en-US", pageNumber);
                }
            }

        } else {
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(internetProblem);
        }

        recyclerViewSimilarMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getSimilarTvShows(tvShowsId, "en-US", pageNumber);
                }
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (similarTvShowsCall != null && similarTvShowsCall.isExecuted()) {
            similarTvShowsCall.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showInterstitial();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void getSimilarTvShows(String tvShowsId, String language, int pageNumber) {
        similarTvShowsCall = ApiClient.getClient().create(Api.class).getSimilarTvShows(tvShowsId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        similarTvShowsCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                circularProgressBar.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    TvMainObject tvMainObject = response.body();
                    if (tvMainObject != null) {
                        totalPages = tvMainObject.getTotal_pages();
                        if (tvMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < tvMainObject.getResults().size(); i++) {
                                similarTvResultList.add(tvMainObject.getResults().get(i));
                                tvShowsAdapter.notifyItemInserted(similarTvResultList.size() - 1);
                            }
                        }
                    }
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetTvShows);
                }
            }

            @Override
            public void onFailure(Call<TvMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                circularProgressBar.setVisibility(View.INVISIBLE);
                textViewError.setVisibility(View.VISIBLE);
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                        textViewError.setText(internetProblem);
                    } else {
                        textViewError.setText(error.getMessage());
                    }
                } else {
                    textViewError.setText(couldNotGetTvShows);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSimilarTvShowsClick(EventBusTvShowsClick eventBusTvShowsClick) {
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.SIMILAR_TV_SHOWS)) {
            Intent intent = new Intent(this, MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, similarTvResultList.get(eventBusTvShowsClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.TV_NAME, similarTvResultList.get(eventBusTvShowsClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.RATING, similarTvResultList.get(eventBusTvShowsClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

}
