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
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.adapters.tvshowsadapter.TvShowsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.celebritiespojo.KnownFor;
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

public class CelebrityMoviesActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageCelebrityMovies)
    TextView textViewError;
    @BindView(R.id.recyclerViewCelebrityMovies)
    RecyclerView recyclerViewCelebrityMovies;
    @BindView(R.id.appBarCelebrityMovies)
    Toolbar toolbarCelebrityMovies;
    @BindView(R.id.circleImageCelebMoviesAppBar)
    CircleImageView circleImageView;
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.adView)
    AdView adView;


    //    Resource fields....
    @BindString(R.string.could_not_get_movies)
    String couldNotGetTvShows;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Adapter fields....
    private ArrayList<KnownFor> knownForArrayList = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private String celebId, celebName, celebImage;
    private List<MoviesResult> moviesResultArrayList = new ArrayList<>();

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrity_movies);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        if (getIntent() != null) {
            celebId = String.valueOf(getIntent().getIntExtra(EndpointKeys.CELEBRITY_ID, -1));
            celebName = getIntent().getStringExtra(EndpointKeys.CELEB_NAME);
            celebImage = getIntent().getStringExtra(EndpointKeys.CELEB_IMAGE);
            knownForArrayList = getIntent().getParcelableArrayListExtra(EndpointKeys.CELEB_MOVIES);
            for (int i = 0; i < knownForArrayList.size(); i++) {
                KnownFor knownFor = knownForArrayList.get(i);
                MoviesResult moviesResult = new MoviesResult();
                moviesResult.setVote_count(knownFor.getVote_count());
                moviesResult.setId(knownFor.getId());
                moviesResult.setVideo(knownFor.getVideo());
                moviesResult.setVote_average(knownFor.getVote_average());
                moviesResult.setTitle(knownFor.getTitle());
                moviesResult.setPopularity(knownFor.getPopularity());
                moviesResult.setPoster_path(knownFor.getPoster_path());
                moviesResult.setOriginal_language(knownFor.getOriginal_language());
                moviesResult.setOriginal_title(knownFor.getOriginal_title());
                moviesResult.setGenre_ids(knownFor.getGenre_ids());
                moviesResult.setBackdrop_path(knownFor.getBackdrop_path());
                moviesResult.setAdult(knownFor.getAdult());
                moviesResult.setOverview(knownFor.getOverview());
                moviesResult.setRelease_date(knownFor.getRelease_date());
                moviesResultArrayList.add(moviesResult);
            }
        }

        //        Setting custom action bar....
        setSupportActionBar(toolbarCelebrityMovies);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(celebName);
            Glide.with(this).load(EndpointUrl.POSTER_BASE_URL + "/" + celebImage).into(circleImageView);
        }

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewBoldFont(textViewError);

        if (ValidUtils.isNetworkAvailable(this)) {
            moviesAdapter = new MoviesAdapter(this, moviesResultArrayList, EndpointKeys.KNOW_FOR_MOVIES);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerViewCelebrityMovies.setLayoutManager(linearLayoutManager);
            recyclerViewCelebrityMovies.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCelebrityMovies.setAdapter(moviesAdapter);
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

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showInterstitial();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusCelebMoviesClick(EventBusMovieClick eventBusMovieClick) {
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.KNOW_FOR_MOVIES)) {
            Intent intent = new Intent(this, MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, moviesResultArrayList.get(eventBusMovieClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.MOVIE_TITLE, moviesResultArrayList.get(eventBusMovieClick.getPosition()).getTitle());
            intent.putExtra(EndpointKeys.RATING, moviesResultArrayList.get(eventBusMovieClick.getPosition()).getVote_average());
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
