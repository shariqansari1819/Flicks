package com.codebosses.flicks.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.celebrity_detail.CelebMoviesDetailAdapter;
import com.codebosses.flicks.adapters.celebrity_detail.CelebTvShowsDetailAdapter;
import com.codebosses.flicks.adapters.celebrity_detail.CelebrityImagesPagerAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.EpisodePhotosAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.celebritiespojo.celebmovies.CelebMoviesData;
import com.codebosses.flicks.pojo.celebritiespojo.celebmovies.CelebMoviesMainObject;
import com.codebosses.flicks.pojo.celebritiespojo.celebtvshows.CelebTvShowsData;
import com.codebosses.flicks.pojo.celebritiespojo.celebtvshows.CelebTvShowsMainObject;
import com.codebosses.flicks.pojo.celebrity_detail.CelebrityDetailMainObject;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosMainObject;
import com.codebosses.flicks.pojo.eventbus.EventBusImageClick;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusPagerImageClick;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.devs.readmoreoption.ReadMoreOption;
import com.huanhailiuxin.coolviewpager.CoolViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

import static com.codebosses.flicks.utils.ValidUtils.transparentStatusAndNavigation;

public class CelebrityDetailActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.coolViewPagerCelebrityDetail)
    CoolViewPager coolViewPagerCelebrityDetail;
    @BindView(R.id.toolbarCelebrityDetail)
    Toolbar toolbarCelebrityDetail;
    @BindView(R.id.imageVieCelebImageCelebrityDetail)
    AppCompatImageView imageViewCelebrity;
    @BindView(R.id.viewBlurCelebrityDetail)
    View viewBlur;
    @BindView(R.id.cardViewThumbnailContainerCelebrityDetail)
    CardView cardViewThumbnail;
    @BindView(R.id.imageViewThumbnailCelebrityDetail)
    ImageView imageViewThumbnail;
    @BindView(R.id.textViewTitleCelebNameCelebrityDetail)
    TextView textViewName;
    @BindView(R.id.textViewKnownForCelebrityDetail)
    TextView textViewKnownFor;
    @BindView(R.id.textViewBornCelebrityDetail)
    TextView textViewBorn;
    @BindView(R.id.textViewBornPlaceCelebrityDetail)
    TextView textViewBornPlace;
    @BindView(R.id.textViewStoryLineHeader)
    TextView textViewStoryLineHeader;
    @BindView(R.id.textViewOverviewCelebrityDetail)
    TextView textViewOverview;
    @BindView(R.id.textViewImagesHeader)
    TextView textViewImagesHeader;
    @BindView(R.id.textViewImagesCountCelebrityDetail)
    TextView textViewImagesCount;
    @BindView(R.id.recyclerViewImagesCelebrityDetail)
    RecyclerView recyclerViewImages;
    @BindView(R.id.textViewMoviesHeader)
    TextView textViewMoviesHeader;
    @BindView(R.id.textViewViewMoreMovies)
    TextView textViewViewMoreMovies;
    @BindView(R.id.recyclerViewMoviesMoviesDetail)
    RecyclerView recyclerViewMovies;
    @BindView(R.id.circularProgressBarCelebrityDetail)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.textViewTvShowsHeader)
    TextView textViewTvShowsHeader;
    @BindView(R.id.textViewViewMoreTvShows)
    TextView textViewViewMoreTvShows;
    @BindView(R.id.recyclerViewTvShowsMoviesDetail)
    RecyclerView recyclerViewTvShows;
    @BindView(R.id.textViewMoviesCountCelebrityDetail)
    TextView textViewMoviesCount;
    @BindView(R.id.textViewTvShowsCountCelebrityDetail)
    TextView textViewTvShowsCount;

    //    Retrofit fields....
    private Call<EpisodePhotosMainObject> celebImagesCall;
    private Call<CelebrityDetailMainObject> celebrityDetailMainObjectCall;
    private Call<CelebMoviesMainObject> celebMoviesMainObjectCall;
    private Call<CelebTvShowsMainObject> celebTvShowsMainObjectCall;

    //    Instance fields....
    private List<EpisodePhotosData> celebImagesList = new ArrayList<>();
    private List<CelebMoviesData> celebMoviesDataArrayList = new ArrayList<>();
    private List<CelebTvShowsData> celebTvShowsDataList = new ArrayList<>();
    private String celebId, celebName, celebImage;

    //    Adapter fields....
    private CelebrityImagesPagerAdapter celebrityImagesPagerAdapter;
    private CelebMoviesDetailAdapter celebMoviesDetailAdapter;
    private CelebTvShowsDetailAdapter celebTvShowsDetailAdapter;
    private EpisodePhotosAdapter imagesAdapter;

    //    Font fields....
    private FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrity_detail);
        transparentStatusAndNavigation(this);
