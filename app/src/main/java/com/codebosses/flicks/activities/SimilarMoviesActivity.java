package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
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

public class SimilarMoviesActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageSimilarMovies)
    TextView textViewError;
    @BindView(R.id.circularProgressBarSimilarMovies)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewSimilarMovies)
    RecyclerView recyclerViewSimilarMovies;
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.appBarSimilarMovies)
    Toolbar toolbarSimilarMovies;
    @BindView(R.id.textViewAppBarMainTitle)
    TextView textViewAppBarTitle;

    //    Resource fields....
    @BindString(R.string.could_not_get_similar_movies)
    String couldNotGetMovies;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Retrofit fields....
    private Call<MoviesMainObject> similarMoviesCall;

    //    Adapter fields....
    private List<MoviesResult> similarMoviesList = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private int pageNumber = 1, totalPages = 0;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_movies);
        ButterKnife.bind(this);

//        Settinf custom action bar....
        setSupportActionBar(toolbarSimilarMovies);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            textViewAppBarTitle.setText(getResources().getString(R.string.similar_movies));
        }

        //        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setTextViewRegularFont(textViewAppBarTitle);

        if (ValidUtils.isNetworkAvailable(this)) {

            moviesAdapter = new MoviesAdapter(this, similarMoviesList, EndpointKeys.SIMILAR_MOVIES);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerViewSimilarMovies.setLayoutManager(linearLayoutManager);
            recyclerViewSimilarMovies.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSimilarMovies.setAdapter(moviesAdapter);

            if (getIntent() != null) {
                movieId = getIntent().getStringExtra(EndpointKeys.MOVIE_ID);
                if (movieId != null) {
                    circularProgressBar.setVisibility(View.VISIBLE);
                    getSimilarMovies(movieId, "en-US", pageNumber);
                }
            }

        } else {
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(internetProblem);
        }

        recyclerViewSimilarMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getSimilarMovies(movieId, "en-US", pageNumber);
                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (similarMoviesCall != null && similarMoviesCall.isExecuted()) {
            similarMoviesCall.cancel();
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

    private void getSimilarMovies(String movieId, String language, int pageNumber) {
        similarMoviesCall = ApiClient.getClient().create(Api.class).getSimilarMovies(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        similarMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                circularProgressBar.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        totalPages = moviesMainObject.getTotal_pages();
                        if (moviesMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                similarMoviesList.add(moviesMainObject.getResults().get(i));
                                moviesAdapter.notifyItemInserted(similarMoviesList.size() - 1);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSimilarMovieClick(EventBusMovieClick eventBusMovieClick) {
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.SIMILAR_MOVIES)) {
            Intent intent = new Intent(this, MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, similarMoviesList.get(eventBusMovieClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.MOVIE_TITLE, similarMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title());
            intent.putExtra(EndpointKeys.RATING, similarMoviesList.get(eventBusMovieClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

}
