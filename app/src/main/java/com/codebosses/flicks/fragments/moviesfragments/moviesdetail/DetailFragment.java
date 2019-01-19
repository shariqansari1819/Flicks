package com.codebosses.flicks.fragments.moviesfragments.moviesdetail;


import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieDetailId;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieGenreList;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.MovieDetailMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.utils.FontUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DetailFragment extends Fragment {

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
    @BindView(R.id.circularProgressBarMoviesDetailFragment)
    CircularProgressBar circularProgressBar;

    //    Font fields...
    FontUtils fontUtils;

    //    Retrofit fields....
    private Call<MovieDetailMainObject> movieDetailMainObjectCall;


    public DetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
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
        if (movieDetailMainObjectCall != null && movieDetailMainObjectCall.isExecuted()) {
            movieDetailMainObjectCall.cancel();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusGetMovieDetail(EventBusMovieDetailId eventBusMovieDetailId) {
        getMovieDetail("en-US", String.valueOf(eventBusMovieDetailId.getMovieId()));
    }

    private void getMovieDetail(String language, String movieId) {
        circularProgressBar.setVisibility(View.VISIBLE);
        movieDetailMainObjectCall = Api.WEB_SERVICE.getMovieDetail(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        movieDetailMainObjectCall.enqueue(new Callback<MovieDetailMainObject>() {
            @Override
            public void onResponse(Call<MovieDetailMainObject> call, retrofit2.Response<MovieDetailMainObject> response) {
                circularProgressBar.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    MovieDetailMainObject movieDetailMainObject = response.body();
                    if (movieDetailMainObject != null) {
                        textViewOriginalTitleHeader.setVisibility(View.VISIBLE);
                        textViewBudgetHeader.setVisibility(View.VISIBLE);
                        textViewHomePageHeader.setVisibility(View.VISIBLE);
                        textViewOverviewHeader.setVisibility(View.VISIBLE);
                        textViewReleaseDateHeader.setVisibility(View.VISIBLE);
                        textViewRevenueHeader.setVisibility(View.VISIBLE);
                        textViewStatusHeader.setVisibility(View.VISIBLE);
                        textViewTagLineHeader.setVisibility(View.VISIBLE);
                        textViewOriginalTitle.setText(movieDetailMainObject.getOriginal_title());
                        textViewBudget.setText(String.valueOf(movieDetailMainObject.getBudget()));
                        textViewHomePage.setText(movieDetailMainObject.getHomepage());
                        textViewOverview.setText(movieDetailMainObject.getOverview());
                        textViewReleaseDate.setText(movieDetailMainObject.getRelease_date());
                        textViewRevenue.setText(String.valueOf(movieDetailMainObject.getRevenue()));
                        textViewStatus.setText(movieDetailMainObject.getStatus());
                        textViewTagLine.setText(movieDetailMainObject.getTagline());
                        if (movieDetailMainObject.getGenres().size() > 0) {
                            EventBus.getDefault().post(new EventBusMovieGenreList(movieDetailMainObject.getGenres()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieDetailMainObject> call, Throwable error) {
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
