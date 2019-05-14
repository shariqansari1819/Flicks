package com.codebosses.flicks.fragments.tvfragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.TvShowsDetailActivity;
import com.codebosses.flicks.adapters.tvshowsadapter.TvShowsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FragmentTopRatedTvShows extends BaseFragment {

    //    Android fields....
    @BindView(R.id.textViewErrorMessageTopRatedTvShows)
    TextView textViewError;
    @BindView(R.id.circularProgressBarTopRatedTvShows)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewTopRatedTvShows)
    RecyclerView recyclerViewTopRatedTvShows;
    @BindView(R.id.imageViewErrorTopRatedTvShows)
    AppCompatImageView imageViewError;
    private LinearLayoutManager linearLayoutManager;


    //    Resource fields....
    @BindString(R.string.could_not_get_top_rated_tv_shows)
    String couldNotGetTvShows;
    @BindString(R.string.internet_problem)
    String internetProblem;

    //    Font fields....
    private FontUtils fontUtils;

    //    Retrofit fields....
    private Call<TvMainObject> tvMainObjectCall;

    //    Adapter fields....
    private TvShowsAdapter tvShowsAdapter;

    //    Instance fields....
    private List<TvResult> tvResultArrayList = new ArrayList<>();
    private int pageNumber = 1, totalPages = 0;

    public FragmentTopRatedTvShows() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_rated_tv_shows, container, false);
        ButterKnife.bind(this, view);

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewError);

        if (getActivity() != null) {
            if (ValidUtils.isNetworkAvailable(getActivity())) {

                tvShowsAdapter = new TvShowsAdapter(getActivity(), tvResultArrayList, EndpointKeys.TOP_RATED_TV_SHOWS);
                linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerViewTopRatedTvShows.setLayoutManager(linearLayoutManager);
                recyclerViewTopRatedTvShows.setAdapter(tvShowsAdapter);
                recyclerViewTopRatedTvShows.setItemAnimator(new FadeInDownAnimator());
                if (recyclerViewTopRatedTvShows.getItemAnimator() != null)
                    recyclerViewTopRatedTvShows.getItemAnimator().setAddDuration(500);

                circularProgressBar.setVisibility(View.VISIBLE);
                getTopRatedTvShows("en-US", pageNumber);

            } else {
                textViewError.setVisibility(View.VISIBLE);
                imageViewError.setVisibility(View.VISIBLE);
                textViewError.setText(internetProblem);
            }
        }
        recyclerViewTopRatedTvShows.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getTopRatedTvShows("en-US", pageNumber);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tvMainObjectCall != null && tvMainObjectCall.isExecuted()) {
            tvMainObjectCall.cancel();
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

    private void getTopRatedTvShows(String language, int pageNumber) {
        tvMainObjectCall = ApiClient.getClient().create(Api.class).getTopRatedTvShows(EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        tvMainObjectCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                circularProgressBar.setVisibility(View.GONE);
                textViewError.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    TvMainObject tvMainObject = response.body();
                    if (tvMainObject != null) {
                        totalPages = tvMainObject.getTotal_pages();
                        if (tvMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < tvMainObject.getResults().size(); i++) {
                                tvResultArrayList.add(tvMainObject.getResults().get(i));
                                tvShowsAdapter.notifyItemInserted(tvResultArrayList.size() - 1);
                            }
                        }
                    }
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    imageViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(couldNotGetTvShows);
                }
            }

            @Override
            public void onFailure(Call<TvMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                circularProgressBar.setVisibility(View.GONE);
                textViewError.setVisibility(View.VISIBLE);
                imageViewError.setVisibility(View.VISIBLE);
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
    public void eventBusTvShowsClick(EventBusTvShowsClick eventBusTvShowsClick) {
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.TOP_RATED_TV_SHOWS)) {
            Intent intent = new Intent(getActivity(), TvShowsDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, tvResultArrayList.get(eventBusTvShowsClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.TV_NAME, tvResultArrayList.get(eventBusTvShowsClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.RATING, tvResultArrayList.get(eventBusTvShowsClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

}
