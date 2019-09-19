package com.codebosses.flicks.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.castandcrewadapter.CastAdapter;
import com.codebosses.flicks.adapters.castandcrewadapter.CrewAdapter;
import com.codebosses.flicks.adapters.moviesdetail.VideosAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.EpisodePhotosAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.castandcrew.CastAndCrewMainObject;
import com.codebosses.flicks.pojo.castandcrew.CastData;
import com.codebosses.flicks.pojo.castandcrew.CrewData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosMainObject;
import com.codebosses.flicks.pojo.eventbus.EventBusImageClick;
import com.codebosses.flicks.pojo.eventbus.EventBusPlayVideo;
import com.codebosses.flicks.pojo.moviespojo.ExternalId;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.codebosses.flicks.pojo.tvseasons.Episode;
import com.codebosses.flicks.utils.DateUtils;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;
import com.codebosses.flicks.utils.customviews.curve_image_view.CrescentoImageView;
import com.devs.readmoreoption.ReadMoreOption;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;

public class EpisodeDetailActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.textViewVideosHeader)
    TextView textViewVideosHeader;
    @BindView(R.id.recyclerViewVideosTvEpisodeDetail)
    RecyclerView recyclerViewVideos;
    @BindView(R.id.circularProgressBarTvEpisodeDetail)
    CircularProgressBar circularProgressBarTvSeasonDetail;
    //    @BindView(R.id.viewBlurTvEpisode)
//    View viewBlur;
    @BindView(R.id.imageViewCoverTvEpisode)
    CrescentoImageView imageViewCover;
    @BindView(R.id.imageButtonPlayTvEpisode)
    AppCompatImageButton imageButtonPlay;
    @BindView(R.id.textViewTitleTvEpisodeDetail)
    TextView textViewTitle;
    @BindView(R.id.ratingBarTvEpisodeDetail)
    MaterialRatingBar ratingBar;
    @BindView(R.id.imageViewThumbnailEpisodeDetail)
    ImageView imageViewThumbnail;
    @BindView(R.id.textViewVotesTvEpisodeDetail)
    TextView textViewVoteCount;
    @BindView(R.id.textViewReleaseDateHeaderTvEpisodeDetail)
    TextView textViewReleaseDateHeader;
    @BindView(R.id.textViewYearTvEpisodeDetail)
    TextView textViewReleaseDate;
    @BindView(R.id.textViewOverviewTvEpisodeDetail)
    TextView textViewOverview;
    @BindView(R.id.cardViewThumbnailContainerEpisodeDetail)
    CardView cardViewThumbnail;
    @BindView(R.id.recyclerViewCastTvEpisodeDetail)
    RecyclerView recyclerViewCast;
    @BindView(R.id.textViewCrewHeader)
    TextView textViewCrewHeader;
    @BindView(R.id.recyclerViewCrewTvEpisodeDetail)
    RecyclerView recyclerViewCrew;
    @BindView(R.id.textViewCastHeader)
    TextView textViewCastHeader;
    @BindView(R.id.nestedScrollViewEpisodeDetail)
    CustomNestedScrollView nestedScrollViewTvEpisodeDetail;
    @BindView(R.id.textViewStoryLineHeader)
    TextView textViewOverViewHeader;
    @BindView(R.id.textViewRatingTvEpisodeDetail)
    TextView textViewTvShowsRating;
    @BindView(R.id.textViewAudienceTvEpisodeDetail)
    TextView textViewAudienceRating;
    @BindView(R.id.textViewPhotosHeader)
    TextView textViewPhotos;
    @BindView(R.id.recyclerViewPhotosEpisodeDetail)
    RecyclerView recyclerViewPhotos;
    @BindView(R.id.toolbarTvEpisodeDetail)
    Toolbar toolbarTvEpisode;
    @BindView(R.id.textViewVideosCountTvEpisodeDetail)
    TextView textViewVideosCount;
    @BindView(R.id.textViewWatchFullMovie)
    AppCompatButton buttonWatchFullMovie;
    @BindView(R.id.textViewImagesCountEpisodeDetail)
    TextView textViewImagesCount;
    private SweetAlertDialog sweetAlertDialog;

    //    Retrofit calls....
    private Call<MoviesTrailerMainObject> moviesTrailerMainObjectCall;
    private Call<EpisodePhotosMainObject> episodePhotosMainObjectCall;
    private Call<ExternalId> externalIdCall;
    private Call<CastAndCrewMainObject> castAndCrewMainObjectCall;

    //    Instance fields....
    private List<MoviesTrailerResult> moviesTrailerResultList = new ArrayList<>();
    private List<CastData> castDataList = new ArrayList<>();
    private List<CrewData> crewDataList = new ArrayList<>();
    private List<EpisodePhotosData> episodePhotosDataList = new ArrayList<>();
    private Episode episode;
    private String tvShowId, seasonNumber, episodeNumber, title;

    //    Adapter fields....
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;
    private VideosAdapter videosAdapter;
    private EpisodePhotosAdapter episodePhotosAdapter;

    //    Font fields....
    private FontUtils fontUtils;

    //    Ad mob fields....
