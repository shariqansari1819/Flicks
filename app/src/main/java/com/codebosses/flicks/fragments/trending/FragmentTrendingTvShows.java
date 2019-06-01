package com.codebosses.flicks.fragments.trending;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.TvShowsDetailActivity;
import com.codebosses.flicks.adapters.tvshowsadapter.TvShowsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.endpoints.EndpointKeys;
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

public class FragmentTrendingTvShows extends Fragment {

    //    Android fields....
    @BindView(R.id.constraintLayoutTrendingTvShows)
    ConstraintLayout constraintLayoutTrending;
    @BindView(R.id.textViewErrorMessageTrendingTvShows)
    TextView textViewError;
    @BindView(R.id.recyclerViewTrendingTvShows)
    RecyclerView recyclerViewTrending;
    @BindView(R.id.circularProgressBarTrendingTvShows)
    CircularProgressBar progressBarTrending;
    @BindView(R.id.imageViewErrorTrendingTvShows)
    ImageView imageViewTrending;
    @BindView(R.id.textViewRetryMessageTrendingTvShows)
    TextView textViewRetry;
    private LinearLayoutManager linearLayoutManager;

    //    Resource fields....
    @BindString(R.string.internet_problem)
    String internetProblem;
    @BindString(R.string.could_not_get_tv_shows)
    String couldNotGetTvShows;

    //    Font fields....
    private FontUtils fontUtils;

    //    Adapter fields....
    private TvShowsAdapter tvShowsAdapter;

    //    Instance fields....
    private List<TvResult> tvResultArrayList = new ArrayList<>();
    private int pageNumber = 1, totalPages = 0;

    //    Retrofit fields....
    private Call<TvMainObject> tvMainObjectCall;


    public FragmentTrendingTvShows() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_tv_shows, container, false);
        ButterKnife.bind(this, view);

//        Setting custom fonts....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setTextViewRegularFont(textViewRetry);

        if (getActivity() != null) {
            linearLayoutManager = new LinearLayoutManager(getActivity());
            tvShowsAdapter = new TvShowsAdapter(getActivity(), tvResultArrayList, EndpointKeys.TRENDING_TV_SHOWS);
            recyclerViewTrending.setAdapter(tvShowsAdapter);
            recyclerViewTrending.setLayoutManager(linearLayoutManager);
            recyclerViewTrending.setItemAnimator(new FadeInDownAnimator());
            if (recyclerViewTrending.getItemAnimator() != null)
                recyclerViewTrending.getItemAnimator().setAddDuration(500);
            loadTrendingTvShowsFirstTime();
        }
        recyclerViewTrending.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getTrendingTvShows(pageNumber);
                }
            }
        });

        return view;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tvMainObjectCall != null && tvMainObjectCall.isExecuted()) {
            tvMainObjectCall.cancel();
        }
    }

    private void getTrendingTvShows(int pageNumber) {
        tvMainObjectCall = ApiClient.getClient().create(Api.class).getTvShowsTrending(Constants.TV, Constants.WEEK, EndpointKeys.THE_MOVIE_DB_API_KEY, pageNumber);
        tvMainObjectCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                progressBarTrending.setVisibility(View.GONE);
                textViewError.setVisibility(View.GONE);
                imageViewTrending.setVisibility(View.GONE);
                textViewRetry.setVisibility(View.GONE);
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
                    if (pageNumber == 1) {
                        textViewError.setVisibility(View.VISIBLE);
                        imageViewTrending.setVisibility(View.VISIBLE);
                        textViewError.setText(couldNotGetTvShows);
                        textViewRetry.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<TvMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                progressBarTrending.setVisibility(View.GONE);
                if (pageNumber == 1) {
                    textViewError.setVisibility(View.VISIBLE);
                    imageViewTrending.setVisibility(View.VISIBLE);
                    textViewRetry.setVisibility(View.VISIBLE);
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                        if (pageNumber == 1)
                            textViewError.setText(internetProblem);
                    } else {
                        if (pageNumber == 1)
                            textViewError.setText(couldNotGetTvShows);
                    }
                } else {
                    if (pageNumber == 1)
                        textViewError.setText(couldNotGetTvShows);
                }
            }
        });
    }

    private void loadTrendingTvShowsFirstTime() {
        if (ValidUtils.isNetworkAvailable(getActivity())) {
            progressBarTrending.setVisibility(View.VISIBLE);
            getTrendingTvShows(pageNumber);
        } else {
            textViewError.setVisibility(View.VISIBLE);
            imageViewTrending.setVisibility(View.VISIBLE);
            textViewRetry.setVisibility(View.VISIBLE);
            textViewError.setText(internetProblem);
        }
    }

    @OnClick(R.id.textViewRetryMessageTrendingTvShows)
    public void onRetryClick(View view) {
        textViewError.setVisibility(View.GONE);
        imageViewTrending.setVisibility(View.GONE);
        textViewRetry.setVisibility(View.GONE);
        loadTrendingTvShowsFirstTime();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusTvShowsClick(EventBusTvShowsClick eventBusTvShowsClick) {
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.TRENDING_TV_SHOWS)) {
            Intent intent = new Intent(getActivity(), TvShowsDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, tvResultArrayList.get(eventBusTvShowsClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.TV_NAME, tvResultArrayList.get(eventBusTvShowsClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.RATING, tvResultArrayList.get(eventBusTvShowsClick.getPosition()).getVote_average());
            startActivity(intent);
        }
    }

}
