package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.castandcrewadapter.CastAdapter;
import com.codebosses.flicks.adapters.castandcrewadapter.CrewAdapter;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.adapters.moviesdetail.MoviesGenreAdapter;
import com.codebosses.flicks.adapters.moviesdetail.SimilarMoviesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.castandcrew.CastAndCrewMainObject;
import com.codebosses.flicks.pojo.castandcrew.CastData;
import com.codebosses.flicks.pojo.castandcrew.CrewData;
import com.codebosses.flicks.pojo.eventbus.EventBusCastAndCrewClick;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.MovieDetailMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.codebosses.flicks.utils.FontUtils;
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

public class MoviesDetailActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.youtubePlayerViewMoviesDetail)
    YouTubePlayerView youTubePlayerView;
    @BindView(R.id.textViewTitleMoviesDetail)
    TextView textViewTitle;
    @BindView(R.id.ratingBarMovieDetail)
    MaterialRatingBar ratingBar;
    @BindView(R.id.imageViewThumbnailMoviesDetail)
    ImageView imageViewThumbnail;
    @BindView(R.id.textViewVotesMoviesDetail)
    TextView textViewVoteCount;
    @BindView(R.id.textViewReleaseDateHeaderMoviesDetail)
    TextView textViewReleaseDateHeader;
    @BindView(R.id.textViewYearMoviesDetail)
    TextView textViewReleaseDate;
    @BindView(R.id.textViewOverviewMoviesDetail)
    TextView textViewOverview;
    @BindView(R.id.cardViewThumbnailContainerMoviesDetail)
    CardView cardViewThumbnail;
    @BindView(R.id.recyclerViewGenreMoviesDetail)
    RecyclerView recyclerViewGenre;
    @BindView(R.id.recyclerViewCastMoviesDetail)
    RecyclerView recyclerViewCast;
    @BindView(R.id.textViewCrewHeader)
    TextView textViewCrewHeader;
    @BindView(R.id.recyclerViewCrewMoviesDetail)
    RecyclerView recyclerViewCrew;
    @BindView(R.id.textViewGenreHeader)
    TextView textViewGenreHeader;
    @BindView(R.id.textViewCastHeader)
    TextView textViewCastHeader;
    @BindView(R.id.textViewViewMoreSimilarMovies)
    TextView textViewViewMoreSimilarMovies;
    @BindView(R.id.textViewSimilarMoviesHeader)
    TextView textViewSimilarMoviesHeader;
    @BindView(R.id.recyclerViewSimilarMoviesMoviesDetail)
    RecyclerView recyclerViewSimilarMovies;
    @BindView(R.id.textViewSuggestionMoviesHeader)
    TextView textViewSuggestionHeader;
    @BindView(R.id.textViewViewMoreSuggestionMovies)
    TextView textViewViewMoreSuggestion;
    @BindView(R.id.recyclerViewSuggestionMoviesMoviesDetail)
    RecyclerView recyclerViewSuggestedMovies;
    private YouTubePlayer youTubePlayer;
    @BindView(R.id.nestedScrollViewMoviesDetail)
    CustomNestedScrollView nestedScrollViewMoviesDetail;
    @BindView(R.id.textViewStoryLineHeader)
    TextView textViewOverViewHeader;
    @BindView(R.id.textViewRatingMovieDetail)
    TextView textViewMovieRating;
    @BindView(R.id.textViewAudienceMovieDetail)
    TextView textViewAudienceRating;

    //    Retrofit calls....
    private Call<MoviesTrailerMainObject> moviesTrailerMainObjectCall;
    private Call<MovieDetailMainObject> movieDetailMainObjectCall;
    private Call<CastAndCrewMainObject> castAndCrewMainObjectCall;
    private Call<MoviesMainObject> similarMoviesCall;
    private Call<MoviesMainObject> suggestedMoviesCall;

    //    Instance fields....
    private List<MoviesTrailerResult> moviesTrailerResultList = new ArrayList<>();
    private List<CastData> castDataList = new ArrayList<>();
    private List<CrewData> crewDataList = new ArrayList<>();
    private List<MoviesResult> similarMoviesList = new ArrayList<>();
    private List<MoviesResult> suggestedMoviesList = new ArrayList<>();
    private String movieId;
    private double rating;
    private int scrollingCounter = 0;

    //    Adapter fields....
    private SimilarMoviesAdapter suggestedMoviesAdapter;
    private SimilarMoviesAdapter similarMoviesAdapter;
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;

    //    Font fields....
    private FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);
        ButterKnife.bind(this);

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewTitle);
        fontUtils.setTextViewLightFont(textViewVoteCount);
        fontUtils.setTextViewRegularFont(textViewReleaseDateHeader);
        fontUtils.setTextViewLightFont(textViewReleaseDate);
        fontUtils.setTextViewRegularFont(textViewGenreHeader);
        fontUtils.setTextViewRegularFont(textViewCastHeader);
        fontUtils.setTextViewRegularFont(textViewCrewHeader);
        fontUtils.setTextViewRegularFont(textViewSimilarMoviesHeader);
        fontUtils.setTextViewRegularFont(textViewViewMoreSimilarMovies);
        fontUtils.setTextViewRegularFont(textViewSuggestionHeader);
        fontUtils.setTextViewRegularFont(textViewViewMoreSuggestion);
        fontUtils.setTextViewRegularFont(textViewOverViewHeader);
        fontUtils.setTextViewLightFont(textViewOverview);
        fontUtils.setTextViewRegularFont(textViewMovieRating);
        fontUtils.setTextViewLightFont(textViewAudienceRating);


