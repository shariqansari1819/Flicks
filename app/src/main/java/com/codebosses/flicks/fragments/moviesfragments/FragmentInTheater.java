package com.codebosses.flicks.fragments.moviesfragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebosses.flicks.R;
import com.codebosses.flicks.fragments.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInTheater extends BaseFragment {


    public FragmentInTheater() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_in_theater, container, false);
    }

}
