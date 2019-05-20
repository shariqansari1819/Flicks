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
import com.codebosses.flicks.pojo.eventbus.EventBusPlayVideo;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;

import com.codebosses.flicks.pojo.tvseasons.Episode;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;
import com.codebosses.flicks.utils.customviews.curve_image_view.CrescentoImageView;
import com.dd.ShadowLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.shadowPlayButtonTvEpisode)
    ShadowLayout shadowLayoutPlayButton;
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

    //    Retrofit calls....
    private Call<MoviesTrailerMainObject> moviesTrailerMainObjectCall;
    private Call<EpisodePhotosMainObject> episodePhotosMainObjectCall;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);
        ButterKnife.bind(this);
        EpisodeDetailActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

//        Setting empty liste adapter to recycler views....
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

    @Override
    protected void onStart() {
        super.onStart();
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
                            for (int i = 0; i < response.body().getStills().size(); i++) {
                                episodePhotosDataList.add(response.body().getStills().get(i));
                                episodePhotosAdapter.notifyItemInserted(i);
                            }
                        } else {
                            recyclerViewPhotos.setVisibility(View.GONE);
                            textViewPhotos.setVisibility(View.GONE);
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
        shadowLayoutPlayButton.setVisibility(View.VISIBLE);
        textViewTitle.setVisibility(View.VISIBLE);
        cardViewThumbnail.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
        textViewAudienceRating.setVisibility(View.VISIBLE);
        textViewTvShowsRating.setVisibility(View.VISIBLE);
        textViewOverViewHeader.setVisibility(View.VISIBLE);
        textViewVoteCount.setVisibility(View.VISIBLE);
        textViewReleaseDateHeader.setVisibility(View.VISIBLE);

        textViewTitle.setText(episode.getName());
        textViewReleaseDate.setText(episode.getAir_date());
        double rating = episode.getVote_average();
        textViewTvShowsRating.setText(String.valueOf(rating));
        ratingBar.setRating((float) rating / 2);
        textViewOverview.setText(episode.getOverview());
        Glide.with(EpisodeDetailActivity.this)
                .load(EndpointUrl.POSTER_BASE_URL + "/" + episode.getStill_path())
                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                .apply(new RequestOptions().fitCenter())
                .into(imageViewThumbnail);
        if (moviesTrailerResultList.size() > 0) {
            Glide.with(EpisodeDetailActivity.this)
                    .load(EndpointUrl.YOUTUBE_THUMBNAIL_BASE_URL + moviesTrailerResultList.get(0).getKey() + "/mqdefault.jpg")
                    .apply(new RequestOptions().centerCrop())
                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                    .into(imageViewCover);
        } else {
            Glide.with(EpisodeDetailActivity.this)
                    .load(EndpointUrl.POSTER_BASE_URL + "/" + episode.getStill_path())
                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                    .apply(new RequestOptions().fitCenter())
                    .into(imageViewCover);
        }
        if (TextUtils.isEmpty(episode.getOverview())) {
            textViewOverview.setVisibility(View.GONE);
            textViewOverViewHeader.setVisibility(View.GONE);
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

}