//        Setting layout managers for recycler view....
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSimilarMovies.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSuggestedMovies.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

//        Creating empty list adapter objects...
        similarMoviesAdapter = new SimilarMoviesAdapter(this, similarMoviesList, EndpointKeys.SIMILAR_MOVIES_DETAIL);
        suggestedMoviesAdapter = new SimilarMoviesAdapter(this, suggestedMoviesList, EndpointKeys.SUGGESTED_MOVIES_DETAIL);
        castAdapter = new CastAdapter(this, castDataList);
        crewAdapter = new CrewAdapter(this, crewDataList);

//        Setting item animator for recycler views....
        recyclerViewSimilarMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSuggestedMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());

//        Setting emoty liste adapter to recycler views....
        recyclerViewSimilarMovies.setAdapter(similarMoviesAdapter);
        recyclerViewSuggestedMovies.setAdapter(suggestedMoviesAdapter);
        recyclerViewCast.setAdapter(castAdapter);
        recyclerViewCrew.setAdapter(crewAdapter);

        if (getIntent() != null) {
            movieId = String.valueOf(getIntent().getIntExtra(EndpointKeys.MOVIE_ID, -1));
            rating = getIntent().getDoubleExtra(EndpointKeys.RATING, 0.0);
            ratingBar.setRating((float) rating / 2);
            getMovieTrailers("en-US", movieId);
            getMovieDetail("en-US", movieId);
            getMovieCredits(movieId);
            getSimilarMovies(movieId, "en-US", 1);
            getSuggestedMovies(movieId, "en-US", 1);
        }

        if (nestedScrollViewMoviesDetail != null) {

            nestedScrollViewMoviesDetail.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

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
    protected void onDestroy() {
        super.onDestroy();
        if (moviesTrailerMainObjectCall != null && moviesTrailerMainObjectCall.isExecuted()) {
            moviesTrailerMainObjectCall.cancel();
        }
        if (movieDetailMainObjectCall != null && movieDetailMainObjectCall.isExecuted()) {
            movieDetailMainObjectCall.cancel();
        }
        if (castAndCrewMainObjectCall != null && castAndCrewMainObjectCall.isExecuted()) {
            castAndCrewMainObjectCall.cancel();
        }
        if (similarMoviesCall != null && similarMoviesCall.isExecuted()) {
            similarMoviesCall.cancel();
        }
        if (suggestedMoviesCall != null && suggestedMoviesCall.isExecuted()) {
            suggestedMoviesCall.cancel();
        }
        youTubePlayerView.release();
    }

    private void getMovieTrailers(String language, String movieId) {
        moviesTrailerMainObjectCall = Api.WEB_SERVICE.getMovieTrailer(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        moviesTrailerMainObjectCall.enqueue(new Callback<MoviesTrailerMainObject>() {
            @Override
            public void onResponse(Call<MoviesTrailerMainObject> call, retrofit2.Response<MoviesTrailerMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesTrailerMainObject moviesTrailerMainObject = response.body();
                    if (moviesTrailerMainObject != null) {
                        moviesTrailerResultList = moviesTrailerMainObject.getResults();
                        if (moviesTrailerResultList.size() > 0) {
                            youTubePlayerView.initialize(new YouTubePlayerInitListener() {
                                @Override
                                public void onInitSuccess(@NonNull YouTubePlayer youTubePlayer) {
                                    youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                                        @Override
                                        public void onReady() {
                                            super.onReady();
                                            MoviesDetailActivity.this.youTubePlayer = youTubePlayer;
                                            youTubePlayer.loadVideo(moviesTrailerResultList.get(0).getKey(), 0);
                                        }
                                    });
                                }
                            }, true);
                        } else {
                            Toast.makeText(MoviesDetailActivity.this, "Could not found trailer of this movie.", Toast.LENGTH_SHORT).show();
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

                    } else {

                    }
                } else {

                }
            }
        });
    }

    private void getMovieDetail(String language, String movieId) {
        movieDetailMainObjectCall = Api.WEB_SERVICE.getMovieDetail(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        movieDetailMainObjectCall.enqueue(new Callback<MovieDetailMainObject>() {
            @Override
            public void onResponse(Call<MovieDetailMainObject> call, retrofit2.Response<MovieDetailMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MovieDetailMainObject movieDetailMainObject = response.body();
                    if (movieDetailMainObject != null) {

                        String originalTitle = movieDetailMainObject.getOriginal_title();
                        String title = movieDetailMainObject.getTitle();
                        String overview = movieDetailMainObject.getOverview();
                        String releaseDate = movieDetailMainObject.getRelease_date();
                        String moviePosterPath = movieDetailMainObject.getPoster_path();
//                        int voteCount = movieDetailMainObject.getVote_count();

                        cardViewThumbnail.setVisibility(View.VISIBLE);
                        textViewReleaseDateHeader.setVisibility(View.VISIBLE);
                        ratingBar.setVisibility(View.VISIBLE);
                        textViewGenreHeader.setVisibility(View.VISIBLE);
                        textViewAudienceRating.setVisibility(View.VISIBLE);
                        textViewMovieRating.setVisibility(View.VISIBLE);
                        textViewOverViewHeader.setVisibility(View.VISIBLE);
                        textViewVoteCount.setVisibility(View.VISIBLE);

                        textViewTitle.setText(originalTitle);
                        textViewReleaseDate.setText(releaseDate);
                        textViewMovieRating.setText(String.valueOf((float) rating / 2));
//                        textViewVoteCount.setText(String.valueOf(voteCount));
                        textViewOverview.setText(overview);
                        Glide.with(MoviesDetailActivity.this)
                                .load(EndpointUrl.POSTER_BASE_URL + "/" + moviePosterPath)
                                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                .apply(new RequestOptions().fitCenter())
                                .into(imageViewThumbnail);
                        recyclerViewGenre.setAdapter(new MoviesGenreAdapter(MoviesDetailActivity.this, movieDetailMainObject.getGenres()));

                    }
                }
            }

            @Override
            public void onFailure(Call<MovieDetailMainObject> call, Throwable error) {
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

    private void getMovieCredits(String movieId) {
        castAndCrewMainObjectCall = Api.WEB_SERVICE.getMovieCredits(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY);
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
                        }
                        if (castAndCrewMainObject.getCrew().size() > 0) {
                            textViewCrewHeader.setVisibility(View.VISIBLE);
                            for (int i = 0; i < castAndCrewMainObject.getCrew().size(); i++) {
                                crewDataList.add(castAndCrewMainObject.getCrew().get(i));
                                crewAdapter.notifyItemInserted(i);
                            }
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

    private void getSimilarMovies(String movieId, String language, int pageNumber) {
        similarMoviesCall = Api.WEB_SERVICE.getSimilarMovies(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        similarMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {

                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewSimilarMoviesHeader.setVisibility(View.VISIBLE);
                            textViewViewMoreSimilarMovies.setVisibility(View.VISIBLE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                similarMoviesList.add(moviesMainObject.getResults().get(i));
                                similarMoviesAdapter.notifyItemInserted(similarMoviesList.size() - 1);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesMainObject> call, Throwable error) {
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

    private void getSuggestedMovies(String movieId, String language, int pageNumber) {
        suggestedMoviesCall = Api.WEB_SERVICE.getSuggestedMovies(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        suggestedMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewSuggestionHeader.setVisibility(View.VISIBLE);
                            textViewViewMoreSuggestion.setVisibility(View.VISIBLE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                suggestedMoviesList.add(moviesMainObject.getResults().get(i));
                                suggestedMoviesAdapter.notifyItemInserted(suggestedMoviesList.size() - 1);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesMainObject> call, Throwable error) {
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

    @OnClick(R.id.textViewViewMoreSimilarMovies)
    public void onViewMoreSimilarMoviesClick(View view) {
        Intent intent = new Intent(this, SimilarMoviesActivity.class);
        intent.putExtra(EndpointKeys.MOVIE_ID, movieId);
        startActivity(intent);
    }

    @OnClick(R.id.textViewViewMoreSuggestionMovies)
    public void onViewMoreSuggestionMoviesClick(View view) {
        Intent intent = new Intent(this, SuggestedMoviesActivity.class);
        intent.putExtra(EndpointKeys.MOVIE_ID, movieId);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSimilarMovieClick(EventBusMovieClick eventBusMovieClick) {
        String movieTitle = "";
        int movieId = 0;
        double rating = 0.0;
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.SIMILAR_MOVIES_DETAIL)) {
            movieId = similarMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = similarMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = similarMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getMovieType().equals(EndpointKeys.SUGGESTED_MOVIES_DETAIL)) {
            movieId = suggestedMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = suggestedMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = suggestedMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        }
        Intent intent = new Intent(this, MoviesDetailActivity.class);
        intent.putExtra(EndpointKeys.MOVIE_ID, movieId);
        intent.putExtra(EndpointKeys.MOVIE_TITLE, movieTitle);
        intent.putExtra(EndpointKeys.RATING, rating);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusCastAndCrewClick(EventBusCastAndCrewClick eventBusCastAndCrewClick) {
        int castId = -1;
        String name = "", image = "";
        if (eventBusCastAndCrewClick.getClickType().equals(EndpointKeys.CAST)) {
            castId = castDataList.get(eventBusCastAndCrewClick.getPosition()).getCast_id();
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