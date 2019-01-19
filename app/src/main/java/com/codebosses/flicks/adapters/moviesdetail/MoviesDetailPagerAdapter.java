package com.codebosses.flicks.adapters.moviesdetail;

import android.content.Context;

import com.codebosses.flicks.R;
import com.codebosses.flicks.fragments.moviesfragments.moviesdetail.DetailFragment;
import com.codebosses.flicks.fragments.moviesfragments.moviesdetail.MoviesDetailGenreFragment;
import com.codebosses.flicks.fragments.moviesfragments.moviesdetail.SimilarMoviesFragment;
import com.codebosses.flicks.fragments.moviesfragments.moviesdetail.SuggestedMoviesFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import butterknife.BindArray;

public class MoviesDetailPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private String[] array;

    public MoviesDetailPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        array = context.getResources().getStringArray(R.array.movies_detail_array);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DetailFragment();
            case 1:
                return new MoviesDetailGenreFragment();
            case 2:
                return new SimilarMoviesFragment();
            case 3:
                return new SuggestedMoviesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return array[position];
    }
}
