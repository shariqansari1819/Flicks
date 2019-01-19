package com.codebosses.flicks.fragments.moviesfragments.moviesdetail;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.moviesdetail.GenreListAdapter;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieGenreList;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.Genre;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MoviesDetailGenreFragment extends Fragment implements AdapterView.OnItemClickListener {

    //    Android fields....
    @BindView(R.id.listViewGenre)
    ListView listView;

    //    Adapter fields...
    private List<Genre> genreList = new ArrayList<>();
    private GenreListAdapter genreListAdapter;

    public MoviesDetailGenreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies_detail_genre, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusGetMovieGenreList(EventBusMovieGenreList eventBusMovieGenreList) {
        genreList = eventBusMovieGenreList.getGenreList();
        genreListAdapter = new GenreListAdapter(getActivity(), genreList);
        listView.setAdapter(genreListAdapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
