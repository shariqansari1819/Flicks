package com.codebosses.flicks.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
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
import com.codebosses.flicks.adapters.moviesdetail.CompanyAdapter;
import com.codebosses.flicks.adapters.moviesdetail.MoviesGenreAdapter;
import com.codebosses.flicks.adapters.moviesdetail.SimilarMoviesAdapter;
import com.codebosses.flicks.adapters.moviesdetail.VideosAdapter;
import com.codebosses.flicks.adapters.reviewsadapter.ReviewsAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.EpisodePhotosAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.common.StringMethods;
import com.codebosses.flicks.database.DatabaseClient;
import com.codebosses.flicks.database.entities.MovieEntity;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.castandcrew.CastAndCrewMainObject;
import com.codebosses.flicks.pojo.castandcrew.CastData;
import com.codebosses.flicks.pojo.castandcrew.CrewData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosMainObject;
import com.codebosses.flicks.pojo.eventbus.EventBusCastAndCrewClick;
import com.codebosses.flicks.pojo.eventbus.EventBusImageClick;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieDetailGenreClick;
import com.codebosses.flicks.pojo.eventbus.EventBusPlayVideo;
import com.codebosses.flicks.pojo.moviespojo.ExternalId;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.Genre;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.MovieDetailMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.codebosses.flicks.pojo.reviews.ReviewsData;
import com.codebosses.flicks.pojo.reviews.ReviewsMainObject;
import com.codebosses.flicks.utils.DateUtils;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;
import com.codebosses.flicks.utils.customviews.curve_image_view.CrescentoImageView;
import com.devs.readmoreoption.ReadMoreOption;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

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
import dmax.dialog.SpotsDialog;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;

public class MoviesDetailActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.textViewVideosHeader)
    TextView textViewVideosHeader;
    @BindView(R.id.recyclerViewVideosMoviesDetail)
    RecyclerView recyclerViewVideos;
    @BindView(R.id.circularProgressBarMoviesDetail)
    CircularProgressBar circularProgressBarMoviesDetail;
    //    @BindView(R.id.viewBlurMoviesDetail)