//        CelebrityDetailActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            celebId = String.valueOf(getIntent().getIntExtra(EndpointKeys.CELEBRITY_ID, -1));
            celebName = getIntent().getStringExtra(EndpointKeys.CELEB_NAME);
            celebImage = getIntent().getStringExtra(EndpointKeys.CELEB_IMAGE);
            if (celebImage == null) {
                celebImage = "";
            }
            if (celebName == null) {
                celebName = "";
            }
        }

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewName);
        fontUtils.setTextViewRegularFont(textViewBorn);
        fontUtils.setTextViewRegularFont(textViewKnownFor);
        fontUtils.setTextViewRegularFont(textViewStoryLineHeader);
        fontUtils.setTextViewLightFont(textViewOverview);
        fontUtils.setTextViewRegularFont(textViewImagesHeader);
        fontUtils.setTextViewLightFont(textViewImagesCount);
        fontUtils.setTextViewRegularFont(textViewMoviesHeader);
        fontUtils.setTextViewRegularFont(textViewViewMoreMovies);
        fontUtils.setTextViewRegularFont(textViewTvShowsHeader);
        fontUtils.setTextViewRegularFont(textViewViewMoreTvShows);
        fontUtils.setTextViewLightFont(textViewMoviesCount);
        fontUtils.setTextViewLightFont(textViewTvShowsCount);

//        Setting layout managers....
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewTvShows.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

//        Initialization of empty adapter fields....
        imagesAdapter = new EpisodePhotosAdapter(this, celebImagesList, EndpointKeys.CELEBRITY_IMAGES);
        celebMoviesDetailAdapter = new CelebMoviesDetailAdapter(this, celebMoviesDataArrayList, EndpointKeys.CELEB_MOVIES);
        celebTvShowsDetailAdapter = new CelebTvShowsDetailAdapter(this, celebTvShowsDataList, EndpointKeys.CELEB_TV_SHOWS);

//        Setting empty Adapter....
        recyclerViewImages.setAdapter(imagesAdapter);
        recyclerViewMovies.setAdapter(celebMoviesDetailAdapter);
        recyclerViewTvShows.setAdapter(celebTvShowsDetailAdapter);

        recyclerViewImages.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTvShows.setItemAnimator(new DefaultItemAnimator());

