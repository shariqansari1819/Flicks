package com.codebosses.flicks.fragments.trending;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import retrofit2.Call;
import retrofit2.Callback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class FragmentTrendingMovies extends Fragment {

    //    Android fields....
    @BindView(R.id.constraintLayoutTrendingMovies)
    ConstraintLayout constraintLayoutTrendingMovies;
    @BindView(R.id.textViewErrorMessageTrendingMovies)
    TextView textViewError;
    @BindView(R.id.recyclerViewTrendingMovies)
    RecyclerView recyclerViewTrending;
    @BindView(R.id.circularProgressBarTrendingMovies)
    CircularProgressBar progressBarTrending;
    @BindView(R.id.imageViewErrorTrendingMovies)
    ImageView imageViewTrending;
    private LinearLayoutManager linearLayoutManager;

    //    Resource fields....
    @BindString(R.string.could_not_get_movies)
    String couldNotGetMovies;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Adapter fields....
    private MoviesAdapter moviesAdapter;

    //    Instance fields....
    private List<MoviesResult> topRatedMoviesList = new ArrayList<>();
    private int pageNumber = 1, totalPages = 0;

    //    Retrofit fields....
    private Call<MoviesMainObject> moviesMainObjectCall;

    public FragmentTrendingMovies() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trending_movies, container, false);
        ButterKnife.bind(this, view);

//        Setting custom fonts....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewError);

        if (getActivity() != null) {
            if (ValidUtils.isNetworkAvailable(getActivity())) {
                linearLayoutManager = new LinearLayoutManager(getActivity());
                moviesAdapter = new MoviesAdapter(getActivity(), topRatedMoviesList, EndpointKeys.TRENDING_MOVIES);
                recyclerViewTrending.setAdapter(moviesAdapter);
                recyclerViewTrending.setLayoutManager(linearLayoutManager);
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
                        getTrendingMovies(pageNumber);
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

    private void getTrendingMovies(int pageNumber) {
        moviesMainObjectCall = ApiClient.getClient().create(Api.class).getMoviesTrending(Constants.MOVIE, Constants.WEEK, EndpointKeys.THE_MOVIE_DB_API_KEY, pageNumber);
        moviesMainObjectCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                progressBarTrending.setVisibility(View.GONE);
                textViewError.setVisibility(View.GONE);
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
                progressBarTrending.setVisibility(View.GONE);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusTrendingMoviesClick(EventBusMovieClick eventBusMovieClick) {
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.TRENDING_MOVIES)) {
            Intent intent = new Intent(getActivity(), MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, topRatedMoviesList.get(eventBusMovieClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.MOVIE_TITLE, topRatedMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title());
            intent.putExtra(EndpointKeys.RATING, topRatedMoviesList.get(eventBusMovieClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

}
