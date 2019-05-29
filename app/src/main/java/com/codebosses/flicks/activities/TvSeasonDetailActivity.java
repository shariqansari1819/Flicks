package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.castandcrewadapter.CastAdapter;
import com.codebosses.flicks.adapters.castandcrewadapter.CrewAdapter;
import com.codebosses.flicks.adapters.moviesdetail.MoviesGenreAdapter;
import com.codebosses.flicks.adapters.moviesdetail.SimilarMoviesAdapter;
import com.codebosses.flicks.adapters.moviesdetail.VideosAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.EpisodePhotosAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.TvEpisodesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.castandcrew.CastAndCrewMainObject;
import com.codebosses.flicks.pojo.castandcrew.CastData;
import com.codebosses.flicks.pojo.castandcrew.CrewData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosMainObject;
import com.codebosses.flicks.pojo.eventbus.EventBusCastAndCrewClick;
import com.codebosses.flicks.pojo.eventbus.EventBusImageClick;
import com.codebosses.flicks.pojo.eventbus.EventBusPlayVideo;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.TvShowsDetailMainObject;
import com.codebosses.flicks.pojo.tvseasons.Episode;
import com.codebosses.flicks.pojo.tvseasons.TvSeasonsMainObject;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;
import com.codebosses.flicks.utils.customviews.curve_image_view.CrescentoImageView;
import com.dd.ShadowLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
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
    @BindView(R.id.textViewVideosHeader)
    TextView textViewVideosHeader;
    @BindView(R.id.recyclerViewVideosTvSeasonDetail)
    RecyclerView recyclerViewVideos;
    @BindView(R.id.circularProgressBarTvSeasonDetail)
    CircularProgressBar circularProgressBarTvSeasonDetail;
    //    @BindView(R.id.viewBlurTvSeason)
//    View viewBlur;
    @BindView(R.id.imageViewCoverTvSeason)
    CrescentoImageView imageViewCover;
    @BindView(R.id.shadowPlayButtonTvSeason)
    ShadowLayout shadowLayoutPlayButton;
    @BindView(R.id.imageButtonPlayTvSeason)
    AppCompatImageButton imageButtonPlay;
    @BindView(R.id.cardViewThumbnailContainerTvSeason)
    CardView cardViewTvSeasonThumbnail;
    @BindView(R.id.textViewTitleTvSeason)
    TextView textViewTitle;
    @BindView(R.id.ratingBarTvSeason)
    MaterialRatingBar ratingBar;
    @BindView(R.id.imageViewThumbnailTvSeason)
    ImageView imageViewThumbnail;
    @BindView(R.id.textViewVotesTvSeason)
    TextView textViewVoteCount;
    @BindView(R.id.textViewReleaseDateHeaderTvSeason)
    TextView textViewReleaseDateHeader;
    @BindView(R.id.textViewYearTvSeason)
    TextView textViewReleaseDate;
    @BindView(R.id.textViewOverviewTvSeason)
    TextView textViewOverview;
    @BindView(R.id.recyclerViewCastTvSeasonDetail)
    RecyclerView recyclerViewCast;
    @BindView(R.id.textViewCrewHeader)
    TextView textViewCrewHeader;
    @BindView(R.id.recyclerViewCrewTvSeasonDetail)
    RecyclerView recyclerViewCrew;
    @BindView(R.id.textViewCastHeader)
    TextView textViewCastHeader;
    @BindView(R.id.nestedScrollViewTvSeasonDetail)
    CustomNestedScrollView nestedScrollViewTvSeasonDetail;
    @BindView(R.id.textViewStoryLineHeader)
    TextView textViewOverViewHeader;
    @BindView(R.id.textViewRatingTvSeason)
    TextView textViewTvSeasonRating;
    @BindView(R.id.textViewAudienceTvSeason)
    TextView textViewAudienceRating;
    @BindView(R.id.textViewEpisodesHeader)
    TextView textViewEpisodesHeader;
    @BindView(R.id.textViewEpisodesNumber)
    TextView textViewEpisodesNumber;
    @BindView(R.id.recyclerViewEpisodesTvSeasonDetail)
    RecyclerView recyclerViewEpisodes;
    @BindView(R.id.toolbarTvSeason)
    Toolbar toolbarTvSeason;
    @BindView(R.id.textViewVideosCountTvSeasonDetail)
    TextView textViewVideosCount;
    @BindView(R.id.textViewImagesHeader)
    TextView textViewImageHeader;
    @BindView(R.id.recyclerViewImagesTvSeasonDetail)
    RecyclerView recyclerViewImages;
    @BindView(R.id.textViewImagesCountTvSeasonDetail)
    TextView textViewImagesCounter;

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
    private List<EpisodePhotosData> imagesPhotoList = new ArrayList<>();

    //    Retrofit fields....
    private Call<TvSeasonsMainObject> callTvSeasons;
    private Call<MoviesTrailerMainObject> tvSeasonTrailerMainObjectCall;
    private Call<CastAndCrewMainObject> castAndCrewMainObjectCall;
    private Call<EpisodePhotosMainObject> moviesImagesCall;

    //    Adapter fields....
    private TvEpisodesAdapter tvEpisodesAdapter;
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;
    private VideosAdapter videosAdapter;
    private EpisodePhotosAdapter imagesAdapter;

    //    Ad mob fields....
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_season_detail);
        ButterKnife.bind(this);
        TvSeasonDetailActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_admob_id));
