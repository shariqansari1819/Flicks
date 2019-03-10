package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.castandcrewadapter.CastAdapter;
import com.codebosses.flicks.adapters.castandcrewadapter.CrewAdapter;
import com.codebosses.flicks.adapters.moviesdetail.MoviesGenreAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.SimilarTvShowsAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.TvShowSeasonsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.castandcrew.CastAndCrewMainObject;
import com.codebosses.flicks.pojo.castandcrew.CastData;
import com.codebosses.flicks.pojo.castandcrew.CrewData;
import com.codebosses.flicks.pojo.eventbus.EventBusCastAndCrewClick;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.Season;
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.TvShowsDetailMainObject;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TvShowsDetailActivity extends AppCompatActivity {

    @BindView(R.id.youtubePlayerViewTvShowsDetail)
    YouTubePlayerView youTubePlayerView;
    @BindView(R.id.textViewTitleTvShowsDetail)
    TextView textViewTitle;
    @BindView(R.id.ratingBarTvShowsDetail)
    MaterialRatingBar ratingBar;
    @BindView(R.id.imageViewThumbnailTvShowsDetail)
    ImageView imageViewThumbnail;
    @BindView(R.id.textViewVotesTvShowsDetail)
    TextView textViewVoteCount;
    @BindView(R.id.textViewReleaseDateHeaderTvShowsDetail)
    TextView textViewReleaseDateHeader;
    @BindView(R.id.textViewYearTvShowsDetail)
    TextView textViewReleaseDate;
    @BindView(R.id.textViewOverviewTvShowsDetail)
    TextView textViewOverview;
    @BindView(R.id.cardViewThumbnailContainerTvShowsDetail)
    CardView cardViewThumbnail;
    @BindView(R.id.recyclerViewGenreTvShowsDetail)
    RecyclerView recyclerViewGenre;
    @BindView(R.id.recyclerViewCastTvShowsDetail)
    RecyclerView recyclerViewCast;
    @BindView(R.id.textViewCrewHeader)
    TextView textViewCrewHeader;
    @BindView(R.id.recyclerViewCrewTvShowsDetail)
    RecyclerView recyclerViewCrew;
    @BindView(R.id.textViewGenreHeader)
    TextView textViewGenreHeader;
    @BindView(R.id.textViewCastHeader)
    TextView textViewCastHeader;
    @BindView(R.id.textViewViewMoreSimilarTvShows)
    TextView textViewViewMoreSimilarTvShows;
    @BindView(R.id.textViewSimilarTvShowsHeader)
    TextView textViewSimilarTvShowsHeader;
    @BindView(R.id.recyclerViewSimilarTvShowsDetail)
    RecyclerView recyclerViewSimilarTvShows;
    @BindView(R.id.textViewSuggestionTvShowsHeader)
    TextView textViewSuggestionHeader;
    @BindView(R.id.textViewViewMoreSuggestionTvShows)
    TextView textViewViewMoreSuggestion;
    @BindView(R.id.recyclerViewSuggestionTvShowsDetail)
    RecyclerView recyclerViewSuggestedTvShows;
    private YouTubePlayer youTubePlayer;
    @BindView(R.id.nestedScrollViewTvShowsDetail)
    CustomNestedScrollView nestedScrollViewTvShowsDetail;
    @BindView(R.id.textViewStoryLineHeader)
    TextView textViewOverViewHeader;
    @BindView(R.id.textViewRatingTvShowsDetail)
    TextView textViewTvShowsRating;
    @BindView(R.id.textViewAudienceTvShowsDetail)
    TextView textViewAudienceRating;
    @BindView(R.id.textViewSeasonsHeader)
    TextView textViewSeasonsHeader;
    @BindView(R.id.textViewSeasonsNumber)
    TextView textViewSeasonsNumber;
    @BindView(R.id.recyclerViewSeasonsTvShowsDetail)
    RecyclerView recyclerViewSeasons;
    @BindView(R.id.toolbarTvShowsDetail)
    Toolbar toolbarTvShowsDetail;

    //    Retrofit calls....
    private Call<MoviesTrailerMainObject> moviesTrailerMainObjectCall;
    private Call<TvShowsDetailMainObject> tvShowsDetailMainObjectCall;
    private Call<TvMainObject> similarTvShowsCall;
    private Call<TvMainObject> suggestedTvShowsCall;
    private Call<CastAndCrewMainObject> castAndCrewMainObjectCall;

    //    Instance fields....
    private List<MoviesTrailerResult> moviesTrailerResultList = new ArrayList<>();
    private List<TvResult> similarTvResultList = new ArrayList<>();
    private List<TvResult> suggestedTvResultList = new ArrayList<>();
    private List<CastData> castDataList = new ArrayList<>();
    private List<CrewData> crewDataList = new ArrayList<>();
    private List<Season> seasonList = new ArrayList<>();
    private String tvShowId, tvShowTitle;
    private double rating;
    private int scrollingCounter = 0;

    //    Adapter fields....
    private SimilarTvShowsAdapter similarTvShowsAdapter;
    private SimilarTvShowsAdapter suggestedTvShowsAdapter;
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;
    private TvShowSeasonsAdapter tvShowSeasonsAdapter;

    //    Font fields....
    private FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_shows_detail);
        ButterKnife.bind(this);

