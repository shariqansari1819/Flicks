package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
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
import com.codebosses.flicks.adapters.moviesdetail.SimilarMoviesAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.TvEpisodesAdapter;
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
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.TvShowsDetailMainObject;
import com.codebosses.flicks.pojo.tvseasons.Episode;
import com.codebosses.flicks.pojo.tvseasons.TvSeasonsMainObject;
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

public class TvSeasonDetailActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.youtubePlayerViewTvSeasonDetail)
    YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    @BindView(R.id.cardViewThumbnailContainerTvSeasonDetail)
    CardView cardViewTvSeasonThumbnail;
    @BindView(R.id.textViewTitleTvSeasonDetail)
    TextView textViewTitle;
    @BindView(R.id.ratingBarTvSeasonDetail)
    MaterialRatingBar ratingBar;
    @BindView(R.id.imageViewThumbnailTvSeasonDetail)
    ImageView imageViewThumbnail;
    @BindView(R.id.textViewVotesTvSeasonDetail)
    TextView textViewVoteCount;
    @BindView(R.id.textViewReleaseDateHeaderTvSeasonDetail)
    TextView textViewReleaseDateHeader;
    @BindView(R.id.textViewYearTvSeasonDetail)
    TextView textViewReleaseDate;
    @BindView(R.id.textViewOverviewTvSeasonDetail)
    TextView textViewOverview;
    //    @BindView(R.id.recyclerViewGenreTvSeasonDetail)
//    RecyclerView recyclerViewGenre;
    @BindView(R.id.recyclerViewCastTvSeasonDetail)
    RecyclerView recyclerViewCast;
    @BindView(R.id.textViewCrewHeader)
    TextView textViewCrewHeader;
    @BindView(R.id.recyclerViewCrewTvSeasonDetail)
    RecyclerView recyclerViewCrew;
    //    @BindView(R.id.textViewGenreHeader)
