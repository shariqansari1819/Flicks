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
import com.codebosses.flicks.activities.TvShowsDetailActivity;
import com.codebosses.flicks.adapters.tvshowsadapter.TvShowsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusSearchText;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
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

public class SearchTvShowsFragment extends Fragment {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageSearchTvShow)
    TextView textViewError;
    @BindView(R.id.circularProgressBarSearchTvShow)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewSearchTvShow)
    RecyclerView recyclerViewSearchTvShow;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.imageViewNotFoundSearchTvShow)
    AppCompatImageView imageViewNotFound;
    private LinearLayoutManager linearLayoutManager;


    //    Resource fields....
    @BindString(R.string.could_not_search_tv_shows)
    String couldNotGetTvShows;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Retrofit fields....
    private Call<TvMainObject> searchTvShowCall;

    //    Adapter fields....
    private List<TvResult> searchTvShowList = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int pageNumber = 1, totalPages = 0;
    private String searchText = "";

    public SearchTvShowsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_tv_shows, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

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
        fontUtils.setTextViewBoldFont(textViewError);

        if (getActivity() != null) {
            if (ValidUtils.isNetworkAvailable(getActivity())) {

                tvShowsAdapter = new TvShowsAdapter(getActivity(), searchTvShowList, EndpointKeys.SEARCH_TV_SHOW);
                linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerViewSearchTvShow.setLayoutManager(linearLayoutManager);
                recyclerViewSearchTvShow.setItemAnimator(new DefaultItemAnimator());
                recyclerViewSearchTvShow.setAdapter(tvShowsAdapter);

            } else {
                textViewError.setVisibility(View.VISIBLE);
                textViewError.setText(internetProblem);
            }
        }
        recyclerViewSearchTvShow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getTopLatestTvShows(searchText, "en-US", pageNumber);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (searchTvShowCall != null && searchTvShowCall.isExecuted()) {
            searchTvShowCall.cancel();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSearchTvShows(EventBusSearchText eventBusSearchText) {
        if (!eventBusSearchText.getSearchText().isEmpty()) {
            pageNumber = 1;
            circularProgressBar.setVisibility(View.VISIBLE);
            searchText = eventBusSearchText.getSearchText();
            tvShowsAdapter.notifyItemRangeRemoved(0, searchTvShowList.size());
            searchTvShowList.clear();
            getTopLatestTvShows(searchText, "en-US", pageNumber);
        }
    }

    private void getTopLatestTvShows(String query, String language, int pageNumber) {
        textViewError.setVisibility(View.GONE);
        imageViewNotFound.setVisibility(View.GONE);
        searchTvShowCall = Api.WEB_SERVICE.searchTvShows(query, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        searchTvShowCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                circularProgressBar.setVisibility(View.INVISIBLE);
                if (response != null && response.isSuccessful()) {
                    TvMainObject tvMainObject = response.body();
                    if (tvMainObject != null) {
                        totalPages = tvMainObject.getTotal_pages();
                        if (tvMainObject.getTotal_results() > 0) {
                            textViewError.setVisibility(View.GONE);
                            for (int i = 0; i < tvMainObject.getResults().size(); i++) {
                                searchTvShowList.add(tvMainObject.getResults().get(i));
                                tvShowsAdapter.notifyItemInserted(searchTvShowList.size() - 1);
                            }
                        } else {
                            textViewError.setVisibility(View.VISIBLE);
                            imageViewNotFound.setVisibility(View.VISIBLE);
                            textViewError.setText(getResources().getString(R.string.no_tv_show_found));
                        }
                    }
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetTvShows);
                    imageViewNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TvMainObject> call, Throwable error) {
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
                    textViewError.setText(couldNotGetTvShows);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSearchTvShow(EventBusTvShowsClick eventBusTvShowsClick) {
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.SEARCH_TV_SHOW)) {
            Intent intent = new Intent(getActivity(), TvShowsDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, searchTvShowList.get(eventBusTvShowsClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.TV_NAME, searchTvShowList.get(eventBusTvShowsClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.RATING, searchTvShowList.get(eventBusTvShowsClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }
}
