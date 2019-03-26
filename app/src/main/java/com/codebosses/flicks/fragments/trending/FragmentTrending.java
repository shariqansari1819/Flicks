package com.codebosses.flicks.fragments.trending;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.CelebrityMoviesActivity;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.activities.TvShowsDetailActivity;
import com.codebosses.flicks.adapters.celebritiesadapter.CelebritiesAdapter;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.adapters.trending.TrendingAdapter;
import com.codebosses.flicks.adapters.tvshowsadapter.TvShowsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesMainObject;
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesResult;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityClick;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FragmentTrending extends BaseFragment {

    //    Android fields....
    @BindView(R.id.constraintLayoutTrending)
    ConstraintLayout constraintLayoutTrending;
    @BindView(R.id.tabLayoutTrending)
    TabLayout tabLayoutTrending;
    @BindView(R.id.viewPagerTrending)
    ViewPager viewPagerTrending;

    //    Instance fields....
    private TrendingAdapter trendingAdapter;

    public FragmentTrending() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        ButterKnife.bind(this, view);

//        Setting pager adapter....
        trendingAdapter = new TrendingAdapter(getChildFragmentManager(),getActivity());
        viewPagerTrending.setAdapter(trendingAdapter);
        tabLayoutTrending.setupWithViewPager(viewPagerTrending);

        viewPagerTrending.setOffscreenPageLimit(2);

        changeTabsFont();

        return view;
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayoutTrending.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    FontUtils.getFontUtils(getActivity()).setTextViewRegularFont((TextView) tabViewChild);
                }
            }
        }
    }

}