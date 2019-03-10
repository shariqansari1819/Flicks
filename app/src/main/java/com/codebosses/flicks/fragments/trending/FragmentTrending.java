package com.codebosses.flicks.fragments.trending;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import retrofit2.Call;
import retrofit2.Callback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.CelebrityMoviesActivity;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.activities.TvShowsDetailActivity;
import com.codebosses.flicks.adapters.celebritiesadapter.CelebritiesAdapter;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.adapters.tvshowsadapter.TvShowsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesMainObject;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesResult;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityClick;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FragmentTrending extends BaseFragment {

    //    Android fields....
    @BindView(R.id.nestedScrollViewTrending)
    HorizontalScrollView nestedScrollViewTrending;
    @BindView(R.id.constraintLayoutTrending)
    ConstraintLayout constraintLayoutTrending;
    @BindView(R.id.textViewMoviesTrending)
    TextView textViewMoviesTrending;
    @BindView(R.id.textViewTvShowsTrending)
    TextView textViewTvShowsTrending;
    @BindView(R.id.textViewCelebritiesTrending)
    TextView textViewCelebritiesTrending;
    @BindView(R.id.textViewErrorMessageTrending)
    TextView textViewError;
    @BindView(R.id.recyclerViewTrending)
    RecyclerView recyclerViewTrending;
    @BindView(R.id.circularProgressBarTrending)
    CircularProgressBar progressBarTrending;
    @BindView(R.id.imageViewErrorTrending)
    ImageView imageViewTrending;
    private LinearLayoutManager linearLayoutManager;

    //    Resource fields....
    @BindString(R.string.could_not_get_upcoming_movies)
    String couldNotGetMovies;
    @BindString(R.string.internet_problem)
    String internetProblem;
    @BindString(R.string.could_not_get_top_rated_tv_shows)
    String couldNotGetTvShows;
    @BindString(R.string.could_not_get_celebrities)
    String couldNotGetCelebrities;

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private TextView currentTextView;
    private List<MoviesResult> topRatedMoviesList = new ArrayList<>();
    private List<TvResult> tvResultArrayList = new ArrayList<>();
    private List<CelebritiesResult> celebritiesResultList = new ArrayList<>();
    private CelebritiesAdapter celebritiesAdapter;
    private MoviesAdapter moviesAdapter;
    private TvShowsAdapter tvShowsAdapter;
    private int pageNumber = 1, totalPages = 0;

    //    Retrofit fields....
    private Call<MoviesMainObject> moviesMainObjectCall;
    private Call<TvMainObject> tvMainObjectCall;
    private Call<CelebritiesMainObject> celebritiesMainObjectCall;


    public FragmentTrending() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        ButterKnife.bind(this, view);

        currentTextView = textViewTvShowsTrending;
        setSelection(textViewMoviesTrending, currentTextView);
        currentTextView = textViewMoviesTrending;

//        Setting custom fonts....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setTextViewRegularFont(textViewMoviesTrending);
        fontUtils.setTextViewRegularFont(textViewCelebritiesTrending);
        fontUtils.setTextViewRegularFont(textViewTvShowsTrending);

        if (getActivity() != null) {
            if (ValidUtils.isNetworkAvailable(getActivity())) {
                linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerViewTrending.setLayoutManager(linearLayoutManager);
                setTrendingMoviesAdapter();
                recyclerViewTrending.setItemAnimator(new FadeInDownAnimator());
                if (recyclerViewTrending.getItemAnimator() != null)
                    recyclerViewTrending.getItemAnimator().setAddDuration(500);

                progressBarTrending.setVisibility(View.VISIBLE);
                getTrendingMovies(pageNumber);

            } else {
                textViewError.setVisibility(View.VISIBLE);
                imageViewTrending.setVisibility(View.VISIBLE);
                textViewError.setText(internetProblem);
            }
        }
        recyclerViewTrending.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        if (currentTextView.equals(textViewMoviesTrending))
                            getTrendingMovies(pageNumber);
                        else if (currentTextView.equals(textViewTvShowsTrending))
                            getTrendingTvShows(pageNumber);
                        else
                            getTrendingCelebrities(pageNumber);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (moviesMainObjectCall != null && moviesMainObjectCall.isExecuted()) {
            moviesMainObjectCall.cancel();
        }
    }

    private void setSelection(TextView textViewSelected, TextView textViewDeselected) {
        textViewSelected.setSelected(true);
        textViewDeselected.setSelected(false);
        textViewSelected.setTextColor(getResources().getColor(R.color.colorWhite));
        textViewDeselected.setTextColor(getResources().getColor(R.color.colorPrimaryText));
    }

    @OnClick(R.id.textViewMoviesTrending)
    public void onMoviesTrendingClick(TextView textView) {
        if (!currentTextView.equals(textViewMoviesTrending)) {
            setSelection(textViewMoviesTrending, currentTextView);
            currentTextView = textViewMoviesTrending;
            pageNumber = 1;
            topRatedMoviesList.clear();
            setTrendingMoviesAdapter();
            progressBarTrending.setVisibility(View.VISIBLE);
            getTrendingMovies(pageNumber);
        }
    }

    @OnClick(R.id.textViewTvShowsTrending)
    public void onTvShowsTrendingClick(TextView textView) {
        if (!currentTextView.equals(textViewTvShowsTrending)) {
            setSelection(textViewTvShowsTrending, currentTextView);
            currentTextView = textViewTvShowsTrending;
            pageNumber = 1;
            tvResultArrayList.clear();
            setTrendingTvShowsAdapter();
            progressBarTrending.setVisibility(View.VISIBLE);
            getTrendingTvShows(pageNumber);
        }
    }

    @OnClick(R.id.textViewCelebritiesTrending)
    public void onCelebritiesTrendingClick(TextView textView) {
        if (!currentTextView.equals(textViewCelebritiesTrending)) {
            setSelection(textViewCelebritiesTrending, currentTextView);
            currentTextView = textViewCelebritiesTrending;
            pageNumber = 1;
            celebritiesResultList.clear();
            setTrendingCelebsAdapter();
            progressBarTrending.setVisibility(View.VISIBLE);
            getTrendingCelebrities(pageNumber);
        }
    }

    private void setTrendingMoviesAdapter() {
        moviesAdapter = new MoviesAdapter(getActivity(), topRatedMoviesList, EndpointKeys.TRENDING_MOVIES);
        recyclerViewTrending.setAdapter(moviesAdapter);
    }

    private void setTrendingTvShowsAdapter() {
        tvShowsAdapter = new TvShowsAdapter(getActivity(), tvResultArrayList, EndpointKeys.TRENDING_TV_SHOWS);
        recyclerViewTrending.setAdapter(tvShowsAdapter);
    }

    private void setTrendingCelebsAdapter() {
        celebritiesAdapter = new CelebritiesAdapter(getActivity(), celebritiesResultList, EndpointKeys.TRENDING_CELEBRITIES);
        recyclerViewTrending.setAdapter(celebritiesAdapter);
    }

    private void getTrendingMovies(int pageNumber) {
        moviesMainObjectCall = Api.WEB_SERVICE.getMoviesTrending(Constants.MOVIE, Constants.WEEK, EndpointKeys.THE_MOVIE_DB_API_KEY, pageNumber);
        moviesMainObjectCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                progressBarTrending.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        totalPages = moviesMainObject.getTotal_pages();
                        if (moviesMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                topRatedMoviesList.add(moviesMainObject.getResults().get(i));
                                moviesAdapter.notifyItemInserted(topRatedMoviesList.size() - 1);
                            }
                        }
                    }
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    imageViewTrending.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetMovies);
                }
            }

            @Override
            public void onFailure(Call<MoviesMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                progressBarTrending.setVisibility(View.INVISIBLE);
                textViewError.setVisibility(View.VISIBLE);
                imageViewTrending.setVisibility(View.VISIBLE);
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                        textViewError.setText(internetProblem);
                    } else {
                        textViewError.setText(error.getMessage());
                    }
                } else {
                    textViewError.setText(couldNotGetMovies);
                }
            }
        });
    }

    private void getTrendingTvShows(int pageNumber) {
        tvMainObjectCall = Api.WEB_SERVICE.getTvShowsTrending(Constants.TV, Constants.WEEK, EndpointKeys.THE_MOVIE_DB_API_KEY, pageNumber);
        tvMainObjectCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                progressBarTrending.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    TvMainObject tvMainObject = response.body();
                    if (tvMainObject != null) {
                        totalPages = tvMainObject.getTotal_pages();
                        if (tvMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < tvMainObject.getResults().size(); i++) {
                                tvResultArrayList.add(tvMainObject.getResults().get(i));
                                tvShowsAdapter.notifyItemInserted(tvResultArrayList.size() - 1);
                            }
                        }
                    }
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    imageViewTrending.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetTvShows);
                }
            }

            @Override
            public void onFailure(Call<TvMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                progressBarTrending.setVisibility(View.INVISIBLE);
                textViewError.setVisibility(View.VISIBLE);
                imageViewTrending.setVisibility(View.VISIBLE);
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                        textViewError.setText(internetProblem);
                    } else {
                        textViewError.setText(error.getMessage());
                    }
                } else {
                    textViewError.setText(couldNotGetTvShows);
                }
            }
        });
    }

    private void getTrendingCelebrities(int pageNumber) {
        celebritiesMainObjectCall = Api.WEB_SERVICE.getCelebTrending(Constants.PERSON, Constants.WEEK, EndpointKeys.THE_MOVIE_DB_API_KEY, pageNumber);
        celebritiesMainObjectCall.enqueue(new Callback<CelebritiesMainObject>() {
            @Override
            public void onResponse(Call<CelebritiesMainObject> call, retrofit2.Response<CelebritiesMainObject> response) {
                progressBarTrending.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    CelebritiesMainObject celebritiesMainObject = response.body();
                    if (celebritiesMainObject != null) {
                        totalPages = celebritiesMainObject.getTotal_pages();
                        if (celebritiesMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < celebritiesMainObject.getResults().size(); i++) {
                                celebritiesResultList.add(celebritiesMainObject.getResults().get(i));
                                celebritiesAdapter.notifyItemInserted(celebritiesResultList.size() - 1);
                            }
                        }
                    }
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetCelebrities);
                    imageViewTrending.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<CelebritiesMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                progressBarTrending.setVisibility(View.INVISIBLE);
                textViewError.setVisibility(View.VISIBLE);
                imageViewTrending.setVisibility(View.VISIBLE);
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                        textViewError.setText(internetProblem);
                    } else {
                        textViewError.setText(error.getMessage());
                    }
                } else {
                    textViewError.setText(couldNotGetCelebrities);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusTopRatedMovieClick(EventBusMovieClick eventBusMovieClick) {
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.TRENDING_MOVIES)) {
            Intent intent = new Intent(getActivity(), MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, topRatedMoviesList.get(eventBusMovieClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.MOVIE_TITLE, topRatedMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title());
            intent.putExtra(EndpointKeys.RATING, topRatedMoviesList.get(eventBusMovieClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusTvShowsClick(EventBusTvShowsClick eventBusTvShowsClick) {
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.TRENDING_TV_SHOWS)) {
            Intent intent = new Intent(getActivity(), TvShowsDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, tvResultArrayList.get(eventBusTvShowsClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.TV_NAME, tvResultArrayList.get(eventBusTvShowsClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.RATING, tvResultArrayList.get(eventBusTvShowsClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusTopRatedCelebrities(EventBusCelebrityClick eventBusCelebrityClick) {
        if (eventBusCelebrityClick.getCelebType().equals(EndpointKeys.TRENDING_CELEBRITIES)) {
            Intent intent = new Intent(getActivity(), CelebrityMoviesActivity.class);
            intent.putExtra(EndpointKeys.CELEBRITY_ID, celebritiesResultList.get(eventBusCelebrityClick.getPosition()).getId());
            intent.putParcelableArrayListExtra(EndpointKeys.CELEB_MOVIES, celebritiesResultList.get(eventBusCelebrityClick.getPosition()).getKnown_for());
            intent.putExtra(EndpointKeys.CELEB_NAME, celebritiesResultList.get(eventBusCelebrityClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.CELEB_IMAGE, celebritiesResultList.get(eventBusCelebrityClick.getPosition()).getProfile_path());
            startActivity(intent);
        }
    }

}