//    View viewBlur;
    @BindView(R.id.imageViewCoverMovieDetail)
    CrescentoImageView imageViewCover;
    @BindView(R.id.imageButtonPlayMoviesDetail)
    AppCompatImageButton imageButtonPlay;
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
    @BindView(R.id.nestedScrollViewMoviesDetail)
    CustomNestedScrollView nestedScrollViewMoviesDetail;
    @BindView(R.id.textViewStoryLineHeader)
    TextView textViewOverViewHeader;
    @BindView(R.id.textViewRatingMovieDetail)
    TextView textViewMovieRating;
    @BindView(R.id.textViewAudienceMovieDetail)
    TextView textViewAudienceRating;
    @BindView(R.id.toolbarMoviesDetail)
    Toolbar toolbarMoviesDetail;
    @BindView(R.id.textViewViewReviewsHeaderMoviesDetail)
    TextView textViewReviewsHeader;
    @BindView(R.id.recyclerViewReviewsMoviesDetail)
    RecyclerView recyclerViewReviews;
    @BindView(R.id.textViewWatchFullMovie)
    AppCompatButton buttonWatchFullMovie;
    @BindView(R.id.textViewVideosCountMoviesDetail)
    TextView textViewVideosCount;
    @BindView(R.id.textViewImagesHeader)
    TextView textViewImageHeader;
    @BindView(R.id.recyclerViewImagesMoviesDetail)
    RecyclerView recyclerViewImages;
    @BindView(R.id.textViewImagesCountMoviesDetail)
    TextView textViewImagesCounter;
    @BindView(R.id.textViewTagLineHeader)
    TextView textViewTagLineHeader;
    @BindView(R.id.textViewTagLineMoviesDetail)
    TextView textViewTagLine;
    @BindView(R.id.imageViewRuntimeMoviesDetail)
    AppCompatImageView imageViewRuntime;
    @BindView(R.id.textViewRuntimeMoviesDetail)
    TextView textViewRuntime;
    @BindView(R.id.textViewCompaniesHeader)
    TextView textViewCompaniesHeader;
    @BindView(R.id.recyclerViewCompaniesMoviesDetail)
    RecyclerView recyclerViewCompanies;
    @BindView(R.id.imageViewFavoriteMoviesDetail)
    AppCompatImageView imageViewFavorite;
    @BindView(R.id.imageViewUnFavoriteMoviesDetail)
    AppCompatImageView imageViewUnFavorite;
    private AlertDialog alertDialog;

    //    Retrofit calls....
    private Call<MoviesTrailerMainObject> moviesTrailerMainObjectCall;
    private Call<MovieDetailMainObject> movieDetailMainObjectCall;
    private Call<CastAndCrewMainObject> castAndCrewMainObjectCall;
    private Call<MoviesMainObject> similarMoviesCall;
    private Call<MoviesMainObject> suggestedMoviesCall;
    private Call<ReviewsMainObject> reviewsMainObjectCall;
    private Call<EpisodePhotosMainObject> moviesImagesCall;
    private Call<ExternalId> externalIdCall;

    //    Instance fields....
    private MovieDetailMainObject movieDetailMainObject;
    private List<MoviesTrailerResult> moviesTrailerResultList = new ArrayList<>();
    private List<CastData> castDataList = new ArrayList<>();
    private List<CrewData> crewDataList = new ArrayList<>();
    private List<MoviesResult> similarMoviesList = new ArrayList<>();
    private List<MoviesResult> suggestedMoviesList = new ArrayList<>();
    private List<ReviewsData> reviewsDataList = new ArrayList<>();
    private List<EpisodePhotosData> imagesPhotoList = new ArrayList<>();
    private List<Genre> genreList = new ArrayList<>();
    private String movieId;
    private double rating;
    private int scrollingCounter = 0;

    //    Adapter fields....
    private SimilarMoviesAdapter suggestedMoviesAdapter;
    private SimilarMoviesAdapter similarMoviesAdapter;
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;
    private ReviewsAdapter reviewsAdapter;
    private VideosAdapter videosAdapter;
    private EpisodePhotosAdapter imagesAdapter;

    //    Font fields....
    private FontUtils fontUtils;

    //    Ad mob fields....
    private InterstitialAd mInterstitialAd;

    //    Room database fields....
    private DatabaseClient databaseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);
        ValidUtils.transparentStatusAndNavigation(this);
//        MoviesDetailActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

//        Initialization of room database field....
        databaseClient = DatabaseClient.getDatabaseClient(this);

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

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewTitle);
        fontUtils.setTextViewLightFont(textViewVoteCount);
        fontUtils.setTextViewRegularFont(textViewReleaseDateHeader);
        fontUtils.setTextViewRegularFont(textViewTagLineHeader);
        fontUtils.setTextViewLightFont(textViewTagLine);
        fontUtils.setTextViewLightFont(textViewReleaseDate);
        fontUtils.setTextViewRegularFont(textViewGenreHeader);
        fontUtils.setTextViewRegularFont(textViewCastHeader);
        fontUtils.setTextViewRegularFont(textViewCrewHeader);
        fontUtils.setTextViewRegularFont(textViewVideosHeader);
        fontUtils.setTextViewRegularFont(textViewSimilarMoviesHeader);
        fontUtils.setTextViewRegularFont(textViewViewMoreSimilarMovies);
        fontUtils.setTextViewRegularFont(textViewSuggestionHeader);
        fontUtils.setTextViewRegularFont(textViewViewMoreSuggestion);
        fontUtils.setTextViewRegularFont(textViewOverViewHeader);
        fontUtils.setTextViewLightFont(textViewOverview);
        fontUtils.setTextViewRegularFont(textViewMovieRating);
        fontUtils.setTextViewLightFont(textViewAudienceRating);
        fontUtils.setTextViewRegularFont(textViewReviewsHeader);
        fontUtils.setTextViewLightFont(textViewVideosCount);
        fontUtils.setTextViewRegularFont(textViewVideosHeader);
        fontUtils.setTextViewRegularFont(textViewImageHeader);
        fontUtils.setTextViewLightFont(textViewImagesCounter);
        fontUtils.setButtonRegularFont(buttonWatchFullMovie);
        fontUtils.setTextViewRegularFont(textViewRuntime);
        fontUtils.setTextViewRegularFont(textViewCompaniesHeader);

