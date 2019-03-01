package com.codebosses.flicks.activities;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.TvShowsDetailMainObject;
import com.codebosses.flicks.pojo.tvseasons.Episode;
import com.codebosses.flicks.pojo.tvseasons.TvSeasonsMainObject;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;

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
//    @BindView(R.id.recyclerViewCastTvSeasonDetail)
//    RecyclerView recyclerViewCast;
//    @BindView(R.id.textViewCrewHeader)
//    TextView textViewCrewHeader;
//    @BindView(R.id.recyclerViewCrewTvSeasonDetail)
//    RecyclerView recyclerViewCrew;
//    @BindView(R.id.textViewGenreHeader)
//    TextView textViewGenreHeader;
//    @BindView(R.id.textViewCastHeader)
//    TextView textViewCastHeader;
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

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private String tvShowId;
    private int seasonNumber;
    private double rating;
    private int scrollingCounter = 0;
    private List<Episode> episodesList = new ArrayList<>();

    //    Retrofit fields....
    private Call<TvSeasonsMainObject> callTvSeasons;

    //    Adapter fields....
    private TvEpisodesAdapter tvEpisodesAdapter;

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
//        fontUtils.setTextViewRegularFont(textViewGenreHeader);
//        fontUtils.setTextViewRegularFont(textViewCastHeader);
//        fontUtils.setTextViewRegularFont(textViewCrewHeader);
        fontUtils.setTextViewRegularFont(textViewEpisodesHeader);
        fontUtils.setTextViewLightFont(textViewEpisodesNumber);
        fontUtils.setTextViewRegularFont(textViewOverViewHeader);
        fontUtils.setTextViewLightFont(textViewOverview);
        fontUtils.setTextViewRegularFont(textViewTvSeasonRating);
        fontUtils.setTextViewLightFont(textViewAudienceRating);


        //        Setting layout managers for recycler view....
//        recyclerViewCast.setLayoutManager(new LinearLayoutManager(TvSeasonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(TvSeasonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(TvSeasonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewEpisodes.setLayoutManager(new GridLayoutManager(this, 3));

//        Creating empty list adapter objects...
//        similarMoviesAdapter = new SimilarMoviesAdapter(this, similarMoviesList, EndpointKeys.SIMILAR_MOVIES_DETAIL);
//        suggestedMoviesAdapter = new SimilarMoviesAdapter(this, suggestedMoviesList, EndpointKeys.SUGGESTED_MOVIES_DETAIL);
//        castAdapter = new CastAdapter(this, castDataList);
//        crewAdapter = new CrewAdapter(this, crewDataList);
        tvEpisodesAdapter = new TvEpisodesAdapter(this, episodesList, EndpointKeys.EPISODE);

//        Setting item animator for recycler views....
        recyclerViewEpisodes.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewGenre.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());

//        Setting emoty liste adapter to recycler views....
//        recyclerViewEpisodes.setAdapter(similarMoviesAdapter);
//        recyclerViewSuggestedMovies.setAdapter(suggestedMoviesAdapter);
//        recyclerViewCast.setAdapter(castAdapter);
//        recyclerViewCrew.setAdapter(crewAdapter);
        recyclerViewEpisodes.setAdapter(tvEpisodesAdapter);

        if (getIntent() != null) {
            tvShowId = getIntent().getStringExtra(EndpointKeys.TV_ID);
            seasonNumber = getIntent().getIntExtra(EndpointKeys.SEASON_NUMBER, -1);
            getTvSeasonDetail("en-US", tvShowId, String.valueOf(seasonNumber));
//            getMovieTrailers("en-US", movieId);
//            getMovieDetail("en-US", movieId);
//            getMovieCredits(movieId);
//            getSimilarMovies(movieId, "en-US", 1);
//            getSuggestedMovies(movieId, "en-US", 1);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusEpisodeClick(EventBusTvShowsClick eventBusTvShowsClick) {

    }

}
