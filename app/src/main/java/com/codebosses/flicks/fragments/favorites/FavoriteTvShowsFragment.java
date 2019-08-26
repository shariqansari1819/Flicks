package com.codebosses.flicks.fragments.favorites;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebosses.flicks.R;

public class FavoriteTvShowsFragment extends Fragment {


    public FavoriteTvShowsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_tv_shows, container, false);
    }

}
