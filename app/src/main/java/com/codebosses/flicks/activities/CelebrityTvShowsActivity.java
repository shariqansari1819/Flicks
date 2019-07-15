package com.codebosses.flicks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.celebritiesadapter.CelebMoviesAdapter;
import com.codebosses.flicks.adapters.celebritiesadapter.CelebTvShowsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.celebritiespojo.celebmovies.CelebMoviesData;
import com.codebosses.flicks.pojo.celebritiespojo.celebmovies.CelebMoviesMainObject;
import com.codebosses.flicks.pojo.celebritiespojo.celebtvshows.CelebTvShowsData;
import com.codebosses.flicks.pojo.celebritiespojo.celebtvshows.CelebTvShowsMainObject;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityTvShowsClick;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.SortingUtils;
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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class CelebrityTvShowsActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageCelebrityTvShows)
    TextView textViewError;
    @BindView(R.id.recyclerViewCelebrityTvShows)
    RecyclerView recyclerViewCelebrityTvShows;
    @BindView(R.id.appBarCelebrityTvShows)
    Toolbar toolbarCelebrityTvShows;
    @BindView(R.id.circleImageCelebMoviesAppBar)
    CircleImageView circleImageView;
    @BindView(R.id.editTextSearchCelebrityTvShows)
    AppCompatEditText editTextSearch;
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.circularProgressBarCelebrityTvShows)
    CircularProgressBar circularProgressBar;

    //    Resource fields....
    @BindString(R.string.could_not_get_tv_shows)
    String couldNotGetTvShows;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Adapter fields....
    private CelebTvShowsAdapter celebMoviesAdapter;
    private String celebId, celebName, celebImage;
    private List<CelebTvShowsData> celebTvShowsDataList = new ArrayList<>();

    //    Retrofit fields....
    private Call<CelebTvShowsMainObject> celebTvShowsMainObjectCall;

    //    Ad mob fields....
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrity_tv_shows);
        ButterKnife.bind(this);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.testing_interstitial_admob_id));
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

        if (getIntent() != null) {
            celebId = String.valueOf(getIntent().getIntExtra(EndpointKeys.CELEBRITY_ID, -1));
            celebName = getIntent().getStringExtra(EndpointKeys.CELEB_NAME);
            celebImage = getIntent().getStringExtra(EndpointKeys.CELEB_IMAGE);
        }

        //        Setting custom action bar....
        setSupportActionBar(toolbarCelebrityTvShows);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(celebName);
            changeToolbarFont(toolbarCelebrityTvShows, this);
            Glide.with(this).load(EndpointUrl.POSTER_BASE_URL + "/" + celebImage).thumbnail(0.1f).into(circleImageView);
        }

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setEditTextRegularFont(editTextSearch);

        if (ValidUtils.isNetworkAvailable(this)) {
            celebMoviesAdapter = new CelebTvShowsAdapter(this, celebTvShowsDataList, EndpointKeys.CELEBRITY_TV_SHOWS);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerViewCelebrityTvShows.setLayoutManager(linearLayoutManager);
            recyclerViewCelebrityTvShows.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCelebrityTvShows.setAdapter(celebMoviesAdapter);
            getCelebrityTvShows("en-US");
        } else {
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(internetProblem);
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

//        All event listeners....
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                celebMoviesAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    public static void changeToolbarFont(Toolbar toolbar, Activity context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    FontUtils.getFontUtils(context).setTextViewRegularFont(tv);
                    break;
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        showInterstitial();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (celebTvShowsMainObjectCall != null && celebTvShowsMainObjectCall.isExecuted()) {
            celebTvShowsMainObjectCall.cancel();
        }
    }

    private void getCelebrityTvShows(String language) {
        circularProgressBar.setVisibility(View.VISIBLE);
        celebTvShowsMainObjectCall = ApiClient.getClient().create(Api.class).getCelebTvShows(celebId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        celebTvShowsMainObjectCall.enqueue(new Callback<CelebTvShowsMainObject>() {
            @Override
            public void onResponse(Call<CelebTvShowsMainObject> call, retrofit2.Response<CelebTvShowsMainObject> response) {
                circularProgressBar.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    CelebTvShowsMainObject celebMoviesMainObject = response.body();
                    if (celebMoviesMainObject != null) {
                        if (celebMoviesMainObject.getCast().size() > 0) {
                            List<CelebTvShowsData> moviesDataList = celebMoviesMainObject.getCast();
                            celebTvShowsDataList.addAll(moviesDataList);
//                            SortingUtils.sortCelebMoviesByDate(celebMoviesDataArrayList);
                            celebMoviesAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetTvShows);
                }
            }

            @Override
            public void onFailure(Call<CelebTvShowsMainObject> call, Throwable error) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusCelebMoviesClick(EventBusCelebrityTvShowsClick eventBusMovieClick) {
        if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.CELEBRITY_TV_SHOWS)) {
            Intent intent = new Intent(this, TvShowsDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, eventBusMovieClick.getId());
            intent.putExtra(EndpointKeys.TV_NAME, celebTvShowsDataList.get(eventBusMovieClick.getPosition()).getOriginalName());
            intent.putExtra(EndpointKeys.RATING, celebTvShowsDataList.get(eventBusMovieClick.getPosition()).getVoteAverage());
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
