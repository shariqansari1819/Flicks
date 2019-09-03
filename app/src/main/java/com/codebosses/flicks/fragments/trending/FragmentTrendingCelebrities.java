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
import com.codebosses.flicks.activities.CelebrityDetailActivity;
import com.codebosses.flicks.activities.CelebrityMoviesActivity;
import com.codebosses.flicks.adapters.celebritiesadapter.CelebritiesAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesMainObject;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesResult;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityClick;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class FragmentTrendingCelebrities extends Fragment {

    //    Android fields....
    @BindView(R.id.constraintLayoutTrendingCelebrities)
    ConstraintLayout constraintLayoutTrending;
    @BindView(R.id.textViewErrorMessageTrendingCelebrities)
    TextView textViewError;
    @BindView(R.id.recyclerViewTrendingCelebrities)
    RecyclerView recyclerViewTrending;
    @BindView(R.id.circularProgressBarTrendingCelebrities)
    CircularProgressBar progressBarTrending;
    @BindView(R.id.imageViewErrorTrendingCelebrities)
    ImageView imageViewTrending;
    @BindView(R.id.textViewRetryMessageTrendingCelebrities)
    TextView textViewRetry;
    private LinearLayoutManager linearLayoutManager;

    //    Resource fields....
    @BindString(R.string.internet_problem)
    String internetProblem;
    @BindString(R.string.could_not_get_celebrities)
    String couldNotGetCelebrities;

    //    Font fields....
    private FontUtils fontUtils;

    //    Adapter fields....
    private CelebritiesAdapter celebritiesAdapter;

    //    Instance fields....
    private List<CelebritiesResult> celebritiesResultList = new ArrayList<>();
    private int pageNumber = 1, totalPages = 0;

    //    Retrofit fields....
    private Call<CelebritiesMainObject> celebritiesMainObjectCall;

    public FragmentTrendingCelebrities() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trending_celebrities, container, false);
        ButterKnife.bind(this, view);

        //        Setting custom fonts....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewError);
        fontUtils.setTextViewRegularFont(textViewRetry);

        if (getActivity() != null) {
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewTrending.setLayoutManager(linearLayoutManager);
            celebritiesAdapter = new CelebritiesAdapter(getActivity(), celebritiesResultList, EndpointKeys.TRENDING_CELEBRITIES);
            recyclerViewTrending.setAdapter(celebritiesAdapter);
            recyclerViewTrending.setItemAnimator(new FadeInDownAnimator());
            if (recyclerViewTrending.getItemAnimator() != null)
                recyclerViewTrending.getItemAnimator().setAddDuration(500);
            loadTrendingCelebritiesFirstTime();
        }
        recyclerViewTrending.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    pageNumber++;
                    if (pageNumber <= totalPages)
                        getTrendingCelebrities(pageNumber);
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
        if (celebritiesMainObjectCall != null && celebritiesMainObjectCall.isExecuted()) {
            celebritiesMainObjectCall.cancel();
        }
    }

    private void getTrendingCelebrities(int pageNumber) {
        celebritiesMainObjectCall = ApiClient.getClient().create(Api.class).getCelebTrending(Constants.PERSON, Constants.WEEK, EndpointKeys.THE_MOVIE_DB_API_KEY, pageNumber);
        celebritiesMainObjectCall.enqueue(new Callback<CelebritiesMainObject>() {
            @Override
            public void onResponse(Call<CelebritiesMainObject> call, retrofit2.Response<CelebritiesMainObject> response) {
                progressBarTrending.setVisibility(View.GONE);
                textViewError.setVisibility(View.GONE);
                imageViewTrending.setVisibility(View.GONE);
                textViewRetry.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    CelebritiesMainObject celebritiesMainObject = response.body();
                    if (celebritiesMainObject != null) {
                        totalPages = celebritiesMainObject.getTotal_pages();
                        if (celebritiesMainObject.getTotal_results() > 0) {
                            for (int i = 0; i < celebritiesMainObject.getResults().size(); i++) {
                                celebritiesResultList.add(celebritiesMainObject.getResults().get(i));
                                celebritiesAdapter.notifyItemInserted(celebritiesResultList.size() - 1);
                            }
                        }
                    }
                } else {
                    if (pageNumber == 1) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(couldNotGetCelebrities);
                        imageViewTrending.setVisibility(View.VISIBLE);
                        textViewRetry.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CelebritiesMainObject> call, Throwable error) {
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
                            textViewError.setText(couldNotGetCelebrities);
                    }
                } else {
                    if (pageNumber == 1)
                        textViewError.setText(couldNotGetCelebrities);
                }
            }
        });
    }

    private void loadTrendingCelebritiesFirstTime() {
        if (ValidUtils.isNetworkAvailable(getActivity())) {
            progressBarTrending.setVisibility(View.VISIBLE);
            getTrendingCelebrities(pageNumber);
        } else {
            textViewError.setVisibility(View.VISIBLE);
            imageViewTrending.setVisibility(View.VISIBLE);
            textViewRetry.setVisibility(View.VISIBLE);
            textViewError.setText(internetProblem);
        }
    }

    @OnClick(R.id.textViewRetryMessageTrendingCelebrities)
    public void onRetryClick(View view) {
        textViewError.setVisibility(View.GONE);
        imageViewTrending.setVisibility(View.GONE);
        textViewRetry.setVisibility(View.GONE);
        loadTrendingCelebritiesFirstTime();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusTopRatedCelebrities(EventBusCelebrityClick eventBusCelebrityClick) {
        if (eventBusCelebrityClick.getCelebType().equals(EndpointKeys.TRENDING_CELEBRITIES)) {
            Intent intent = new Intent(getActivity(), CelebrityDetailActivity.class);
            intent.putExtra(EndpointKeys.CELEBRITY_ID, celebritiesResultList.get(eventBusCelebrityClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.CELEB_NAME, celebritiesResultList.get(eventBusCelebrityClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.CELEB_IMAGE, celebritiesResultList.get(eventBusCelebrityClick.getPosition()).getProfile_path());
            startActivity(intent);
        }
    }

}
