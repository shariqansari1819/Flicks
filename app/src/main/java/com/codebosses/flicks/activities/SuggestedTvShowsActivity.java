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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SuggestedTvShowsActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageSuggestedTvShows)
    TextView textViewError;
    @BindView(R.id.circularProgressBarSuggestedTvShows)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewSuggestedTvShows)
    RecyclerView recyclerViewSuggestedTvShows;
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.appBarSuggestedTvShows)
    Toolbar toolbarSuggestedTvShows;
    @BindView(R.id.textViewAppBarMainTitle)
    TextView textViewAppBarTitle;
    @BindView(R.id.adView)
    AdView adView;

    //    Resource fields....
    @BindString(R.string.could_not_get_suggested_tv_shows)
    String couldNotGetTvShows;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Retrofit fields....
    private Call<TvMainObject> suggestedTvShowsCall;

    //    Adapter fields....
    private List<TvResult> suggestedTvShowsList = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int pageNumber = 1, totalPages = 0;
    private String tvShowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_tv_shows);
        ButterKnife.bind(this);

        //        Setting custom action bar....
        setSupportActionBar(toolbarSuggestedTvShows);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            textViewAppBarTitle.setText(getResources().getString(R.string.recommended_tv_shows));
        }

        //        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setTextViewRegularFont(textViewAppBarTitle);

        if (ValidUtils.isNetworkAvailable(this)) {

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });

            tvShowsAdapter = new TvShowsAdapter(this, suggestedTvShowsList, EndpointKeys.SUGGESTED_TV_SHOWS);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerViewSuggestedTvShows.setLayoutManager(linearLayoutManager);
            recyclerViewSuggestedTvShows.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSuggestedTvShows.setAdapter(tvShowsAdapter);
            if (getIntent() != null) {
                tvShowId = getIntent().getStringExtra(EndpointKeys.TV_ID);
                if (tvShowId != null) {
                    circularProgressBar.setVisibility(View.VISIBLE);
                    getSuggestedTvShows(tvShowId, "en-US", pageNumber);
                }
            }

        } else {
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(internetProblem);
        }

        recyclerViewSuggestedTvShows.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getSuggestedTvShows(tvShowId, "en-US", pageNumber);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (suggestedTvShowsCall != null && suggestedTvShowsCall.isExecuted()) {
            suggestedTvShowsCall.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    private void getSuggestedTvShows(String tvShowId, String language, int pageNumber) {
        suggestedTvShowsCall = ApiClient.getClient().create(Api.class).getSuggestedTvShows(tvShowId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        suggestedTvShowsCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                circularProgressBar.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    TvMainObject tvMainObject = response.body();
                    if (tvMainObject != null) {
                        totalPages = tvMainObject.getTotal_pages();
                        if (tvMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < tvMainObject.getResults().size(); i++) {
                                suggestedTvShowsList.add(tvMainObject.getResults().get(i));
                                tvShowsAdapter.notifyItemInserted(suggestedTvShowsList.size() - 1);
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
    public void eventBusSuggestedTvShowClick(EventBusTvShowsClick eventBusTvShowsClick) {
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.SUGGESTED_TV_SHOWS)) {
            Intent intent = new Intent(this, MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, suggestedTvShowsList.get(eventBusTvShowsClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.TV_NAME, suggestedTvShowsList.get(eventBusTvShowsClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.RATING, suggestedTvShowsList.get(eventBusTvShowsClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }
}
