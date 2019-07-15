package com.codebosses.flicks.fragments.offline;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.codebosses.flicks.R;
import com.codebosses.flicks.fragments.base.BaseFragment;

import butterknife.ButterKnife;


public class OfflineFragment extends BaseFragment {


    public OfflineFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

}