//        AdRequest adRequestInterstitial = new AdRequest.Builder().build();
//        mInterstitialAd.loadAd(adRequestInterstitial);
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                showInterstitial();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//            }
//        });

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
        fontUtils.setTextViewRegularFont(textViewVideosHeader);
        fontUtils.setTextViewRegularFont(textViewImageHeader);
        fontUtils.setTextViewLightFont(textViewImagesCounter);

        //        Setting layout managers for recycler view....
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(TvSeasonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(TvSeasonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(TvSeasonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(TvSeasonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewEpisodes.setLayoutManager(new GridLayoutManager(this, 3));

//        Creating empty list adapter objects...
        castAdapter = new CastAdapter(this, castDataList);
        crewAdapter = new CrewAdapter(this, crewDataList);
        tvEpisodesAdapter = new TvEpisodesAdapter(this, episodesList, EndpointKeys.EPISODE);
        videosAdapter = new VideosAdapter(this, moviesTrailerResultList);
        imagesAdapter = new EpisodePhotosAdapter(this, imagesPhotoList, EndpointKeys.TV_SEASON_IMAGES);

//        Setting item animator for recycler views....
        recyclerViewEpisodes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewImages.setItemAnimator(new DefaultItemAnimator());

//        Setting empty liste adapter to recycler views....
        recyclerViewCast.setAdapter(castAdapter);
        recyclerViewCrew.setAdapter(crewAdapter);
        recyclerViewEpisodes.setAdapter(tvEpisodesAdapter);
        recyclerViewVideos.setAdapter(videosAdapter);
        recyclerViewImages.setAdapter(imagesAdapter);

        setSupportActionBar(toolbarTvSeason);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        if (getIntent() != null) {
            tvShowId = getIntent().getStringExtra(EndpointKeys.TV_ID);
            seasonNumber = getIntent().getIntExtra(EndpointKeys.SEASON_NUMBER, -1);
            title = getIntent().getStringExtra(EndpointKeys.TV_NAME);
            getTvSeasonDetail("en-US", tvShowId, String.valueOf(seasonNumber));
            getTvSeasonTrailers("en-US", tvShowId, String.valueOf(seasonNumber));
            getTvSeasonCredits(tvShowId, String.valueOf(seasonNumber));
            getTvSeasonImages(tvShowId, seasonNumber, "");
        }

    }

//    private void showInterstitial() {
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//            AdRequest adRequest = new AdRequest.Builder().build();
//            mInterstitialAd.loadAd(adRequest);
//        }
//    }

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
        if (moviesImagesCall != null && moviesImagesCall.isExecuted()) {
            moviesImagesCall.cancel();
        }
    }

    private void getTvSeasonCredits(String tvId, String seasonNumber) {
        castAndCrewMainObjectCall = ApiClient.getClient().create(Api.class).getTvSeasonCredits(tvId, seasonNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, "en-US");
        castAndCrewMainObjectCall.enqueue(new Callback<CastAndCrewMainObject>() {
            @Override
            public void onResponse(Call<CastAndCrewMainObject> call, retrofit2.Response<CastAndCrewMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    CastAndCrewMainObject castAndCrewMainObject = response.body();
                    if (castAndCrewMainObject != null) {
                        if (castAndCrewMainObject.getCast().size() > 0) {
                            textViewCastHeader.setVisibility(View.VISIBLE);
                            recyclerViewCast.setVisibility(View.VISIBLE);
                            for (int i = 0; i < castAndCrewMainObject.getCast().size(); i++) {
                                castDataList.add(castAndCrewMainObject.getCast().get(i));
                                castAdapter.notifyItemInserted(i);
                            }
                        } else {
                            textViewCastHeader.setVisibility(View.GONE);
                            recyclerViewCast.setVisibility(View.GONE);
                        }
                        if (castAndCrewMainObject.getCrew().size() > 0) {
                            textViewCrewHeader.setVisibility(View.VISIBLE);
                            recyclerViewCrew.setVisibility(View.VISIBLE);
                            for (int i = 0; i < castAndCrewMainObject.getCrew().size(); i++) {
                                crewDataList.add(castAndCrewMainObject.getCrew().get(i));
                                crewAdapter.notifyItemInserted(i);
                            }
                        } else {
                            textViewCrewHeader.setVisibility(View.GONE);
                            recyclerViewCrew.setVisibility(View.GONE);
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
        tvSeasonTrailerMainObjectCall = ApiClient.getClient().create(Api.class).getTvSeasonTrailer(tvId, seasonNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        tvSeasonTrailerMainObjectCall.enqueue(new Callback<MoviesTrailerMainObject>() {
            @Override
            public void onResponse(Call<MoviesTrailerMainObject> call, retrofit2.Response<MoviesTrailerMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesTrailerMainObject moviesTrailerMainObject = response.body();
                    if (moviesTrailerMainObject != null) {
                        if (moviesTrailerMainObject.getResults().size() > 0) {
                            recyclerViewVideos.setVisibility(View.VISIBLE);
                            textViewVideosHeader.setVisibility(View.VISIBLE);
                            imageButtonPlay.setVisibility(View.VISIBLE);
                            textViewVideosCount.setVisibility(View.VISIBLE);
                            Glide.with(TvSeasonDetailActivity.this)
                                    .load(EndpointUrl.YOUTUBE_THUMBNAIL_BASE_URL + response.body().getResults().get(0).getKey() + "/mqdefault.jpg")
                                    .apply(new RequestOptions().centerCrop())
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .into(imageViewCover);
                            for (int i = 0; i < moviesTrailerMainObject.getResults().size(); i++) {
                                moviesTrailerResultList.add(moviesTrailerMainObject.getResults().get(i));
                                videosAdapter.notifyItemInserted(i);
                            }
                            textViewVideosCount.setText("(" + moviesTrailerResultList.size() + ")");
                        } else {
                            textViewVideosHeader.setVisibility(View.GONE);
                            textViewVideosCount.setVisibility(View.GONE);
                            imageButtonPlay.setVisibility(View.GONE);
                            recyclerViewVideos.setVisibility(View.GONE);
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

    private void getTvSeasonImages(String tvShowId, int seasonNumber, String language) {
        moviesImagesCall = ApiClient.getClient().create(Api.class).getTvSeasonImages(tvShowId, seasonNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        moviesImagesCall.enqueue(new Callback<EpisodePhotosMainObject>() {
            @Override
            public void onResponse(Call<EpisodePhotosMainObject> call, retrofit2.Response<EpisodePhotosMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    EpisodePhotosMainObject imagesMainObject = response.body();
                    if (imagesMainObject != null) {
                        if (imagesMainObject.getPosters() != null && imagesMainObject.getPosters().size() > 0) {
                            textViewImageHeader.setVisibility(View.VISIBLE);
                            textViewImagesCounter.setVisibility(View.VISIBLE);
                            recyclerViewImages.setVisibility(View.VISIBLE);
                            for (int i = 0; i < imagesMainObject.getPosters().size(); i++) {
                                imagesPhotoList.add(imagesMainObject.getPosters().get(i));
                                imagesAdapter.notifyItemInserted(i);
                            }
                            textViewImagesCounter.setText("(" + imagesPhotoList.size() + ")");
                        } else {
                            textViewImageHeader.setVisibility(View.GONE);
                            textViewImagesCounter.setVisibility(View.GONE);
                            recyclerViewImages.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EpisodePhotosMainObject> call, Throwable error) {
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

    private void getTvSeasonDetail(String language, String tvId, String seasonNumber) {
        circularProgressBarTvSeasonDetail.setVisibility(View.VISIBLE);
        callTvSeasons = ApiClient.getClient().create(Api.class).getTvSeasonDetail(tvId, seasonNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        callTvSeasons.enqueue(new Callback<TvSeasonsMainObject>() {
            @Override
            public void onResponse(Call<TvSeasonsMainObject> call, retrofit2.Response<TvSeasonsMainObject> response) {
                circularProgressBarTvSeasonDetail.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    TvSeasonsMainObject tvSeasonsMainObject = response.body();
                    if (tvSeasonsMainObject != null) {

                        String originalName = tvSeasonsMainObject.getName();
                        String overview = tvSeasonsMainObject.getOverview();
                        String firstAirDate = tvSeasonsMainObject.getAir_date();
                        String tvShowPosterPath = tvSeasonsMainObject.getPoster_path();

//                        viewBlur.setVisibility(View.VISIBLE);
                        shadowLayoutPlayButton.setVisibility(View.VISIBLE);
                        cardViewTvSeasonThumbnail.setVisibility(View.VISIBLE);
                        textViewReleaseDateHeader.setVisibility(View.VISIBLE);
                        ratingBar.setVisibility(View.VISIBLE);
                        textViewAudienceRating.setVisibility(View.VISIBLE);
                        textViewTvSeasonRating.setVisibility(View.VISIBLE);
                        textViewOverViewHeader.setVisibility(View.VISIBLE);
                        textViewVoteCount.setVisibility(View.VISIBLE);

                        textViewEpisodesNumber.setText("(" + tvSeasonsMainObject.getEpisodes().size() + ")");
                        textViewTitle.setText(originalName);
                        textViewReleaseDate.setText(firstAirDate);
                        textViewTvSeasonRating.setText(String.valueOf(rating));
                        ratingBar.setRating((float) rating / 2);
                        textViewOverview.setText(overview);

                        Glide.with(TvSeasonDetailActivity.this)
                                .load(EndpointUrl.POSTER_BASE_URL + "/" + tvShowPosterPath)
                                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                .into(imageViewThumbnail);
                        if (moviesTrailerResultList.size() > 0) {
                            Glide.with(TvSeasonDetailActivity.this)
                                    .load(EndpointUrl.YOUTUBE_THUMBNAIL_BASE_URL + moviesTrailerResultList.get(0).getKey() + "/mqdefault.jpg")
                                    .apply(new RequestOptions().centerCrop())
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .into(imageViewCover);
                        } else {
                            Glide.with(TvSeasonDetailActivity.this)
                                    .load(EndpointUrl.POSTER_BASE_URL + "/" + tvShowPosterPath)
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .apply(new RequestOptions().fitCenter())
                                    .into(imageViewCover);
                        }
                        if (tvSeasonsMainObject.getEpisodes().size() > 0) {
                            textViewEpisodesHeader.setVisibility(View.VISIBLE);
                            textViewEpisodesNumber.setVisibility(View.VISIBLE);
                            recyclerViewEpisodes.setVisibility(View.VISIBLE);
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
                circularProgressBarTvSeasonDetail.setVisibility(View.GONE);
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

    @OnClick({R.id.imageButtonPlayTvSeason, R.id.imageViewCoverTvSeason})
    public void onPlayButtonClick(View view) {
        if (moviesTrailerResultList.size() > 0) {
            startTrailerActivity(moviesTrailerResultList.get(0).getKey());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusPlayVideo(EventBusPlayVideo eventBusPlayVideo) {
        if (moviesTrailerResultList.size() > 0)
            startTrailerActivity(moviesTrailerResultList.get(eventBusPlayVideo.getPosition()).getKey());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusImageClick(EventBusImageClick eventBusImageClick) {
        if (eventBusImageClick.getClickType().equals(EndpointKeys.TV_SEASON_IMAGES)) {
            startImageSliderActivity(eventBusImageClick.getPosition());
        }
    }

    private void startImageSliderActivity(int position) {
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < imagesPhotoList.size(); i++) {
            images.add(EndpointUrl.SLIDER_IMAGE_BASE_URL + imagesPhotoList.get(i).getFile_path());
        }
        Intent intent = new Intent(this, ImagesSliderActivity.class);
        intent.putExtra("images", images);
        intent.putExtra(EndpointKeys.CELEB_NAME, textViewTitle.getText().toString());
        intent.putExtra(EndpointKeys.IMAGE_POSITION, position);
        startActivity(intent);
    }

    private void startTrailerActivity(String key) {
        Intent intent = new Intent(this, TrailerActivity.class);
        intent.putExtra(EndpointKeys.YOUTUBE_KEY, key);
        startActivity(intent);
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
        Intent intent = new Intent(this, CelebrityDetailActivity.class);
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