//        Setting layout managers for recycler view....
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSimilarMovies.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSuggestedMovies.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCompanies.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(MoviesDetailActivity.this));

//        Creating empty list adapter objects...
        similarMoviesAdapter = new SimilarMoviesAdapter(this, similarMoviesList, EndpointKeys.SIMILAR_MOVIES_DETAIL);
        suggestedMoviesAdapter = new SimilarMoviesAdapter(this, suggestedMoviesList, EndpointKeys.SUGGESTED_MOVIES_DETAIL);
        castAdapter = new CastAdapter(this, castDataList);
        crewAdapter = new CrewAdapter(this, crewDataList);
        reviewsAdapter = new ReviewsAdapter(this, reviewsDataList);
        videosAdapter = new VideosAdapter(this, moviesTrailerResultList);
        imagesAdapter = new EpisodePhotosAdapter(this, imagesPhotoList, EndpointKeys.MOVIES_IMAGES);

//        Setting item animator for recycler views....
        recyclerViewSimilarMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSuggestedMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewImages.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCompanies.setItemAnimator(new DefaultItemAnimator());

//        Setting empty list adapter to recycler views....
        recyclerViewSimilarMovies.setAdapter(similarMoviesAdapter);
        recyclerViewSuggestedMovies.setAdapter(suggestedMoviesAdapter);
        recyclerViewCast.setAdapter(castAdapter);
        recyclerViewCrew.setAdapter(crewAdapter);
        recyclerViewReviews.setAdapter(reviewsAdapter);
        recyclerViewVideos.setAdapter(videosAdapter);
        recyclerViewImages.setAdapter(imagesAdapter);

        setSupportActionBar(toolbarMoviesDetail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null) {
            if (ValidUtils.isNetworkAvailable(this)) {
                movieId = String.valueOf(getIntent().getIntExtra(EndpointKeys.MOVIE_ID, -1));
                rating = getIntent().getDoubleExtra(EndpointKeys.RATING, 0.0);
//            movieTitle = getIntent().getStringExtra(EndpointKeys.MOVIE_TITLE);
                ratingBar.setRating((float) rating / 2);
                getMovieTrailers("en-US", movieId);
                getMovieDetail("en-US", movieId);
                getMovieCredits(movieId);
                getSimilarMovies(movieId, "en-US", 1);
                getSuggestedMovies(movieId, "en-US", 1);
                getMovieReviews(movieId, "en-US", 1);
                getMovieImages(movieId, "");
            } else {

            }
        }

