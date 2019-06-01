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
public class FragmentInTheater extends BaseFragment {


    //    Android fields....
    @BindView(R.id.textViewErrorMessageInTheater)
    TextView textViewError;
    @BindView(R.id.circularProgressBarInTheater)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewInTheater)
    RecyclerView recyclerViewInTheater;
    @BindView(R.id.imageViewErrorInTheater)
    AppCompatImageView imageViewError;
    @BindView(R.id.textViewRetryInTheater)
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
    private Call<MoviesMainObject> inTheaterCall;

    //    Adapter fields....
    private MoviesAdapter moviesAdapter;

    //    Instance fields...
    private List<MoviesResult> inTheaterList = new ArrayList<>();
    private int pageNumber = 1, totalPages = 0;

    public FragmentInTheater() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_theater, container, false);
        ButterKnife.bind(this, view);

        //        Setting custom font....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setTextViewRegularFont(textViewRetry);

        if (getActivity() != null) {
            moviesAdapter = new MoviesAdapter(getActivity(), inTheaterList, EndpointKeys.IN_THEATER);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewInTheater.setLayoutManager(linearLayoutManager);
            recyclerViewInTheater.setAdapter(moviesAdapter);
            recyclerViewInTheater.setItemAnimator(new FadeInDownAnimator());
            if (recyclerViewInTheater.getItemAnimator() != null)
                recyclerViewInTheater.getItemAnimator().setAddDuration(500);
            loadInTheaterMoviesFirstTime();
        }
        recyclerViewInTheater.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getInTheater("en-US", "", pageNumber);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (inTheaterCall != null && inTheaterCall.isExecuted()) {
            inTheaterCall.cancel();
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

    private void getInTheater(String language, String region, int pageNumber) {
        inTheaterCall = ApiClient.getClient().create(Api.class).getInTheaterMovies(EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber, region);
        inTheaterCall.enqueue(new Callback<MoviesMainObject>() {
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
                                inTheaterList.add(moviesMainObject.getResults().get(i));
                                moviesAdapter.notifyItemInserted(inTheaterList.size() - 1);
                            }
                        }
                    }
                } else {
                    if (pageNumber == 1) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(couldNotGetMovies);
                        imageViewError.setVisibility(View.VISIBLE);
                        textViewRetry.setVisibility(View.VISIBLE);
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
                    imageViewError.setVisibility(View.VISIBLE);
                    textViewError.setVisibility(View.VISIBLE);
                    textViewRetry.setVisibility(View.VISIBLE);
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

    private void loadInTheaterMoviesFirstTime() {
        if (ValidUtils.isNetworkAvailable(getActivity())) {
            circularProgressBar.setVisibility(View.VISIBLE);
            getInTheater("en-US", "", pageNumber);
        } else {
            textViewError.setVisibility(View.VISIBLE);
            imageViewError.setVisibility(View.VISIBLE);
            textViewRetry.setVisibility(View.VISIBLE);
            textViewError.setText(internetProblem);
        }
    }

    @OnClick(R.id.textViewRetryInTheater)
    public void onRetryClick(View view) {
        textViewError.setVisibility(View.GONE);
        imageViewError.setVisibility(View.GONE);
        textViewRetry.setVisibility(View.GONE);
        loadInTheaterMoviesFirstTime();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusInTheaterClick(EventBusMovieClick eventBusMovieClick) {
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.IN_THEATER)) {
            Intent intent = new Intent(getActivity(), MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, inTheaterList.get(eventBusMovieClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.MOVIE_TITLE, inTheaterList.get(eventBusMovieClick.getPosition()).getOriginal_title());
            intent.putExtra(EndpointKeys.RATING, inTheaterList.get(eventBusMovieClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

}