//    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);
        ValidUtils.transparentStatusAndNavigation(this);
        ButterKnife.bind(this);
//        EpisodeDetailActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewTitle);
        fontUtils.setTextViewLightFont(textViewVoteCount);
        fontUtils.setTextViewRegularFont(textViewReleaseDateHeader);
        fontUtils.setTextViewLightFont(textViewReleaseDate);
        fontUtils.setTextViewRegularFont(textViewCastHeader);
        fontUtils.setTextViewRegularFont(textViewCrewHeader);
        fontUtils.setTextViewRegularFont(textViewOverViewHeader);
        fontUtils.setTextViewLightFont(textViewOverview);
        fontUtils.setTextViewLightFont(textViewAudienceRating);
        fontUtils.setTextViewRegularFont(textViewPhotos);
        fontUtils.setTextViewRegularFont(textViewVideosHeader);
        fontUtils.setTextViewRegularFont(textViewVideosCount);
        fontUtils.setButtonRegularFont(buttonWatchFullMovie);
        fontUtils.setTextViewRegularFont(textViewImagesCount);

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

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#03a9f4"));
        sweetAlertDialog.setCancelable(false);

        //        Setting layout managers for recycler view....
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(EpisodeDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(EpisodeDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(EpisodeDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(EpisodeDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

//        Creating empty list adapter objects...
        castAdapter = new CastAdapter(this, castDataList);
        crewAdapter = new CrewAdapter(this, crewDataList);
        videosAdapter = new VideosAdapter(this, moviesTrailerResultList);
        episodePhotosAdapter = new EpisodePhotosAdapter(this, episodePhotosDataList, EndpointKeys.EPISODE_IMAGES);

//        Setting item animator for recycler views....
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPhotos.setItemAnimator(new DefaultItemAnimator());

//        Setting empty list adapter to recycler views....
        recyclerViewCast.setAdapter(castAdapter);
        recyclerViewCrew.setAdapter(crewAdapter);
        recyclerViewVideos.setAdapter(videosAdapter);
        recyclerViewPhotos.setAdapter(episodePhotosAdapter);

        setSupportActionBar(toolbarTvEpisode);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null) {
            tvShowId = getIntent().getStringExtra(EndpointKeys.TV_ID);
            seasonNumber = String.valueOf(getIntent().getIntExtra(EndpointKeys.SEASON_NUMBER, -1));
            episodeNumber = String.valueOf(getIntent().getIntExtra(EndpointKeys.EPISODE_NUMBER, -1));
            episode = getIntent().getParcelableExtra(EndpointKeys.EPISODE_DETAIL);
            title = getIntent().getStringExtra(EndpointKeys.TV_NAME);
            setEpisodeData();
            getTvEpisodeTrailer("en-US", tvShowId, seasonNumber, episodeNumber);
            getTvEpisodePhotos("en-US", tvShowId, seasonNumber, episodeNumber);
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
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (episodePhotosMainObjectCall != null && episodePhotosMainObjectCall.isExecuted()) {
            episodePhotosMainObjectCall.cancel();
        }
        if (moviesTrailerMainObjectCall != null && moviesTrailerMainObjectCall.isExecuted()) {
            moviesTrailerMainObjectCall.cancel();
        }
    }

    private void getTvEpisodeTrailer(String language, String tvId, String seasonNumber, String episodeNumber) {
        moviesTrailerMainObjectCall = ApiClient.getClient().create(Api.class).getTvEpisodeTrailer(tvId, seasonNumber, episodeNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        moviesTrailerMainObjectCall.enqueue(new Callback<MoviesTrailerMainObject>() {
            @Override
            public void onResponse(Call<MoviesTrailerMainObject> call, retrofit2.Response<MoviesTrailerMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesTrailerMainObject moviesTrailerMainObject = response.body();
                    if (moviesTrailerMainObject != null) {
                        if (moviesTrailerMainObject.getResults().size() > 0) {
                            recyclerViewVideos.setVisibility(View.VISIBLE);
                            textViewVideosCount.setVisibility(View.VISIBLE);
                            textViewVideosHeader.setVisibility(View.VISIBLE);
                            imageButtonPlay.setVisibility(View.VISIBLE);
                            Glide.with(EpisodeDetailActivity.this)
                                    .load(EndpointUrl.YOUTUBE_THUMBNAIL_BASE_URL + response.body().getResults().get(0).getKey() + "/mqdefault.jpg")
                                    .apply(new RequestOptions().centerCrop())
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .thumbnail(0.1f)
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

    private void getTvEpisodePhotos(String language, String tvId, String seasonNumber, String episodeNumber) {
        episodePhotosMainObjectCall = ApiClient.getClient().create(Api.class).getTvEpisodePhotos(tvId, seasonNumber, episodeNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        episodePhotosMainObjectCall.enqueue(new Callback<EpisodePhotosMainObject>() {
            @Override
            public void onResponse(Call<EpisodePhotosMainObject> call, retrofit2.Response<EpisodePhotosMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStills().size() > 0) {
                            recyclerViewPhotos.setVisibility(View.VISIBLE);
                            textViewPhotos.setVisibility(View.VISIBLE);
                            textViewImagesCount.setVisibility(View.GONE);
                            textViewImagesCount.setText("(" + response.body().getStills().size() + ")");
                            for (int i = 0; i < response.body().getStills().size(); i++) {
                                episodePhotosDataList.add(response.body().getStills().get(i));
                                episodePhotosAdapter.notifyItemInserted(i);
                            }
                        } else {
                            recyclerViewPhotos.setVisibility(View.GONE);
                            textViewPhotos.setVisibility(View.GONE);
                            textViewImagesCount.setVisibility(View.GONE);
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

    private void setEpisodeData() {
//        if (episode.getCrew().size() > 0) {
//            for (int i = 0; i < episode.getCrew().size(); i++) {
//                textViewCrewHeader.setVisibility(View.VISIBLE);
//                crewDataList.add(episode.getCrew().get(i));
//                crewAdapter.notifyItemInserted(i);
//            }
//        }
//        if (episode.getGuest_stars().size() > 0) {
//            textViewCastHeader.setVisibility(View.VISIBLE);
//            for (int i = 0; i < episode.getGuest_stars().size(); i++) {
//                castDataList.add(episode.getGuest_stars().get(i));
//                castAdapter.notifyItemInserted(i);
//            }
//        }
//        viewBlur.setVisibility(View.VISIBLE);
        String name = episode.getName();
        String releaseDate = episode.getAir_date();
        double rating = episode.getVote_average();
        String overview = episode.getOverview();

//        imageButtonPlay.setVisibility(View.VISIBLE);
        textViewTitle.setVisibility(View.VISIBLE);
        cardViewThumbnail.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
        textViewAudienceRating.setVisibility(View.VISIBLE);
        textViewTvShowsRating.setVisibility(View.VISIBLE);
        textViewVoteCount.setVisibility(View.VISIBLE);

        if (name != null && !name.isEmpty()) {
            textViewTitle.setText(episode.getName());
        }
        if (releaseDate != null && !releaseDate.isEmpty()) {
            textViewReleaseDate.setText(episode.getAir_date());
            textViewReleaseDate.setVisibility(View.VISIBLE);
            textViewReleaseDateHeader.setVisibility(View.VISIBLE);
        }
        textViewTvShowsRating.setText(String.valueOf(rating));
        ratingBar.setRating((float) rating / 2);
        if (overview != null && !overview.isEmpty()) {
            textViewOverViewHeader.setVisibility(View.VISIBLE);
            textViewOverview.setVisibility(View.VISIBLE);
            ReadMoreOption readMoreOption = new ReadMoreOption.Builder(EpisodeDetailActivity.this)
                    .textLength(2, ReadMoreOption.TYPE_LINE) // OR
                    //.textLength(300, ReadMoreOption.TYPE_CHARACTER)
                    .moreLabel("MORE")
                    .lessLabel("LESS")
                    .moreLabelColor(Color.RED)
                    .lessLabelColor(getResources().getColor(R.color.colorAccent))
                    .labelUnderLine(true)
                    .expandAnimation(true)
                    .build();

            readMoreOption.addReadMoreTo(textViewOverview, overview);
        }
        Glide.with(EpisodeDetailActivity.this)
                .load(EndpointUrl.POSTER_BASE_URL + "/" + episode.getStill_path())
                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                .thumbnail(0.1f)
                .into(imageViewThumbnail);
        if (moviesTrailerResultList.size() > 0) {
            Glide.with(EpisodeDetailActivity.this)
                    .load(EndpointUrl.YOUTUBE_THUMBNAIL_BASE_URL + moviesTrailerResultList.get(0).getKey() + "/hqdefault.jpg")
                    .apply(new RequestOptions().centerCrop())
                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                    .thumbnail(0.1f)
                    .into(imageViewCover);
        } else {
            Glide.with(EpisodeDetailActivity.this)
                    .load(EndpointUrl.POSTER_BASE_URL + episode.getStill_path())
                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                    .apply(new RequestOptions().fitCenter())
                    .thumbnail(0.1f)
                    .into(imageViewCover);
        }
        if (TextUtils.isEmpty(episode.getOverview())) {
            textViewOverview.setVisibility(View.GONE);
            textViewOverViewHeader.setVisibility(View.GONE);
        }

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(episode.getAir_date());
            if (!DateUtils.isAfterToday(date.getTime())) {
                buttonWatchFullMovie.setVisibility(View.VISIBLE);
            } else {
                buttonWatchFullMovie.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
    }

    @OnClick({R.id.imageButtonPlayTvEpisode, R.id.imageViewCoverTvEpisode})
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
        if (eventBusImageClick.getClickType().equals(EndpointKeys.EPISODE_IMAGES)) {
            startImageSliderActivity(eventBusImageClick.getPosition());
        }
    }

    @OnClick(R.id.textViewWatchFullMovie)
    public void onWatchFullMovieClick(View view) {
        getTvExternalId(tvShowId);
    }

    private void getTvExternalId(String tvShowId) {
        sweetAlertDialog.setTitleText("Extracting episode source...");
        sweetAlertDialog.show();
        externalIdCall = ApiClient.getClient().create(Api.class).getTvExternalId(tvShowId, EndpointKeys.THE_MOVIE_DB_API_KEY);
        externalIdCall.enqueue(new Callback<ExternalId>() {
            @Override
            public void onResponse(Call<ExternalId> call, retrofit2.Response<ExternalId> response) {
                if (response != null && response.isSuccessful()) {
                    ExternalId externalId = response.body();
                    if (externalId != null) {
                        generateTicket(externalId.getImdbId(), seasonNumber);
                    }
                }
            }

            @Override
            public void onFailure(Call<ExternalId> call, Throwable error) {
                sweetAlertDialog.dismiss();
                Toast.makeText(EpisodeDetailActivity.this, "Could not get episode.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateTicket(String tvShowId, String seasonNumber) {
        sweetAlertDialog.setTitleText("Loading episode...");
        AndroidNetworking.get("https://api6.ipify.org/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String ipAddress) {
                        AndroidNetworking.get(EndpointUrl.VIDEO_SPIDER_BASE_URL)
                                .addQueryParameter("key", EndpointKeys.VIDEO_SPIDER_KEY)
                                .addQueryParameter("secret_key", EndpointKeys.VIDEO_SPIDER_SECRET_KEY)
                                .addQueryParameter("video_id", tvShowId)
                                .addQueryParameter("s", seasonNumber)
                                .addQueryParameter("ip", ipAddress)
                                .build()
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        sweetAlertDialog.dismiss();
                                        String url = "https://videospider.stream/getvideo?key=" + EndpointKeys.VIDEO_SPIDER_KEY + "&video_id=" + tvShowId + "&tv=1&s=" + seasonNumber + "&e=" + episodeNumber + "&ticket=" + response;
//                                        Handler handler = new Handler();
//                                        new Thread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                try {
//                                                    URL myUrl = new URL(url);
//                                                    HttpURLConnection ucon = (HttpURLConnection) myUrl.openConnection();
//                                                    ucon.setInstanceFollowRedirects(false);
//                                                    URL secondURL = new URL(ucon.getHeaderField("Location"));
//                                                    URLConnection conn = secondURL.openConnection();
//                                                    handler.post(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            Toast.makeText(EpisodeDetailActivity.this, secondURL.toString(), Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                                } catch (MalformedURLException e) {
//
//                                                } catch (IOException e) {
//
//                                                }
//                                            }
//                                        }).start();
                                        Intent intent = new Intent(EpisodeDetailActivity.this, FullMovieActivity.class);
                                        intent.putExtra(EndpointKeys.MOVIE_URL, url);
                                        startActivity(intent);
//                                        new FinestWebView.Builder(MoviesDetailActivity.this).theme(R.style.FinestWebViewTheme)
//                                                .titleDefault(textViewTitle.getText().toString())
//                                                .showUrl(false)
//                                                .webViewBuiltInZoomControls(true)
//                                                .webViewDisplayZoomControls(true)
//                                                .showSwipeRefreshLayout(true)
//                                                .menuSelector(R.drawable.selector_light_theme)
//                                                .menuTextGravity(Gravity.CENTER)
//                                                .menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft)
//                                                .dividerHeight(0)
//                                                .gradientDivider(false)
//                                                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
//                                                .addWebViewListener(new WebViewListener() {
//                                                    @Override
//                                                    public void onReceivedTouchIconUrl(String url, boolean precomposed) {
//                                                        super.onReceivedTouchIconUrl(url, precomposed);
//                                                        Toast.makeText(MoviesDetailActivity.this, url, Toast.LENGTH_SHORT).show();
//                                                    }
//                                                })
//                                                .show(url);
//                                        AdBlocksWebViewActivity.startWebView(MoviesDetailActivity.this, url, getResources().getColor(R.color.colorWhite));
//                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                                        startActivity(Intent.createChooser(browserIntent, "Watch using"));
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        sweetAlertDialog.dismiss();
                                        Toast.makeText(EpisodeDetailActivity.this, anError.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onError(ANError anError) {
                        sweetAlertDialog.dismiss();
                    }
                });
    }

    private void startImageSliderActivity(int position) {
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < episodePhotosDataList.size(); i++) {
            images.add(EndpointUrl.SLIDER_IMAGE_BASE_URL + episodePhotosDataList.get(i).getFile_path());
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
        intent.putExtra("name", title);
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

}