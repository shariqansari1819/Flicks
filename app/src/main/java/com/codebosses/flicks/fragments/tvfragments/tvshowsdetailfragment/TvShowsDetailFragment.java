package com.codebosses.flicks.fragments.tvfragments.tvshowsdetailfragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieDetailId;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieGenreList;
import com.codebosses.flicks.pojo.eventbus.EventBusTvGenreList;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowDetailId;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.MovieDetailMainObject;
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.TvShowsDetailMainObject;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TvShowsDetailFragment extends Fragment {


    //    Android fields....
    @BindView(R.id.textViewOriginalTitleHeader)
    TextView textViewOriginalTitleHeader;
    @BindView(R.id.textViewOriginalTitleText)
    TextView textViewOriginalTitle;
    @BindView(R.id.textViewOverviewHeader)
    TextView textViewOverviewHeader;
    @BindView(R.id.textViewOverviewText)
    TextView textViewOverview;
    @BindView(R.id.textViewHomepageHeader)
    TextView textViewHomePageHeader;
    @BindView(R.id.textViewHomepageText)
    TextView textViewHomePage;
    @BindView(R.id.textViewBudgetHeader)
    TextView textViewBudgetHeader;
    @BindView(R.id.textViewBudgetText)
    TextView textViewBudget;
    @BindView(R.id.textViewReleaseDateHeader)
    TextView textViewReleaseDateHeader;
    @BindView(R.id.textViewReleaseDateText)
    TextView textViewReleaseDate;
    @BindView(R.id.textViewRevenueHeader)
    TextView textViewRevenueHeader;
    @BindView(R.id.textViewRevenueText)
    TextView textViewRevenue;
    @BindView(R.id.textViewTagLineHeader)
    TextView textViewTagLineHeader;
    @BindView(R.id.textViewTagLineText)
    TextView textViewTagLine;
    @BindView(R.id.textViewStatusHeader)
    TextView textViewStatusHeader;
    @BindView(R.id.textViewStatusText)
    TextView textViewStatus;
    @BindView(R.id.circularProgressBarTvShowsDetailFragment)
    CircularProgressBar circularProgressBar;

    //    Font fields...
    FontUtils fontUtils;

    //    Retrofit fields....
    private Call<TvShowsDetailMainObject> tvShowsDetailMainObjectCall;

    public TvShowsDetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tv_shows_detail, container, false);
        ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        Font fields initialization....
        fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewBoldFont(textViewOriginalTitleHeader);
        fontUtils.setTextViewBoldFont(textViewOverviewHeader);
        fontUtils.setTextViewBoldFont(textViewHomePageHeader);
        fontUtils.setTextViewBoldFont(textViewBudgetHeader);
        fontUtils.setTextViewBoldFont(textViewReleaseDateHeader);
        fontUtils.setTextViewBoldFont(textViewRevenueHeader);
        fontUtils.setTextViewBoldFont(textViewTagLineHeader);
        fontUtils.setTextViewBoldFont(textViewStatusHeader);
        fontUtils.setTextViewRegularFont(textViewOriginalTitle);
        fontUtils.setTextViewRegularFont(textViewOverview);
        fontUtils.setTextViewRegularFont(textViewHomePage);
        fontUtils.setTextViewRegularFont(textViewBudget);
        fontUtils.setTextViewRegularFont(textViewReleaseDate);
        fontUtils.setTextViewRegularFont(textViewRevenue);
        fontUtils.setTextViewRegularFont(textViewTagLine);
        fontUtils.setTextViewRegularFont(textViewStatus);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tvShowsDetailMainObjectCall != null && tvShowsDetailMainObjectCall.isExecuted()) {
            tvShowsDetailMainObjectCall.cancel();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusGetTvShowsDetail(EventBusTvShowDetailId eventBusTvShowDetailId) {
        getTvDetail("en-US", String.valueOf(eventBusTvShowDetailId.getTvId()));
    }

    private void getTvDetail(String language, String tvId) {
        circularProgressBar.setVisibility(View.VISIBLE);
        tvShowsDetailMainObjectCall = Api.WEB_SERVICE.getTvDetail(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        tvShowsDetailMainObjectCall.enqueue(new Callback<TvShowsDetailMainObject>() {
            @Override
            public void onResponse(Call<TvShowsDetailMainObject> call, retrofit2.Response<TvShowsDetailMainObject> response) {
                circularProgressBar.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    TvShowsDetailMainObject tvShowsDetailMainObject = response.body();
                    if (tvShowsDetailMainObject != null) {
                        textViewOriginalTitleHeader.setVisibility(View.VISIBLE);
                        textViewHomePageHeader.setVisibility(View.VISIBLE);
                        textViewOverviewHeader.setVisibility(View.VISIBLE);
                        textViewReleaseDateHeader.setVisibility(View.VISIBLE);
                        textViewStatusHeader.setVisibility(View.VISIBLE);
                        textViewOriginalTitle.setText(tvShowsDetailMainObject.getOriginal_name());
                        textViewHomePage.setText(tvShowsDetailMainObject.getHomepage());
                        textViewOverview.setText(tvShowsDetailMainObject.getOverview());
                        textViewReleaseDate.setText(tvShowsDetailMainObject.getFirst_air_date());
                        textViewStatus.setText(tvShowsDetailMainObject.getStatus());
                        if (tvShowsDetailMainObject.getGenres().size() > 0) {
                            EventBus.getDefault().post(new EventBusTvGenreList(tvShowsDetailMainObject.getGenres()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TvShowsDetailMainObject> call, Throwable error) {
                circularProgressBar.setVisibility(View.GONE);
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
//                        textViewError.setText(internetProblem);
                    } else {
//                        textViewError.setText(error.getMessage());
                    }
                } else {
//                    textViewError.setText(couldNotGetCelebrities);
                }
            }
        });
    }

}
