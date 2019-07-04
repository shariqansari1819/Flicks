package com.codebosses.flicks.fragments.genrefragments;


import android.content.Intent;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.GenreMoviesActivity;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.activities.TvShowsDetailActivity;
import com.codebosses.flicks.adapters.genre.GenreTvShowsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import retrofit2.Call;
import retrofit2.Callback;

public class FragmentTvShowsGenre extends BaseFragment {

    //    Android fields....
    @BindView(R.id.nestedScrollViewGenre)
    CustomNestedScrollView customNestedScrollView;
    @BindView(R.id.textViewActionTvShowsHeaderGenre)
    TextView textViewActionHeader;
    @BindView(R.id.textViewViewMoreActionTvShowsGenre)
    TextView textViewActionMore;
    @BindView(R.id.recyclerViewActionTvShowsGenre)
    RecyclerView recyclerViewActionTvShows;
    @BindView(R.id.textViewAdventureTvShowsHeaderGenre)
    TextView textViewAdventureHeader;
    @BindView(R.id.textViewViewMoreAdventureTvShowsGenre)
    TextView textViewAdventureMore;
    @BindView(R.id.recyclerViewAdventureTvShowsGenre)
    RecyclerView recyclerViewAdventureTvShows;
    @BindView(R.id.textViewAnimatedTvShowsHeaderGenre)
    TextView textViewAnimatedHeader;
    @BindView(R.id.textViewViewMoreAnimatedTvShowsGenre)
    TextView textViewAnimatedMore;
    @BindView(R.id.recyclerViewAnimatedTvShowsGenre)
    RecyclerView recyclerViewAnimatedTvShows;
    @BindView(R.id.textViewScienceFictionTvShowsHeaderGenre)
    TextView textViewScienceFictionHeader;
    @BindView(R.id.textViewViewMoreScienceFictionTvShowsGenre)
    TextView textViewScienceFictionMore;
    @BindView(R.id.recyclerViewScienceFictionGenre)
    RecyclerView recyclerViewScienceFictionTvShows;
    @BindView(R.id.textViewRomanticTvShowsHeaderGenre)
    TextView textViewRomanticHeader;
    @BindView(R.id.textViewViewMoreRomanticTvShowsGenre)
    TextView textViewRomanticMore;
    @BindView(R.id.recyclerViewRomanticGenre)
    RecyclerView recyclerViewRomanticTvShows;
    @BindView(R.id.textViewCrimeTvShowsHeaderGenre)
    TextView textViewCrimeHeader;
    @BindView(R.id.textViewViewMoreCrimeTvShowsGenre)
    TextView textViewCrimeMore;
    @BindView(R.id.recyclerViewCrimeGenre)
    RecyclerView recyclerViewCrimeTvShows;
    @BindView(R.id.textViewHorrorTvShowsHeaderGenre)
    TextView textViewHorrorHeader;
    @BindView(R.id.textViewViewMoreHorrorTvShowsGenre)
    TextView textViewHorrorMore;
    @BindView(R.id.recyclerViewHorrorGenre)
    RecyclerView recyclerViewHorror;
    @BindView(R.id.textViewThrillerTvShowsHeaderGenre)
    TextView textViewThrillerHeader;
    @BindView(R.id.textViewViewMoreThrillerTvShowsGenre)
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
    @BindView(R.id.textViewComedyTvShowsHeaderGenre)
    TextView textViewComedyTvShowsHeader;
    @BindView(R.id.textViewViewMoreComedyTvShowsGenre)
    TextView textViewComedyTvShowsMore;
    @BindView(R.id.recyclerViewComedyGenre)
    RecyclerView recyclerViewComedy;

    //    Resource fields....
    @BindString(R.string.could_not_get_tv_shows)
    String couldNotGetTvShows;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Adapter fields...
    private GenreTvShowsAdapter actionTvShowsAdapter;
    private GenreTvShowsAdapter adventureTvShowsAdapter;
    private GenreTvShowsAdapter animatedTvShowsAdapter;
    private GenreTvShowsAdapter crimeTvShowsAdapter;
    private GenreTvShowsAdapter romanticTvShowsAdapter;
    private GenreTvShowsAdapter scienceFictionTvShowsAdapter;
    private GenreTvShowsAdapter horrorTvShowsAdapter;
    private GenreTvShowsAdapter thrillerTvShowsAdapter;
    private GenreTvShowsAdapter comedyTvShowsAdapter;