//        if (nestedScrollViewMoviesDetail != null) {
//
//            nestedScrollViewMoviesDetail.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//
//                if (scrollY > oldScrollY) {
//                    if (scrollingCounter == 0) {
//                        if (youTubePlayer != null)
//                            youTubePlayer.pause();
//                    }
//                    scrollingCounter++;
//                }
//                if (scrollY < oldScrollY) {
//                }
//
//                if (scrollY == 0) {
//                    if (youTubePlayer != null) {
//                        youTubePlayer.play();
//                    }
//                    scrollingCounter = 0;
//                }
//
//                if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight())) {
//                }
//            });
//        }

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
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
    protected void onStart() {
        super.onStart();
        showInterstitial();
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
        if (reviewsMainObjectCall != null && reviewsMainObjectCall.isExecuted()) {
            reviewsMainObjectCall.cancel();
        }
        if (moviesImagesCall != null && moviesImagesCall.isExecuted()) {
            moviesImagesCall.cancel();
        }
    }

    private void getMovieTrailers(String language, String movieId) {
        moviesTrailerMainObjectCall = ApiClient.getClient().create(Api.class).getMovieTrailer(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        moviesTrailerMainObjectCall.enqueue(new Callback<MoviesTrailerMainObject>() {
            @Override
            public void onResponse(Call<MoviesTrailerMainObject> call, retrofit2.Response<MoviesTrailerMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesTrailerMainObject moviesTrailerMainObject = response.body();
                    if (moviesTrailerMainObject != null) {
                        if (moviesTrailerMainObject.getResults().size() > 0) {
                            textViewVideosHeader.setVisibility(View.VISIBLE);
                            textViewVideosCount.setVisibility(View.VISIBLE);
                            recyclerViewVideos.setVisibility(View.VISIBLE);
                            Glide.with(MoviesDetailActivity.this)
                                    .load(EndpointUrl.YOUTUBE_THUMBNAIL_BASE_URL + response.body().getResults().get(0).getKey() + "/hqdefault.jpg")
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
                            recyclerViewVideos.setVisibility(View.GONE);
                            imageButtonPlay.setVisibility(View.GONE);
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

    private void getMovieImages(String movieId, String language) {
        moviesImagesCall = ApiClient.getClient().create(Api.class).getMoviesImages(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, "");
        moviesImagesCall.enqueue(new Callback<EpisodePhotosMainObject>() {
            @Override
            public void onResponse(Call<EpisodePhotosMainObject> call, retrofit2.Response<EpisodePhotosMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    EpisodePhotosMainObject imagesMainObject = response.body();
                    if (imagesMainObject != null) {
                        if (imagesMainObject.getBackdrops() != null && imagesMainObject.getBackdrops().size() > 0) {
                            textViewImageHeader.setVisibility(View.VISIBLE);
                            textViewImagesCounter.setVisibility(View.VISIBLE);
                            recyclerViewImages.setVisibility(View.VISIBLE);
                            for (int i = 0; i < imagesMainObject.getBackdrops().size(); i++) {
                                imagesPhotoList.add(imagesMainObject.getBackdrops().get(i));
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

    private void getMovieDetail(String language, String movieId) {
        circularProgressBarMoviesDetail.setVisibility(View.VISIBLE);
        movieDetailMainObjectCall = ApiClient.getClient().create(Api.class).getMovieDetail(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        movieDetailMainObjectCall.enqueue(new Callback<MovieDetailMainObject>() {
            @Override
            public void onResponse(Call<MovieDetailMainObject> call, retrofit2.Response<MovieDetailMainObject> response) {
                circularProgressBarMoviesDetail.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    movieDetailMainObject = response.body();
                    if (movieDetailMainObject != null) {

                        String originalTitle = movieDetailMainObject.getOriginal_title();
                        String overview = movieDetailMainObject.getOverview();
                        String releaseDate = movieDetailMainObject.getRelease_date();
                        String moviePosterPath = movieDetailMainObject.getPoster_path();
                        String tagLine = movieDetailMainObject.getTagline();
                        String backdropPath = movieDetailMainObject.getBackdrop_path();
                        Integer runtime = movieDetailMainObject.getRuntime();
                        genreList = movieDetailMainObject.getGenres();

                        isMovieFavorite();

                        if (originalTitle != null && !originalTitle.isEmpty()) {
                            textViewTitle.setText(originalTitle);
                        }

                        if (overview != null && !overview.isEmpty()) {
                            textViewOverViewHeader.setVisibility(View.VISIBLE);
                            textViewOverview.setVisibility(View.VISIBLE);
//                            textViewOverview.setText(overview);

                            ReadMoreOption readMoreOption = new ReadMoreOption.Builder(MoviesDetailActivity.this)
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

                        if (releaseDate != null && !releaseDate.isEmpty()) {
                            textViewReleaseDateHeader.setVisibility(View.VISIBLE);
                            textViewReleaseDate.setVisibility(View.VISIBLE);
                            textViewReleaseDate.setText(releaseDate);
                        }

                        if (tagLine != null && !tagLine.isEmpty()) {
                            textViewTagLineHeader.setVisibility(View.VISIBLE);
                            textViewTagLine.setVisibility(View.VISIBLE);
                            textViewTagLine.setText(tagLine);
                        }

                        if (runtime != null && runtime != 0) {
                            imageViewRuntime.setVisibility(View.VISIBLE);
                            textViewRuntime.setVisibility(View.VISIBLE);
                            textViewRuntime.setText(DateUtils.getMovieTime(runtime));
                        }

//                        viewBlur.setVisibility(View.VISIBLE);
                        imageButtonPlay.setVisibility(View.VISIBLE);
                        cardViewThumbnail.setVisibility(View.VISIBLE);
                        ratingBar.setVisibility(View.VISIBLE);
                        textViewAudienceRating.setVisibility(View.VISIBLE);
                        textViewMovieRating.setVisibility(View.VISIBLE);
                        textViewVoteCount.setVisibility(View.VISIBLE);

                        textViewMovieRating.setText(String.valueOf(rating));

                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDate);
                            if (!DateUtils.isAfterToday(date.getTime())) {
                                buttonWatchFullMovie.setVisibility(View.VISIBLE);
                            } else {
                                buttonWatchFullMovie.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {

                        }

                        Glide.with(MoviesDetailActivity.this)
                                .load(EndpointUrl.POSTER_BASE_URL + "/" + moviePosterPath)
                                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                .thumbnail(0.1f)
                                .into(imageViewThumbnail);
                        if (moviesTrailerResultList.size() > 0) {
                            Glide.with(MoviesDetailActivity.this)
                                    .load(EndpointUrl.YOUTUBE_THUMBNAIL_BASE_URL + moviesTrailerResultList.get(0).getKey() + "/mqdefault.jpg")
                                    .apply(new RequestOptions().centerCrop())
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .thumbnail(0.1f)
                                    .into(imageViewCover);
                        } else {
                            if (backdropPath == null || backdropPath.equals(""))
                                backdropPath = moviePosterPath;
                            Glide.with(MoviesDetailActivity.this)
                                    .load(EndpointUrl.POSTER_BASE_URL + "/" + backdropPath)
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .apply(new RequestOptions().fitCenter())
                                    .thumbnail(0.1f)
                                    .into(imageViewCover);
                        }

                        if (genreList.size() > 0) {
                            textViewGenreHeader.setVisibility(View.VISIBLE);
                            recyclerViewGenre.setVisibility(View.VISIBLE);
                            recyclerViewGenre.setAdapter(new MoviesGenreAdapter(MoviesDetailActivity.this, genreList));
                        } else {
                            textViewGenreHeader.setVisibility(View.GONE);
                            recyclerViewGenre.setVisibility(View.GONE);
                        }

                        if (movieDetailMainObject.getProduction_companies() != null && movieDetailMainObject.getProduction_companies().size() > 0) {
                            textViewCompaniesHeader.setVisibility(View.VISIBLE);
                            recyclerViewCompanies.setVisibility(View.VISIBLE);
                            recyclerViewCompanies.setAdapter(new CompanyAdapter(MoviesDetailActivity.this, movieDetailMainObject.getProduction_companies()));
                        } else {
                            textViewCompaniesHeader.setVisibility(View.GONE);
                            recyclerViewCompanies.setVisibility(View.GONE);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<MovieDetailMainObject> call, Throwable error) {
                circularProgressBarMoviesDetail.setVisibility(View.GONE);
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
        castAndCrewMainObjectCall = ApiClient.getClient().create(Api.class).getMovieCredits(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY);
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

    private void getSimilarMovies(String movieId, String language, int pageNumber) {
        similarMoviesCall = ApiClient.getClient().create(Api.class).getSimilarMovies(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        similarMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {

                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewSimilarMoviesHeader.setVisibility(View.VISIBLE);
                            textViewViewMoreSimilarMovies.setVisibility(View.VISIBLE);
                            recyclerViewSimilarMovies.setVisibility(View.VISIBLE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                similarMoviesList.add(moviesMainObject.getResults().get(i));
                                similarMoviesAdapter.notifyItemInserted(similarMoviesList.size() - 1);
                            }
                        } else {
                            textViewSimilarMoviesHeader.setVisibility(View.GONE);
                            textViewViewMoreSimilarMovies.setVisibility(View.GONE);
                            recyclerViewSimilarMovies.setVisibility(View.GONE);
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
        suggestedMoviesCall = ApiClient.getClient().create(Api.class).getSuggestedMovies(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        suggestedMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewSuggestionHeader.setVisibility(View.VISIBLE);
                            textViewViewMoreSuggestion.setVisibility(View.VISIBLE);
                            recyclerViewSuggestedMovies.setVisibility(View.VISIBLE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                suggestedMoviesList.add(moviesMainObject.getResults().get(i));
                                suggestedMoviesAdapter.notifyItemInserted(suggestedMoviesList.size() - 1);
                            }
                        } else {
                            textViewSuggestionHeader.setVisibility(View.GONE);
                            textViewViewMoreSuggestion.setVisibility(View.GONE);
                            recyclerViewSuggestedMovies.setVisibility(View.GONE);
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

    private void getMovieReviews(String movieId, String language, int pageNumber) {
        reviewsMainObjectCall = ApiClient.getClient().create(Api.class).getMovieReviews(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        reviewsMainObjectCall.enqueue(new Callback<ReviewsMainObject>() {
            @Override
            public void onResponse(Call<ReviewsMainObject> call, retrofit2.Response<ReviewsMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    ReviewsMainObject reviewsMainObject = response.body();
                    if (reviewsMainObject != null) {
                        if (reviewsMainObject.getTotal_results() > 0) {
                            textViewReviewsHeader.setVisibility(View.VISIBLE);
                            recyclerViewReviews.setVisibility(View.VISIBLE);
                            for (int i = 0; i < reviewsMainObject.getResults().size(); i++) {
                                reviewsDataList.add(reviewsMainObject.getResults().get(i));
                                reviewsAdapter.notifyItemInserted(reviewsDataList.size() - 1);
                            }
                        } else {
                            textViewReviewsHeader.setVisibility(View.GONE);
                            recyclerViewReviews.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewsMainObject> call, Throwable error) {
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

    @OnClick({R.id.imageButtonPlayMoviesDetail, R.id.imageViewCoverMovieDetail})
    public void onPlayButtonClick(View view) {
        if (moviesTrailerResultList.size() > 0) {
            startTrailerActivity(moviesTrailerResultList.get(0).getKey(), moviesTrailerResultList.get(0).getName());
        }
    }

    @OnClick(R.id.textViewWatchFullMovie)
    public void onWatchFullMovieClick(View view) {
        getMovieExternalId(movieId);
    }

    @OnClick(R.id.imageViewFavoriteMoviesDetail)
    public void onFavoriteClick(View view) {
        new DeleteFromFavoriteTask().execute(Integer.parseInt(movieId));
    }

    @OnClick(R.id.imageViewUnFavoriteMoviesDetail)
    public void onUnFavoriteClick(View view) {
        MovieEntity movieEntity = new MovieEntity(Integer.parseInt(movieId), movieDetailMainObject.getPoster_path(), movieDetailMainObject.getTitle(), movieDetailMainObject.getOverview(), movieDetailMainObject.getRelease_date(), movieDetailMainObject.getPopularity(), movieDetailMainObject.getVote_average());
        new AddToFavoriteListTask().execute(movieEntity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusPlayVideo(EventBusPlayVideo eventBusPlayVideo) {
        if (moviesTrailerResultList.size() > 0)
            startTrailerActivity(moviesTrailerResultList.get(eventBusPlayVideo.getPosition()).getKey(), moviesTrailerResultList.get(eventBusPlayVideo.getPosition()).getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusImageClick(EventBusImageClick eventBusImageClick) {
        if (eventBusImageClick.getClickType().equals(EndpointKeys.MOVIES_IMAGES)) {
            startImageSliderActivity(eventBusImageClick.getPosition());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusGenreClick(EventBusMovieDetailGenreClick eventBusMovieDetailGenreClick) {
        Intent intent = new Intent(this, GenreMoviesActivity.class);
        intent.putExtra(EndpointKeys.GENRE_TYPE, StringMethods.getMovieGenreTypeById(genreList.get(eventBusMovieDetailGenreClick.getPosition()).getId()));
        intent.putExtra(EndpointKeys.GENRE_ID, genreList.get(eventBusMovieDetailGenreClick.getPosition()).getId());
        intent.putExtra(EndpointKeys.SORT_TYPE, Constants.POPULARITY_DESC);
        intent.putExtra(EndpointKeys.TYPE, EndpointKeys.MOVIES);
        startActivity(intent);
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

    private void startTrailerActivity(String key, String name) {
        Intent intent = new Intent(this, TrailerActivity.class);
        intent.putExtra(EndpointKeys.YOUTUBE_KEY, key);
        intent.putExtra("name", name);
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

    private void getMovieExternalId(String movieId) {
        alertDialog = new SpotsDialog.Builder().setContext(this).build();
        alertDialog.setMessage("Extracting video source...");
        alertDialog.show();
        externalIdCall = ApiClient.getClient().create(Api.class).getMovieExternalId(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY);
        externalIdCall.enqueue(new Callback<ExternalId>() {
            @Override
            public void onResponse(Call<ExternalId> call, retrofit2.Response<ExternalId> response) {
                if (response != null && response.isSuccessful()) {
                    ExternalId externalId = response.body();
                    if (externalId != null) {
                        generateTicket(externalId.getImdbId());
                    }
                }
            }

            @Override
            public void onFailure(Call<ExternalId> call, Throwable error) {
                alertDialog.dismiss();
                Toast.makeText(MoviesDetailActivity.this, "Could not get movie.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateTicket(String videoId) {
        alertDialog.setMessage("Please wait...");
        AndroidNetworking.get("https://api6.ipify.org/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String ipAddress) {
                        AndroidNetworking.get(EndpointUrl.VIDEO_SPIDER_BASE_URL)
                                .addQueryParameter("key", EndpointKeys.VIDEO_SPIDER_KEY)
                                .addQueryParameter("secret_key", EndpointKeys.VIDEO_SPIDER_SECRET_KEY)
                                .addQueryParameter("video_id", videoId)
                                .addQueryParameter("s", "0")
                                .addQueryParameter("ip", ipAddress)
                                .build()
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        alertDialog.dismiss();
                                        String url = "https://videospider.stream/getvideo?key=" + EndpointKeys.VIDEO_SPIDER_KEY + "&video_id=" + videoId + "&ticket=" + response + "";
//                                        Intent intent = new Intent(MoviesDetailActivity.this, FullMovieActivity.class);
//                                        intent.putExtra(EndpointKeys.MOVIE_URL, url);
//                                        startActivity(intent);
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
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        startActivity(Intent.createChooser(browserIntent, "Watch movie using"));
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        alertDialog.dismiss();
                                        Toast.makeText(MoviesDetailActivity.this, anError.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onError(ANError anError) {
                        alertDialog.dismiss();
                    }
                });
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
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.SIMILAR_MOVIES_DETAIL) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.SUGGESTED_MOVIES_DETAIL)) {
            Intent intent = new Intent(this, MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, movieId);
            intent.putExtra(EndpointKeys.MOVIE_TITLE, movieTitle);
            intent.putExtra(EndpointKeys.RATING, rating);
            startActivity(intent);
        }
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

    private void isMovieFavorite() {
        new GetMovieByIdTask().execute(Integer.parseInt(movieId));
    }

    class GetMovieByIdTask extends AsyncTask<Integer, Void, MovieEntity> {

        @Override
        protected MovieEntity doInBackground(Integer... integers) {
            return databaseClient.getFlicksDatabase().getFlicksDao().getFavoriteMovieById(integers[0]);
        }

        @Override
        protected void onPostExecute(MovieEntity aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid != null) {
                imageViewFavorite.setVisibility(View.VISIBLE);
                imageViewUnFavorite.setVisibility(View.GONE);
            } else {
                imageViewFavorite.setVisibility(View.GONE);
                imageViewUnFavorite.setVisibility(View.VISIBLE);
            }
        }
    }

    class AddToFavoriteListTask extends AsyncTask<MovieEntity, Void, Void> {

        @Override
        protected Void doInBackground(MovieEntity... movieEntities) {
            databaseClient.getFlicksDatabase().getFlicksDao().insertMovie(movieEntities[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isMovieFavorite();
        }
    }

    class DeleteFromFavoriteTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            databaseClient.getFlicksDatabase().getFlicksDao().deleteMovieById(integers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isMovieFavorite();
        }
    }

}