package com.codebosses.flicks.fragments.searchfragments;


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
import retrofit2.Call;
import retrofit2.Callback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.CelebrityMoviesActivity;
import com.codebosses.flicks.adapters.celebritiesadapter.CelebritiesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesMainObject;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesResult;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityClick;
import com.codebosses.flicks.pojo.eventbus.EventBusSearchText;
import com.codebosses.flicks.utils.CommonSorting;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SearchCelebrityFragment extends Fragment {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageSearchCelebrity)
    TextView textViewError;
    @BindView(R.id.circularProgressBarSearchCelebrity)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewSearchCelebrity)
    RecyclerView recyclerViewSearchCelebrity;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.imageViewNotFoundSearchCelebrity)
    AppCompatImageView imageViewNotFound;
    private LinearLayoutManager linearLayoutManager;


    //    Resource fields....
    @BindString(R.string.could_not_search_celebrities)
    String couldNotGetCelebrities;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Retrofit fields....
    private Call<CelebritiesMainObject> searchCelebritiesCall;

    //    Adapter fields....
    private List<CelebritiesResult> searchCelebritiesList = new ArrayList<>();
    private CelebritiesAdapter celebritiesAdapter;
    private int pageNumber = 1, totalPages = 0;
    private String searchText;

    public SearchCelebrityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_celebrity, container, false);
        ButterKnife.bind(this, view);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        //        Setting custom font....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewError);

        if (getActivity() != null) {
            if (ValidUtils.isNetworkAvailable(getActivity())) {

                celebritiesAdapter = new CelebritiesAdapter(getActivity(), searchCelebritiesList, EndpointKeys.SEARCH_CELEBRITY);
                linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerViewSearchCelebrity.setLayoutManager(linearLayoutManager);
                recyclerViewSearchCelebrity.setItemAnimator(new DefaultItemAnimator());
                recyclerViewSearchCelebrity.setAdapter(celebritiesAdapter);

            } else {
                textViewError.setVisibility(View.VISIBLE);
                textViewError.setText(internetProblem);
            }
        }
        recyclerViewSearchCelebrity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        searchCelebrities(searchText, "en-US", pageNumber);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchCelebritiesCall != null && searchCelebritiesCall.isExecuted()) {
            searchCelebritiesCall.cancel();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSearchCelebrity(EventBusSearchText eventBusSearchText) {
        if (!eventBusSearchText.getSearchText().isEmpty()) {
            pageNumber = 1;
            searchText = eventBusSearchText.getSearchText();
            celebritiesAdapter.notifyItemRangeRemoved(0, searchCelebritiesList.size());
            searchCelebritiesList.clear();
            circularProgressBar.setVisibility(View.VISIBLE);
            searchCelebrities(searchText, "en-US", pageNumber);
        }
    }

    private void searchCelebrities(String query, String language, int pageNumber) {
        textViewError.setVisibility(View.GONE);
        imageViewNotFound.setVisibility(View.GONE);
        searchCelebritiesCall = Api.WEB_SERVICE.searchCelebrity(query, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber, true);
        searchCelebritiesCall.enqueue(new Callback<CelebritiesMainObject>() {
            @Override
            public void onResponse(Call<CelebritiesMainObject> call, retrofit2.Response<CelebritiesMainObject> response) {
                circularProgressBar.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    CelebritiesMainObject celebritiesMainObject = response.body();
                    if (celebritiesMainObject != null) {
                        totalPages = celebritiesMainObject.getTotal_pages();
                        if (celebritiesMainObject.getTotal_results() > 0) {
                            textViewError.setVisibility(View.GONE);
                            List<CelebritiesResult> celebritiesResults = celebritiesMainObject.getResults();
                            CommonSorting.sortCelebritiesByRating(celebritiesResults);
                            for (int i = 0; i < celebritiesResults.size(); i++) {
                                searchCelebritiesList.add(celebritiesResults.get(i));
                                celebritiesAdapter.notifyItemInserted(searchCelebritiesList.size() - 1);
                            }
                        } else {
                            textViewError.setVisibility(View.VISIBLE);
                            imageViewNotFound.setVisibility(View.VISIBLE);
                            textViewError.setText(getResources().getString(R.string.no_celebrity_found));
                        }
                    }
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    imageViewNotFound.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetCelebrities);
                }
            }

            @Override
            public void onFailure(Call<CelebritiesMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                circularProgressBar.setVisibility(View.INVISIBLE);
                imageViewNotFound.setVisibility(View.VISIBLE);
                textViewError.setVisibility(View.VISIBLE);
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
    public void eventBusTopRatedCelebrities(EventBusCelebrityClick eventBusCelebrityClick) {
        if (eventBusCelebrityClick.getCelebType().equals(EndpointKeys.SEARCH_CELEBRITY)) {
            Intent intent = new Intent(getActivity(), CelebrityMoviesActivity.class);
            intent.putExtra(EndpointKeys.CELEBRITY_ID, searchCelebritiesList.get(eventBusCelebrityClick.getPosition()).getId());
            intent.putParcelableArrayListExtra(EndpointKeys.CELEB_MOVIES, searchCelebritiesList.get(eventBusCelebrityClick.getPosition()).getKnown_for());
            intent.putExtra(EndpointKeys.CELEB_NAME, searchCelebritiesList.get(eventBusCelebrityClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.CELEB_IMAGE, searchCelebritiesList.get(eventBusCelebrityClick.getPosition()).getProfile_path());
            startActivity(intent);
        }
    }
}
