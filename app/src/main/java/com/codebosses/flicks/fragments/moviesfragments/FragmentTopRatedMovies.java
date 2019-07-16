package com.codebosses.flicks.fragments.moviesfragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTopRatedMovies extends BaseFragment {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageTopRatedMovies)
    TextView textViewError;
    @BindView(R.id.circularProgressBarTopRatedMovies)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewTopRatedMovies)
    RecyclerView recyclerViewTopRatedMovies;
    @BindView(R.id.imageViewErrorTopRatedMovies)
    AppCompatImageView imageViewError;
    @BindView(R.id.textViewRetryMessageTopRatedMovies)
    TextView textViewRetry;
    private LinearLayoutManager linearLayoutManager;

    //    Resource fields....
    @BindString(R.string.could_not_get_movies)
    String couldNotGetMovies;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Retrofit fields....
    private Call<MoviesMainObject> topRatedMoviesCall;

    //    Adapter fields....
    private MoviesAdapter moviesAdapter;

    //    Instance fields....
    private List<MoviesResult> topRatedMoviesList = new ArrayList<>();
    private int pageNumber = 1, totalPages = 0;

    public FragmentTopRatedMovies() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_top_rated_movies, container, false);
        ButterKnife.bind(this, view);

        //        Setting custom font....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setTextViewRegularFont(textViewRetry);

        if (getActivity() != null) {
            moviesAdapter = new MoviesAdapter(getActivity(), topRatedMoviesList, EndpointKeys.TOP_RATED_MOVIES);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewTopRatedMovies.setLayoutManager(linearLayoutManager);
            recyclerViewTopRatedMovies.setAdapter(moviesAdapter);
            recyclerViewTopRatedMovies.setItemAnimator(new FadeInDownAnimator());
            if (recyclerViewTopRatedMovies.getItemAnimator() != null)
                recyclerViewTopRatedMovies.getItemAnimator().setAddDuration(500);
            loadTopRatedMoviesFirstTime();
        }
        recyclerViewTopRatedMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getTopRatedMovies("en-US", "", pageNumber);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (topRatedMoviesCall != null && topRatedMoviesCall.isExecuted()) {
            topRatedMoviesCall.cancel();
        }
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

    private void getTopRatedMovies(String language, String region, int pageNumber) {
        topRatedMoviesCall = ApiClient.getClient().create(Api.class).getTopRatedMovies(EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber, region);
        topRatedMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                circularProgressBar.setVisibility(View.GONE);
                textViewError.setVisibility(View.GONE);
                imageViewError.setVisibility(View.GONE);
                textViewRetry.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        totalPages = moviesMainObject.getTotal_pages();
                        if (moviesMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                try {
                                    if (!moviesMainObject.getResults().get(i).getTitle().equalsIgnoreCase("venom") &&
                                            !moviesMainObject.getResults().get(i).getRelease_date().equalsIgnoreCase("2018-09-28")) {
                                        topRatedMoviesList.add(moviesMainObject.getResults().get(i));
                                        moviesAdapter.notifyItemInserted(topRatedMoviesList.size() - 1);
                                    }
                                }catch (Exception e){

                                }
                            }
                        }
                    }
                } else {
                    if (pageNumber == 1) {
                        textViewRetry.setVisibility(View.VISIBLE);
                        textViewError.setVisibility(View.VISIBLE);
                        imageViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(couldNotGetMovies);
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                circularProgressBar.setVisibility(View.GONE);
                if (pageNumber == 1) {
                    textViewRetry.setVisibility(View.VISIBLE);
                    textViewError.setVisibility(View.VISIBLE);
                    imageViewError.setVisibility(View.VISIBLE);
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                        if (pageNumber == 1)
                            textViewError.setText(internetProblem);
                    } else {
                        if (pageNumber == 1)
                            textViewError.setText(couldNotGetMovies);
                    }
                } else {
                    if (pageNumber == 1)
                        textViewError.setText(couldNotGetMovies);
                }
            }
        });
    }

    private void loadTopRatedMoviesFirstTime() {
        if (ValidUtils.isNetworkAvailable(getActivity())) {
            circularProgressBar.setVisibility(View.VISIBLE);
            getTopRatedMovies("en-US", "", pageNumber);
        } else {
            textViewError.setVisibility(View.VISIBLE);
            imageViewError.setVisibility(View.VISIBLE);
            textViewRetry.setVisibility(View.VISIBLE);
            textViewError.setText(internetProblem);
        }
    }

    @OnClick(R.id.textViewRetryMessageTopRatedMovies)
    public void onRetryClick(View view) {
        textViewError.setVisibility(View.GONE);
        imageViewError.setVisibility(View.GONE);
        textViewRetry.setVisibility(View.GONE);
        loadTopRatedMoviesFirstTime();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusTopRatedMovieClick(EventBusMovieClick eventBusMovieClick) {
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.TOP_RATED_MOVIES)) {
            Intent intent = new Intent(getActivity(), MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, topRatedMoviesList.get(eventBusMovieClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.MOVIE_TITLE, topRatedMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title());
            intent.putExtra(EndpointKeys.RATING, topRatedMoviesList.get(eventBusMovieClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

}
