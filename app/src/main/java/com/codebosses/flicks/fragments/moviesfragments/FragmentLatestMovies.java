package com.codebosses.flicks.fragments.moviesfragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLatestMovies extends BaseFragment {


    //    Android fields....
    @BindView(R.id.textViewErrorMessageLatestMovies)
    TextView textViewError;
    @BindView(R.id.circularProgressBarLatestMovies)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewLatestMovies)
    RecyclerView recyclerViewLatestMovies;
    private LinearLayoutManager linearLayoutManager;


    //    Resource fields....
    @BindString(R.string.could_not_get_upcoming_movies)
    String couldNotGetMovies;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Retrofit fields....
    private Call<MoviesMainObject> latestMoviesCall;

    //    Adapter fields....
    private List<MoviesResult> latestMoviesList = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private int pageNumber = 1, totalPages = 0;

    public FragmentLatestMovies() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_movies, container, false);
        ButterKnife.bind(this, view);

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewBoldFont(textViewError);

        if (getActivity() != null) {
            if (ValidUtils.isNetworkAvailable(getActivity())) {

                moviesAdapter = new MoviesAdapter(getActivity(), latestMoviesList);
                linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerViewLatestMovies.setLayoutManager(linearLayoutManager);
                recyclerViewLatestMovies.setItemAnimator(new DefaultItemAnimator());
                recyclerViewLatestMovies.setAdapter(moviesAdapter);

                circularProgressBar.setVisibility(View.VISIBLE);
                getLatestMovies("en-US", "", pageNumber);

            } else {
                textViewError.setVisibility(View.VISIBLE);
                textViewError.setText(internetProblem);
            }
        }
        recyclerViewLatestMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getLatestMovies("en-US", "", pageNumber);
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (latestMoviesCall != null && latestMoviesCall.isExecuted()) {
            latestMoviesCall.cancel();
        }
    }

    private void getLatestMovies(String language, String region, int pageNumber) {
        latestMoviesCall = Api.WEB_SERVICE.getLatestMovies(EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber, region);
        latestMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                circularProgressBar.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        totalPages = moviesMainObject.getTotal_pages();
                        if (moviesMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                latestMoviesList.add(moviesMainObject.getResults().get(i));
                                moviesAdapter.notifyItemInserted(latestMoviesList.size() - 1);
                            }
                        }
                    }
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetMovies);
                }
            }

            @Override
            public void onFailure(Call<MoviesMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                circularProgressBar.setVisibility(View.INVISIBLE);
                textViewError.setVisibility(View.VISIBLE);
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

}