//        Setting custom action bar....
        setSupportActionBar(toolbarCelebrityDetail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (ValidUtils.isNetworkAvailable(this)) {
            getCelebImages(celebId);
            getCelebDetail(celebId);
            getCelebrityMovies(celebId);
            getCelebrityTvShows(celebId);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (celebImagesCall != null && celebImagesCall.isExecuted()) {
            celebImagesCall.cancel();
        }
        if (celebrityDetailMainObjectCall != null && celebrityDetailMainObjectCall.isExecuted()) {
            celebrityDetailMainObjectCall.cancel();
        }
        if (celebMoviesMainObjectCall != null && celebMoviesMainObjectCall.isExecuted()) {
            celebMoviesMainObjectCall.cancel();
        }
        if (celebTvShowsMainObjectCall != null && celebTvShowsMainObjectCall.isExecuted()) {
            celebTvShowsMainObjectCall.cancel();
        }
    }

    @OnClick(R.id.textViewViewMoreMovies)
    public void onViewMoreMoviesClick(View view) {
        Intent intent = new Intent(this, CelebrityMoviesActivity.class);
        intent.putExtra(EndpointKeys.CELEBRITY_ID, Integer.parseInt(celebId));
        intent.putExtra(EndpointKeys.CELEB_NAME, celebName);
        intent.putExtra(EndpointKeys.CELEB_IMAGE, celebImage);
        startActivity(intent);
    }

    @OnClick(R.id.textViewViewMoreTvShows)
    public void onViewMoreTvShowsClick(View view) {
        Intent intent = new Intent(this, CelebrityTvShowsActivity.class);
        intent.putExtra(EndpointKeys.CELEBRITY_ID, Integer.parseInt(celebId));
        intent.putExtra(EndpointKeys.CELEB_NAME, celebName);
        intent.putExtra(EndpointKeys.CELEB_IMAGE, celebImage);
        startActivity(intent);
    }

    private void getCelebImages(String personId) {
        celebImagesCall = ApiClient.getClient().create(Api.class).getCelebImages(personId, EndpointKeys.THE_MOVIE_DB_API_KEY);
        celebImagesCall.enqueue(new Callback<EpisodePhotosMainObject>() {
            @Override
            public void onResponse(Call<EpisodePhotosMainObject> call, retrofit2.Response<EpisodePhotosMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    EpisodePhotosMainObject imagesMainObject = response.body();
                    viewBlur.setVisibility(View.VISIBLE);
                    if (imagesMainObject != null) {
                        if (imagesMainObject.getProfiles() != null && imagesMainObject.getProfiles().size() > 0) {
                            celebImagesList.addAll(imagesMainObject.getProfiles());
                            imageViewCelebrity.setVisibility(View.INVISIBLE);
                            coolViewPagerCelebrityDetail.setVisibility(View.VISIBLE);
                            celebrityImagesPagerAdapter = new CelebrityImagesPagerAdapter(CelebrityDetailActivity.this, celebImagesList);
                            coolViewPagerCelebrityDetail.setAdapter(celebrityImagesPagerAdapter);
                            textViewImagesHeader.setVisibility(View.VISIBLE);
                            textViewImagesCount.setVisibility(View.VISIBLE);
                            recyclerViewImages.setVisibility(View.VISIBLE);
                            textViewImagesCount.setText("(" + celebImagesList.size() + ")");
                            imagesAdapter.notifyItemRangeInserted(0, celebImagesList.size());
                        } else {
                            imageViewCelebrity.setVisibility(View.VISIBLE);
                            Glide.with(CelebrityDetailActivity.this)
                                    .load(EndpointUrl.PROFILE_BASE_URL + celebImage)
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .thumbnail(0.1f)
                                    .into(imageViewCelebrity);
                            coolViewPagerCelebrityDetail.setVisibility(View.INVISIBLE);
                            textViewImagesHeader.setVisibility(View.GONE);
                            textViewImagesCount.setVisibility(View.GONE);
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

    private void getCelebDetail(String personId) {
        circularProgressBar.setVisibility(View.VISIBLE);
        celebrityDetailMainObjectCall = ApiClient.getClient().create(Api.class).getCelebDetail(personId, EndpointKeys.THE_MOVIE_DB_API_KEY, "en-US");
        celebrityDetailMainObjectCall.enqueue(new Callback<CelebrityDetailMainObject>() {
            @Override
            public void onResponse(Call<CelebrityDetailMainObject> call, retrofit2.Response<CelebrityDetailMainObject> response) {
                circularProgressBar.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    CelebrityDetailMainObject celebrityDetailMainObject = response.body();
                    if (celebrityDetailMainObject != null) {
                        textViewKnownFor.setText(celebrityDetailMainObject.getKnownForDepartment());
                        textViewName.setText(celebrityDetailMainObject.getName());
                        textViewBornPlace.setText(celebrityDetailMainObject.getPlaceOfBirth());

                        if (celebrityDetailMainObject.getProfilePath() != null && !celebrityDetailMainObject.getProfilePath().isEmpty()) {
                            cardViewThumbnail.setVisibility(View.VISIBLE);
                            Glide.with(CelebrityDetailActivity.this)
                                    .load(EndpointUrl.PROFILE_BASE_URL + celebrityDetailMainObject.getProfilePath())
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .thumbnail(0.1f)
                                    .into(imageViewThumbnail);
                        }
                        if(celebrityDetailMainObject.getBirthday() != null && !celebrityDetailMainObject.getBirthday().isEmpty()){
                            textViewBorn.setText("Born: " + celebrityDetailMainObject.getBirthday());
                            textViewBorn.setVisibility(View.VISIBLE);
                        }else{
                            textViewBorn.setVisibility(View.GONE);
                        }
                        if (!celebrityDetailMainObject.getBiography().isEmpty()) {
                            textViewStoryLineHeader.setVisibility(View.VISIBLE);
                            textViewStoryLineHeader.setText("About " + celebrityDetailMainObject.getName());
                            ReadMoreOption readMoreOption = new ReadMoreOption.Builder(CelebrityDetailActivity.this)
                                    .textLength(2, ReadMoreOption.TYPE_LINE) // OR
                                    //.textLength(300, ReadMoreOption.TYPE_CHARACTER)
                                    .moreLabel("MORE")
                                    .lessLabel("LESS")
                                    .moreLabelColor(Color.RED)
                                    .lessLabelColor(getResources().getColor(R.color.colorAccent))
                                    .labelUnderLine(true)
                                    .expandAnimation(true)
                                    .build();

                            readMoreOption.addReadMoreTo(textViewOverview, celebrityDetailMainObject.getBiography());
                        } else {
                            textViewStoryLineHeader.setVisibility(View.GONE);
                            textViewOverview.setVisibility(View.GONE);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<CelebrityDetailMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                circularProgressBar.setVisibility(View.GONE);
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {

                    } else {

                    }
                } else {

                }
            }
        });
    }

    private void getCelebrityMovies(String language) {
        celebMoviesMainObjectCall = ApiClient.getClient().create(Api.class).getCelebMovies(celebId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        celebMoviesMainObjectCall.enqueue(new Callback<CelebMoviesMainObject>() {
            @Override
            public void onResponse(Call<CelebMoviesMainObject> call, retrofit2.Response<CelebMoviesMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    CelebMoviesMainObject celebMoviesMainObject = response.body();
                    if (celebMoviesMainObject != null) {
                        if (celebMoviesMainObject.getCast().size() > 0) {
                            textViewMoviesCount.setVisibility(View.VISIBLE);
                            textViewMoviesHeader.setVisibility(View.VISIBLE);
                            textViewViewMoreMovies.setVisibility(View.VISIBLE);
                            recyclerViewMovies.setVisibility(View.VISIBLE);
                            textViewMoviesCount.setText("(" + celebMoviesMainObject.getCast().size() + ")");
                            for (int i = 0; i < celebMoviesMainObject.getCast().size(); i++) {
                                celebMoviesDataArrayList.add(celebMoviesMainObject.getCast().get(i));
                                celebMoviesDetailAdapter.notifyItemInserted(i);
                            }
                        } else {
                            textViewMoviesCount.setVisibility(View.GONE);
                            textViewMoviesHeader.setVisibility(View.GONE);
                            textViewViewMoreMovies.setVisibility(View.GONE);
                            recyclerViewMovies.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CelebMoviesMainObject> call, Throwable error) {
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

    private void getCelebrityTvShows(String language) {
        celebTvShowsMainObjectCall = ApiClient.getClient().create(Api.class).getCelebTvShows(celebId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        celebTvShowsMainObjectCall.enqueue(new Callback<CelebTvShowsMainObject>() {
            @Override
            public void onResponse(Call<CelebTvShowsMainObject> call, retrofit2.Response<CelebTvShowsMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    CelebTvShowsMainObject celebMoviesMainObject = response.body();
                    if (celebMoviesMainObject != null) {
                        if (celebMoviesMainObject.getCast().size() > 0) {
                            textViewTvShowsHeader.setVisibility(View.VISIBLE);
                            textViewViewMoreTvShows.setVisibility(View.VISIBLE);
                            recyclerViewTvShows.setVisibility(View.VISIBLE);
                            textViewTvShowsCount.setVisibility(View.VISIBLE);
                            textViewTvShowsCount.setText("(" + celebMoviesMainObject.getCast().size() + ")");
                            for (int i = 0; i < celebMoviesMainObject.getCast().size(); i++) {
                                celebTvShowsDataList.add(celebMoviesMainObject.getCast().get(i));
                                celebTvShowsDetailAdapter.notifyItemInserted(i);
                            }
                        } else {
                            textViewTvShowsHeader.setVisibility(View.GONE);
                            textViewViewMoreTvShows.setVisibility(View.GONE);
                            recyclerViewTvShows.setVisibility(View.GONE);
                            textViewTvShowsCount.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CelebTvShowsMainObject> call, Throwable error) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusCelebTvShowsClick(EventBusTvShowsClick eventBusTvShowsClick) {
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.CELEB_TV_SHOWS)) {
            Intent intent = new Intent(this, TvShowsDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, celebTvShowsDataList.get(eventBusTvShowsClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.TV_NAME, celebTvShowsDataList.get(eventBusTvShowsClick.getPosition()).getOriginalName());
            intent.putExtra(EndpointKeys.RATING, celebTvShowsDataList.get(eventBusTvShowsClick.getPosition()).getVoteAverage());
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusCelebMoviesClick(EventBusMovieClick eventBusMovieClick) {
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.CELEB_MOVIES)) {
            Intent intent = new Intent(this, MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, celebMoviesDataArrayList.get(eventBusMovieClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.MOVIE_TITLE, celebMoviesDataArrayList.get(eventBusMovieClick.getPosition()).getTitle());
            intent.putExtra(EndpointKeys.RATING, celebMoviesDataArrayList.get(eventBusMovieClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusPagerImageClick(EventBusPagerImageClick eventBusPagerImageClick) {
        startImageSliderActivity(eventBusPagerImageClick.getPosition());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusImageClick(EventBusImageClick eventBusImageClick) {
        if (eventBusImageClick.getClickType().equals(EndpointKeys.CELEBRITY_IMAGES)) {
            startImageSliderActivity(eventBusImageClick.getPosition());
        }
    }

    private void startImageSliderActivity(int position) {
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < celebImagesList.size(); i++) {
            images.add(EndpointUrl.POSTER_BASE_URL + celebImagesList.get(i).getFile_path());
        }
        Intent intent = new Intent(this, ImagesSliderActivity.class);
        intent.putExtra("images", images);
        intent.putExtra(EndpointKeys.CELEB_NAME, celebName);
        intent.putExtra(EndpointKeys.IMAGE_POSITION, position);
        startActivity(intent);
    }

}