//        Setting custom fonts....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewTitle);
        fontUtils.setTextViewLightFont(textViewVoteCount);
        fontUtils.setTextViewRegularFont(textViewReleaseDateHeader);
        fontUtils.setTextViewLightFont(textViewReleaseDate);
        fontUtils.setTextViewRegularFont(textViewGenreHeader);
        fontUtils.setTextViewRegularFont(textViewCastHeader);
        fontUtils.setTextViewRegularFont(textViewCrewHeader);
        fontUtils.setTextViewRegularFont(textViewSimilarTvShowsHeader);
        fontUtils.setTextViewRegularFont(textViewViewMoreSimilarTvShows);
        fontUtils.setTextViewRegularFont(textViewSuggestionHeader);
        fontUtils.setTextViewRegularFont(textViewViewMoreSuggestion);
        fontUtils.setTextViewRegularFont(textViewOverViewHeader);
        fontUtils.setTextViewLightFont(textViewOverview);
        fontUtils.setTextViewRegularFont(textViewTvShowsRating);
        fontUtils.setTextViewLightFont(textViewAudienceRating);
        fontUtils.setTextViewRegularFont(textViewSeasonsHeader);
        fontUtils.setTextViewLightFont(textViewSeasonsNumber);

        //        Setting layout managers for recycler view....
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSimilarTvShows.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSuggestedTvShows.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSeasons.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

//        Creating empty list adapter objects...
        similarTvShowsAdapter = new SimilarTvShowsAdapter(this, similarTvResultList, EndpointKeys.SIMILAR_TV_SHOWS_DETAIL);
        suggestedTvShowsAdapter = new SimilarTvShowsAdapter(this, suggestedTvResultList, EndpointKeys.SUGGESTED_TV_SHOWS_DETAIL);
        castAdapter = new CastAdapter(this, castDataList);
        crewAdapter = new CrewAdapter(this, crewDataList);
        tvShowSeasonsAdapter = new TvShowSeasonsAdapter(this, seasonList, EndpointKeys.SEASON);

//        Setting item animator for recycler views....
        recyclerViewSimilarTvShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSuggestedTvShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSeasons.setItemAnimator(new DefaultItemAnimator());

//        Setting emoty liste adapter to recycler views....
        recyclerViewSimilarTvShows.setAdapter(similarTvShowsAdapter);
        recyclerViewSuggestedTvShows.setAdapter(suggestedTvShowsAdapter);
        recyclerViewCast.setAdapter(castAdapter);
        recyclerViewCrew.setAdapter(crewAdapter);
        recyclerViewSeasons.setAdapter(tvShowSeasonsAdapter);

        if (getIntent() != null) {
            tvShowId = String.valueOf(getIntent().getIntExtra(EndpointKeys.TV_ID, -1));
            tvShowTitle = getIntent().getStringExtra(EndpointKeys.TV_NAME);
            rating = getIntent().getDoubleExtra(EndpointKeys.RATING, 0.0);
            ratingBar.setRating((float) rating / 2);
            getTvTrailers("en-US", tvShowId);
            getTvDetail("en-US", tvShowId);
            getSimilarTvShows(tvShowId, "en-US", 1);
            getSuggestedTvShows(tvShowId, "en-US", 1);
            getTvShowCredits(tvShowId);
        }


