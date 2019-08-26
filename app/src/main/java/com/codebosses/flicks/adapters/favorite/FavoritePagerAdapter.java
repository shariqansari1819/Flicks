package com.codebosses.flicks.adapters.favorite;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.codebosses.flicks.R;
import com.codebosses.flicks.fragments.favorites.FavoriteMoviesFragment;
import com.codebosses.flicks.fragments.favorites.FavoriteTvShowsFragment;
import com.codebosses.flicks.fragments.trending.FragmentTrendingCelebrities;
import com.codebosses.flicks.fragments.trending.FragmentTrendingMovies;
import com.codebosses.flicks.fragments.trending.FragmentTrendingTvShows;

public class FavoritePagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public FavoritePagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FavoriteMoviesFragment();
            case 1:
                return new FavoriteTvShowsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.favorite_string_array)[position];
    }
}