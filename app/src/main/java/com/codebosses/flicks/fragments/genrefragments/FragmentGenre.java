package com.codebosses.flicks.fragments.genrefragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.GenreMoviesActivity;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.adapters.moviesdetail.SimilarMoviesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class FragmentGenre extends BaseFragment {

    //    Android fields....
    @BindView(R.id.nestedScrollViewGenre)
    CustomNestedScrollView customNestedScrollView;
    @BindView(R.id.textViewActionMoviesHeaderGenre)
    TextView textViewActionHeader;
    @BindView(R.id.textViewViewMoreActionMoviesGenre)
    TextView textViewActionMore;
    @BindView(R.id.recyclerViewActionMoviesGenre)
    RecyclerView recyclerViewActionMovies;
    @BindView(R.id.textViewAdventureMoviesHeaderGenre)
    TextView textViewAdventureHeader;
    @BindView(R.id.textViewViewMoreAdventureMoviesGenre)
    TextView textViewAdventureMore;
    @BindView(R.id.recyclerViewAdventureMoviesGenre)
    RecyclerView recyclerViewAdventureMovies;
    @BindView(R.id.textViewAnimatedMoviesHeaderGenre)
    TextView textViewAnimatedHeader;
    @BindView(R.id.textViewViewMoreAnimatedMoviesGenre)
    TextView textViewAnimatedMore;
    @BindView(R.id.recyclerViewAnimatedMoviesGenre)
    RecyclerView recyclerViewAnimatedMovies;
    @BindView(R.id.textViewScienceFictionMoviesHeaderGenre)
    TextView textViewScienceFictionHeader;
    @BindView(R.id.textViewViewMoreScienceFictionMoviesGenre)
    TextView textViewScienceFictionMore;
    @BindView(R.id.recyclerViewScienceFictionGenre)
    RecyclerView recyclerViewScienceFictionMovies;
    @BindView(R.id.textViewRomanticMoviesHeaderGenre)
    TextView textViewRomanticHeader;
    @BindView(R.id.textViewViewMoreRomanticMoviesGenre)
    TextView textViewRomanticMore;
    @BindView(R.id.recyclerViewRomanticGenre)
    RecyclerView recyclerViewRomanticMovies;
    @BindView(R.id.textViewCrimeMoviesHeaderGenre)
    TextView textViewCrimeHeader;
    @BindView(R.id.textViewViewMoreCrimeMoviesGenre)
    TextView textViewCrimeMore;
    @BindView(R.id.recyclerViewCrimeGenre)
    RecyclerView recyclerViewCrimeMovies;
    @BindView(R.id.circularProgressBarGenre)
    CircularProgressBar circularProgressBar;

    //    Font fields....
    private FontUtils fontUtils;

    //    Adapter fields...
    private SimilarMoviesAdapter actionMoviesAdapter;
    private SimilarMoviesAdapter adventureMoviesAdapter;
    private SimilarMoviesAdapter animatedMoviesAdapter;
    private SimilarMoviesAdapter crimeMoviesAdapter;
    private SimilarMoviesAdapter romanticMoviesAdapter;
    private SimilarMoviesAdapter scienceFictionMoviesAdapter;

    //    Instance fields....
    private List<MoviesResult> actionMoviesList = new ArrayList<>();
    private List<MoviesResult> adventureMoviesList = new ArrayList<>();
    private List<MoviesResult> animatedMoviesList = new ArrayList<>();
    private List<MoviesResult> crimeMoviesList = new ArrayList<>();
    private List<MoviesResult> romanticMoviesList = new ArrayList<>();
    private List<MoviesResult> scienceFictionMoviesList = new ArrayList<>();

    //    Retrofit fields...
    private Call<MoviesMainObject> discoverMoviesCall;

    public FragmentGenre() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre, container, false);
        ButterKnife.bind(this, view);