    //    Instance fields....
    private List<TvResult> actionTvShowsList = new ArrayList<>();
    private List<TvResult> adventureTvShowsList = new ArrayList<>();
    private List<TvResult> animatedTvShowsList = new ArrayList<>();
    private List<TvResult> crimeTvShowsList = new ArrayList<>();
    private List<TvResult> romanticTvShowsList = new ArrayList<>();
    private List<TvResult> scienceFictionTvShowsList = new ArrayList<>();
    private List<TvResult> horrorTvShowsList = new ArrayList<>();
    private List<TvResult> thrillerTvShowsList = new ArrayList<>();
    private List<TvResult> comedyTvShowsList = new ArrayList<>();
    private boolean isFirstTimeLoaded;

    //    Retrofit fields...
    private Call<TvMainObject> discoverTvShowsCall;

    public FragmentTvShowsGenre() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_shows_genre, container, false);
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
        fontUtils.setTextViewRegularFont(textViewComedyTvShowsHeader);
        fontUtils.setTextViewRegularFont(textViewComedyTvShowsMore);

        if (getActivity() != null) {
            if (ValidUtils.isNetworkAvailable(getActivity())) {

//            Setting layout managers....
                recyclerViewActionTvShows.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewAdventureTvShows.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewAnimatedTvShows.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewCrimeTvShows.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewRomanticTvShows.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewScienceFictionTvShows.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewHorror.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewThriller.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewComedy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

//            Initialization of empty adapter....
                actionTvShowsAdapter = new GenreTvShowsAdapter(getActivity(), actionTvShowsList, EndpointKeys.ACTION_TV_SHOWS);
                adventureTvShowsAdapter = new GenreTvShowsAdapter(getActivity(), adventureTvShowsList, EndpointKeys.ADVENTURE_TV_SHOWS);
                animatedTvShowsAdapter = new GenreTvShowsAdapter(getActivity(), animatedTvShowsList, EndpointKeys.ANIMATED_TV_SHOWS);
                romanticTvShowsAdapter = new GenreTvShowsAdapter(getActivity(), romanticTvShowsList, EndpointKeys.ROMANTIC_TV_SHOWS);
                scienceFictionTvShowsAdapter = new GenreTvShowsAdapter(getActivity(), scienceFictionTvShowsList, EndpointKeys.SCIENCE_FICTION_TV_SHOWS);
                crimeTvShowsAdapter = new GenreTvShowsAdapter(getActivity(), crimeTvShowsList, EndpointKeys.CRIME_TV_SHOWS);
                horrorTvShowsAdapter = new GenreTvShowsAdapter(getActivity(), horrorTvShowsList, EndpointKeys.HORROR_TV_SHOWS);
                thrillerTvShowsAdapter = new GenreTvShowsAdapter(getActivity(), thrillerTvShowsList, EndpointKeys.THRILLER_TV_SHOWS);
                comedyTvShowsAdapter = new GenreTvShowsAdapter(getActivity(), comedyTvShowsList, EndpointKeys.COMEDY_TV_SHOWS);

//            Setting empty adapter....
                recyclerViewActionTvShows.setAdapter(actionTvShowsAdapter);
                recyclerViewAdventureTvShows.setAdapter(adventureTvShowsAdapter);
                recyclerViewAnimatedTvShows.setAdapter(animatedTvShowsAdapter);
                recyclerViewCrimeTvShows.setAdapter(crimeTvShowsAdapter);
                recyclerViewRomanticTvShows.setAdapter(romanticTvShowsAdapter);
                recyclerViewScienceFictionTvShows.setAdapter(scienceFictionTvShowsAdapter);
                recyclerViewHorror.setAdapter(horrorTvShowsAdapter);
                recyclerViewThriller.setAdapter(thrillerTvShowsAdapter);
                recyclerViewComedy.setAdapter(comedyTvShowsAdapter);

//            Setting item animator....
                recyclerViewActionTvShows.setItemAnimator(new FadeInAnimator());
                recyclerViewAdventureTvShows.setItemAnimator(new FadeInAnimator());
                recyclerViewAnimatedTvShows.setItemAnimator(new FadeInAnimator());
                recyclerViewCrimeTvShows.setItemAnimator(new FadeInAnimator());
                recyclerViewRomanticTvShows.setItemAnimator(new FadeInAnimator());
                recyclerViewScienceFictionTvShows.setItemAnimator(new FadeInAnimator());
                recyclerViewHorror.setItemAnimator(new FadeInAnimator());
                recyclerViewThriller.setItemAnimator(new FadeInAnimator());
                recyclerViewComedy.setItemAnimator(new FadeInAnimator());

                loadGenreTvShows();

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
        if (discoverTvShowsCall != null && discoverTvShowsCall.isExecuted()) {
            discoverTvShowsCall.cancel();
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

    private void getGenreTvShows(String language, int pageNumber, int genreId, String sortType) {
        circularProgressBar.setVisibility(View.VISIBLE);
        discoverTvShowsCall = ApiClient.getClient().create(Api.class).getGenreTvShows(EndpointKeys.THE_MOVIE_DB_API_KEY, language, sortType, pageNumber, genreId);
        discoverTvShowsCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                circularProgressBar.setVisibility(View.GONE);
                textViewError.setVisibility(View.GONE);
                imageViewError.setVisibility(View.GONE);
                textViewRetry.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    TvMainObject TvShowsMainObject = response.body();
                    if (TvShowsMainObject != null) {
                        isFirstTimeLoaded = true;
                        switch (genreId) {
                            case Constants.ACTION_ID:
                                if (TvShowsMainObject.getTotal_results() > 0) {
                                    textViewActionHeader.setVisibility(View.VISIBLE);
                                    textViewActionMore.setVisibility(View.VISIBLE);
                                    recyclerViewActionTvShows.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < TvShowsMainObject.getResults().size(); i++) {
                                        actionTvShowsList.add(TvShowsMainObject.getResults().get(i));
                                        actionTvShowsAdapter.notifyItemInserted(actionTvShowsList.size() - 1);
                                    }
                                } else {
                                    textViewActionHeader.setVisibility(View.GONE);
                                    textViewActionMore.setVisibility(View.GONE);
                                    recyclerViewActionTvShows.setVisibility(View.VISIBLE);
                                }
                                break;
                            case Constants.ADVENTURE_ID:
                                if (TvShowsMainObject.getTotal_results() > 0) {
                                    textViewAdventureHeader.setVisibility(View.VISIBLE);
                                    textViewAdventureMore.setVisibility(View.VISIBLE);
                                    recyclerViewAdventureTvShows.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < TvShowsMainObject.getResults().size(); i++) {
                                        adventureTvShowsList.add(TvShowsMainObject.getResults().get(i));
                                        adventureTvShowsAdapter.notifyItemInserted(adventureTvShowsList.size() - 1);
                                    }
                                } else {
                                    textViewAdventureHeader.setVisibility(View.GONE);
                                    textViewAdventureMore.setVisibility(View.GONE);
                                    recyclerViewAdventureTvShows.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.ANIMATED_ID:
                                if (TvShowsMainObject.getTotal_results() > 0) {
                                    textViewAnimatedHeader.setVisibility(View.VISIBLE);
                                    textViewAnimatedMore.setVisibility(View.VISIBLE);
                                    recyclerViewAnimatedTvShows.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < TvShowsMainObject.getResults().size(); i++) {
                                        animatedTvShowsList.add(TvShowsMainObject.getResults().get(i));
                                        animatedTvShowsAdapter.notifyItemInserted(animatedTvShowsList.size() - 1);
                                    }
                                } else {
                                    textViewAnimatedHeader.setVisibility(View.GONE);
                                    textViewAnimatedMore.setVisibility(View.GONE);
                                    recyclerViewAnimatedTvShows.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.CRIME_ID:
                                if (TvShowsMainObject.getTotal_results() > 0) {
                                    textViewCrimeHeader.setVisibility(View.VISIBLE);
                                    textViewCrimeMore.setVisibility(View.VISIBLE);
                                    recyclerViewCrimeTvShows.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < TvShowsMainObject.getResults().size(); i++) {
                                        crimeTvShowsList.add(TvShowsMainObject.getResults().get(i));
                                        crimeTvShowsAdapter.notifyItemInserted(crimeTvShowsList.size() - 1);
                                    }
                                } else {
                                    textViewCrimeHeader.setVisibility(View.GONE);
                                    textViewCrimeMore.setVisibility(View.GONE);
                                    recyclerViewCrimeTvShows.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.ROMANTIC_ID:
                                if (TvShowsMainObject.getTotal_results() > 0) {
                                    textViewRomanticHeader.setVisibility(View.VISIBLE);
                                    textViewRomanticMore.setVisibility(View.VISIBLE);
                                    recyclerViewRomanticTvShows.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < TvShowsMainObject.getResults().size(); i++) {
                                        romanticTvShowsList.add(TvShowsMainObject.getResults().get(i));
                                        romanticTvShowsAdapter.notifyItemInserted(romanticTvShowsList.size() - 1);
                                    }
                                } else {
                                    textViewRomanticHeader.setVisibility(View.GONE);
                                    textViewRomanticMore.setVisibility(View.GONE);
                                    recyclerViewRomanticTvShows.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.SCIENCE_FICTION_ID:
                                if (TvShowsMainObject.getTotal_results() > 0) {
                                    textViewScienceFictionHeader.setVisibility(View.VISIBLE);
                                    textViewScienceFictionMore.setVisibility(View.VISIBLE);
                                    recyclerViewScienceFictionTvShows.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < TvShowsMainObject.getResults().size(); i++) {
                                        scienceFictionTvShowsList.add(TvShowsMainObject.getResults().get(i));
                                        scienceFictionTvShowsAdapter.notifyItemInserted(scienceFictionTvShowsList.size() - 1);
                                    }
                                } else {
                                    textViewScienceFictionHeader.setVisibility(View.GONE);
                                    textViewScienceFictionMore.setVisibility(View.GONE);
                                    recyclerViewScienceFictionTvShows.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.HORROR_ID:
                                if (TvShowsMainObject.getTotal_results() > 0) {
                                    textViewHorrorHeader.setVisibility(View.VISIBLE);
                                    textViewHorrorMore.setVisibility(View.VISIBLE);
                                    recyclerViewHorror.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < TvShowsMainObject.getResults().size(); i++) {
                                        horrorTvShowsList.add(TvShowsMainObject.getResults().get(i));
                                        horrorTvShowsAdapter.notifyItemInserted(horrorTvShowsList.size() - 1);
                                    }
                                } else {
                                    textViewHorrorHeader.setVisibility(View.GONE);
                                    textViewHorrorMore.setVisibility(View.GONE);
                                    recyclerViewHorror.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.THRILLER_ID:
                                if (TvShowsMainObject.getTotal_results() > 0) {
                                    textViewThrillerHeader.setVisibility(View.VISIBLE);
                                    textViewThrilerMore.setVisibility(View.VISIBLE);
                                    recyclerViewThriller.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < TvShowsMainObject.getResults().size(); i++) {
                                        thrillerTvShowsList.add(TvShowsMainObject.getResults().get(i));
                                        thrillerTvShowsAdapter.notifyItemInserted(thrillerTvShowsList.size() - 1);
                                    }
                                } else {
                                    textViewThrillerHeader.setVisibility(View.GONE);
                                    textViewThrilerMore.setVisibility(View.GONE);
                                    recyclerViewThriller.setVisibility(View.GONE);
                                }
                                break;
                            case Constants.COMEDY:
                                if (TvShowsMainObject.getTotal_results() > 0) {
                                    textViewComedyTvShowsHeader.setVisibility(View.VISIBLE);
                                    textViewComedyTvShowsMore.setVisibility(View.VISIBLE);
                                    recyclerViewComedy.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < TvShowsMainObject.getResults().size(); i++) {
                                        comedyTvShowsList.add(TvShowsMainObject.getResults().get(i));
                                        comedyTvShowsAdapter.notifyItemInserted(comedyTvShowsList.size() - 1);
                                    }
                                } else {
                                    textViewComedyTvShowsHeader.setVisibility(View.GONE);
                                    textViewComedyTvShowsMore.setVisibility(View.GONE);
                                    recyclerViewComedy.setVisibility(View.GONE);
                                }
                                break;
                        }
                    }
                } else {
                    if (!isFirstTimeLoaded) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(couldNotGetTvShows);
                        imageViewError.setVisibility(View.VISIBLE);
                        textViewRetry.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<TvMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                circularProgressBar.setVisibility(View.GONE);
                if (!isFirstTimeLoaded) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetTvShows);
                    imageViewError.setVisibility(View.VISIBLE);
                    textViewRetry.setVisibility(View.VISIBLE);
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                        if (!isFirstTimeLoaded)
                            textViewError.setText(internetProblem);
                    } else {
                        if (!isFirstTimeLoaded)
                            textViewError.setText(couldNotGetTvShows);
                    }
                } else {
                    if (!isFirstTimeLoaded)
                        textViewError.setText(couldNotGetTvShows);
                }
            }
        });
    }

    @OnClick(R.id.textViewViewMoreActionTvShowsGenre)
    public void onActionViewMoreClick(View view) {
        startGenreTvShowsActivity(EndpointKeys.ACTION_TV_SHOWS, Constants.ACTION_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreAdventureTvShowsGenre)
    public void onAdventureViewMoreClick(View view) {
        startGenreTvShowsActivity(EndpointKeys.ADVENTURE_TV_SHOWS, Constants.ADVENTURE_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreAnimatedTvShowsGenre)
    public void onAnimatedViewMoreClick(View view) {
        startGenreTvShowsActivity(EndpointKeys.ANIMATED_TV_SHOWS, Constants.ANIMATED_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreCrimeTvShowsGenre)
    public void onCrimeViewMoreClick(View view) {
        startGenreTvShowsActivity(EndpointKeys.CRIME_TV_SHOWS, Constants.CRIME_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreRomanticTvShowsGenre)
    public void onRomanticViewMoreClick(View view) {
        startGenreTvShowsActivity(EndpointKeys.ROMANTIC_MOVOES, Constants.ROMANTIC_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreScienceFictionTvShowsGenre)
    public void onScienceFictionViewMoreClick(View view) {
        startGenreTvShowsActivity(EndpointKeys.SCIENCE_FICTION_TV_SHOWS, Constants.SCIENCE_FICTION_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreHorrorTvShowsGenre)
    public void onHorrorViewMoreClick(View view) {
        startGenreTvShowsActivity(EndpointKeys.HORROR_TV_SHOWS, Constants.HORROR_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreThrillerTvShowsGenre)
    public void onThrillerViewMoreClick(View view) {
        startGenreTvShowsActivity(EndpointKeys.THRILLER_TV_SHOWS, Constants.THRILLER_ID, Constants.POPULARITY_DESC);
    }

    @OnClick(R.id.textViewViewMoreComedyTvShowsGenre)
    public void onComedyViewMoreClick(View view) {
        startGenreTvShowsActivity(EndpointKeys.COMEDY_TV_SHOWS, Constants.COMEDY, Constants.POPULARITY_DESC);
    }

    private void startGenreTvShowsActivity(String type, int id, String sortType) {
        Intent intent = new Intent(getActivity(), GenreMoviesActivity.class);
        intent.putExtra(EndpointKeys.GENRE_TYPE, type);
        intent.putExtra(EndpointKeys.GENRE_ID, id);
        intent.putExtra(EndpointKeys.SORT_TYPE, sortType);
        intent.putExtra(EndpointKeys.TYPE, EndpointKeys.TV_SHOWS);
        startActivity(intent);
    }

    private void loadGenreTvShows() {
        if (ValidUtils.isNetworkAvailable(getActivity())) {
            circularProgressBar.setVisibility(View.VISIBLE);
            getGenreTvShows("en-US", 1, Constants.ACTION_ID, Constants.POPULARITY_DESC);
            getGenreTvShows("en-US", 1, Constants.ADVENTURE_ID, Constants.POPULARITY_DESC);
            getGenreTvShows("en-US", 1, Constants.ANIMATED_ID, Constants.POPULARITY_DESC);
            getGenreTvShows("en-US", 1, Constants.ROMANTIC_ID, Constants.POPULARITY_DESC);
            getGenreTvShows("en-US", 1, Constants.SCIENCE_FICTION_ID, Constants.POPULARITY_DESC);
            getGenreTvShows("en-US", 1, Constants.CRIME_ID, Constants.POPULARITY_DESC);
            getGenreTvShows("en-US", 1, Constants.HORROR_ID, Constants.POPULARITY_DESC);
            getGenreTvShows("en-US", 1, Constants.THRILLER_ID, Constants.POPULARITY_DESC);
            getGenreTvShows("en-US", 1, Constants.COMEDY, Constants.POPULARITY_DESC);
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
        loadGenreTvShows();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSimilarMovieClick(EventBusTvShowsClick eventBusMovieClick) {
        String tvTitle = "";
        int tvId = 0;
        double rating = 0.0;
        if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.ACTION_TV_SHOWS)) {
            tvId = actionTvShowsList.get(eventBusMovieClick.getPosition()).getId();
            tvTitle = actionTvShowsList.get(eventBusMovieClick.getPosition()).getOriginal_name();
            rating = actionTvShowsList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.ADVENTURE_TV_SHOWS)) {
            tvId = adventureTvShowsList.get(eventBusMovieClick.getPosition()).getId();
            tvTitle = adventureTvShowsList.get(eventBusMovieClick.getPosition()).getOriginal_name();
            rating = adventureTvShowsList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.ANIMATED_TV_SHOWS)) {
            tvId = animatedTvShowsList.get(eventBusMovieClick.getPosition()).getId();
            tvTitle = animatedTvShowsList.get(eventBusMovieClick.getPosition()).getOriginal_name();
            rating = animatedTvShowsList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.ROMANTIC_MOVOES)) {
            tvId = romanticTvShowsList.get(eventBusMovieClick.getPosition()).getId();
            tvTitle = romanticTvShowsList.get(eventBusMovieClick.getPosition()).getOriginal_name();
            rating = romanticTvShowsList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.CRIME_TV_SHOWS)) {
            tvId = crimeTvShowsList.get(eventBusMovieClick.getPosition()).getId();
            tvTitle = crimeTvShowsList.get(eventBusMovieClick.getPosition()).getOriginal_name();
            rating = crimeTvShowsList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.SCIENCE_FICTION)) {
            tvId = scienceFictionTvShowsList.get(eventBusMovieClick.getPosition()).getId();
            tvTitle = scienceFictionTvShowsList.get(eventBusMovieClick.getPosition()).getOriginal_name();
            rating = scienceFictionTvShowsList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.HORROR_TV_SHOWS)) {
            tvId = horrorTvShowsList.get(eventBusMovieClick.getPosition()).getId();
            tvTitle = horrorTvShowsList.get(eventBusMovieClick.getPosition()).getOriginal_name();
            rating = horrorTvShowsList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.THRILLER_TV_SHOWS)) {
            tvId = thrillerTvShowsList.get(eventBusMovieClick.getPosition()).getId();
            tvTitle = thrillerTvShowsList.get(eventBusMovieClick.getPosition()).getOriginal_name();
            rating = thrillerTvShowsList.get(eventBusMovieClick.getPosition()).getVote_average();
        } else if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.COMEDY_TV_SHOWS)) {
            tvId = comedyTvShowsList.get(eventBusMovieClick.getPosition()).getId();
            tvTitle = comedyTvShowsList.get(eventBusMovieClick.getPosition()).getOriginal_name();
            rating = comedyTvShowsList.get(eventBusMovieClick.getPosition()).getVote_average();
        }
        if (eventBusMovieClick.getTvShowType().equals(EndpointKeys.ACTION_TV_SHOWS) ||
                eventBusMovieClick.getTvShowType().equals(EndpointKeys.ADVENTURE_TV_SHOWS) ||
                eventBusMovieClick.getTvShowType().equals(EndpointKeys.ANIMATED_TV_SHOWS) ||
                eventBusMovieClick.getTvShowType().equals(EndpointKeys.ROMANTIC_MOVOES) ||
                eventBusMovieClick.getTvShowType().equals(EndpointKeys.CRIME_TV_SHOWS) ||
                eventBusMovieClick.getTvShowType().equals(EndpointKeys.SCIENCE_FICTION) ||
                eventBusMovieClick.getTvShowType().equals(EndpointKeys.HORROR_TV_SHOWS) ||
                eventBusMovieClick.getTvShowType().equals(EndpointKeys.THRILLER_TV_SHOWS) ||
                eventBusMovieClick.getTvShowType().equals(EndpointKeys.COMEDY_TV_SHOWS)) {
            Intent intent = new Intent(getActivity(), TvShowsDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, tvId);
            intent.putExtra(EndpointKeys.TV_NAME, tvTitle);
            intent.putExtra(EndpointKeys.RATING, rating);
            startActivity(intent);
        }
    }


}
