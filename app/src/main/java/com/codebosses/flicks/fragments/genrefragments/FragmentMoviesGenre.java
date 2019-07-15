package com.codebosses.flicks.fragments.genrefragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.GenreMoviesActivity;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.adapters.genre.GenreAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import retrofit2.Call;
import retrofit2.Callback;

public class FragmentMoviesGenre extends BaseFragment {

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
    @BindView(R.id.textViewHorrorMoviesHeaderGenre)
    TextView textViewHorrorHeader;
    @BindView(R.id.textViewViewMoreHorrorMoviesGenre)
    TextView textViewHorrorMore;
    @BindView(R.id.recyclerViewHorrorGenre)
    RecyclerView recyclerViewHorror;
    @BindView(R.id.textViewThrillerMoviesHeaderGenre)
    TextView textViewThrillerHeader;
    @BindView(R.id.textViewViewMoreThrillerMoviesGenre)
    TextView textViewThrilerMore;
    @BindView(R.id.recyclerViewThrillerGenre)
    RecyclerView recyclerViewThriller;
    @BindView(R.id.circularProgressBarGenre)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.imageViewErrorGenre)
    AppCompatImageView imageViewError;
    @BindView(R.id.textViewErrorMessageGenre)
    TextView textViewError;
    @BindView(R.id.textViewRetryMessageGenre)
    TextView textViewRetry;
    @BindView(R.id.textViewComedyMoviesHeaderGenre)
    TextView textViewComedyMoviesHeader;
    @BindView(R.id.textViewViewMoreComedyMoviesGenre)
    TextView textViewComedyMoviesMore;
    @BindView(R.id.recyclerViewComedyGenre)
    RecyclerView recyclerViewComedy;
    @BindView(R.id.textViewFamilyMoviesHeaderGenre)
    TextView textViewFamilyMoviesHeader;
    @BindView(R.id.textViewViewMoreFamilyMoviesGenre)
    TextView textViewFamilyMoviesMore;
    @BindView(R.id.recyclerViewFamilyGenre)
    RecyclerView recyclerViewFamily;

    //    Resource fields....
    @BindString(R.string.could_not_get_movies)
    String couldNotGetMovies;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Adapter fields...
    private GenreAdapter actionMoviesAdapter;
    private GenreAdapter adventureMoviesAdapter;
    private GenreAdapter animatedMoviesAdapter;
    private GenreAdapter crimeMoviesAdapter;
    private GenreAdapter romanticMoviesAdapter;
    private GenreAdapter scienceFictionMoviesAdapter;
    private GenreAdapter horrorMoviesAdapter;
    private GenreAdapter thrillerMoviesAdapter;
    private GenreAdapter comedyMoviesAdapter;
    private GenreAdapter familyMoviesAdapter;

    //    Instance fields....
    private List<MoviesResult> actionMoviesList = new ArrayList<>();
    private List<MoviesResult> adventureMoviesList = new ArrayList<>();
    private List<MoviesResult> animatedMoviesList = new ArrayList<>();
    private List<MoviesResult> crimeMoviesList = new ArrayList<>();
    private List<MoviesResult> romanticMoviesList = new ArrayList<>();
    private List<MoviesResult> scienceFictionMoviesList = new ArrayList<>();
    private List<MoviesResult> horrorMoviesList = new ArrayList<>();
    private List<MoviesResult> thrillerMoviesList = new ArrayList<>();
    private List<MoviesResult> comedyMoviesList = new ArrayList<>();
    private List<MoviesResult> familyMoviesList = new ArrayList<>();
    private boolean isFirstTimeLoaded;

    //    Retrofit fields...
    private Call<MoviesMainObject> discoverMoviesCall;

    public FragmentMoviesGenre() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_genre, container, false);
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
        fontUtils.setTextViewRegularFont(textViewHorrorHeader);
        fontUtils.setTextViewRegularFont(textViewHorrorMore);
        fontUtils.setTextViewRegularFont(textViewThrillerHeader);
        fontUtils.setTextViewRegularFont(textViewThrilerMore);
        fontUtils.setTextViewRegularFont(textViewRetry);
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setTextViewRegularFont(textViewComedyMoviesHeader);
        fontUtils.setTextViewRegularFont(textViewComedyMoviesMore);
        fontUtils.setTextViewRegularFont(textViewFamilyMoviesHeader);
        fontUtils.setTextViewRegularFont(textViewFamilyMoviesMore);

        if (getActivity() != null) {
            if (ValidUtils.isNetworkAvailable(getActivity())) {

//            Setting layout managers....
                recyclerViewActionMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewAdventureMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewAnimatedMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewCrimeMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewRomanticMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewScienceFictionMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewHorror.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewThriller.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewComedy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewFamily.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

//            Initialization of empty adapter....
                actionMoviesAdapter = new GenreAdapter(getActivity(), actionMoviesList, EndpointKeys.ACTION_MOVIES);
                adventureMoviesAdapter = new GenreAdapter(getActivity(), adventureMoviesList, EndpointKeys.ADVENTURE_MOVIES);
                animatedMoviesAdapter = new GenreAdapter(getActivity(), animatedMoviesList, EndpointKeys.ANIMATED_MOVIES);
                romanticMoviesAdapter = new GenreAdapter(getActivity(), romanticMoviesList, EndpointKeys.ROMANTIC_MOVOES);
                scienceFictionMoviesAdapter = new GenreAdapter(getActivity(), scienceFictionMoviesList, EndpointKeys.SCIENCE_FICTION_MOVIES);
                crimeMoviesAdapter = new GenreAdapter(getActivity(), crimeMoviesList, EndpointKeys.CRIME_MOVIES);
                horrorMoviesAdapter = new GenreAdapter(getActivity(), horrorMoviesList, EndpointKeys.HORROR_MOVIES);
                thrillerMoviesAdapter = new GenreAdapter(getActivity(), thrillerMoviesList, EndpointKeys.THRILLER_MOVIES);
                comedyMoviesAdapter = new GenreAdapter(getActivity(), comedyMoviesList, EndpointKeys.COMEDY_MOVIES);
                familyMoviesAdapter = new GenreAdapter(getActivity(), familyMoviesList, EndpointKeys.FAMILY_MOVIES);

//            Setting empty adapter....
                recyclerViewActionMovies.setAdapter(actionMoviesAdapter);
                recyclerViewAdventureMovies.setAdapter(adventureMoviesAdapter);
                recyclerViewAnimatedMovies.setAdapter(animatedMoviesAdapter);
                recyclerViewCrimeMovies.setAdapter(crimeMoviesAdapter);
                recyclerViewRomanticMovies.setAdapter(romanticMoviesAdapter);
                recyclerViewScienceFictionMovies.setAdapter(scienceFictionMoviesAdapter);
                recyclerViewHorror.setAdapter(horrorMoviesAdapter);
                recyclerViewThriller.setAdapter(thrillerMoviesAdapter);
                recyclerViewComedy.setAdapter(comedyMoviesAdapter);
                recyclerViewFamily.setAdapter(familyMoviesAdapter);

//            Setting item animator....
                recyclerViewActionMovies.setItemAnimator(new FadeInAnimator());
                recyclerViewAdventureMovies.setItemAnimator(new FadeInAnimator());
                recyclerViewAnimatedMovies.setItemAnimator(new FadeInAnimator());
                recyclerViewCrimeMovies.setItemAnimator(new FadeInAnimator());
                recyclerViewRomanticMovies.setItemAnimator(new FadeInAnimator());
                recyclerViewScienceFictionMovies.setItemAnimator(new FadeInAnimator());
                recyclerViewHorror.setItemAnimator(new FadeInAnimator());
                recyclerViewThriller.setItemAnimator(new FadeInAnimator());
                recyclerViewComedy.setItemAnimator(new FadeInAnimator());
                recyclerViewFamily.setItemAnimator(new FadeInAnimator());

                loadGenreMovies();

            } else {
                textViewError.setVisibility(View.VISIBLE);
                imageViewError.setVisibility(View.VISIBLE);
                textViewError.setText(internetProblem);
                textViewRetry.setVisibility(View.VISIBLE);
            }
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

    private void getGenreMovies(String language, int pageNumber, int genreId, String sortType) {
        circularProgressBar.setVisibility(View.VISIBLE);
        discoverMoviesCall = ApiClient.getClient().create(Api.class).getGenreMovies(EndpointKeys.THE_MOVIE_DB_API_KEY, language, sortType, false, true, pageNumber, genreId);
        discoverMoviesCall.enqueue(new Callback<MoviesMainObject>() {
            @Override
            public void onResponse(Call<MoviesMainObject> call, retrofit2.Response<MoviesMainObject> response) {
                circularProgressBar.setVisibility(View.GONE);
                textViewError.setVisibility(View.GONE);
                imageViewError.setVisibility(View.GONE);
                textViewRetry.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    MoviesMainObject moviesMainObject = response.body();
                    if (moviesMainObject != null) {
                        isFirstTimeLoaded = true;
                        switch (genreId) {
                            case Constants.ACTION_ID:
                                if (moviesMainObject.getTotal_results() > 0) {
                                    textViewActionHeader.setVisibility(View.VISIBLE);
                                    textViewActionMore.setVisibility(View.VISIBLE);
                                    recyclerViewActionMovies.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                        actionMoviesList.add(moviesMainObject.getResults().get(i));
                                        actionMoviesAdapter.notifyItemInserted(actionMoviesList.size() - 1);
                                    }
                                } else {
                                    textViewActionHeader.setVisibility(View.GONE);
                                    textViewActionMore.setVisibility(View.GONE);
                                    recyclerViewActionMovies.setVisibility(View.VISIBLE);
                                }
                                break;
                            case Constants.ADVENTURE_ID:
                                if (moviesMainObject.getTotal_results() > 0) {
                                    textViewAdventureHeader.setVisibility(View.VISIBLE);
                                    textViewAdventureMore.setVisibility(View.VISIBLE);
                                    recyclerViewAdventureMovies.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                        adventureMoviesList.add(moviesMainObject.getResults().get(i));
                                        adventureMoviesAdapter.notifyItemInserted(adventureMoviesList.size() - 1);
                                    }
                                } else {
                                    textViewAdventureHeader.setVisibility(View.GONE);
                                    textViewAdventureMore.setVisibility(View.GONE);
                                    recyclerViewAdventureMovies.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.ANIMATED_ID:
                                if (moviesMainObject.getTotal_results() > 0) {
                                    textViewAnimatedHeader.setVisibility(View.VISIBLE);
                                    textViewAnimatedMore.setVisibility(View.VISIBLE);
                                    recyclerViewAnimatedMovies.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                        animatedMoviesList.add(moviesMainObject.getResults().get(i));
                                        animatedMoviesAdapter.notifyItemInserted(animatedMoviesList.size() - 1);
                                    }
                                } else {
                                    textViewAnimatedHeader.setVisibility(View.GONE);
                                    textViewAnimatedMore.setVisibility(View.GONE);
                                    recyclerViewAnimatedMovies.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.CRIME_ID:
                                if (moviesMainObject.getTotal_results() > 0) {
                                    textViewCrimeHeader.setVisibility(View.VISIBLE);
                                    textViewCrimeMore.setVisibility(View.VISIBLE);
                                    recyclerViewCrimeMovies.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                        crimeMoviesList.add(moviesMainObject.getResults().get(i));
                                        crimeMoviesAdapter.notifyItemInserted(crimeMoviesList.size() - 1);
                                    }
                                } else {
                                    textViewCrimeHeader.setVisibility(View.GONE);
                                    textViewCrimeMore.setVisibility(View.GONE);
                                    recyclerViewCrimeMovies.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.ROMANTIC_ID:
                                if (moviesMainObject.getTotal_results() > 0) {
                                    textViewRomanticHeader.setVisibility(View.VISIBLE);
                                    textViewRomanticMore.setVisibility(View.VISIBLE);
                                    recyclerViewRomanticMovies.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                        romanticMoviesList.add(moviesMainObject.getResults().get(i));
                                        romanticMoviesAdapter.notifyItemInserted(romanticMoviesList.size() - 1);
                                    }
                                } else {
                                    textViewRomanticHeader.setVisibility(View.GONE);
                                    textViewRomanticMore.setVisibility(View.GONE);
                                    recyclerViewRomanticMovies.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.SCIENCE_FICTION_ID:
                                if (moviesMainObject.getTotal_results() > 0) {
                                    textViewScienceFictionHeader.setVisibility(View.VISIBLE);
                                    textViewScienceFictionMore.setVisibility(View.VISIBLE);
                                    recyclerViewScienceFictionMovies.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                        scienceFictionMoviesList.add(moviesMainObject.getResults().get(i));
                                        scienceFictionMoviesAdapter.notifyItemInserted(scienceFictionMoviesList.size() - 1);
                                    }
                                } else {
                                    textViewScienceFictionHeader.setVisibility(View.GONE);
                                    textViewScienceFictionMore.setVisibility(View.GONE);
                                    recyclerViewScienceFictionMovies.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.HORROR_ID:
                                if (moviesMainObject.getTotal_results() > 0) {
                                    textViewHorrorHeader.setVisibility(View.VISIBLE);
                                    textViewHorrorMore.setVisibility(View.VISIBLE);
                                    recyclerViewHorror.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                        horrorMoviesList.add(moviesMainObject.getResults().get(i));
                                        horrorMoviesAdapter.notifyItemInserted(horrorMoviesList.size() - 1);
                                    }
                                } else {
                                    textViewHorrorHeader.setVisibility(View.GONE);
                                    textViewHorrorMore.setVisibility(View.GONE);
                                    recyclerViewHorror.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.THRILLER_ID:
                                if (moviesMainObject.getTotal_results() > 0) {
                                    textViewThrillerHeader.setVisibility(View.VISIBLE);
                                    textViewThrilerMore.setVisibility(View.VISIBLE);
                                    recyclerViewThriller.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                        thrillerMoviesList.add(moviesMainObject.getResults().get(i));
                                        thrillerMoviesAdapter.notifyItemInserted(thrillerMoviesList.size() - 1);
                                    }
                                } else {
                                    textViewThrillerHeader.setVisibility(View.GONE);
                                    textViewThrilerMore.setVisibility(View.GONE);
                                    recyclerViewThriller.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.COMEDY:
                                if (moviesMainObject.getTotal_results() > 0) {
                                    textViewComedyMoviesHeader.setVisibility(View.VISIBLE);
                                    textViewComedyMoviesMore.setVisibility(View.VISIBLE);
                                    recyclerViewComedy.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                        comedyMoviesList.add(moviesMainObject.getResults().get(i));
                                        comedyMoviesAdapter.notifyItemInserted(comedyMoviesList.size() - 1);
                                    }
                                } else {
                                    textViewComedyMoviesHeader.setVisibility(View.GONE);
                                    textViewComedyMoviesMore.setVisibility(View.GONE);
                                    recyclerViewComedy.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.FAMILY_ID:
                                if (moviesMainObject.getTotal_results() > 0) {
                                    textViewFamilyMoviesHeader.setVisibility(View.VISIBLE);
                                    textViewFamilyMoviesMore.setVisibility(View.VISIBLE);
                                    recyclerViewFamily.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < moviesMainObject.getResults().size(); i++) {
                                        familyMoviesList.add(moviesMainObject.getResults().get(i));
                                        familyMoviesAdapter.notifyItemInserted(familyMoviesList.size() - 1);
                                    }
                                } else {
                                    textViewFamilyMoviesHeader.setVisibility(View.GONE);
                                    textViewFamilyMoviesMore.setVisibility(View.GONE);
                                    recyclerViewFamily.setVisibility(View.GONE);
                                }
                                break;
                        }
                    }
                } else {
                    if (!isFirstTimeLoaded) {
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
                if (!isFirstTimeLoaded) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetMovies);
                    imageViewError.setVisibility(View.VISIBLE);
                    textViewRetry.setVisibility(View.VISIBLE);
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                        if (!isFirstTimeLoaded)
                            textViewError.setText(internetProblem);
                    } else {
                        if (!isFirstTimeLoaded)
                            textViewError.setText(couldNotGetMovies);
                    }
                } else {
                    if (!isFirstTimeLoaded)
                        textViewError.setText(couldNotGetMovies);
                }
            }
        });
    }

    @OnClick(R.id.textViewViewMoreActionMoviesGenre)
    public void onActionViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.ACTION_MOVIES, Constants.ACTION_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreAdventureMoviesGenre)
    public void onAdventureViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.ADVENTURE_MOVIES, Constants.ADVENTURE_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreAnimatedMoviesGenre)
    public void onAnimatedViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.ANIMATED_MOVIES, Constants.ANIMATED_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreCrimeMoviesGenre)
    public void onCrimeViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.CRIME_MOVIES, Constants.CRIME_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreRomanticMoviesGenre)
    public void onRomanticViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.ROMANTIC_MOVOES, Constants.ROMANTIC_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreScienceFictionMoviesGenre)
    public void onScienceFictionViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.SCIENCE_FICTION_MOVIES, Constants.SCIENCE_FICTION_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreHorrorMoviesGenre)
    public void onHorrorViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.HORROR_MOVIES, Constants.HORROR_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreThrillerMoviesGenre)
    public void onThrillerViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.THRILLER_MOVIES, Constants.THRILLER_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreComedyMoviesGenre)
    public void onComedyViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.COMEDY_MOVIES, Constants.COMEDY, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreFamilyMoviesGenre)
    public void onFamilyViewMoreClick(View view) {
        startGenreMoviesActivity(EndpointKeys.FAMILY_MOVIES, Constants.FAMILY_ID, Constants.POPULARITY_DESC);
    }

    private void startGenreMoviesActivity(String type, int id, String sortType) {
        Intent intent = new Intent(getActivity(), GenreMoviesActivity.class);
        intent.putExtra(EndpointKeys.GENRE_TYPE, type);
        intent.putExtra(EndpointKeys.GENRE_ID, id);
        intent.putExtra(EndpointKeys.SORT_TYPE, sortType);
        intent.putExtra(EndpointKeys.TYPE, EndpointKeys.MOVIES);
        startActivity(intent);
    }

    private void loadGenreMovies() {
        if (ValidUtils.isNetworkAvailable(getActivity())) {
            circularProgressBar.setVisibility(View.VISIBLE);
            getGenreMovies("en-US", 1, Constants.ACTION_ID, Constants.POPULARITY_DESC);
            getGenreMovies("en-US", 1, Constants.ADVENTURE_ID, Constants.POPULARITY_DESC);
            getGenreMovies("en-US", 1, Constants.ANIMATED_ID, Constants.POPULARITY_DESC);
            getGenreMovies("en-US", 1, Constants.ROMANTIC_ID, Constants.POPULARITY_DESC);
            getGenreMovies("en-US", 1, Constants.SCIENCE_FICTION_ID, Constants.POPULARITY_DESC);
            getGenreMovies("en-US", 1, Constants.CRIME_ID, Constants.POPULARITY_DESC);
            getGenreMovies("en-US", 1, Constants.HORROR_ID, Constants.POPULARITY_DESC);
            getGenreMovies("en-US", 1, Constants.THRILLER_ID, Constants.POPULARITY_DESC);
            getGenreMovies("en-US", 1, Constants.COMEDY, Constants.POPULARITY_DESC);
            getGenreMovies("en-US", 1, Constants.FAMILY_ID, Constants.POPULARITY_DESC);
        } else {
            textViewError.setVisibility(View.VISIBLE);
            imageViewError.setVisibility(View.VISIBLE);
            textViewRetry.setVisibility(View.VISIBLE);
            textViewError.setText(internetProblem);
        }
    }

    @OnClick(R.id.textViewRetryMessageGenre)
    public void onRetryClick(View view) {
        textViewError.setVisibility(View.GONE);
        imageViewError.setVisibility(View.GONE);
        textViewRetry.setVisibility(View.GONE);
        loadGenreMovies();
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
        } else if (eventBusMovieClick.getMovieType().equals(EndpointKeys.HORROR_MOVIES)) {
            movieId = horrorMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = horrorMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = horrorMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getMovieType().equals(EndpointKeys.THRILLER_MOVIES)) {
            movieId = thrillerMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = thrillerMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = thrillerMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getMovieType().equals(EndpointKeys.COMEDY_MOVIES)) {
            movieId = comedyMoviesList.get(eventBusMovieClick.getPosition()).getId();
            movieTitle = comedyMoviesList.get(eventBusMovieClick.getPosition()).getOriginal_title();
            rating = comedyMoviesList.get(eventBusMovieClick.getPosition()).getVote_average();
        }
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.ACTION_MOVIES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.ADVENTURE_MOVIES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.ANIMATED_MOVIES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.ROMANTIC_MOVOES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.CRIME_MOVIES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.SCIENCE_FICTION) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.HORROR_MOVIES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.THRILLER_MOVIES) ||
                eventBusMovieClick.getMovieType().equals(EndpointKeys.COMEDY_MOVIES)) {
            Intent intent = new Intent(getActivity(), MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, movieId);
            intent.putExtra(EndpointKeys.MOVIE_TITLE, movieTitle);
            intent.putExtra(EndpointKeys.RATING, rating);
            startActivity(intent);
        }
    }

}