//        Setting custom font to android views....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewActionHeader);
        fontUtils.setTextViewRegularFont(textViewAdventureHeader);
        fontUtils.setTextViewRegularFont(textViewAnimatedHeader);
        fontUtils.setTextViewRegularFont(textViewRomanticHeader);
        fontUtils.setTextViewRegularFont(textViewCrimeHeader);
        fontUtils.setTextViewRegularFont(textViewScienceFictionHeader);
        fontUtils.setTextViewRegularFont(textViewActionMore);
        fontUtils.setTextViewRegularFont(textViewAdventureMore);
        fontUtils.setTextViewRegularFont(textViewAnimatedMore);
        fontUtils.setTextViewRegularFont(textViewCrimeMore);
        fontUtils.setTextViewRegularFont(textViewRomanticMore);
        fontUtils.setTextViewRegularFont(textViewScienceFictionMore);

        if (getActivity() != null) {

//            Setting layout managers....
            recyclerViewActionMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewAdventureMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewAnimatedMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewCrimeMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewRomanticMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewScienceFictionMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

//            Setting item animator....
            recyclerViewActionMovies.setItemAnimator(new DefaultItemAnimator());
            recyclerViewAdventureMovies.setItemAnimator(new DefaultItemAnimator());
            recyclerViewAnimatedMovies.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCrimeMovies.setItemAnimator(new DefaultItemAnimator());
            recyclerViewRomanticMovies.setItemAnimator(new DefaultItemAnimator());
            recyclerViewScienceFictionMovies.setItemAnimator(new DefaultItemAnimator());

//            Initialization of empty adapter....
            actionMoviesAdapter = new SimilarMoviesAdapter(getActivity(), actionMoviesList, EndpointKeys.ACTION_MOVIES);
            adventureMoviesAdapter = new SimilarMoviesAdapter(getActivity(), adventureMoviesList, EndpointKeys.ADVENTURE_MOVIES);
            animatedMoviesAdapter = new SimilarMoviesAdapter(getActivity(), animatedMoviesList, EndpointKeys.ANIMATED_MOVIES);
            romanticMoviesAdapter = new SimilarMoviesAdapter(getActivity(), romanticMoviesList, EndpointKeys.ROMANTIC_MOVOES);
            scienceFictionMoviesAdapter = new SimilarMoviesAdapter(getActivity(), scienceFictionMoviesList, EndpointKeys.SCIENCE_FICTION_MOVIES);
            crimeMoviesAdapter = new SimilarMoviesAdapter(getActivity(), crimeMoviesList, EndpointKeys.CRIME_MOVIES);

//            Setting empty adapter....
            recyclerViewActionMovies.setAdapter(actionMoviesAdapter);
            recyclerViewAdventureMovies.setAdapter(adventureMoviesAdapter);
            recyclerViewAnimatedMovies.setAdapter(animatedMoviesAdapter);
            recyclerViewCrimeMovies.setAdapter(crimeMoviesAdapter);
            recyclerViewRomanticMovies.setAdapter(romanticMoviesAdapter);
            recyclerViewScienceFictionMovies.setAdapter(scienceFictionMoviesAdapter);

            getActionMovies("en-US", 1);
            getAdventureMovies("en-US", 1);
            getAnimatedMovies("en-US", 1);
            getScienceFictionMovies("en-US", 1);
            getRomanticMovies("en-US", 1);
            getCrimeMovies("en-US", 1);

        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (discoverMoviesCall != null && discoverMoviesCall.isExecuted()) {
            discoverMoviesCall.cancel();
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

    private void getActionMovies(String language, int pageNumber) {
        circularProgressBar.setVisibility(View.VISIBLE);
        discoverMoviesCall = Api.WEB_SERVICE.getGenreMovies(EndpointKeys.THE_MOVIE_DB_API_KEY, language, "popularity.desc", false, true, pageNumber, Constants.ACTION_ID);
        discoverMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                circularProgressBar.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewActionHeader.setVisibility(View.VISIBLE);
                            textViewActionMore.setVisibility(View.VISIBLE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                actionMoviesList.add(moviesMainObject.getResults().get(i));
                                actionMoviesAdapter.notifyItemInserted(actionMoviesList.size() - 1);
                            }
                        } else {
                            textViewActionHeader.setVisibility(View.GONE);
                            textViewActionMore.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesMainObject> call, Throwable error) {
                circularProgressBar.setVisibility(View.GONE);
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

    private void getAdventureMovies(String language, int pageNumber) {
        discoverMoviesCall = Api.WEB_SERVICE.getGenreMovies(EndpointKeys.THE_MOVIE_DB_API_KEY, language, "vote_count.desc", false, true, pageNumber, Constants.ADVENTURE_ID);
        discoverMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewAdventureHeader.setVisibility(View.VISIBLE);
                            textViewAdventureMore.setVisibility(View.VISIBLE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                adventureMoviesList.add(moviesMainObject.getResults().get(i));
                                adventureMoviesAdapter.notifyItemInserted(adventureMoviesList.size() - 1);
                            }
                        } else {
                            textViewAdventureHeader.setVisibility(View.GONE);
                            textViewAdventureMore.setVisibility(View.GONE);
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

    private void getAnimatedMovies(String language, int pageNumber) {
        discoverMoviesCall = Api.WEB_SERVICE.getGenreMovies(EndpointKeys.THE_MOVIE_DB_API_KEY, language, "revenue.desc", false, true, pageNumber, Constants.ANIMATED_ID);
        discoverMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewAnimatedHeader.setVisibility(View.VISIBLE);
                            textViewAnimatedMore.setVisibility(View.VISIBLE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                animatedMoviesList.add(moviesMainObject.getResults().get(i));
                                animatedMoviesAdapter.notifyItemInserted(animatedMoviesList.size() - 1);
                            }
                        } else {
                            textViewAnimatedHeader.setVisibility(View.GONE);
                            textViewAnimatedMore.setVisibility(View.GONE);
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

    private void getScienceFictionMovies(String language, int pageNumber) {
        discoverMoviesCall = Api.WEB_SERVICE.getGenreMovies(EndpointKeys.THE_MOVIE_DB_API_KEY, language, "vote_average.desc", false, true, pageNumber, Constants.SCIENCE_FICTION_ID);
        discoverMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewScienceFictionHeader.setVisibility(View.VISIBLE);
                            textViewScienceFictionMore.setVisibility(View.VISIBLE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                scienceFictionMoviesList.add(moviesMainObject.getResults().get(i));
                                scienceFictionMoviesAdapter.notifyItemInserted(scienceFictionMoviesList.size() - 1);
                            }
                        } else {
                            textViewScienceFictionHeader.setVisibility(View.GONE);
                            textViewScienceFictionMore.setVisibility(View.GONE);
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

    private void getRomanticMovies(String language, int pageNumber) {
        discoverMoviesCall = Api.WEB_SERVICE.getGenreMovies(EndpointKeys.THE_MOVIE_DB_API_KEY, language, "vote_average.desc", false, true, pageNumber, Constants.ROMANTIC_ID);
        discoverMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewRomanticHeader.setVisibility(View.VISIBLE);
                            textViewRomanticMore.setVisibility(View.VISIBLE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                romanticMoviesList.add(moviesMainObject.getResults().get(i));
                                romanticMoviesAdapter.notifyItemInserted(romanticMoviesList.size() - 1);
                            }
                        } else {
                            textViewRomanticHeader.setVisibility(View.GONE);
                            textViewRomanticMore.setVisibility(View.GONE);
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

    private void getCrimeMovies(String language, int pageNumber) {
        discoverMoviesCall = Api.WEB_SERVICE.getGenreMovies(EndpointKeys.THE_MOVIE_DB_API_KEY, language, "vote_average.desc", false, true, pageNumber, Constants.CRIME_ID);
        discoverMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        if (moviesMainObject.getTotal_results() > 0) {
                            textViewCrimeHeader.setVisibility(View.VISIBLE);
                            textViewCrimeMore.setVisibility(View.VISIBLE);
                            for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                crimeMoviesList.add(moviesMainObject.getResults().get(i));
                                crimeMoviesAdapter.notifyItemInserted(crimeMoviesList.size() - 1);
                            }
                        } else {
                            textViewCrimeHeader.setVisibility(View.GONE);
                            textViewCrimeMore.setVisibility(View.GONE);
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

    @OnClick(R.id.textViewViewMoreActionMoviesGenre)
    public void onActionViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.ACTION_MOVIES, Constants.ACTION_ID, "popularity.desc");
    }

    @OnClick(R.id.textViewViewMoreAdventureMoviesGenre)
    public void onAdventureViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.ADVENTURE_MOVIES, Constants.ADVENTURE_ID, "vote_count.desc");
    }

    @OnClick(R.id.textViewViewMoreAnimatedMoviesGenre)
    public void onAnimatedViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.ANIMATED_MOVIES, Constants.ANIMATED_ID, "revenue.desc");
    }

    @OnClick(R.id.textViewViewMoreCrimeMoviesGenre)
    public void onCrimeViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.CRIME_MOVIES, Constants.CRIME_ID, "vote_average.desc");
    }

    @OnClick(R.id.textViewViewMoreRomanticMoviesGenre)
    public void onRomanticViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.ROMANTIC_MOVOES, Constants.ROMANTIC_ID, "vote_average.desc");
    }

    @OnClick(R.id.textViewViewMoreScienceFictionMoviesGenre)
    public void onScienceFictionViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.SCIENCE_FICTION_MOVIES, Constants.SCIENCE_FICTION_ID, "vote_average.desc");
    }

    private void startGenreMoviesActivity(String type, int id, String sortType) {
        Intent intent = new Intent(getActivity(), GenreMoviesActivity.class);
        intent.putExtra(EndpointKeys.GENRE_TYPE, type);
        intent.putExtra(EndpointKeys.GENRE_ID, id);
        intent.putExtra(EndpointKeys.SORT_TYPE, sortType);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSimilarMovieClick(EventBusMovieClick eventBusMovieClick) {
        String movieTitle = "";
        int movieId = 0;
        double rating = 0.0;
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.ACTION_MOVIES)) {
            movieId = actionMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = actionMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = actionMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getMovieType().equals(EndpointKeys.ADVENTURE_MOVIES)) {
            movieId = adventureMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = adventureMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = adventureMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getMovieType().equals(EndpointKeys.ANIMATED_MOVIES)) {
            movieId = animatedMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = animatedMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = animatedMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getMovieType().equals(EndpointKeys.ROMANTIC_MOVOES)) {
            movieId = romanticMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = romanticMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = romanticMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getMovieType().equals(EndpointKeys.CRIME_MOVIES)) {
            movieId = crimeMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = crimeMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = crimeMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getMovieType().equals(EndpointKeys.SCIENCE_FICTION)) {
            movieId = scienceFictionMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = scienceFictionMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = scienceFictionMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        }
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.ACTION_MOVIES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.ADVENTURE_MOVIES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.ANIMATED_MOVIES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.ROMANTIC_MOVOES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.CRIME_MOVIES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.SCIENCE_FICTION)) {
            Intent intent = new Intent(getActivity(), MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, movieId);
            intent.putExtra(EndpointKeys.MOVIE_TITLE, movieTitle);
            intent.putExtra(EndpointKeys.RATING, rating);
            startActivity(intent);
        }
    }

}
