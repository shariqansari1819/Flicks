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

import com.codebosses.flicks.adapters.tvshowsdetail.EpisodePhotosAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.TvEpisodesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.castandcrew.CastAndCrewMainObject;
import com.codebosses.flicks.pojo.castandcrew.CastData;
import com.codebosses.flicks.pojo.castandcrew.CrewData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;

import com.codebosses.flicks.pojo.tvseasons.Episode;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import java.util.ArrayList;
import java.util.List;

public class EpisodeDetailActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.youtubePlayerViewEpisodeDetail)
    YouTubePlayerView youTubePlayerView;
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
    private YouTubePlayer youTubePlayer;
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
    private int scrollingCounter = 0;

    //    Adapter fields....
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;
    private EpisodePhotosAdapter episodePhotosAdapter;

    //    Font fields....
    private FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);
        ButterKnife.bind(this);

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


        //        Setting layout managers for recycler view....
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(EpisodeDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(EpisodeDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(EpisodeDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

//        Creating empty list adapter objects...
        castAdapter = new CastAdapter(this, castDataList);
        crewAdapter = new CrewAdapter(this, crewDataList);
        episodePhotosAdapter = new EpisodePhotosAdapter(this, episodePhotosDataList);

//        Setting item animator for recycler views....
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPhotos.setItemAnimator(new DefaultItemAnimator());

//        Setting empty liste adapter to recycler views....
        recyclerViewCast.setAdapter(castAdapter);
        recyclerViewCrew.setAdapter(crewAdapter);
        recyclerViewPhotos.setAdapter(episodePhotosAdapter);

        if (getIntent() != null) {
            tvShowId = getIntent().getStringExtra(EndpointKeys.TV_ID);
            seasonNumber = String.valueOf(getIntent().getIntExtra(EndpointKeys.SEASON_NUMBER, -1));
            episodeNumber = String.valueOf(getIntent().getIntExtra(EndpointKeys.EPISODE_NUMBER, -1));
            episode = getIntent().getParcelableExtra(EndpointKeys.EPISODE_DETAIL);
            title = getIntent().getStringExtra(EndpointKeys.TV_NAME);
            setEpisodeData();
//            getTvSeasonDetail("en-US", tvShowId, String.valueOf(seasonNumber));
            getTvEpisodeTrailer("en-US", tvShowId, seasonNumber, episodeNumber);
            getTvEpisodePhotos("en-US", tvShowId, seasonNumber, episodeNumber);
//            getTvSeasonCredits(tvShowId, String.valueOf(seasonNumber));
        }

        if (nestedScrollViewTvEpisodeDetail != null) {

            nestedScrollViewTvEpisodeDetail.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

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
        moviesTrailerMainObjectCall = Api.WEB_SERVICE.getTvEpisodeTrailer(tvId, seasonNumber, episodeNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        moviesTrailerMainObjectCall.enqueue(new Callback<MoviesTrailerMainObject>() {
            @Override
            public void onResponse(Call<MoviesTrailerMainObject> call, retrofit2.Response<MoviesTrailerMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesTrailerMainObject moviesTrailerMainObject = response.body();
                    if (moviesTrailerMainObject != null) {
                        moviesTrailerResultList = moviesTrailerMainObject.getResults();
                        if (moviesTrailerResultList.size() > 0) {
                            toolbarTvEpisode.setVisibility(View.GONE);
                            EpisodeDetailActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            youTubePlayerView.initialize(new YouTubePlayerInitListener() {
                                @Override
                                public void onInitSuccess(@NonNull YouTubePlayer youTubePlayer) {
                                    youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                                        @Override
                                        public void onReady() {
                                            super.onReady();
                                            EpisodeDetailActivity.this.youTubePlayer = youTubePlayer;
                                            youTubePlayer.loadVideo(moviesTrailerResultList.get(0).getKey(), 0);
                                        }
                                    });
                                }
                            }, true);
                        } else {
                            youTubePlayerView.setVisibility(View.GONE);
                            toolbarTvEpisode.setVisibility(View.VISIBLE);
                            setSupportActionBar(toolbarTvEpisode);
                            if (getSupportActionBar() != null) {
                                getSupportActionBar().setTitle(title);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                ValidUtils.changeToolbarFont(toolbarTvEpisode, EpisodeDetailActivity.this);
                            }
                            Toast.makeText(EpisodeDetailActivity.this, "Could not found trailer of this episode.", Toast.LENGTH_SHORT).show();
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
        episodePhotosMainObjectCall = Api.WEB_SERVICE.getTvEpisodePhotos(tvId, seasonNumber, episodeNumber, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        episodePhotosMainObjectCall.enqueue(new Callback<EpisodePhotosMainObject>() {
            @Override
            public void onResponse(Call<EpisodePhotosMainObject> call, retrofit2.Response<EpisodePhotosMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStills().size() > 0) {
                            textViewPhotos.setVisibility(View.VISIBLE);
                            for (int i = 0; i < response.body().getStills().size(); i++) {
                                episodePhotosDataList.add(response.body().getStills().get(i));
                                episodePhotosAdapter.notifyItemInserted(i);
                            }
                        } else {
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
        textViewTvShowsRating.setText(String.valueOf((float) rating / 2));
        textViewOverview.setText(episode.getOverview());
        Glide.with(EpisodeDetailActivity.this)
                .load(EndpointUrl.POSTER_BASE_URL + "/" + episode.getStill_path())
                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                .apply(new RequestOptions().fitCenter())
                .into(imageViewThumbnail);
        if (TextUtils.isEmpty(episode.getOverview())) {
            textViewOverview.setVisibility(View.GONE);
            textViewOverViewHeader.setVisibility(View.GONE);
        }
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