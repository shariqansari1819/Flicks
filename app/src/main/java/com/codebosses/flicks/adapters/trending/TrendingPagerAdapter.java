package com.codebosses.flicks.adapters.trending;

import android.content.Context;

import com.codebosses.flicks.R;
import com.codebosses.flicks.fragments.trending.FragmentTrendingCelebrities;
import com.codebosses.flicks.fragments.trending.FragmentTrendingMovies;
import com.codebosses.flicks.fragments.trending.FragmentTrendingTvShows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TrendingPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public TrendingPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentTrendingMovies();
            case 1:
                return new FragmentTrendingTvShows();
            case 2:
                return new FragmentTrendingCelebrities();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.trending_tabs_array)[position];
    }
}