//    TextView textViewGenreHeader;
    @BindView(R.id.textViewCastHeader)
    TextView textViewCastHeader;
    @BindView(R.id.nestedScrollViewTvSeasonDetail)
    CustomNestedScrollView nestedScrollViewTvSeasonDetail;
    @BindView(R.id.textViewStoryLineHeader)
    TextView textViewOverViewHeader;
    @BindView(R.id.textViewRatingTvSeasonDetail)
    TextView textViewTvSeasonRating;
    @BindView(R.id.textViewAudienceTvSeasonDetail)
    TextView textViewAudienceRating;
    @BindView(R.id.textViewEpisodesHeader)
    TextView textViewEpisodesHeader;
    @BindView(R.id.textViewEpisodesNumber)
    TextView textViewEpisodesNumber;
    @BindView(R.id.recyclerViewEpisodesTvSeasonDetail)
    RecyclerView recyclerViewEpisodes;
    @BindView(R.id.toolbarTvSeasonDetail)
    Toolbar toolbarTvSeason;

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private String tvShowId, title;
    private int seasonNumber;
    private double rating;
    private int scrollingCounter = 0;
    private ArrayList<Episode> episodesList = new ArrayList<>();
    private List<MoviesTrailerResult> moviesTrailerResultList = new ArrayList<>();
    private List<CastData> castDataList = new ArrayList<>();
    private List<CrewData> crewDataList = new ArrayList<>();

    //    Retrofit fields....
    private Call<TvSeasonsMainObject> callTvSeasons;
    private Call<MoviesTrailerMainObject> tvSeasonTrailerMainObjectCall;
    private Call<CastAndCrewMainObject> castAndCrewMainObjectCall;

    //    Adapter fields....
    private TvEpisodesAdapter tvEpisodesAdapter;
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_season_detail);
        ButterKnife.bind(this);

        //        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewTitle);
        fontUtils.setTextViewLightFont(textViewVoteCount);
        fontUtils.setTextViewRegularFont(textViewReleaseDateHeader);
        fontUtils.setTextViewLightFont(textViewReleaseDate);
        fontUtils.setTextViewRegularFont(textViewCastHeader);
        fontUtils.setTextViewRegularFont(textViewCrewHeader);
        fontUtils.setTextViewRegularFont(textViewEpisodesHeader);
        fontUtils.setTextViewLightFont(textViewEpisodesNumber);
        fontUtils.setTextViewRegularFont(textViewOverViewHeader);
        fontUtils.setTextViewLightFont(textViewOverview);
        fontUtils.setTextViewRegularFont(textViewTvSeasonRating);
        fontUtils.setTextViewLightFont(textViewAudienceRating);


        //        Setting layout managers for recycler view....
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(TvSeasonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(TvSeasonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewEpisodes.setLayoutManager(new GridLayoutManager(this, 3));

//        Creating empty list adapter objects...
        castAdapter = new CastAdapter(this, castDataList);
        crewAdapter = new CrewAdapter(this, crewDataList);
        tvEpisodesAdapter = new TvEpisodesAdapter(this, episodesList, EndpointKeys.EPISODE);

//        Setting item animator for recycler views....
        recyclerViewEpisodes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());

//        Setting empty liste adapter to recycler views....
        recyclerViewCast.setAdapter(castAdapter);
        recyclerViewCrew.setAdapter(crewAdapter);
        recyclerViewEpisodes.setAdapter(tvEpisodesAdapter);

        if (getIntent() != null) {
            tvShowId = getIntent().getStringExtra(EndpointKeys.TV_ID);
            seasonNumber = getIntent().getIntExtra(EndpointKeys.SEASON_NUMBER, -1);
            title = getIntent().getStringExtra(EndpointKeys.TV_NAME);
            getTvSeasonDetail("en-US", tvShowId, String.valueOf(seasonNumber));
            getTvSeasonTrailers("en-US", tvShowId, String.valueOf(seasonNumber));
            getTvSeasonCredits(tvShowId, String.valueOf(seasonNumber));
        }

        if (nestedScrollViewTvSeasonDetail != null) {

            nestedScrollViewTvSeasonDetail.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

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
    protected void onDestroy() {
        super.onDestroy();
        if (callTvSeasons != null && callTvSeasons.isExecuted()) {
            callTvSeasons.cancel();
        }
        if (tvSeasonTrailerMainObjectCall != null && tvSeasonTrailerMainObjectCall.isExecuted()) {
            tvSeasonTrailerMainObjectCall.cancel();
        }
        if (castAndCrewMainObjectCall != null && castAndCrewMainObjectCall.isExecuted()) {
            castAndCrewMainObjectCall.cancel();
        }
    }

    private void getTvSeasonCredits(String tvId, String seasonNumber) {
        castAndCrewMainObjectCall = Api.WEB_SERVICE.getTvSeasonCredits(tvId, seasonNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, "en-US");
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

    private void getTvSeasonTrailers(String language, String tvId, String seasonNumber) {
        tvSeasonTrailerMainObjectCall = Api.WEB_SERVICE.getTvSeasonTrailer(tvId, seasonNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        tvSeasonTrailerMainObjectCall.enqueue(new Callback<MoviesTrailerMainObject>() {
            @Override
            public void onResponse(Call<MoviesTrailerMainObject> call, retrofit2.Response<MoviesTrailerMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesTrailerMainObject moviesTrailerMainObject = response.body();
                    if (moviesTrailerMainObject != null) {
                        moviesTrailerResultList = moviesTrailerMainObject.getResults();
                        if (moviesTrailerResultList.size() > 0) {
                            toolbarTvSeason.setVisibility(View.GONE);
                            TvSeasonDetailActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            youTubePlayerView.initialize(new YouTubePlayerInitListener() {
                                @Override
                                public void onInitSuccess(@NonNull YouTubePlayer youTubePlayer) {
                                    youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                                        @Override
                                        public void onReady() {
                                            super.onReady();
                                            TvSeasonDetailActivity.this.youTubePlayer = youTubePlayer;
                                            youTubePlayer.loadVideo(moviesTrailerResultList.get(0).getKey(), 0);
                                        }
                                    });
                                }
                            }, true);
                        } else {
                            youTubePlayerView.setVisibility(View.GONE);
                            toolbarTvSeason.setVisibility(View.VISIBLE);
                            setSupportActionBar(toolbarTvSeason);
                            if (getSupportActionBar() != null) {
                                getSupportActionBar().setTitle(title);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                ValidUtils.changeToolbarFont(toolbarTvSeason, TvSeasonDetailActivity.this);
                            }
                            Toast.makeText(TvSeasonDetailActivity.this, "Could not found trailer of this season.", Toast.LENGTH_SHORT).show();
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

    private void getTvSeasonDetail(String language, String tvId, String seasonNumber) {
        callTvSeasons = Api.WEB_SERVICE.getTvSeasonDetail(tvId, seasonNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        callTvSeasons.enqueue(new Callback<TvSeasonsMainObject>() {
            @Override
            public void onResponse(Call<TvSeasonsMainObject> call, retrofit2.Response<TvSeasonsMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    TvSeasonsMainObject tvSeasonsMainObject = response.body();
                    if (tvSeasonsMainObject != null) {

                        String originalName = tvSeasonsMainObject.getName();
                        String overview = tvSeasonsMainObject.getOverview();
                        String firstAirDate = tvSeasonsMainObject.getAir_date();
                        String tvShowPosterPath = tvSeasonsMainObject.getPoster_path();

                        cardViewTvSeasonThumbnail.setVisibility(View.VISIBLE);
                        textViewReleaseDateHeader.setVisibility(View.VISIBLE);
                        ratingBar.setVisibility(View.VISIBLE);
//                        textViewGenreHeader.setVisibility(View.VISIBLE);
                        textViewAudienceRating.setVisibility(View.VISIBLE);
                        textViewTvSeasonRating.setVisibility(View.VISIBLE);
                        textViewOverViewHeader.setVisibility(View.VISIBLE);
                        textViewVoteCount.setVisibility(View.VISIBLE);

                        textViewEpisodesNumber.setText("(" + tvSeasonsMainObject.getEpisodes().size() + ")");
                        textViewTitle.setText(originalName);
                        textViewReleaseDate.setText(firstAirDate);
                        textViewTvSeasonRating.setText(String.valueOf((float) rating / 2));
                        textViewOverview.setText(overview);
                        Glide.with(TvSeasonDetailActivity.this)
                                .load(EndpointUrl.POSTER_BASE_URL + "/" + tvShowPosterPath)
                                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                .apply(new RequestOptions().fitCenter())
                                .into(imageViewThumbnail);
//                        recyclerViewGenre.setAdapter(new MoviesGenreAdapter(TvSeasonDetailActivity.this, tvSeasonsMainObject.getGenres()));
                        if (tvSeasonsMainObject.getEpisodes().size() > 0) {
                            textViewEpisodesHeader.setVisibility(View.VISIBLE);
                            textViewEpisodesNumber.setVisibility(View.VISIBLE);
                            for (int i = 0; i < tvSeasonsMainObject.getEpisodes().size(); i++) {
                                episodesList.add(tvSeasonsMainObject.getEpisodes().get(i));
                                tvEpisodesAdapter.notifyItemInserted(episodesList.size() - 1);
                            }
                        }
                        if (TextUtils.isEmpty(overview)) {
                            textViewOverview.setVisibility(View.GONE);
                            textViewOverViewHeader.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TvSeasonsMainObject> call, Throwable error) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusEpisodeClick(EventBusTvShowsClick eventBusTvShowsClick) {
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.EPISODE)) {
            Intent intent = new Intent(this, EpisodeDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, tvShowId);
            intent.putExtra(EndpointKeys.SEASON_NUMBER, seasonNumber);
            intent.putExtra(EndpointKeys.EPISODE_NUMBER, episodesList.get(eventBusTvShowsClick.getPosition()).getEpisode_number());
            intent.putExtra(EndpointKeys.EPISODE_DETAIL, episodesList.get(eventBusTvShowsClick.getPosition()));
            intent.putExtra(EndpointKeys.TV_NAME, episodesList.get(eventBusTvShowsClick.getPosition()).getName());
            startActivity(intent);
        }
    }

}
