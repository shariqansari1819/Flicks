package com.codebosses.flicks.adapters.searchadapter;

import android.content.Context;

import com.codebosses.flicks.R;
import com.codebosses.flicks.fragments.searchfragments.SearchCelebrityFragment;
import com.codebosses.flicks.fragments.searchfragments.SearchMoviesFragment;
import com.codebosses.flicks.fragments.searchfragments.SearchTvShowsFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SearchPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private String[] array;

    public SearchPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        array = context.getResources().getStringArray(R.array.search_tabs_array);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchMoviesFragment();
            case 1:
                return new SearchTvShowsFragment();
            case 2:
                return new SearchCelebrityFragment();
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
        return array[position];
    }
}