//        Checking if user scroll nested scroll view so that youtube player can pause/resume....
        if (nestedScrollViewTvShowsDetail != null) {

            nestedScrollViewTvShowsDetail.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                if (scrollY > oldScrollY) {
                    if (scrollingCounter == 0) {
                        if (youTubePlayer != null)
                            youTubePlayer.pause();
                    }
                    scrollingCounter++;
                }
                if (scrollY < oldScrollY) {
                }

                if (scrollY == 0) {
                    if (youTubePlayer != null) {
                        youTubePlayer.play();
                    }
                    scrollingCounter = 0;
                }

                if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight())) {
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (youTubePlayer != null) {
            youTubePlayer.play();
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (youTubePlayer != null) {
            youTubePlayer.pause();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (moviesTrailerMainObjectCall != null && moviesTrailerMainObjectCall.isExecuted()) {
            moviesTrailerMainObjectCall.cancel();
        }
        if (tvShowsDetailMainObjectCall != null && tvShowsDetailMainObjectCall.isExecuted()) {
            tvShowsDetailMainObjectCall.cancel();
        }
        if (similarTvShowsCall != null && similarTvShowsCall.isExecuted()) {
            similarTvShowsCall.cancel();
        }
        if (suggestedTvShowsCall != null && suggestedTvShowsCall.isExecuted()) {
            suggestedTvShowsCall.cancel();
        }
        if (castAndCrewMainObjectCall != null && castAndCrewMainObjectCall.isExecuted()) {
            castAndCrewMainObjectCall.cancel();
        }
        youTubePlayerView.release();
    }

    @OnClick(R.id.textViewViewMoreSimilarTvShows)
    public void onViewMoreSimilarTvShowsClick(TextView textViewSimilar) {
        Intent intent = new Intent(this, SimilarTvShowsActivity.class);
        intent.putExtra(EndpointKeys.TV_ID, tvShowId);
        startActivity(intent);
    }

    @OnClick(R.id.textViewViewMoreSuggestionTvShows)
    public void onViewMoreSuggestedTvShowsClick(TextView textViewSuggestion) {
        Intent intent = new Intent(this, SuggestedTvShowsActivity.class);
        intent.putExtra(EndpointKeys.TV_ID, tvShowId);
        startActivity(intent);
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
                            toolbarTvShowsDetail.setVisibility(View.GONE);
                            TvShowsDetailActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            youTubePlayerView.initialize(new YouTubePlayerInitListener() {
                                @Override
                                public void onInitSuccess(@NonNull YouTubePlayer youTubePlayer) {
                                    youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                                        @Override
                                        public void onReady() {
                                            super.onReady();
                                            TvShowsDetailActivity.this.youTubePlayer = youTubePlayer;
                                            youTubePlayer.loadVideo(moviesTrailerResultList.get(0).getKey(), 0);
                                        }
                                    });
                                }
                            }, true);
                        } else {
                            youTubePlayerView.setVisibility(View.GONE);
                            toolbarTvShowsDetail.setVisibility(View.VISIBLE);
                            setSupportActionBar(toolbarTvShowsDetail);
                            if (getSupportActionBar() != null) {
                                getSupportActionBar().setTitle(tvShowTitle);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                ValidUtils.changeToolbarFont(toolbarTvShowsDetail, TvShowsDetailActivity.this);
                            }
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

    private void getTvDetail(String language, String tvId) {
        tvShowsDetailMainObjectCall = Api.WEB_SERVICE.getTvDetail(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        tvShowsDetailMainObjectCall.enqueue(new Callback<TvShowsDetailMainObject>() {
            @Override
            public void onResponse(Call<TvShowsDetailMainObject> call, retrofit2.Response<TvShowsDetailMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    TvShowsDetailMainObject tvShowsDetailMainObject = response.body();
                    if (tvShowsDetailMainObject != null) {

                        String originalName = tvShowsDetailMainObject.getOriginal_name();
                        String overview = tvShowsDetailMainObject.getOverview();
                        String firstAirDate = tvShowsDetailMainObject.getFirst_air_date();
                        String tvShowPosterPath = tvShowsDetailMainObject.getPoster_path();

                        cardViewThumbnail.setVisibility(View.VISIBLE);
                        textViewReleaseDateHeader.setVisibility(View.VISIBLE);
                        ratingBar.setVisibility(View.VISIBLE);
                        textViewGenreHeader.setVisibility(View.VISIBLE);
                        textViewAudienceRating.setVisibility(View.VISIBLE);
                        textViewTvShowsRating.setVisibility(View.VISIBLE);
                        textViewOverViewHeader.setVisibility(View.VISIBLE);
                        textViewVoteCount.setVisibility(View.VISIBLE);

                        textViewSeasonsNumber.setText("(" + tvShowsDetailMainObject.getSeasons().size() + ")");
                        textViewTitle.setText(originalName);
                        textViewReleaseDate.setText(firstAirDate);
                        textViewTvShowsRating.setText(String.valueOf((float) rating / 2));
                        textViewOverview.setText(overview);
                        Glide.with(TvShowsDetailActivity.this)
                                .load(EndpointUrl.POSTER_BASE_URL + "/" + tvShowPosterPath)
                                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                .apply(new RequestOptions().fitCenter())
                                .into(imageViewThumbnail);
                        recyclerViewGenre.setAdapter(new MoviesGenreAdapter(TvShowsDetailActivity.this, tvShowsDetailMainObject.getGenres()));
                        if (tvShowsDetailMainObject.getSeasons().size() > 0) {
                            textViewSeasonsHeader.setVisibility(View.VISIBLE);
                            textViewSeasonsNumber.setVisibility(View.VISIBLE);
                            for (int i = 0; i < tvShowsDetailMainObject.getSeasons().size(); i++) {
                                seasonList.add(tvShowsDetailMainObject.getSeasons().get(i));
                                tvShowSeasonsAdapter.notifyItemInserted(seasonList.size() - 1);
                            }
                        }

                        if (TextUtils.isEmpty(overview)) {
                            textViewOverview.setVisibility(View.GONE);
                            textViewOverViewHeader.setVisibility(View.GONE);
                        }
                        if (tvShowsDetailMainObject.getGenres().size() == 0) {
                            textViewGenreHeader.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TvShowsDetailMainObject> call, Throwable error) {
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

    private void getSimilarTvShows(String tvId, String language, int pageNumber) {
        similarTvShowsCall = Api.WEB_SERVICE.getSimilarTvShows(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        similarTvShowsCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    TvMainObject tvMainObject = response.body();
                    if (tvMainObject != null) {
                        if (tvMainObject.getTotal_results() > 0) {
                            textViewSimilarTvShowsHeader.setVisibility(View.VISIBLE);
                            textViewViewMoreSimilarTvShows.setVisibility(View.VISIBLE);
                            for (int i = 0; i < tvMainObject.getResults().size(); i++) {
                                similarTvResultList.add(tvMainObject.getResults().get(i));
                                similarTvShowsAdapter.notifyItemInserted(similarTvResultList.size() - 1);
                            }
                        } else {
                            textViewSimilarTvShowsHeader.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TvMainObject> call, Throwable error) {
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
//                    textViewError.setText(couldNotGetMovies);
                }
            }
        });
    }

    private void getSuggestedTvShows(String tvId, String language, int pageNumber) {
        suggestedTvShowsCall = Api.WEB_SERVICE.getSuggestedTvShows(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        suggestedTvShowsCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    TvMainObject tvMainObject = response.body();
                    if (tvMainObject != null) {
                        if (tvMainObject.getTotal_results() > 0) {
                            textViewSuggestionHeader.setVisibility(View.VISIBLE);
                            textViewViewMoreSuggestion.setVisibility(View.VISIBLE);
                            for (int i = 0; i < tvMainObject.getResults().size(); i++) {
                                suggestedTvResultList.add(tvMainObject.getResults().get(i));
                                suggestedTvShowsAdapter.notifyItemInserted(suggestedTvResultList.size() - 1);
                            }
                        } else {
                            textViewSuggestionHeader.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TvMainObject> call, Throwable error) {
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
//                    textViewError.setText(couldNotGetMovies);
                }
            }
        });
    }

    private void getTvShowCredits(String tvId) {
        castAndCrewMainObjectCall = Api.WEB_SERVICE.getTvCredits(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY);
        castAndCrewMainObjectCall.enqueue(new Callback<CastAndCrewMainObject>() {
            @Override
            public void onResponse(Call<CastAndCrewMainObject> call, retrofit2.Response<CastAndCrewMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    CastAndCrewMainObject castAndCrewMainObject = response.body();
                    if (castAndCrewMainObject != null) {
                        if (castAndCrewMainObject.getCast().size() > 0) {
                            textViewCastHeader.setVisibility(View.VISIBLE);
                            for (int i = 0; i < castAndCrewMainObject.getCast().size(); i++) {
                                castDataList.add(castAndCrewMainObject.getCast().get(i));
                                castAdapter.notifyItemInserted(i);
                            }
                        } else {
                            textViewCastHeader.setVisibility(View.GONE);
                        }
                        if (castAndCrewMainObject.getCrew().size() > 0) {
                            textViewCrewHeader.setVisibility(View.VISIBLE);
                            for (int i = 0; i < castAndCrewMainObject.getCrew().size(); i++) {
                                crewDataList.add(castAndCrewMainObject.getCrew().get(i));
                                crewAdapter.notifyItemInserted(i);
                            }
                        } else {
                            textViewCrewHeader.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CastAndCrewMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                    } else {
                    }
                } else {
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
    public void eventBusSimilarTvShowClick(EventBusTvShowsClick eventBusTvShowsClick) {
        String tvTitle = "";
        int tvId = 0;
        double rating = 0.0;
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.SIMILAR_TV_SHOWS_DETAIL)) {
            tvId = similarTvResultList.get(eventBusTvShowsClick.getPosition()).getId();
            tvTitle = similarTvResultList.get(eventBusTvShowsClick.getPosition()).getName();
            rating = similarTvResultList.get(eventBusTvShowsClick.getPosition()).getVote_average();
        } else if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.SUGGESTED_TV_SHOWS_DETAIL)) {
            tvId = suggestedTvResultList.get(eventBusTvShowsClick.getPosition()).getId();
            tvTitle = suggestedTvResultList.get(eventBusTvShowsClick.getPosition()).getName();
            rating = suggestedTvResultList.get(eventBusTvShowsClick.getPosition()).getVote_average();
        } else if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.SEASON)) {
            Intent intent = new Intent(this, TvSeasonDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, tvShowId);
            intent.putExtra(EndpointKeys.SEASON_NUMBER, seasonList.get(eventBusTvShowsClick.getPosition()).getSeason_number());
            intent.putExtra(EndpointKeys.TV_NAME, seasonList.get(eventBusTvShowsClick.getPosition()).getName());
            startActivity(intent);
            return;
        }
        Intent intent = new Intent(this, TvShowsDetailActivity.class);
        intent.putExtra(EndpointKeys.TV_ID, tvId);
        intent.putExtra(EndpointKeys.TV_NAME, tvTitle);
        intent.putExtra(EndpointKeys.RATING, rating);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusCastAndCrewClick(EventBusCastAndCrewClick eventBusCastAndCrewClick) {
        int castId = -1;
        String name = "", image = "";
        if (eventBusCastAndCrewClick.getClickType().equals(EndpointKeys.CAST)) {
            castId = castDataList.get(eventBusCastAndCrewClick.getPosition()).getId();
            name = castDataList.get(eventBusCastAndCrewClick.getPosition()).getName();
            image = castDataList.get(eventBusCastAndCrewClick.getPosition()).getProfile_path();
        } else {
            castId = crewDataList.get(eventBusCastAndCrewClick.getPosition()).getId();
            name = crewDataList.get(eventBusCastAndCrewClick.getPosition()).getName();
            image = crewDataList.get(eventBusCastAndCrewClick.getPosition()).getProfile_path();
        }
        Intent intent = new Intent(this, CelebrityMoviesActivity.class);
        intent.putExtra(EndpointKeys.CELEBRITY_ID, castId);
        intent.putExtra(EndpointKeys.CELEB_NAME, name);
        intent.putExtra(EndpointKeys.CELEB_IMAGE, image);
        startActivity(intent);
    }


}