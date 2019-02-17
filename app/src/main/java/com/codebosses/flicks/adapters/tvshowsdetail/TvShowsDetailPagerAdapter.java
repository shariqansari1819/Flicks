package com.codebosses.flicks.adapters.tvshowsdetail;

import android.content.Context;

import com.codebosses.flicks.R;
import com.codebosses.flicks.fragments.tvfragments.tvshowsdetailfragment.SimilarTvShowsFragment;
import com.codebosses.flicks.fragments.tvfragments.tvshowsdetailfragment.SuggestedTvShowsFragment;
import com.codebosses.flicks.fragments.tvfragments.tvshowsdetailfragment.TvShowsDetailFragment;
import com.codebosses.flicks.fragments.tvfragments.tvshowsdetailfragment.TvShowsGenreFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TvShowsDetailPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private String[] array;

    public TvShowsDetailPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        array = context.getResources().getStringArray(R.array.movies_detail_array);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TvShowsDetailFragment();
            case 1:
                return new TvShowsGenreFragment();
            case 2:
                return new SimilarTvShowsFragment();
            case 3:
                return new SuggestedTvShowsFragment();
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
