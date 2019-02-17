package com.codebosses.flicks.fragments.tvfragments.tvshowsdetailfragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.moviesdetail.GenreListAdapter;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieGenreList;
import com.codebosses.flicks.pojo.eventbus.EventBusTvGenreList;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.Genre;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TvShowsGenreFragment extends Fragment implements AdapterView.OnItemClickListener {


    //    Android fields....
    @BindView(R.id.listViewTvShowGenre)
    ListView listView;

    //    Adapter fields...
    private List<Genre> genreList = new ArrayList<>();
    private GenreListAdapter genreListAdapter;

    public TvShowsGenreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_shows_genre, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusGetTvShowsGenreList(EventBusTvGenreList eventBusTvGenreList) {
        genreList = eventBusTvGenreList.getGenreList();
        genreListAdapter = new GenreListAdapter(getActivity(), genreList);
        listView.setAdapter(genreListAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
